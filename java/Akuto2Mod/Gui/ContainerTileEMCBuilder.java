package Akuto2Mod.Gui;

import Akuto2Mod.TileEntity.TileEMCBuilder;
import buildcraft.core.lib.gui.BuildCraftContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerTileEMCBuilder extends BuildCraftContainer{

	public ContainerTileEMCBuilder(InventoryPlayer player, TileEMCBuilder emcBuilder) {
		super(emcBuilder.getSizeInventory());
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return false;
	}

}
