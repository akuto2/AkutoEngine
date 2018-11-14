package Akuto2Mod.Items;

import java.util.List;

import Akuto2Mod.Utils.AchievementHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class ItemBlockTankEX extends ItemBlock{
	public ItemBlockTankEX(Block tank){
		super(tank);
		setMaxDamage(0);
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		player.triggerAchievement(AchievementHandler.getTank);
		super.onCreated(stack, world, player);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		super.addInformation(stack, player, list, par4);
		if(stack.hasTagCompound()){
			NBTTagCompound compound = stack.stackTagCompound;
			if(compound.hasKey("tank")){
				compound = compound.getCompoundTag("tank");

				FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(compound);
				if(fluidStack != null){
					list.add(String.format("%s : %.3f / %d mb", new Object[]{fluidStack.getFluid().getLocalizedName(fluidStack), Double.valueOf(fluidStack.amount / 1000.D), Integer.valueOf(2100000000)}));
				}
			}
		}
	}

	@Override
	public boolean getShareTag(){
		return true;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack){
		return "tile.tankEX";
	}
}
