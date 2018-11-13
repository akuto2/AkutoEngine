package Akuto2Mod.Gui;

import org.lwjgl.opengl.GL11;

import Akuto2Mod.TileEntity.TileFillerEX;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiFillerEX extends GuiContainer{
	private static final ResourceLocation tex = new ResourceLocation("akutoengine:textures/gui/filler_gui.png");
	private static final ResourceLocation blockTex = new ResourceLocation("akutoengine:textures/gui/filler_gui_block.png");
	IInventory playerInventory;
	TileFillerEX fillerEX;

	public GuiFillerEX(IInventory inventory, TileFillerEX fillerEX) {
		super(new ContainerFillerEX(inventory, fillerEX));
		playerInventory = inventory;
		this.fillerEX = fillerEX;
		xSize = 175;
		ySize = 240;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		fontRendererObj.drawString("Filler", 75, 5, 0x404040);
		fontRendererObj.drawString("Filling Resources", 8, 74, 0x404040);
		fontRendererObj.drawString("Inventory", 8, 142, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(tex);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}
