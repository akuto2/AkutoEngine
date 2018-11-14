package Akuto2Mod.Items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockEMCContainer extends ItemBlock{

	public ItemBlockEMCContainer(Block block) {
		super(block);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "_" + stack.getItemDamage();
	}
}
