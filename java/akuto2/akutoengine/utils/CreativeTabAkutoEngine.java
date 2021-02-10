package akuto2.akutoengine.utils;

import akuto2.akutoengine.AkutoEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabAkutoEngine extends CreativeTabs {
	public CreativeTabAkutoEngine(String type) {
		super(type);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return Item.getItemFromBlock(AkutoEngine.engineBlock);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel(){
		return "AkutoEngine";
	}
}
