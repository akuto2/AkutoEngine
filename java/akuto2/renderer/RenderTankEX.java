package akuto2.renderer;

import org.lwjgl.opengl.GL11;

import akuto2.tiles.TileEntityTankEX;
import buildcraft.lib.client.render.fluid.FluidRenderer;
import buildcraft.lib.client.render.fluid.FluidSpriteType;
import buildcraft.lib.fluid.FluidSmoother.FluidStackInterp;
import buildcraft.lib.misc.RenderUtil;
import buildcraft.lib.misc.RenderUtil.AutoTessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;

public class RenderTankEX extends TileEntitySpecialRenderer<TileEntityTankEX>{
	private static final Vec3d MIN = new Vec3d(0.13, 0.01, 0.13);
	private static final Vec3d MAX = new Vec3d(0.86, 0.99, 0.86);
	private static final Vec3d MIN_CONNECTED = new Vec3d(0.13, 0, 0.13);
	private static final Vec3d MAX_CONNECTED = new Vec3d(0.86, 1 - 1e-5, 0.86);

	public RenderTankEX() {}

	@Override
	public void render(TileEntityTankEX te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		FluidStackInterp forRender = te.getFluidForRender(partialTicks);
		if(forRender == null) {
			return;
		}
		Minecraft.getMinecraft().mcProfiler.startSection("akutoengine");
		Minecraft.getMinecraft().mcProfiler.startSection("tank");

		RenderHelper.disableStandardItemLighting();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

		try(AutoTessellator tess = RenderUtil.getThreadLocalUnusedTessellator()){
			BufferBuilder bb = tess.tessellator.getBuffer();
			bb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			bb.setTranslation(x, y, z);

			boolean[] sideRender = { true, true, true, true, true, true };
			boolean connectedUp = isFullyConnected(te, EnumFacing.UP, partialTicks);
			boolean connectedDown = isFullyConnected(te, EnumFacing.DOWN, partialTicks);
			sideRender[EnumFacing.DOWN.ordinal()] = !connectedDown;
			sideRender[EnumFacing.UP.ordinal()] = !connectedUp;

			Vec3d min = connectedDown ? MIN_CONNECTED : MIN;
			Vec3d max = connectedUp ? MAX_CONNECTED : MAX;
			FluidStack fluid = forRender.fluid;
			int blockLight = fluid.getFluid().getLuminosity(fluid);
			int combinedLight = te.getWorld().getCombinedLight(te.getPos(), blockLight);

			FluidRenderer.vertex.lighti(combinedLight);

			FluidRenderer.renderFluid(FluidSpriteType.STILL, fluid, forRender.amount, te.tank.getCapacity(), min, max, bb, sideRender);

			bb.setTranslation(0, 0, 0);
			tess.tessellator.draw();
		}

		RenderHelper.enableStandardItemLighting();

		Minecraft.getMinecraft().mcProfiler.endSection();
		Minecraft.getMinecraft().mcProfiler.endSection();
	}

	private static boolean isFullyConnected(TileEntityTankEX thisTank, EnumFacing facing, float partialTicks) {
		BlockPos pos = thisTank.getPos().offset(facing);
		TileEntity tile = thisTank.getWorld().getTileEntity(pos);
		if(tile instanceof TileEntityTankEX) {
			TileEntityTankEX tank = (TileEntityTankEX)tile;
			if(!TileEntityTankEX.canTanksConnect(thisTank, tank, facing)) {
				return false;
			}
			FluidStackInterp forRender = tank.getFluidForRender(partialTicks);
			if(forRender == null) {
				return false;
			}
			FluidStack fluid = forRender.fluid;
			if(fluid == null || forRender.amount <= 0) {
				return false;
			}
			else if(thisTank.getFluidForRender(partialTicks) == null || !fluid.isFluidEqual(thisTank.getFluidForRender(partialTicks).fluid)) {
				return false;
			}
			if(fluid.getFluid().isGaseous(fluid)) {
				facing = facing.getOpposite();
			}
			return forRender.amount >= tank.tank.getCapacity() || facing == EnumFacing.UP;
		}
		else {
			return false;
		}
	}
}
