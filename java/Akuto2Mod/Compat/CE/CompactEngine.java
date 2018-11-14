package Akuto2Mod.Compat.CE;

import static Akuto2Mod.Akuto2Core.*;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CompactEngine {
	public static void init() {
		Item engineBlock = GameRegistry.findItem("CompactEngine", "compactengineblock");
		ItemStack engine1 = new ItemStack(engineBlock, 1, 0);
    	ItemStack engine2 = new ItemStack(engineBlock, 1, 1);
    	ItemStack engine3 = new ItemStack(engineBlock, 1, 2);
    	ItemStack engine4 = new ItemStack(engineBlock, 1, 3);
    	ItemStack engine5 = new ItemStack(engineBlock, 1, 4);
    	GameRegistry.addRecipe(autoEngine2, "e", "w", 'w', engine1, 'e', engineChip);
    	GameRegistry.addRecipe(autoEngine3, "e", "w", 'w', engine2, 'e', engineChip);
    	GameRegistry.addRecipe(autoEngine4, "e", "w", 'w', engine3, 'e', engineChip);
    	GameRegistry.addRecipe(autoEngine5, "e", "w", 'w', engine4, 'e', engineChip);
    	GameRegistry.addRecipe(autoEngine6, "e", "w", 'w', engine5, 'e', engineChip);
	}
}
