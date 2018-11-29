package Akuto2Mod.Compat.PE;

import static Akuto2Mod.Akuto2Core.*;

import Akuto2Mod.EMCHandler;
import Akuto2Mod.Items.ItemKleinStarsEX;
import Akuto2Mod.TileEntity.TileEmcContainer;
import cpw.mods.fml.common.registry.GameRegistry;
import moze_intel.projecte.gameObjs.ObjHandler;
import net.minecraft.item.ItemStack;

public class ProjectE {
	public static void prePE() {
		kleinStars1 = new ItemKleinStarsEX(409600000).setTextureName("akutoengine:kleinstars1").setUnlocalizedName("kleinStarsEX1");
		GameRegistry.registerItem(kleinStars1, "kleinStarsEx1");
		kleinStars2 = new ItemKleinStarsEX(2048000000).setTextureName("akutoengine:kleinstars2").setUnlocalizedName("kleinStarsEX2");
		GameRegistry.registerItem(kleinStars2, "kleinStarsEx2");
//		emcContainer = new EmcContainer().setCreativeTab(tabAkutoEngine);
//		GameRegistry.registerBlock(emcContainer, ItemBlockEMCContainer.class, "emcContainer");
	}

	public static void initPE() {
		GameRegistry.registerTileEntity(TileEmcContainer.class, "tile.emcContainer");
		EMCHandler.registerEMC();
	    ItemStack Stars = new ItemStack(ObjHandler.kleinStars, 1, 5);
	    GameRegistry.addRecipe(new ItemStack(kleinStars1), new Object[] { "xxx", "x x", "xxx", Character.valueOf('x'), Stars });
	    GameRegistry.addRecipe(new ItemStack(kleinStars2), new Object[] { " x ", "xxx", " x ", Character.valueOf('x'), kleinStars1 });
	}
}
