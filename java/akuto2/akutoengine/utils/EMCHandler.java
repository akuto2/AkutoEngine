package akuto2.akutoengine.utils;

import akuto2.akutoengine.AkutoEngine;
import buildcraft.BuildCraftSilicon;
import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.item.ItemStack;

public class EMCHandler {
	public static void registerEMC(){
		ProjectEAPI.getEMCProxy().registerCustomEMC(AkutoEngine.coreElementary1, 100000000);
		ProjectEAPI.getEMCProxy().registerCustomEMC(AkutoEngine.coreElementary2, 200000000);
		ProjectEAPI.getEMCProxy().registerCustomEMC(AkutoEngine.engineChip, 4096);
		ProjectEAPI.getEMCProxy().registerCustomEMC(new ItemStack(BuildCraftSilicon.redstoneChipset, 1, 0), 640);
		ProjectEAPI.getEMCProxy().registerCustomEMC(new ItemStack(BuildCraftSilicon.redstoneChipset, 1, 1), 1024);
		ProjectEAPI.getEMCProxy().registerCustomEMC(new ItemStack(BuildCraftSilicon.redstoneChipset, 1, 2), 2048);
		ProjectEAPI.getEMCProxy().registerCustomEMC(new ItemStack(BuildCraftSilicon.redstoneChipset, 1, 3), 8192);
	}

	public static int getEMC(ItemStack stack) {
		return ProjectEAPI.getEMCProxy().getValue(stack);
	}
}
