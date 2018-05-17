package Akuto2Mod.Items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemPumpEX extends ItemBlock{
	public ItemPumpEX(Block block) {
		super(block);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int meta = stack.getItemDamage() + 1;
		return (meta > 6) ? null : "tile.pumpEX_" + meta;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return StatCollector.translateToLocal(getUnlocalizedName(stack));
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}
}
