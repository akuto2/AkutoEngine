package akuto2.akutoengine;

import java.util.logging.Logger;

import akuto2.akutoengine.compat.Compat;
import akuto2.akutoengine.gui.GuiHandler;
import akuto2.akutoengine.proxies.CommonProxy;
import akuto2.akutoengine.utils.CreativeTabAkutoEngine;
import akuto2.akutoengine.utils.ModInfo;
import lib.utils.UpdateChecker;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "akutoengine", name = "AkutoEngine", version = "2.0.13", dependencies = "required-after:akutolib@[3.0.1,);required-after:buildcraftenergy;after:projecte")
public class AkutoEngine {
	@Instance("akutoengine")
	public static AkutoEngine instance;
	@Metadata("akutoengine")
	public static ModMetadata meta;
	@SidedProxy(clientSide = "akuto2.akutoengine.proxies.ClientProxy", serverSide = "akuto2.akutoengine.proxies.CommonProxy")
	public static CommonProxy proxy;

	public static UpdateChecker update = null;
	public static final CreativeTabs tabs = new CreativeTabAkutoEngine("AkutoEngine");

	public static Logger logger = Logger.getLogger("AkutoEngine");

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ModInfo.registerInfo(meta);
		update = new UpdateChecker("akutoengine", meta);
		update.checkUpdate();
		NetworkRegistry.INSTANCE.registerGuiHandler(AkutoEngine.instance, new GuiHandler());
		Compat.census();
	}

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		proxy.registerTileEntitySpecialRenderer();
		ObjHandler.registerTileEntity();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		if(update != null && event.getSide() == Side.SERVER) {
			update.notifyUpdate(event.getServer(), event.getSide());
		}
	}
}
