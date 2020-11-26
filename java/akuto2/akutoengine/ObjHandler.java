package akuto2.akutoengine;

import com.google.common.collect.ImmutableSet;

import akuto2.akutoengine.blocks.BlockAkutoEngine;
import akuto2.akutoengine.blocks.BlockFillerEX;
import akuto2.akutoengine.blocks.BlockTankEX;
import akuto2.akutoengine.compat.Compat;
import akuto2.akutoengine.items.ItemBlockAkutoEngine;
import akuto2.akutoengine.items.ItemBlockTankEX;
import akuto2.akutoengine.items.ItemFillerPattern;
import akuto2.akutoengine.patterns.FillerClearLiquid;
import akuto2.akutoengine.patterns.FillerEraser;
import akuto2.akutoengine.patterns.FillerFillAll;
import akuto2.akutoengine.patterns.FillerFillBox;
import akuto2.akutoengine.patterns.FillerFillWall;
import akuto2.akutoengine.patterns.FillerFlattener;
import akuto2.akutoengine.patterns.FillerFlooring;
import akuto2.akutoengine.patterns.FillerHoe;
import akuto2.akutoengine.patterns.FillerHoleFill;
import akuto2.akutoengine.patterns.FillerPatternCore;
import akuto2.akutoengine.patterns.FillerPatternRecipe;
import akuto2.akutoengine.patterns.FillerQuarry;
import akuto2.akutoengine.patterns.FillerRemover;
import akuto2.akutoengine.patterns.FillerRemover2;
import akuto2.akutoengine.patterns.FillerTorch;
import akuto2.akutoengine.patterns.FillerTower;
import akuto2.akutoengine.patterns.FillerUnderFill;
import akuto2.akutoengine.tiles.TileEntityFillerEX;
import akuto2.akutoengine.tiles.TileEntityTankEX;
import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine;
import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine128;
import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine2048;
import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine32;
import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine512;
import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine8;
import akuto2.akutoengine.tiles.engines.TileEntityFinalEngine;
import akuto2.akutoengine.tiles.engines.TileEntitySuperEngine;
import akuto2.akutoengine.tiles.engines.TileEntitySuperEngine2;
import buildcraft.api.mj.MjAPI;
import buildcraft.api.recipes.AssemblyRecipeBasic;
import buildcraft.api.recipes.IngredientStack;
import buildcraft.core.BCCoreBlocks;
import buildcraft.factory.BCFactoryBlocks;
import buildcraft.lib.recipe.AssemblyRecipeRegistry;
import buildcraft.lib.recipe.IngredientNBTBC;
import buildcraft.silicon.BCSiliconItems;
import buildcraft.silicon.gate.EnumGateLogic;
import buildcraft.silicon.gate.EnumGateMaterial;
import buildcraft.silicon.gate.EnumGateModifier;
import buildcraft.silicon.gate.GateVariant;
import lib.utils.RecipeRegister;
import lib.utils.Register;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = "akutoengine")
public class ObjHandler {
	public static BlockAkutoEngine engineBlock;
	public static Block fillerEX;
	public static Block tankEX;

	public static ItemFillerPattern fillerModule;
	public static Item engineItem;
	public static Item engineChip;
	public static Item heatPearl;
	public static Item coreElementary;
	public static Item coreElementary2;
	public static Item engineCore;
	public static Item engineCore2;

	public static Register register = new Register("akutoengine", AkutoEngine.tabs);

	@SubscribeEvent
	public static void registerBlock(RegistryEvent.Register<Block> event) {
		register.setRegistry(event.getRegistry());
		engineBlock = new BlockAkutoEngine();
		fillerEX = new BlockFillerEX();
		tankEX = new BlockTankEX();

		register.register(engineBlock, "akutoengine");
		register.register(fillerEX, "fillerex");
		register.register(tankEX, "tankex");

		Compat.registerBlock(register);
	}

