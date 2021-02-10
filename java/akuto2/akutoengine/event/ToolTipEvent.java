package akuto2.akutoengine.event;

import akuto2.akutoengine.AkutoEngine;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class ToolTipEvent {
	@SubscribeEvent
	public void tTipEvent(ItemTooltipEvent event){
		ItemStack current = event.itemStack;
		Item currentItem = current.getItem();
		Block currentBlock = Block.getBlockFromItem(currentItem);

		if(currentBlock == AkutoEngine.engineBlock){
			int metadata = current.getItemDamage();
			switch(metadata){
			case 0:
				event.toolTip.add("Generate Rate: 1RF/t");
				break;
			case 1:
				event.toolTip.add("Generate Rate: 8RF/t");
				break;
			case 2:
				event.toolTip.add("Generate Rate: 32RF/t");
				break;
			case 3:
				event.toolTip.add("Generate Rate: 128RF/t");
				break;
			case 4:
				event.toolTip.add("Generate Rate: 512RF/t");
				break;
			case 5:
				event.toolTip.add("Generate Rate: 2048RF/t");
				break;
			}
		}
	}
}
