package Akuto2Mod.Items;

import Akuto2Mod.Utils.AchievementHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemPumpEX extends ItemBlock{
	public ItemPumpEX(Block block) {
		super(block);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		if(stack.getItemDamage() == 0) {
			player.triggerAchievement(AchievementHandler.getPump);
		}
		super.onCreated(stack, world, player);
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
