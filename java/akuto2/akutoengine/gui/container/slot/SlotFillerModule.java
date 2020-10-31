package akuto2.akutoengine.gui.container.slot;

import akuto2.akutoengine.gui.container.ContainerFillerEX;
import akuto2.akutoengine.gui.container.inventory.InventoryFillerEXCrafting;
import akuto2.akutoengine.patterns.FillerPatternRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;

public class SlotFillerModule extends Slot{
	private final ContainerFillerEX container;
	private final InventoryFillerEXCrafting crafting;
	private final EntityPlayer player;

	public SlotFillerModule(ContainerFillerEX container, InventoryFillerEXCrafting crafting, EntityPlayer player, int id, int x, int y) {
		super(container.craftResult, id, x, y);
		this.player = player;
		this.container = container;
		this.crafting = crafting;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	@Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack resultStack) {
		ForgeHooks.setCraftingPlayer(thePlayer);
		NonNullList<ItemStack> nonNullList = FillerPatternRecipe.getRemainingItems(crafting);
		ForgeHooks.setCraftingPlayer(null);

		for(int slot = 0; slot < nonNullList.size(); ++slot) {
			ItemStack stack = crafting.getStackInSlot(slot);
			ItemStack stack1 = nonNullList.get(slot);

			if(!stack.isEmpty()) {
				crafting.decrStackSize(slot, 1);
				stack = crafting.getStackInSlot(slot);
			}

			if(!stack1.isEmpty()) {
				if(stack.isEmpty()) {
					crafting.setInventorySlotContents(slot, stack1);
				}
				else if(ItemStack.areItemsEqual(stack, stack1) && ItemStack.areItemStackTagsEqual(stack, stack1)) {
					stack1.grow(stack.getCount());
					crafting.setInventorySlotContents(slot, stack1);
				}
				else if(!player.inventory.addItemStackToInventory(stack1)) {
					player.dropItem(stack1, false);
				}
			}
		}
		return resultStack;
	}
}
