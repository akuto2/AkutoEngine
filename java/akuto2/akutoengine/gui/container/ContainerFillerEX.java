package akuto2.akutoengine.gui.container;

import akuto2.akutoengine.ObjHandler;
import akuto2.akutoengine.gui.container.inventory.InventoryFillerEXCrafting;
import akuto2.akutoengine.gui.container.slot.SlotFillerModule;
import akuto2.akutoengine.patterns.FillerPatternCore;
import akuto2.akutoengine.patterns.FillerPatternRecipe;
import akuto2.akutoengine.tiles.TileEntityFillerEX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.world.World;

public class ContainerFillerEX extends Container{
	public TileEntityFillerEX fillerEX;
	public int inventorySize;
	public InventoryFillerEXCrafting crafting;
	public IInventory craftResult = new InventoryCraftResult();
	private final World world;
	private final EntityPlayer player;

	public ContainerFillerEX(InventoryPlayer player, World world, TileEntityFillerEX fillerEX) {
		this.world = world;
		this.player = player.player;
		this.fillerEX = fillerEX;
		inventorySize = fillerEX.getSizeInventory();
		crafting = new InventoryFillerEXCrafting(this, fillerEX);

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				addSlotToContainer(new Slot(crafting, j + i * 3, 31 + j * 18, 16 + i * 18));
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

		addSlotToContainer(new SlotFillerModule(this, crafting, player.player, 0, 125, 34));
	}

	public void onCraftMatrixChanged() {
		onCraftMatrixChanged(crafting);
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		if(!world.isRemote) {
			EntityPlayerMP playerMP = (EntityPlayerMP)player;
			FillerPatternCore pattern = FillerPatternRecipe.findMatchingRecipe(crafting);
			ItemStack stack = ItemStack.EMPTY;
			if(pattern != null && pattern instanceof FillerPatternCore) {
				stack = pattern.moduleItem.copy();
			}
			else {
				stack = ItemStack.EMPTY;
			}
			craftResult.setInventorySlotContents(0, stack);
			playerMP.connection.sendPacket(new SPacketSetSlot(windowId, inventorySlots.size() - 1, stack));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if(slot != null && slot.getHasStack()) {
			ItemStack stack2 = slot.getStack();
			stack = stack2.copy();

			if(index == inventorySlots.size() - 1) {
				if(!mergeItemStack(stack2, inventorySize, inventorySlots.size() - 1, false)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(stack2, stack);
				onCraftMatrixChanged(fillerEX);
			}
			else if(index < inventorySize) {
				if(!mergeItemStack(stack2, inventorySize, inventorySlots.size() - 1, false)) {
					return ItemStack.EMPTY;
				}
			}
			else if(index >= 9 && stack2.getItem() == ObjHandler.fillerModule) {
				if(!mergeItemStack(stack2, 4, 5, false) && !mergeItemStack(stack2, 0, 4, false) && !mergeItemStack(stack2, 5, 9, false) && !mergeItemStack(stack2, 9, inventorySize, false)) {
					return ItemStack.EMPTY;
				}
				fillerEX.computeRecipe();
			}
			else if(!mergeItemStack(stack2, 9, inventorySize, false)) {
				return ItemStack.EMPTY;
			}

			if(stack2.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}
			else {
				slot.onSlotChanged();
			}

			if(stack2.getCount() == stack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, stack2);
		}

		return stack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
}
