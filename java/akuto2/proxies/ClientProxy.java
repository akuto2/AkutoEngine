package akuto2.proxies;

import java.util.Collections;

import akuto2.ObjHandler;
import akuto2.renderer.engines.RenderAkutoEngine;
import akuto2.tiles.engines.TileEntityAkutoEngine;
import akuto2.tiles.engines.TileEntityAkutoEngine128;
import akuto2.tiles.engines.TileEntityAkutoEngine2048;
import akuto2.tiles.engines.TileEntityAkutoEngine32;
import akuto2.tiles.engines.TileEntityAkutoEngine512;
import akuto2.tiles.engines.TileEntityAkutoEngine8;
import akuto2.tiles.engines.TileEntityFinalEngine;
import akuto2.tiles.engines.TileEntitySuperEngine;
import akuto2.tiles.engines.TileEntitySuperEngine2;
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
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAkutoEngine.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAkutoEngine8.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAkutoEngine32.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAkutoEngine128.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAkutoEngine512.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAkutoEngine2048.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySuperEngine.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySuperEngine2.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFinalEngine.class, (TileEntitySpecialRenderer)RenderAkutoEngine.INSTANCE);
	}
}
