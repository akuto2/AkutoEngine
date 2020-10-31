package akuto2.akutoengine.renderer;

import akuto2.akutoengine.tiles.TileEntityFillerEX;
import buildcraft.core.client.BuildCraftLaserManager;
import buildcraft.lib.client.render.laser.LaserBoxRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.profiler.Profiler;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFillerEX extends FastTESR<TileEntityFillerEX>{
	@Override
	public void renderTileEntityFast(TileEntityFillerEX te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
		Profiler profiler = Minecraft.getMinecraft().mcProfiler;
		profiler.startSection("ae");
		profiler.startSection("fillerex");

		profiler.startSection("box");
		if(te.markerBox) {
			buffer.setTranslation(x - te.getPos().getX(), y - te.getPos().getY(), z - te.getPos().getZ());
			LaserBoxRenderer.renderLaserBoxDynamic(te.box, BuildCraftLaserManager.STRIPES_WRITE, buffer, true);
			buffer.setTranslation(0, 0, 0);
		}
		profiler.endSection();

		profiler.endSection();
		profiler.endSection();
	}

	@Override
	public boolean isGlobalRenderer(TileEntityFillerEX te) {
		return true;
	}
}
