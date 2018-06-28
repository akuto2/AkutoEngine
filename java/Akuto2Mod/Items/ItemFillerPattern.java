package Akuto2Mod.Items;

import java.util.List;

import Akuto2Mod.Akuto2Core;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemFillerPattern extends Item{
	public int maxItem;
	@SideOnly(Side.CLIENT)
	private IIcon[] icon;

	public ItemFillerPattern() {
		super();
		setMaxDamage(0);
		setHasSubtypes(true);
		setCreativeTab(Akuto2Core.tabAkutoEngine);
		maxItem = 10;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		icon = new IIcon[maxItem];

		for(int i = 0; i < maxItem; i++) {
			icon[i] = iconRegister.registerIcon("akutoengine:pattern/fillerpattern_" + i);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return icon[meta];
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		for(int i = 0; i < maxItem; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "fillerPattern." + stack.getItemDamage();
	}
}
