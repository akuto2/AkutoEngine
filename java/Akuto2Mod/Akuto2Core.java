package Akuto2Mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import Akuto2Mod.Blocks.BlcokHEMFSU;
import Akuto2Mod.Blocks.BlockAutoEngine;
import Akuto2Mod.Blocks.BlockFilllerEX;
import Akuto2Mod.Blocks.BlockHUMFSU;
import Akuto2Mod.Blocks.BlockPumpEX;
import Akuto2Mod.Blocks.BlockTankEX;
import Akuto2Mod.Blocks.BlockUMFSU;
import Akuto2Mod.CreativeTab.CreativeTabAkutoEngine;
import Akuto2Mod.Event.CommonEventHandler;
import Akuto2Mod.Event.ToolTipEvent;
import Akuto2Mod.Gui.GuiHandler;
import Akuto2Mod.Items.ItemAutoEngine;
import Akuto2Mod.Items.ItemBlockHemfsu;
import Akuto2Mod.Items.ItemBlockHumfsu;
import Akuto2Mod.Items.ItemBlockTankEX;
import Akuto2Mod.Items.ItemBlockUmfsu;
import Akuto2Mod.Items.ItemFillerPattern;
import Akuto2Mod.Items.ItemKleinStarsEX;
import Akuto2Mod.Items.ItemPumpEX;
import Akuto2Mod.Items.engineCore;
import Akuto2Mod.Pattern.FillerEraser;
import Akuto2Mod.Pattern.FillerFillAll;
import Akuto2Mod.Pattern.FillerFillBox;
import Akuto2Mod.Pattern.FillerFillWall;
import Akuto2Mod.Pattern.FillerFlattener;
import Akuto2Mod.Pattern.FillerFlooring;
import Akuto2Mod.Pattern.FillerHoleFill;
import Akuto2Mod.Pattern.FillerPatternCore;
import Akuto2Mod.Pattern.FillerPatternRecipe;
import Akuto2Mod.Pattern.FillerRemover;
import Akuto2Mod.Pattern.FillerRemover2;
import Akuto2Mod.Pattern.FillerTorch;
import Akuto2Mod.Pattern.FillerTower;
import Akuto2Mod.Pattern.FillerUnderFill;
import Akuto2Mod.TileEntity.TileEntityHemfsu;
import Akuto2Mod.TileEntity.TileEntityHumfsu;
import Akuto2Mod.TileEntity.TileEntityUmfsu;
import Akuto2Mod.TileEntity.TileFillerEX;
import Akuto2Mod.TileEntity.TilePumpEX;
import Akuto2Mod.TileEntity.TileTankEX;
import Akuto2Mod.TileEntity.Engine.TileAutoEngine;
import Akuto2Mod.TileEntity.Engine.TileAutoEngine128;
import Akuto2Mod.TileEntity.Engine.TileAutoEngine2048;
import Akuto2Mod.TileEntity.Engine.TileAutoEngine32;
import Akuto2Mod.TileEntity.Engine.TileAutoEngine512;
import Akuto2Mod.TileEntity.Engine.TileAutoEngine8;
import Akuto2Mod.TileEntity.Engine.TileFinalEngine;
import Akuto2Mod.TileEntity.Engine.TileSuperEngine;
import Akuto2Mod.TileEntity.Engine.TileSuperEngine2;
import Akuto2Mod.Utils.ModInfo;
import Akuto2Mod.Utils.Update;
import buildcraft.BuildCraftBuilders;
import buildcraft.BuildCraftCore;
import buildcraft.BuildCraftFactory;
import buildcraft.BuildCraftSilicon;
import buildcraft.BuildCraftTransport;
import buildcraft.api.blueprints.BuilderAPI;
import buildcraft.api.recipes.BuildcraftRecipeRegistry;
import buildcraft.factory.schematics.SchematicPump;
import buildcraft.transport.gates.GateDefinition;
import buildcraft.transport.gates.ItemGate;
import compactengine.CompactEngine;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import ic2.core.Ic2Items;
import infinitychest.InfinityChest;
import infinitychest.InfinityChestBlock;
import moze_intel.projecte.gameObjs.ObjHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod (modid = "AkutoEngine", name = "AkutoEngine", version = "1.3.4", dependencies ="required-after:BuildCraft|Energy;after:IC2;after:ProjectE;", useMetadata = true)
public class Akuto2Core {
	@Instance("AkutoEngine")
	public static Akuto2Core instance;
	@Metadata("AkutoEngine")
	public static ModMetadata meta;
	@SidedProxy(clientSide = "Akuto2Mod.Client.ClientProxy", serverSide = "Akuto2Mod.CommonProxy")
	public static CommonProxy proxy;
	public static Update update = null;

