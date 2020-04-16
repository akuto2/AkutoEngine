package Akuto2Mod.Gui;

import Akuto2Mod.Gui.Slot.SlotEMCBuilder;
import Akuto2Mod.TileEntity.TileEMCBuilder;
import Akuto2Mod.Utils.EMCWorldSave;
import buildcraft.core.lib.gui.BuildCraftContainer;
import buildcraft.core.lib.gui.widgets.ScrollbarWidget;
import buildcraft.core.lib.gui.widgets.Widget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerEMCBuilder extends BuildCraftContainer{
	protected ScrollbarWidget scrollbarWidget;
	protected IInventory playerInventory;
	protected TileEMCBuilder emcBuilder;
	private static EMCWorldSave worldData;

	public ContainerEMCBuilder(InventoryPlayer player, TileEMCBuilder emcBuilder) {
		super(emcBuilder.getSizeInventory());
		this.playerInventory = player;
		this.emcBuilder = emcBuilder;
		worldData = this.emcBuilder.getWorldSaveData(this.emcBuilder.getWorldObj());
		scrollbarWidget = new ScrollbarWidget(172, 17, 18, 0, 108);
		scrollbarWidget.hidden = true;
		addWidget((Widget)scrollbarWidget);
		addSlotToContainer(new Slot(emcBuilder, 0, 80, 27));
		addSlotToContainer(new SlotEMCBuilder(emcBuilder, 133, 27, worldData));

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInventory, 1 + i + j * 9, 8 + j * 18, 72 + i * 18));
			}
		}

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInventory, i + j * 9 + 9, 8 + i * 18, 140 + i * 18));
			}
		}

		for(int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 198));
		}

		if(!(emcBuilder.getWorldObj()).isRemote && playerInventory instanceof InventoryPlayer) {
			emcBuilder.updateRequirementsOnGuiOpen(((InventoryPlayer)playerInventory).player);
			emcBuilder.addGuiWatcher(((InventoryPlayer)playerInventory).player);
		}
	}

	public TileEMCBuilder getEMCBuilder() {
		return emcBuilder;
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		emcBuilder.removeGuiWatcher(player);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return emcBuilder.isUseableByPlayer(player);
	}

}
