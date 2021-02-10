package akuto2.akutoengine.items;

import akuto2.akutoengine.AkutoEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moze_intel.projecte.api.item.IItemEmc;
import moze_intel.projecte.gameObjs.items.ItemPE;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemKleinStarsEX extends ItemPE implements IItemEmc {
	private int maxemc;
	@SideOnly(Side.CLIENT)
	private IIcon icons;

	public ItemKleinStarsEX(int maxemc){
		this.maxemc = maxemc;
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setCreativeTab(AkutoEngine.tabAkutoEngine);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return stack.hasTagCompound();
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		double starEmc = getEmc(stack);

		if (starEmc == 0)
		{
			return 1.0D;
		}

		return 1.0D - starEmc / this.maxemc;
	}


	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		return stack;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
	{
		if (!stack.hasTagCompound())
		{
			stack.stackTagCompound = new NBTTagCompound();
		}
	}

	@Override
	public double addEmc(ItemStack stack, double toAdd)
	{
		double add = Math.min(getMaximumEmc(stack) - getStoredEmc(stack), toAdd);
		ItemPE.addEmcToStack(stack, add);
		return add;
	}

	@Override
	public double extractEmc(ItemStack stack, double toRemove)
	{
		double sub = Math.min(getStoredEmc(stack), toRemove);
		ItemPE.removeEmc(stack, sub);
		return sub;
	}

	@Override
	public double getStoredEmc(ItemStack stack)
	{
		return ItemPE.getEmc(stack);
	}

	@Override
	public double getMaximumEmc(ItemStack stack)
	{
		return this.maxemc;
	}
}
