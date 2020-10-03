package akuto2.gui;

import org.lwjgl.opengl.GL11;

import akuto2.gui.container.ContainerFillerEX;
import akuto2.tiles.TileEntityFillerEX;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiFillerEX extends GuiContainer{
	private static final ResourceLocation TEXTURE = new ResourceLocation("akutoengine:textures/gui/filler_gui.png");

	public GuiFillerEX(InventoryPlayer player, World world, TileEntityFillerEX fillerEX) {
		super(new ContainerFillerEX(player, world, fillerEX));
		xSize = 175;
		ySize = 240;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString("Filler", 75, 5, 0x404040);
		fontRenderer.drawString("Filling Resources", 8, 74, 0x404040);
		fontRenderer.drawString("Inventory", 8, 142, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}
