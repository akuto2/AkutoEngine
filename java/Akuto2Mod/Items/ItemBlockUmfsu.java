package Akuto2Mod.Items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemBlockUmfsu extends ItemBlock {
	public ItemBlockUmfsu(Block block){
		super(block);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack){
		return super.getUnlocalizedName();
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltipList, boolean par4){
	    String output = StatCollector.translateToLocal("ic2.item.tooltip.Output") + " " + 8192 + "EU/t";
	    String capacity = StatCollector.translateToLocal("ic2.item.tooltip.Capacity") + " " + "200m EU";
	    String stored = StatCollector.translateToLocal("ic2.item.tooltip.Store") + " ";
	    tooltipList.add(output + " " + capacity);
	    if (stack.hasTagCompound()) {
	      tooltipList.add(stored + stack.getTagCompound().getInteger("energy") + " EU");
	    } else {
	      tooltipList.add(stored + 0 + " EU");
	    }
	}
}
