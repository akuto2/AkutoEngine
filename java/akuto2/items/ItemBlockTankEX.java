package akuto2.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class ItemBlockTankEX extends ItemBlock{
	public ItemBlockTankEX(Block block) {
		super(block);
		setMaxDamage(0);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if(stack.hasTagCompound()) {
			NBTTagCompound compound = stack.getTagCompound();
			if(compound.hasKey("tank")) {
				compound = compound.getCompoundTag("tank");

				FluidStack fluid = FluidStack.loadFluidStackFromNBT(compound);
				if(fluid != null) {
					tooltip.add(String.format("%s : %.3f / %d mb", new Object[] { fluid.getFluid().getLocalizedName(fluid), Double.valueOf(fluid.amount / 1000.0D), Integer.valueOf(2100000000) }));
				}
			}
		}
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName();
	}
}
