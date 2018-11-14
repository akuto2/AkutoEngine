package Akuto2Mod.Gui;

import Akuto2Mod.Akuto2Core;
import Akuto2Mod.Gui.Slot.SlotFillerModule;
import Akuto2Mod.Pattern.FillerPatternCore;
import Akuto2Mod.Pattern.FillerPatternRecipe;
import Akuto2Mod.TileEntity.TileFillerEX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFillerEX extends Container{
	public IInventory playerInventory;
	public TileFillerEX fillerEX;
	public int inventorySize;
	public InventoryCrafting crafting;
	public IInventory craftResult = new InventoryCraftResult();

	public ContainerFillerEX(IInventory player, TileFillerEX fillerEX) {
		inventorySize = fillerEX.getSizeInventory();
		playerInventory = player;
		this.fillerEX = fillerEX;

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				addSlotToContainer(new Slot(fillerEX, j + i * 3, 31 + j * 18, 16 + i * 18));
			}
		}

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(fillerEX, 9 + j + i * 9, 8 + j * 18, 85 + i * 18));
			}
		}

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 153 + i * 18));
			}
		}

		for(int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player, i, 8 + i * 18, 211));
		}

		addSlotToContainer(new SlotFillerModule(this, 0, 125, 34));
		onCraftMatrixChanged(fillerEX);
	}

	@Override
	public ItemStack slotClick(int i, int j, int k, EntityPlayer player) {
		ItemStack stack = super.slotClick(i, j, k, player);
		onCraftMatrixChanged(fillerEX);
		return stack;
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventory) {
		FillerPatternCore pattern = FillerPatternRecipe.findMatchingRecipe(fillerEX);
		if(pattern != null && pattern instanceof FillerPatternCore) {
			craftResult.setInventorySlotContents(0, ((FillerPatternCore)pattern).moduleItem.copy());
		}
		else {
			craftResult.setInventorySlotContents(0, null);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		ItemStack stack = null;
		Slot slot = (Slot)inventorySlots.get(i);

		if(slot != null && slot.getHasStack()) {
			ItemStack stack2 = slot.getStack();
			stack = stack2.copy();

			if(i == inventorySlots.size() - 1) {
				if(!mergeItemStack(stack2, inventorySize, inventorySlots.size() - 1, false)) {
					return null;
				}
				slot.onPickupFromSlot(player, stack2);
				onCraftMatrixChanged(fillerEX);
			}
			if(i < inventorySize) {
				if(!mergeItemStack(stack2, inventorySize, inventorySlots.size() - 1, false)) {
					return null;
				}
			}
			else if(i >= 9 && stack2.getItem() == Akuto2Core.fillerModule) {
				if(!mergeItemStack(stack2, 4, 5, false) && !mergeItemStack(stack2, 0, 4, false) && !mergeItemStack(stack2, 5, 9, false) && !mergeItemStack(stack2, 9, inventorySize, false)) {
					return null;
				}
				fillerEX.computeRecipe();
			}
			else if(!mergeItemStack(stack2, 9, inventorySize, false)) {
				return null;
			}

			if(stack2.stackSize == 0) {
				slot.putStack(null);
			}
			else {
				slot.onSlotChanged();
			}
		}
		return stack;
	}
}
