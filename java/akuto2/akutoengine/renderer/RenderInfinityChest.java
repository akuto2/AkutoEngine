package akuto2.akutoengine.renderer;

import java.util.HashSet;
import java.util.Set;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import akuto2.akutoengine.proxies.ClientProxy;
import akuto2.akutoengine.tiles.TileEntityInfinityChest;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.MinecraftForgeClient;

public class RenderInfinityChest extends TileEntitySpecialRenderer{
	private static RenderBlocks renderBlocks;
	private FontRenderer fontRenderer;
	private ItemStack inChestItemStack;

	public RenderInfinityChest() {
		renderBlocks = new RenderInfinityChestBlocks();
		fontRenderer = ClientProxy.mc.fontRenderer;
	}

	public void render(TileEntityInfinityChest chest, double x, double y, double z, float partialTick) {
		if(chest == null) {
			return;
		}
		ItemStack stack = chest.getStack(1);
		if(stack != null) {
			if(inChestItemStack == null || !stack.isItemEqual(inChestItemStack) || !ItemStack.areItemStacksEqual(stack, inChestItemStack)) {
				inChestItemStack = stack.copy();
			}
			Item item = stack.getItem();
			Block block = Block.getBlockFromItem(item);
			int itemDamage = inChestItemStack.getItemDamage();
			boolean isBlock = (stack.getItem() instanceof ItemBlock);
			int facing = chest.getBlockMetadata();
			float rotationYaw = 0.0F;
			if(facing == 2)
				rotationYaw = 180.0F;
			if(facing == 3)
				rotationYaw = 0.0F;
			if(facing == 4)
				rotationYaw = 270.0F;
			if(facing == 5)
				rotationYaw = 90.0F;
			float shiftX = 0.5F;
			float shiftY = 0.35F;
			float shiftZ = 0.5F;
			float blockScale = 0.6F;
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glTranslated(x, y, z);
			GL11.glPushMatrix();
			GL11.glTranslatef(shiftX, shiftY, shiftZ);
			GL11.glScalef(blockScale, blockScale, blockScale);
			GL11.glPushMatrix();
			GL11.glRotatef(rotationYaw, 0.0F, 1.0F, 0.0F);
			EntityItem customItem = new EntityItem(chest.getWorldObj());
			customItem.setEntityItemStack(inChestItemStack);
			IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(inChestItemStack, ItemRenderType.ENTITY);
			ResourceLocation resourceLocation = RenderManager.instance.renderEngine.getResourceLocation(inChestItemStack.getItemSpriteNumber());
			bindTexture(resourceLocation);
			if(customRenderer != null) {
				customRenderer.renderItem(ItemRenderType.ENTITY, inChestItemStack, new Object[] { renderBlocks, customItem });
			}
			else if(isBlock && RenderBlocks.renderItemIn3d(block.getRenderType())) {
				renderBlocks.renderBlockAsItem(block, itemDamage, 1.0F);
			}
			else {
				renderChestItem(inChestItemStack, shiftY);
			}
			GL11.glPopMatrix();
			GL11.glPopMatrix();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	private void renderChestItem(ItemStack stack, float shiftY) {
		Item item = stack.getItem();
		int damage = stack.getItemDamage();
		Tessellator tessellator = Tessellator.instance;
		float xPos = 0.5F;
		float yPos = 0.25F;
		GL11.glTranslatef(0.0F, -shiftY, 0.0F);
		float blockScale = 1.2F;
		GL11.glScalef(-blockScale, blockScale, blockScale);
		int passes = item.getRenderPasses(damage);
		for(int pass = 0; pass < passes; pass++) {
			IIcon icon = item.getIcon(stack, pass);
			float minU = icon.getMinU();
			float maxU = icon.getMaxU();
			float minV = icon.getMinV();
			float maxV = icon.getMaxV();
			int color = item.getColorFromItemStack(stack, pass);
			float red = 0.00390625F * (color >> 16);
			float green = 0.00390625F * ((color & 0xFFFF) >> 8);
			float blue = 0.00390625F * (color & 0xFF);
			GL11.glColor4f(red, green, blue, 1.0F);
			GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			tessellator.addVertexWithUV((0.0F - xPos), (0.0F - yPos), 0.0D, minU, maxV);
			tessellator.addVertexWithUV((1.0F - xPos), (0.0F - yPos), 0.0D, maxU, maxV);
			tessellator.addVertexWithUV((1.0F - xPos), (1.0F - yPos), 0.0D, maxU, minV);
			tessellator.addVertexWithUV((0.0F - xPos), (1.0F - yPos), 0.0D, minU, minV);
			tessellator.draw();
			GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			tessellator.addVertexWithUV((0.0F - xPos), (0.0F - yPos), 0.0D, minU, maxV);
			tessellator.addVertexWithUV((1.0F - xPos), (0.0F - yPos), 0.0D, maxU, maxV);
			tessellator.addVertexWithUV((1.0F - xPos), (1.0F - yPos), 0.0D, maxU, minV);
			tessellator.addVertexWithUV((0.0F - xPos), (1.0F - yPos), 0.0D, minU, minV);
			tessellator.draw();
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick) {
		render((TileEntityInfinityChest)tile, x, y, z, partialTick);
	}

	private static class RenderInfinityChestBlocks extends RenderBlocks {
		private static final Set<Block> shouldRotateRenderBlocks = new HashSet();

		static {
			shouldRotateRenderBlocks.add(Blocks.chest);
			shouldRotateRenderBlocks.add(Blocks.trapped_chest);
			shouldRotateRenderBlocks.add(Blocks.ender_chest);
			shouldRotateRenderBlocks.add(Blocks.fence);
			shouldRotateRenderBlocks.add(Blocks.fence_gate);
			shouldRotateRenderBlocks.add(Blocks.nether_brick_fence);
			shouldRotateRenderBlocks.add(Blocks.furnace);
			shouldRotateRenderBlocks.add(Blocks.dropper);
			shouldRotateRenderBlocks.add(Blocks.dispenser);
		}

		@Override
		public void renderBlockAsItem(Block block, int meta, float brightness) {
			GL11.glPushMatrix();
			if(shouldRotateRenderBlocks.contains(block))
				GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			super.renderBlockAsItem(block, meta, brightness);
			GL11.glPopMatrix();
		}
	}
}