	public static final CreativeTabs tabAkutoEngine = new CreativeTabAkutoEngine("AkutoEngine");
	boolean isFinalType;
	public static int intervalTorch;

	public static Block engineBlock;
	public static Block umfsUint;
	public static Block humfsUint;
	public static Block hemfsUint;
	public static Block infinityChest;
	public static Block fillerEX;
	public static Block TankEX;
	public static Block pumpEX;
	public static Item engineItem;
	public static Item engineChip;
	public static Item heatPearl;
	public static Item coreElementary1;
	public static Item coreElementary2;
	public static Item engineCore;
	public static Item engineCore2;
	public static Item kleinStars1;
	public static Item kleinStars2;
	public static ItemFillerPattern fillerModule;

	public static ItemStack autoEngine1;
	public static ItemStack autoEngine2;
	public static ItemStack autoEngine3;
	public static ItemStack autoEngine4;
	public static ItemStack autoEngine5;
	public static ItemStack autoEngine6;
	public static ItemStack superEngine1;
	public static ItemStack superEngine2;
	public static ItemStack finalEngine;
	public static ItemStack pumpEX1;
	public static ItemStack pumpEX2;
	public static ItemStack pumpEX3;
	public static ItemStack pumpEX4;
	public static ItemStack infinityPump;
	public static final List<String> TEXTURE_STRING_LIST = Arrays.asList(new String[]{"base_wood1", "base_wood2", "base_wood3", "base_wood4", "base_wood5", "base_wood6", "base_wood7", "base_wood8", "base_wood9"});
	public static final List<ResourceLocation> RESOURCE_LOCATION_LIST = new ArrayList();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		isFinalType = config.get("Finaltype", "mode", false, "Add Final type education recipe").getBoolean();
		intervalTorch = config.getInt("Filler", "intervalTorch", 6, 2, 64, "Torch Module Interval: 2 - 64");
		config.save();
		ModInfo.load(meta);
		update = new Update();
		update.checkUpdate();
		engineBlock = new BlockAutoEngine().setResistance(10.0f).setBlockName("AutoEngine:Akuto2Wood");
		GameRegistry.registerBlock(engineBlock, ItemAutoEngine.class, "autoengine");
		engineCore = (new engineCore()).setUnlocalizedName("engineCore").setTextureName("akutoengine:engineCore");
		GameRegistry.registerItem(engineCore, "engineCore");
		engineCore2 = (new engineCore()).setUnlocalizedName("engineCoreMk2").setTextureName("akutoengine:engineCoreMk2");
		GameRegistry.registerItem(engineCore2, "engineCoreMk2");
		coreElementary1 = (new engineCore()).setUnlocalizedName("coreElementary").setTextureName("akutoengine:coreElementary");
		GameRegistry.registerItem(coreElementary1, "coreElemntary");
		coreElementary2 = (new engineCore()).setUnlocalizedName("coreElementary2").setTextureName("akutoengine:coreElementary2");
		GameRegistry.registerItem(coreElementary2, "coreElemntary2");
		engineChip = (new engineCore()).setUnlocalizedName("engineChip").setTextureName("akutoengine:engineChip");
		GameRegistry.registerItem(engineChip, "engineChip");
		heatPearl = (new engineCore()).setUnlocalizedName("heatPearl").setTextureName("akutoengine:heatPearl");
		GameRegistry.registerItem(heatPearl, "heatPearl");
		TankEX = (new BlockTankEX()).setCreativeTab(tabAkutoEngine);
		GameRegistry.registerBlock(TankEX, ItemBlockTankEX.class, "TankEX");
		pumpEX = (new BlockPumpEX()).setCreativeTab(tabAkutoEngine);
		GameRegistry.registerBlock(pumpEX, ItemPumpEX.class, "pumpEX");
		BuilderAPI.schematicRegistry.registerSchematicBlock(pumpEX, SchematicPump.class, new Object[0]);
		fillerEX = (new BlockFilllerEX());
		GameRegistry.registerBlock(fillerEX, "fillerEX");
		fillerModule = new ItemFillerPattern();
		GameRegistry.registerItem(fillerModule, "fillerModule");
		registerFiller(new FillerFillAll(), "bbb", "bbb", "bbb", 0);
		registerFiller(new FillerEraser(), "ggg", "g g", "ggg", 1);
		registerFiller(new FillerRemover(), "ggg", "ggg", "ggg", 2);
		registerFiller(new FillerRemover2(), "   ", "ggg", "ggg", 3);
		registerFiller(new FillerFlattener(), "   ", "ggg", "bbb", 4);
		registerFiller(new FillerHoleFill(), "   ", "   ", "bbb", 5);
		registerFiller(new FillerUnderFill(), "   ", "bbb", "bbb", 6);
		registerFiller(new FillerFillBox(), "bbb", "b b", "bbb", 7);
		registerFiller(new FillerFillWall(), "b b", "b b", "b b", 8);
		registerFiller(new FillerFlooring(), "   ", "bbb", "ggg", 9);
		registerFiller(new FillerTorch(), "b b", " b ", "b b", 10);
		registerFiller(new FillerTower(), " bb", " bb", " bb", 11);
		GameRegistry.registerTileEntity(TileFillerEX.class, "tile.fillerEX");
		if(Loader.isModLoaded("IC2")){
			preIC2();
		}
		if(Loader.isModLoaded("ProjectE")){
			prePE();
		}
		if(Loader.isModLoaded("InfinityChest")){
			infinityChest = new InfinityChestBlock().setBlockName("ExplosionProInfinityChest").setResistance(100.0F).setCreativeTab(tabAkutoEngine);
			GameRegistry.registerBlock(infinityChest, "ExplosionProInfinityChest");
		}
	}

	@Mod.EventHandler
	public void Init(FMLInitializationEvent event){
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		autoEngine1 = new ItemStack(engineBlock, 1, 0);
		autoEngine2 = new ItemStack(engineBlock, 1, 1);
		autoEngine3 = new ItemStack(engineBlock, 1, 2);
		autoEngine4 = new ItemStack(engineBlock, 1, 3);
		autoEngine5 = new ItemStack(engineBlock, 1, 4);
		autoEngine6 = new ItemStack(engineBlock, 1, 5);
		superEngine1 = new ItemStack(engineBlock, 1, 6);
		superEngine2 = new ItemStack(engineBlock, 1, 7);
		pumpEX1 = new ItemStack(pumpEX, 1, 0);
		pumpEX2 = new ItemStack(pumpEX, 1, 2);
		pumpEX3 = new ItemStack(pumpEX, 1, 3);
		infinityPump = new ItemStack(pumpEX, 1, 1);

		FMLCommonHandler.instance().bus().register(new CommonEventHandler());
		MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
		MinecraftForge.EVENT_BUS.register(new ToolTipEvent());
		proxy.registerTileEntitySpecialRenderer();
		GameRegistry.registerTileEntity(TileAutoEngine.class, "tile.autoengine");
		GameRegistry.registerTileEntity(TileAutoEngine8.class, "tile.autoengine8");
		GameRegistry.registerTileEntity(TileAutoEngine32.class, "tile.autoengine32");
		GameRegistry.registerTileEntity(TileAutoEngine128.class, "tile.autoengine128");
		GameRegistry.registerTileEntity(TileAutoEngine512.class, "tile.autoengine512");
		GameRegistry.registerTileEntity(TileAutoEngine2048.class, "tile.autoengine2048");
		GameRegistry.registerTileEntity(TileSuperEngine.class, "tile.superengine");
		GameRegistry.registerTileEntity(TileSuperEngine2.class, "tile.superengine2");
		GameRegistry.registerTileEntity(TileFinalEngine.class, "tile.finalengine");
		GameRegistry.registerTileEntity(TileTankEX.class, "tile.tankEX");
		GameRegistry.registerTileEntity(TilePumpEX.class, "tile.pumpEX");

		ItemStack woodEngine = new ItemStack(BuildCraftCore.engineBlock, 1, 0);
		ItemStack ironEngine = new ItemStack(BuildCraftCore.engineBlock, 1, 2);
		ItemStack ironGear = new ItemStack(BuildCraftCore.ironGearItem);
		ItemStack diaGear = new ItemStack(BuildCraftCore.diamondGearItem);
		ItemStack diaChip = new ItemStack(BuildCraftSilicon.redstoneChipset, 1, 3);
		ItemStack redstoneChip = new ItemStack(BuildCraftSilicon.redstoneChipset, 1, 0);
		ItemStack ironANDGate = ItemGate.makeGateItem(GateDefinition.GateMaterial.IRON, GateDefinition.GateLogic.AND);
		ItemStack goldANDGate = ItemGate.makeGateItem(GateDefinition.GateMaterial.GOLD, GateDefinition.GateLogic.AND);
		ItemStack goldORGate = ItemGate.makeGateItem(GateDefinition.GateMaterial.GOLD, GateDefinition.GateLogic.OR);
		ItemStack diaANDGate = ItemGate.makeGateItem(GateDefinition.GateMaterial.DIAMOND, GateDefinition.GateLogic.AND);
		ItemStack diaORGate = ItemGate.makeGateItem(GateDefinition.GateMaterial.DIAMOND, GateDefinition.GateLogic.OR);
		ItemStack ironFluidPipe = new ItemStack(BuildCraftTransport.pipeFluidsIron, 1, 0);
		ItemStack goldFluidPipe = new ItemStack(BuildCraftTransport.pipeFluidsGold, 1, 0);
		ItemStack diamondFluidPipe = new ItemStack(BuildCraftTransport.pipeFluidsDiamond, 1, 0);
		ItemStack filler = new ItemStack(BuildCraftBuilders.fillerBlock, 1, 0);
		ItemStack tank = new ItemStack(BuildCraftFactory.tankBlock, 1, 0);
		ItemStack pump = new ItemStack(BuildCraftFactory.pumpBlock, 1, 0);

		GameRegistry.addRecipe(new ItemStack(heatPearl), "bb", "bb", 'b', Items.blaze_rod);
		GameRegistry.addRecipe(autoEngine1, "e", "w", 'w', woodEngine, 'e', engineChip);
		GameRegistry.addRecipe(autoEngine2, "aaa", "aia", "aaa", 'a', autoEngine1, 'i', ironGear);
		GameRegistry.addRecipe(autoEngine3, "dad", "aia", "dad", 'a', autoEngine2, 'd', diaGear, 'i', ironEngine);
		GameRegistry.addRecipe(autoEngine4, "dad", "aia", "dad", 'a', autoEngine3, 'd', diaChip, 'i', ironEngine);
		GameRegistry.addRecipe(autoEngine5, "gag", "aia", "gag", 'a', autoEngine4, 'g', goldORGate, 'i', ironEngine);
		GameRegistry.addRecipe(autoEngine6, "dad", "aia", "dad", 'a', autoEngine5, 'd', diaORGate, 'i', ironEngine);
		GameRegistry.addRecipe(superEngine1, "eae", "aia", "eae", 'a', autoEngine6, 'e', engineCore, 'i', ironEngine);
		GameRegistry.addRecipe(superEngine2, "eae", "aea", "eae", 'a', superEngine1, 'e', engineCore2);
		GameRegistry.addRecipe(new ItemStack(engineCore), " e ", "ece", " e ", 'e', engineChip, 'c', coreElementary1);
		GameRegistry.addRecipe(new ItemStack(engineCore2), "aca", "cec", "aca", 'c', coreElementary1, 'e', coreElementary2, 'a', engineChip);
		GameRegistry.addRecipe(new ItemStack(fillerEX), "b", 'b', filler);
		GameRegistry.addRecipe(filler, "b", 'b', fillerEX);
		GameRegistry.addRecipe(pumpEX1, "tit", "gpg", "sis", 't', tank, 'g', ironANDGate, 'p', pump, 'i', ironFluidPipe, 's', new ItemStack(Items.dye, 1, 8));
		GameRegistry.addRecipe(infinityPump, "tct", "wpl", "bgb", 't', TankEX, 'p', pumpEX1, 'c', engineCore2, 'w', Items.water_bucket, 'l', Items.lava_bucket, 'g', goldFluidPipe, 'b', new ItemStack(Items.dye, 1, 12));
		GameRegistry.addRecipe(pumpEX2, "tgt", "apa", "sgs", 't', tank, 'g', goldFluidPipe, 'p', pumpEX1, 'a', goldANDGate, 's', new ItemStack(Items.dye, 1, 8));
		GameRegistry.addRecipe(pumpEX3, "tdt", "apa", "sds", 't', tank, 'd', diamondFluidPipe, 'p', pumpEX2, 'a', diaANDGate, 's', new ItemStack(Items.dye, 1, 0));
		GameRegistry.addRecipe(new ItemStack(TankEX), "eae", "ata", "eae", 'e', engineCore, 'a', engineCore2, 't', tank);
		BuildcraftRecipeRegistry.assemblyTable.addRecipe("engineChip", Math.round(600000.0F * BuildCraftSilicon.chipsetCostMultiplier), new ItemStack(engineChip), new Object[]{redstoneChip, new ItemStack(heatPearl, 4), Blocks.lever});
		if(isFinalType){
			finalEngine = new ItemStack(engineBlock, 1, 8);
			GameRegistry.addRecipe(finalEngine, "bab", "aea", "bab", 'e', superEngine2, 'b', Blocks.bedrock, 'a', engineCore2);
			pumpEX4 = new ItemStack(pumpEX, 1, 4);
			GameRegistry.addRecipe(pumpEX4, "tct", "dpd", "bcb", 't', TankEX, 'c', engineCore2, 'd', diaANDGate, 'p', pumpEX3, 'b', Blocks.bedrock);
		}
		if (Loader.isModLoaded("IC2")){
			initIC2();
	    }
	    if (Loader.isModLoaded("ProjectE")){
	    	initPE();
	    }
	    if(Loader.isModLoaded("CompactEngine")){
	    	initCE();
	    }
	    if(Loader.isModLoaded("InfinityChest")){
	    	GameRegistry.addRecipe(new ItemStack(infinityChest), "aaa", "aca", "aaa", 'a', Blocks.bedrock, 'c', InfinityChest.infinityChest);
	    }
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event){

	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		if((update != null) && (event.getSide() == Side.SERVER)) {
			update.notifyUpdate(event.getServer(), event.getSide());
		}
	}

	public void registerFiller(FillerPatternCore pattern, String s1, String s2, String s3, int meta) {
		FillerPatternRecipe.addRecipe(pattern, s1,s2,s3, 'b', Blocks.brick_block, 'g', Blocks.glass);
		registerFiller(pattern, meta);
	}

	public void registerFiller(FillerPatternCore pattern, int meta) {
		pattern.moduleItem = new ItemStack(fillerModule, 1, meta);
		FillerPatternRecipe.addRecipe(pattern, pattern.moduleItem);
		fillerModule.maxItem = meta + 1;
	}

	public void preIC2() {
		umfsUint = new BlockUMFSU();
		GameRegistry.registerBlock(umfsUint, ItemBlockUmfsu.class, "umfsUinit");
		humfsUint = new BlockHUMFSU();
		GameRegistry.registerBlock(humfsUint, ItemBlockHumfsu.class, "humfsUinit");
		hemfsUint = new BlcokHEMFSU();
		GameRegistry.registerBlock(hemfsUint, ItemBlockHemfsu.class, "hemfsUinit");
		GameRegistry.registerTileEntity(TileEntityUmfsu.class, "tile.umfsu");
		GameRegistry.registerTileEntity(TileEntityHumfsu.class, "tile.humfsu");
		GameRegistry.registerTileEntity(TileEntityHemfsu.class, "tile.hemfsu");
	}

	public void initIC2() {
		ItemStack MFSU = Ic2Items.mfsUnit;
	    ItemStack Iridium = Ic2Items.iridiumPlate;
	    ItemStack AdvBlock = Ic2Items.advancedMachine;
	    GameRegistry.addRecipe(new ItemStack(umfsUint), new Object[] { "mam", "imi", "mam", Character.valueOf('m'), MFSU, Character.valueOf('a'), coreElementary2, Character.valueOf('i'), Iridium });
	    GameRegistry.addRecipe(new ItemStack(humfsUint), new Object[] { "imi", "mam", "imi", Character.valueOf('i'), Iridium, Character.valueOf('a'), AdvBlock, Character.valueOf('m'), umfsUint });
	    GameRegistry.addRecipe(new ItemStack(hemfsUint), new Object[] { "huh", "iai", "huh", Character.valueOf('h'), humfsUint, Character.valueOf('u'), umfsUint, Character.valueOf('a'), AdvBlock, Character.valueOf('i'), Iridium });
	}

	public void prePE() {
		kleinStars1 = new ItemKleinStarsEX(409600000).setTextureName("akutoengine:kleinstars1").setUnlocalizedName("kleinStarsEX1");
		GameRegistry.registerItem(kleinStars1, "kleinStarsEx1");
		kleinStars2 = new ItemKleinStarsEX(2048000000).setTextureName("akutoengine:kleinstars2").setUnlocalizedName("kleinStarsEX2");
		GameRegistry.registerItem(kleinStars2, "kleinStarsEx2");
	}

	public void initPE() {
		EMCHandler.registerEMC();
	    ItemStack Stars = new ItemStack(ObjHandler.kleinStars, 1, 5);
	    GameRegistry.addRecipe(new ItemStack(kleinStars1), new Object[] { "xxx", "x x", "xxx", Character.valueOf('x'), Stars });
	    GameRegistry.addRecipe(new ItemStack(kleinStars2), new Object[] { " x ", "xxx", " x ", Character.valueOf('x'), kleinStars1 });
	}

	public void initCE() {
		ItemStack engine1 = new ItemStack(CompactEngine.engineBlock, 1, 0);
    	ItemStack engine2 = new ItemStack(CompactEngine.engineBlock, 1, 1);
    	ItemStack engine3 = new ItemStack(CompactEngine.engineBlock, 1, 2);
    	ItemStack engine4 = new ItemStack(CompactEngine.engineBlock, 1, 3);
    	ItemStack engine5 = new ItemStack(CompactEngine.engineBlock, 1, 4);
    	GameRegistry.addRecipe(autoEngine2, "e", "w", 'w', engine1, 'e', engineChip);
    	GameRegistry.addRecipe(autoEngine3, "e", "w", 'w', engine2, 'e', engineChip);
    	GameRegistry.addRecipe(autoEngine4, "e", "w", 'w', engine3, 'e', engineChip);
    	GameRegistry.addRecipe(autoEngine5, "e", "w", 'w', engine4, 'e', engineChip);
    	GameRegistry.addRecipe(autoEngine6, "e", "w", 'w', engine5, 'e', engineChip);
	}



	public static void addChat(String message)
	{

		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(message));
		}
		else if(FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
            MinecraftServer.getServer().getConfigurationManager().sendChatMsgImpl(new ChatComponentText(message), true);
		}
	}

	public static void addChat(String format,Object... args)
	{
		addChat(String.format(format,args));
	}
	static {
		Iterator arg = TEXTURE_STRING_LIST.iterator();
		while(arg.hasNext()){
			String str = (String)arg.next();
			RESOURCE_LOCATION_LIST.add(new ResourceLocation("AkutoEngine".toLowerCase(), String.format("textures/blocks/%s.png", new Object[]{str})));
		}
	}
}
