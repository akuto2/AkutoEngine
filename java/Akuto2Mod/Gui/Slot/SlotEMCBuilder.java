package Akuto2Mod.Gui.Slot;

import Akuto2Mod.EMCHandler;
import Akuto2Mod.Utils.EMCWorldSave;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotEMCBuilder extends Slot{
	private EMCWorldSave worldData;

	public SlotEMCBuilder(IInventory inventory, int i, int j, EMCWorldSave worldData) {
		super(inventory, 0, i, j);
		this.worldData = worldData;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return EMCHandler.getEMC(stack) > 0 && worldData.isMaxEmc();
	}

	@Override
	public void putStack(ItemStack stack) {
		if(stack != null) {
			while(stack.stackSize > 0) {
				if(!worldData.addEMC(stack)) {
					break;
				}
				--stack.stackSize;
			}
		}
	}
}
