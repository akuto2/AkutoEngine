package Akuto2Mod.Gui;

import Akuto2Mod.TileEntity.TileEMCBuilder;
import buildcraft.core.lib.gui.GuiAdvancedInterface;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiTileEMCBuilder extends GuiAdvancedInterface{
	private static final ResourceLocation REGULAR_TEXTURE = new ResourceLocation("akutoengine:textures/gui/emcbuilder.png");
	private static final ResourceLocation BLUEPRINT_TEXTURE = new ResourceLocation("akutoengine:textures/gui/emcbuilder_blueprint.png");
	private TileEMCBuilder emcBuilder;
	private GuiButton selectedButton;

	public GuiTileEMCBuilder(InventoryPlayer player, TileEMCBuilder emcBuilder) {
		super(new ContainerTileEMCBuilder(player, emcBuilder), emcBuilder, BLUEPRINT_TEXTURE);
		this.emcBuilder = emcBuilder;
		xSize = 256;
		ySize = 225;

		resetNullSlots(24);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {

	}

}
