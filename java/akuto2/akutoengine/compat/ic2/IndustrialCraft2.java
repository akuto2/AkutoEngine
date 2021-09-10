package akuto2.akutoengine.compat.ic2;

import static akuto2.akutoengine.AkutoEngine.*;

import akuto2.akutoengine.blocks.BlockHEMFSU;
import akuto2.akutoengine.blocks.BlockHUMFSU;
import akuto2.akutoengine.blocks.BlockUMFSU;
import akuto2.akutoengine.items.ItemBlockHemfsu;
import akuto2.akutoengine.items.ItemBlockHumfsu;
import akuto2.akutoengine.items.ItemBlockUmfsu;
import akuto2.akutoengine.tiles.TileEntityHemfsu;
import akuto2.akutoengine.tiles.TileEntityHumfsu;
import akuto2.akutoengine.tiles.TileEntityUmfsu;
import cpw.mods.fml.common.registry.GameRegistry;
import ic2.core.Ic2Items;
import net.minecraft.item.ItemStack;

public class IndustrialCraft2 {
	public static void preIC2() {
		umfsUint = new BlockUMFSU();
		register.register(umfsUint, ItemBlockUmfsu.class, "umfsUinit");
		humfsUint = new BlockHUMFSU();
		register.register(humfsUint, ItemBlockHumfsu.class, "humfsUinit");
		hemfsUint = new BlockHEMFSU();
		register.register(hemfsUint, ItemBlockHemfsu.class, "hemfsUinit");
		GameRegistry.registerTileEntity(TileEntityUmfsu.class, "tile.umfsu");
		GameRegistry.registerTileEntity(TileEntityHumfsu.class, "tile.humfsu");
		GameRegistry.registerTileEntity(TileEntityHemfsu.class, "tile.hemfsu");
	}

	public static void initIC2() {
		ItemStack MFSU = Ic2Items.mfsUnit;
	    ItemStack Iridium = Ic2Items.iridiumPlate;
	    ItemStack AdvBlock = Ic2Items.advancedMachine;
	    GameRegistry.addRecipe(new ItemStack(umfsUint), new Object[] { "mam", "imi", "mam", Character.valueOf('m'), MFSU, Character.valueOf('a'), coreElementary2, Character.valueOf('i'), Iridium });
	    GameRegistry.addRecipe(new ItemStack(humfsUint), new Object[] { "imi", "mam", "imi", Character.valueOf('i'), Iridium, Character.valueOf('a'), AdvBlock, Character.valueOf('m'), umfsUint });
	    GameRegistry.addRecipe(new ItemStack(hemfsUint), new Object[] { "huh", "iai", "huh", Character.valueOf('h'), humfsUint, Character.valueOf('u'), umfsUint, Character.valueOf('a'), AdvBlock, Character.valueOf('i'), Iridium });
	}
}
