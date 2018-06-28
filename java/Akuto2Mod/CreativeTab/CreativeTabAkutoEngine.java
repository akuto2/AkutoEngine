package Akuto2Mod.CreativeTab;

import Akuto2Mod.Akuto2Core;
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
		return Item.getItemFromBlock(Akuto2Core.engineBlock);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel(){
		return "AkutoEngine";
	}
}
