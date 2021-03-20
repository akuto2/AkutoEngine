package akuto2.akutoengine.gui;

import java.math.BigInteger;

import akuto2.akutoengine.gui.slot.InfinityChestLockSlot;
import akuto2.akutoengine.gui.slot.InfinityChestSlot;
import akuto2.akutoengine.tiles.TileEntityInfinityChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerInfinityChest extends Container{
	private TileEntityInfinityChest chest;
	private InventoryPlayer inventoryPlayer;
	private IInventory guiInventory = new InventoryBasic("slots", false, 1);

	public ContainerInfinityChest(InventoryPlayer inventoryPlayer, TileEntityInfinityChest tile) {
		this.inventoryPlayer = inventoryPlayer;
		chest = tile;
		chest.setContainer(this);

		addSlotToContainer(new InfinityChestLockSlot(guiInventory, 0, 12, 21));
		addSlotToContainer(new InfinityChestSlot(tile, 0, 80, 55));
		addSlotToContainer(new InfinityChestSlot(tile, 1, 134, 55));

		changeSlot();

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 76 + i * 18));
			}
		}

		for(int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 134));
		}
	}

	public void changeSlot() {
		if(!chest.hasStack()) {
			guiInventory.setInventorySlotContents(0, null);
			return;
		}
		ItemStack chestStack = chest.getStack(1);
		guiInventory.setInventorySlotContents(0, chestStack);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return chest.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack stack = null;
		Slot slot = (Slot)inventorySlots.get(slotIndex);

		if(slot != null && slot.getHasStack()) {
			ItemStack stack2 = slot.getStack();
			stack = stack2.copy();

			if(slotIndex == 0) {
				return null;
			}
			else if(slotIndex < 3) {
				if(!mergeItemStack(stack2, 3, 39, true)) {
					return null;
				}
			}
			else if(chest.isItemValidForSlot(0, stack2)) {
				if(!mergeItemStack(stack2, 1, 2, false)) {
					return null;
				}
			}

			if(stack2.stackSize == 0) {
				slot.putStack(null);
			}
			else {
				slot.onSlotChanged();
			}

			if(stack2.stackSize == stack.stackSize) {
				return null;
			}

			if(slotIndex == 2) {
				chest.decrStack(BigInteger.valueOf(stack.stackSize), BigInteger.valueOf(stack.stackSize));
			}

			slot.onPickupFromSlot(player, stack2);
		}

		return stack;
	}

	@Override
	protected void retrySlotClick(int slot, int mouse, boolean par3, EntityPlayer player) {}
}
