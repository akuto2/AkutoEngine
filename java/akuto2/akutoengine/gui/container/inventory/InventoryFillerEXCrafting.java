package akuto2.akutoengine.gui.container.inventory;

import akuto2.akutoengine.gui.container.ContainerFillerEX;
import akuto2.akutoengine.tiles.TileEntityFillerEX;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventoryFillerEXCrafting extends InventoryCrafting{
	private TileEntityFillerEX fillerEX;
	private ContainerFillerEX container;

	public InventoryFillerEXCrafting(ContainerFillerEX container, TileEntityFillerEX fillerEX) {
		super(container, 3, 3);
		this.container = container;
		this.fillerEX = fillerEX;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return fillerEX.getStackInSlot(index);
	}

	@Override
	public ItemStack getStackInRowAndColumn(int row, int column) {
		if(row >= 0 && row < 9) {
			int x = row + column * 9;
			return getStackInSlot(x);
		}
		else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack stack = fillerEX.getStackInSlot(index);
		if(!stack.isEmpty()) {
			ItemStack stack2;
			if(stack.getCount() <= count) {
				stack2 = stack.copy();
				fillerEX.setInventorySlotContents(index, ItemStack.EMPTY);
				container.onCraftMatrixChanged(this);
				return stack2;
			}
			else {
				stack2 = stack.splitStack(count);
				if(stack.getCount() == 0) {
					fillerEX.setInventorySlotContents(index, ItemStack.EMPTY);
				}
				container.onCraftMatrixChanged(this);
				return stack2;
			}
		}
		else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		fillerEX.setInventorySlotContents(index, stack);
		container.onCraftMatrixChanged(this);
	}
}
