package Akuto2Mod.Compat.IC2;

import static Akuto2Mod.Akuto2Core.*;

import Akuto2Mod.Blocks.BlcokHEMFSU;
import Akuto2Mod.Blocks.BlockHUMFSU;
import Akuto2Mod.Blocks.BlockUMFSU;
import Akuto2Mod.Items.ItemBlockHemfsu;
import Akuto2Mod.Items.ItemBlockHumfsu;
import Akuto2Mod.Items.ItemBlockUmfsu;
import Akuto2Mod.TileEntity.TileEntityHemfsu;
import Akuto2Mod.TileEntity.TileEntityHumfsu;
import Akuto2Mod.TileEntity.TileEntityUmfsu;
import cpw.mods.fml.common.registry.GameRegistry;
import ic2.core.Ic2Items;
import net.minecraft.item.ItemStack;

public class IndustrialCraft2 {
	public static void preIC2() {
		umfsUint = new BlockUMFSU();
		register.register(umfsUint, ItemBlockUmfsu.class, "umfsUinit");
		humfsUint = new BlockHUMFSU();
		register.register(humfsUint, ItemBlockHumfsu.class, "humfsUinit");
		hemfsUint = new BlcokHEMFSU();
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
