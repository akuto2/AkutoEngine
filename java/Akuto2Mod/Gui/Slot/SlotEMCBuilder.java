package Akuto2Mod.Gui.Slot;

import Akuto2Mod.EMCHandler;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotEMCBuilder extends Slot{

	public SlotEMCBuilder(IInventory inventory, int i, int j) {
		super(inventory, 0, i, j);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return EMCHandler.getEMC(stack) > 0;
	}

}
