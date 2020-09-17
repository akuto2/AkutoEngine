package akuto2.proxies;

import java.util.Collections;

import akuto2.ObjHandler;
import akuto2.renderer.engines.RenderAkutoEngine;
import akuto2.tiles.engines.TileAkutoEngine;
import akuto2.tiles.engines.TileAkutoEngine128;
import akuto2.tiles.engines.TileAkutoEngine2048;
import akuto2.tiles.engines.TileAkutoEngine32;
import akuto2.tiles.engines.TileAkutoEngine512;
import akuto2.tiles.engines.TileAkutoEngine8;
import akuto2.tiles.engines.TileFinalEngine;
import akuto2.tiles.engines.TileSuperEngine;
import akuto2.tiles.engines.TileSuperEngine2;
import akuto2.utils.enums.EnumEngineType;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = "akutoengine")
public class ClientProxy extends CommonProxy{

	@SubscribeEvent
	public static void ModelRegistry(ModelRegistryEvent event) {
		for(EnumEngineType engineType : EnumEngineType.VALUES) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ObjHandler.engineBlock), engineType.ordinal(), new ModelResourceLocation(engineType.resourceLocation, "inventory"));
		}

		ModelLoader.setCustomStateMapper((Block)ObjHandler.engineBlock, b -> Collections.emptyMap());
	}

	@Override
	public void registerTileEntitySpecialRenderer() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileAkutoEngine.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileAkutoEngine8.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileAkutoEngine32.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileAkutoEngine128.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileAkutoEngine512.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileAkutoEngine2048.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileSuperEngine.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileSuperEngine2.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileFinalEngine.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
	}
}
