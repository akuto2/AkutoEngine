package akuto2.akutoengine.compat.infinity;

import static akuto2.akutoengine.AkutoEngine.*;

import cpw.mods.fml.common.registry.GameRegistry;
import infinitychest.InfinityChest;
import infinitychest.InfinityChestBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class Infinity {
	public static void pre() {
		exprosionProInfinityChest = new InfinityChestBlock().setBlockName("ExplosionProInfinityChest").setResistance(100.0F).setCreativeTab(tabAkutoEngine);
		GameRegistry.registerBlock(exprosionProInfinityChest, "ExplosionProInfinityChest");
	}

	public static void init() {
		GameRegistry.addRecipe(new ItemStack(exprosionProInfinityChest), "aaa", "aca", "aaa", 'a', Blocks.bedrock, 'c', InfinityChest.infinityChest);
	}
}
