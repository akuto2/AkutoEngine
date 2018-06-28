package Akuto2Mod.Utils;

import Akuto2Mod.EMCHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class EMCWorldSave extends WorldSavedData{
	public static long emc = 0L;
	private static long emcLimit = 9000000000000000000L;

	public EMCWorldSave(String fileName) {
		super(fileName);
	}

	public boolean addEMC(ItemStack stack) {
		if(emc >= emcLimit) {
			return false;
		}
		emc += EMCHandler.getEMC(stack);
		if(emc > emcLimit) {
			emc = emcLimit;
		}
		markDirty();
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		emc = compound.getLong("EMCBuilder_EMC");
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		compound.setLong("EMCBuilder_EMC", emc);
	}
}
