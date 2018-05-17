package Akuto2Mod.Client;

import Akuto2Mod.Akuto2Core;
import Akuto2Mod.CommonProxy;
import Akuto2Mod.Renderer.RenderFillerEX;
import Akuto2Mod.Renderer.RenderTankEX;
import Akuto2Mod.TileEntity.TileFillerEX;
import Akuto2Mod.TileEntity.TileTankEX;
import Akuto2Mod.TileEntity.Engine.TileAutoEngineCore;
import buildcraft.core.lib.engines.RenderEngine;
import buildcraft.core.lib.engines.TileEngineBase;
import buildcraft.core.render.RenderingEntityBlocks;
import buildcraft.core.render.RenderingEntityBlocks.EntityRenderIndex;
import buildcraft.factory.render.RenderTank;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy{
	private static ResourceLocation chamberResourceLocation = new ResourceLocation("buildcraftenergy:textures/blocks/engineStone/chamber.png");

	public void registerRenderInformation(){}

	@Override
	public void registerTileEntitySpecialRenderer() {
		 ClientRegistry.bindTileEntitySpecialRenderer(TileAutoEngineCore.class, new RenderEngine());
		 ClientRegistry.bindTileEntitySpecialRenderer(TileTankEX.class, new RenderTank());
		 ClientRegistry.bindTileEntitySpecialRenderer(TileFillerEX.class, new RenderFillerEX());
         for(int i = 0; i < 9; ++i) {
            RenderingEntityBlocks.blockByEntityRenders.put(new EntityRenderIndex(Akuto2Core.engineBlock, i), new RenderEngine((ResourceLocation)Akuto2Core.RESOURCE_LOCATION_LIST.get(i), chamberResourceLocation, TileEngineBase.TRUNK_RED_TEXTURE));
         }
         MinecraftForgeClient.registerItemRenderer(GameRegistry.findItem("AkutoEngine", "TankEX"), new RenderTankEX());
	}

	@Override
	public World getClientWorld()
	{
		return FMLClientHandler.instance().getClient().theWorld;
	}
}
