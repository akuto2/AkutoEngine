package akuto2;

import akuto2.blocks.BlockAkutoEngine;
import akuto2.items.ItemBlockAkutoEngine;
import akuto2.tiles.engines.TileAkutoEngine;
import akuto2.tiles.engines.TileAkutoEngine128;
import akuto2.tiles.engines.TileAkutoEngine2048;
import akuto2.tiles.engines.TileAkutoEngine32;
import akuto2.tiles.engines.TileAkutoEngine512;
import akuto2.tiles.engines.TileAkutoEngine8;
import akuto2.tiles.engines.TileFinalEngine;
import akuto2.tiles.engines.TileSuperEngine;
import akuto2.tiles.engines.TileSuperEngine2;
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
		GameRegistry.registerTileEntity(TileAkutoEngine.class, new ResourceLocation("akutoengine", "engines.akutoengine"));
		GameRegistry.registerTileEntity(TileAkutoEngine8.class, new ResourceLocation("akutoengine", "engines.akutoengine8"));
		GameRegistry.registerTileEntity(TileAkutoEngine32.class, new ResourceLocation("akutoengine", "engines.akutoengine32"));
		GameRegistry.registerTileEntity(TileAkutoEngine128.class, new ResourceLocation("akutoengine", "engines.akutoengine128"));
		GameRegistry.registerTileEntity(TileAkutoEngine512.class, new ResourceLocation("akutoengine", "engines.akutoengine512"));
		GameRegistry.registerTileEntity(TileAkutoEngine2048.class, new ResourceLocation("akutoengine", "engines.akutoengine2048"));
		GameRegistry.registerTileEntity(TileSuperEngine.class, new ResourceLocation("akutoengine", "engines.superengine"));
		GameRegistry.registerTileEntity(TileSuperEngine2.class, new ResourceLocation("akutoengine", "engines.superengine2"));
		GameRegistry.registerTileEntity(TileFinalEngine.class, new ResourceLocation("akutoengine", "engines.finalengine"));
	}
}
