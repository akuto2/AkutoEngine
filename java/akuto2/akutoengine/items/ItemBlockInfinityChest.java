package akuto2.akutoengine.items;

import java.math.BigInteger;
import java.util.List;

import akuto2.akutoengine.tiles.TileEntityInfinityChest;
import akuto2.akutoengine.utils.InfinityChestFormatUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockInfinityChest extends ItemBlock{
	public ItemBlockInfinityChest(Block block) {
		super(block);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		boolean result = super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
		if(result && stack.stackSize <= 0) {
			InventoryPlayer inventoryPlayer = player.inventory;
			stack.stackSize = 1;
			inventoryPlayer.setInventorySlotContents(inventoryPlayer.currentItem, null);
		}
		return result;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if(stack.hasTagCompound()) {
			ItemStack contents = TileEntityInfinityChest.readStackFromNBT(stack.getTagCompound());
			if(contents != null) {
				BigInteger count = TileEntityInfinityChest.readCountFromNBT(stack.getTagCompound());
				list.add(" " + contents.getDisplayName());
				list.add(" " + InfinityChestFormatUtils.formatStack(count, true, false));
				contents.getItem().addInformation(contents, player, list, par4);
			}
		}
	}
}
