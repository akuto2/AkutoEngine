package akuto2.proxies;

import java.util.Collections;

import akuto2.ObjHandler;
import akuto2.renderer.RenderAkutoEngine;
import akuto2.renderer.RenderFillerEX;
import akuto2.tiles.TileEntityFillerEX;
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
		ModelLoader.setCustomStateMapper((Block)ObjHandler.engineBlock, b -> Collections.emptyMap());

		for(EnumEngineType engineType : EnumEngineType.VALUES) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ObjHandler.engineBlock), engineType.ordinal(), new ModelResourceLocation(engineType.resourceLocation, "inventory"));
		}
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ObjHandler.fillerEX), 0, new ModelResourceLocation("akutoengine:fillerex", "inventory"));
		for(int i = 0; i < ObjHandler.fillerModule.maxItem; i++) {
			ModelLoader.setCustomModelResourceLocation(ObjHandler.fillerModule, i, new ModelResourceLocation("akutoengine:fillermodule/fillermodule_" + i, "inventory"));
		}

		ModelLoader.setCustomModelResourceLocation(ObjHandler.engineChip, 0, new ModelResourceLocation("akutoengine:enginechip", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ObjHandler.heatPearl, 0, new ModelResourceLocation("akutoengine:heatpearl", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ObjHandler.engineCore, 0, new ModelResourceLocation("akutoengine:enginecore", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ObjHandler.engineCore2, 0, new ModelResourceLocation("akutoengine:enginecoremk2", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ObjHandler.coreElementary, 0, new ModelResourceLocation("akutoengine:coreelementary", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ObjHandler.coreElementary2, 0, new ModelResourceLocation("akutoengine:coreelementary2", "inventory"));
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
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFillerEX.class, (TileEntitySpecialRenderer)new RenderFillerEX());
	}
}
