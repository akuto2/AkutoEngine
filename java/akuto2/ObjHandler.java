package akuto2;

import akuto2.blocks.BlockAkutoEngine;
import akuto2.items.ItemBlockAkutoEngine;
import akuto2.tiles.engines.TileEntityAkutoEngine;
import akuto2.tiles.engines.TileEntityAkutoEngine128;
import akuto2.tiles.engines.TileEntityAkutoEngine2048;
import akuto2.tiles.engines.TileEntityAkutoEngine32;
import akuto2.tiles.engines.TileEntityAkutoEngine512;
import akuto2.tiles.engines.TileEntityAkutoEngine8;
import akuto2.tiles.engines.TileEntityFinalEngine;
import akuto2.tiles.engines.TileEntitySuperEngine;
import akuto2.tiles.engines.TileEntitySuperEngine2;
import lib.utils.Register;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber(modid = "akutoengine")
public class ObjHandler {
	public static BlockAkutoEngine engineBlock;
	public static Block fillerEX;

	public static Register register = new Register("akutoengine", AkutoEngine.tabs);

	@SubscribeEvent
	public static void registerBlock(RegistryEvent.Register<Block> event) {
		register.setRegistry(event.getRegistry());
		engineBlock = new BlockAkutoEngine();

		register.register(engineBlock, "akutoengine");
	}

	@SubscribeEvent
	public static void registerItem(RegistryEvent.Register<Item> event) {
		register.setRegistry(event.getRegistry());
		register.register(new ItemBlockAkutoEngine(engineBlock).setRegistryName(engineBlock.getRegistryName()));
	}

	public static void registerTileEntity() {
		GameRegistry.registerTileEntity(TileEntityAkutoEngine.class, new ResourceLocation("akutoengine", "engines.akutoengine"));
		GameRegistry.registerTileEntity(TileEntityAkutoEngine8.class, new ResourceLocation("akutoengine", "engines.akutoengine8"));
		GameRegistry.registerTileEntity(TileEntityAkutoEngine32.class, new ResourceLocation("akutoengine", "engines.akutoengine32"));
		GameRegistry.registerTileEntity(TileEntityAkutoEngine128.class, new ResourceLocation("akutoengine", "engines.akutoengine128"));
		GameRegistry.registerTileEntity(TileEntityAkutoEngine512.class, new ResourceLocation("akutoengine", "engines.akutoengine512"));
		GameRegistry.registerTileEntity(TileEntityAkutoEngine2048.class, new ResourceLocation("akutoengine", "engines.akutoengine2048"));
		GameRegistry.registerTileEntity(TileEntitySuperEngine.class, new ResourceLocation("akutoengine", "engines.superengine"));
		GameRegistry.registerTileEntity(TileEntitySuperEngine2.class, new ResourceLocation("akutoengine", "engines.superengine2"));
		GameRegistry.registerTileEntity(TileEntityFinalEngine.class, new ResourceLocation("akutoengine", "engines.finalengine"));
	}
}
