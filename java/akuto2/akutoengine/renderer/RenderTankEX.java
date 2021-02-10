package akuto2.akutoengine.renderer;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class RenderTankEX implements IItemRenderer{

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack itemStack, ItemRendererHelper helper) {
		if(helper == ItemRendererHelper.EQUIPPED_BLOCK) {
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
		RenderBlocks renderBlocks = (RenderBlocks)data[0];

		TextureManager textureManager = RenderManager.instance.renderEngine;
		if(textureManager == null) {
			return;
		}
		GL11.glPushAttrib(1048575);

		textureManager.bindTexture(textureManager.getResourceLocation(itemStack.getItemSpriteNumber()));
		Item item = itemStack.getItem();
		Block block = ((ItemBlock)item).field_150939_a;

		block.canRenderInPass(0);
		GL11.glPushMatrix();
		GL11.glDepthFunc(515);
		GL11.glEnable(3042);

		GL11.glEnable(3008);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		renderBlocks.renderBlockAsItem(block, itemStack.getItemDamage(), 1.0F);
		GL11.glDisable(3042);
	    GL11.glDepthFunc(515);
	    GL11.glPopMatrix();

	    GL11.glPushMatrix();
	    GL11.glDepthFunc(515);
	    GL11.glEnable(3042);
	    block.canRenderInPass(1);
	    renderBlocks.renderBlockAsItem(block, itemStack.getItemDamage(), 1.0F);
	    GL11.glDisable(3042);
	    GL11.glDepthFunc(515);
	    GL11.glPopMatrix();

	    GL11.glPopAttrib();
	}

}
