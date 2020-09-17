package akuto2.utils;

import akuto2.ObjHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabAkutoEngine extends CreativeTabs{
	public CreativeTabAkutoEngine(String name) {
		super(name);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ObjHandler.engineBlock, 1, 0);
	}

	@Override
	public String getTranslatedTabLabel() {
		return "AkutoEngine";
	}
}
