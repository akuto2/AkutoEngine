package akuto2.compat.pe.items;

import javax.annotation.Nonnull;

import akuto2.utils.Utils;
import moze_intel.projecte.PECore;
import moze_intel.projecte.api.item.IItemEmc;
import moze_intel.projecte.gameObjs.items.ItemPE;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemKleinStarsEX extends ItemPE implements IItemEmc{
	public ItemKleinStarsEX() {
		setUnlocalizedName("kleinStarsEX");
		setHasSubtypes(true);
		setMaxDamage(0);
		setMaxStackSize(1);
		setNoRepair();
	}

	@Override
	public boolean showDurabilityBar(@Nonnull ItemStack stack) {
		return stack.hasTagCompound();
	}

	@Override
	public double getDurabilityForDisplay(@Nonnull ItemStack stack) {
		long starEmc = getEmc(stack);
		return starEmc == 0L ? 1.0D : 1.0D - (double)starEmc / (double)Utils.getKleinStarEXMaxEmc(stack);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		if(!worldIn.isRemote && PECore.DEV_ENVIRONMENT) {
			setEmc(stack, Utils.getKleinStarEXMaxEmc(stack));
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		}
		else {
			return ActionResult.newResult(EnumActionResult.PASS, stack);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + (stack.getItemDamage() + 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(isInCreativeTab(tab)) {
			for(int i = 0; i < 2; i++) {
				items.add(new ItemStack(this, 1, i));
			}
		}
	}

	@Override
	public long addEmc(@Nonnull ItemStack stack, long emc) {
		long add = Math.min(getMaximumEmc(stack) - getStoredEmc(stack), emc);
		ItemPE.addEmcToStack(stack, add);
		return add;
	}

	@Override
	public long extractEmc(@Nonnull ItemStack stack, long emc) {
		long sub = Math.min(getStoredEmc(stack), emc);
		ItemPE.removeEmc(stack, sub);
		return sub;
	}

	@Override
	public long getMaximumEmc(@Nonnull ItemStack stack) {
		return Utils.getKleinStarEXMaxEmc(stack);
	}

	@Override
	public long getStoredEmc(@Nonnull ItemStack stack) {
		return ItemPE.getEmc(stack);
	}

}