	@SubscribeEvent
	public static void registerItem(RegistryEvent.Register<Item> event) {
		register.setRegistry(event.getRegistry());
		fillerModule = new ItemFillerPattern();
		engineChip = new Item().setUnlocalizedName("engineChip");
		heatPearl = new Item().setUnlocalizedName("heatPearl");
		engineCore = new Item().setUnlocalizedName("engineCore");
		engineCore2 = new Item().setUnlocalizedName("engineCoreMk2");
		coreElementary = new Item().setUnlocalizedName("coreElementary");
		coreElementary2 = new Item().setUnlocalizedName("coreElementary2");

		register.register(new ItemBlockAkutoEngine(engineBlock).setRegistryName(engineBlock.getRegistryName()));
		register.register(new ItemBlock(fillerEX).setRegistryName(fillerEX.getRegistryName()));
		register.register(new ItemBlockTankEX(tankEX).setRegistryName(tankEX.getRegistryName()));
		register.register(fillerModule, "fillermodule");
		register.register(engineChip, "engineChip");
		register.register(heatPearl, "heatPearl");
		register.register(engineCore, "engineCore");
		register.register(engineCore2, "engineCoreMk2");
		register.register(coreElementary, "coreElementary");
		register.register(coreElementary2, "coreElementary2");
		registerFillerModules();

		Compat.registerItem(register);
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		ItemStack ironEngine = new ItemStack(BCCoreBlocks.engine, 1, 2);
		Ingredient redstoneChip = Ingredient.fromStacks(new ItemStack(BCSiliconItems.redstoneChipset, 1, 0));
		IngredientNBTBC ironANDGate = new IngredientNBTBC(BCSiliconItems.plugGate.getStack(new GateVariant(EnumGateLogic.AND, EnumGateMaterial.IRON, EnumGateModifier.NO_MODIFIER)));
		IngredientNBTBC goldORGate = new IngredientNBTBC(BCSiliconItems.plugGate.getStack(new GateVariant(EnumGateLogic.OR, EnumGateMaterial.GOLD, EnumGateModifier.NO_MODIFIER)));
		IngredientNBTBC goldANDGate = new IngredientNBTBC(BCSiliconItems.plugGate.getStack(new GateVariant(EnumGateLogic.AND, EnumGateMaterial.GOLD, EnumGateModifier.NO_MODIFIER)));
		IngredientNBTBC goldORDiamondGate = new IngredientNBTBC(BCSiliconItems.plugGate.getStack(new GateVariant(EnumGateLogic.OR, EnumGateMaterial.GOLD, EnumGateModifier.DIAMOND)));
		IngredientNBTBC goldANDDiamondGate = new IngredientNBTBC(BCSiliconItems.plugGate.getStack(new GateVariant(EnumGateLogic.AND, EnumGateMaterial.GOLD, EnumGateModifier.DIAMOND)));
		// ゲートを使うレシピはJsonだと指定できないのでこっちで追加
		IForgeRegistry<IRecipe> registry = event.getRegistry();
		RecipeRegister.addRecipe(registry, "akutoengine", "akutoengine512", new ItemStack(engineBlock, 1, 4), "gag", "aia", "gag", 'a', new ItemStack(engineBlock, 1, 3), 'g', goldORGate, 'i', ironEngine);
		RecipeRegister.addRecipe(registry, "akutoengine", "akutoengine2048", new ItemStack(engineBlock, 1, 5), "dad", "aia", "dad", 'a', new ItemStack(engineBlock, 1, 4), 'd', goldORDiamondGate, 'i', ironEngine);
		RecipeRegister.addRecipe(registry, "akutoengine", "tankex", new ItemStack(tankEX), "eae", "ata", "eae", 'e', engineCore, 'a', engineCore2, 't', BCFactoryBlocks.tank);
		// レーザーを使うレシピの追加
		AssemblyRecipeRegistry.register(new AssemblyRecipeBasic("enginechip", 60000 * MjAPI.MJ, ImmutableSet.of(IngredientStack.of(redstoneChip), IngredientStack.of(new ItemStack(heatPearl, 4))), new ItemStack(engineChip)));
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
		GameRegistry.registerTileEntity(TileEntityFillerEX.class, new ResourceLocation("akutoengine", "tile.fillerEX"));
		GameRegistry.registerTileEntity(TileEntityTankEX.class, new ResourceLocation("akutoengine", "tile.tankEX"));

		Compat.registerTileEntity();
	}

	public static void registerFillerModules() {
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
		registerFiller(new FillerClearLiquid(), "   ", "ggg", "www", 12);
		registerFiller(new FillerHoe(), "bb ", " b ", " b ", 13);
		registerFiller(new FillerQuarry(), "g g", "g g", "bbb", 14);
	}

	public static void registerFiller(FillerPatternCore pattern, String s1, String s2, String s3, int meta) {
		FillerPatternRecipe.addRecipe(pattern, s1, s2, s3, 'b', Blocks.BRICK_BLOCK, 'g', Blocks.GLASS, 'w', Items.WATER_BUCKET);
		registerFiller(pattern, meta);
	}

	public static void registerFiller(FillerPatternCore pattern, int meta) {
		pattern.moduleItem = new ItemStack(fillerModule, 1, meta);
		FillerPatternRecipe.addRecipe(pattern, pattern.moduleItem);
		fillerModule.maxItem = meta + 1;
	}
}
