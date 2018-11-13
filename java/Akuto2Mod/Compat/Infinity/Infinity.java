package Akuto2Mod.Compat.Infinity;

import static Akuto2Mod.Akuto2Core.*;

import cpw.mods.fml.common.registry.GameRegistry;
import infinitychest.InfinityChest;
import infinitychest.InfinityChestBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class Infinity {
	public static void pre() {
		infinityChest = new InfinityChestBlock().setBlockName("ExplosionProInfinityChest").setResistance(100.0F).setCreativeTab(tabAkutoEngine);
		GameRegistry.registerBlock(infinityChest, "ExplosionProInfinityChest");
	}

	public static void init() {
		GameRegistry.addRecipe(new ItemStack(infinityChest), "aaa", "aca", "aaa", 'a', Blocks.bedrock, 'c', InfinityChest.infinityChest);
	}
}
