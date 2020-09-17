package akuto2.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import akuto2.tiles.engines.TileAkutoEngine;
import akuto2.tiles.engines.TileAkutoEngine128;
import akuto2.tiles.engines.TileAkutoEngine2048;
import akuto2.tiles.engines.TileAkutoEngine32;
import akuto2.tiles.engines.TileAkutoEngine512;
import akuto2.tiles.engines.TileAkutoEngine8;
import akuto2.tiles.engines.TileAkutoEngineBase;
import akuto2.tiles.engines.TileFinalEngine;
import akuto2.tiles.engines.TileSuperEngine;
import akuto2.tiles.engines.TileSuperEngine2;
import akuto2.utils.enums.EnumEngineType;
import buildcraft.api.enums.EnumPowerStage;
import buildcraft.lib.client.model.ModelHolderVariable;
import buildcraft.lib.client.model.ModelItemSimple;
import buildcraft.lib.client.model.MutableQuad;
import buildcraft.lib.expression.DefaultContexts;
import buildcraft.lib.expression.FunctionContext;
import buildcraft.lib.expression.node.value.NodeVariableDouble;
import buildcraft.lib.expression.node.value.NodeVariableObject;
import buildcraft.lib.misc.ExpressionCompat;
import buildcraft.lib.misc.data.ModelVariableData;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * エンジンのモデルを登録するためのクラス
 */
@EventBusSubscriber(modid = "akutoengine")
public class EngineModels {
	private static final NodeVariableDouble ENGINE_PROGRESS;
	private static final NodeVariableObject<EnumPowerStage> ENGINE_STAGE;
	private static final NodeVariableObject<EnumFacing> ENGINE_FACING;
	private static final HashMap<EnumEngineType, ModelHolderVariable> AKUTO_ENGINE;

	static {
		FunctionContext fnCtx = new FunctionContext(ExpressionCompat.ENUM_POWER_STAGE, DefaultContexts.createWithAll());
		ENGINE_PROGRESS = fnCtx.putVariableDouble("progress");
		ENGINE_STAGE = fnCtx.putVariableObject("stage", EnumPowerStage.class);
		ENGINE_FACING = fnCtx.putVariableObject("direction", EnumFacing.class);
		AKUTO_ENGINE = new HashMap<EnumEngineType, ModelHolderVariable>();
		for(EnumEngineType engineType : EnumEngineType.VALUES) {
			AKUTO_ENGINE.put(engineType, new ModelHolderVariable("akutoengine:models/block/engine/akutoengine" + engineType.name + ".json", fnCtx));
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onTextureStitchPre(TextureStitchEvent.Pre event) {

	}

	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event) {
		ENGINE_PROGRESS.value = 0.2;
		ENGINE_STAGE.value = EnumPowerStage.RED;
		ENGINE_FACING.value = EnumFacing.UP;
		ModelVariableData varData = new ModelVariableData();
		for(EnumEngineType engineType : EnumEngineType.VALUES) {
			varData.setNodes(AKUTO_ENGINE.get(engineType).createTickableNodes());
			varData.tick();
			varData.refresh();
			event.getModelRegistry().putObject(new ModelResourceLocation(engineType.getItemModelLocation(), "inventory"), new ModelItemSimple((List)Arrays.<MutableQuad>stream(AKUTO_ENGINE.get(engineType).getCutoutQuads()).map(MutableQuad::toBakedItem).collect(Collectors.toList()), ModelItemSimple.TRANSFORM_BLOCK, true));
		}
	}

	private static MutableQuad[] getEngineQuads(ModelHolderVariable model, TileAkutoEngineBase tile, float partialTicks) {
		ENGINE_PROGRESS.value = tile.getProgressClient(partialTicks);
		ENGINE_STAGE.value = tile.getPowerStage();
		ENGINE_FACING.value = tile.getCurrentFacing();
		if(tile.clientModelData.hasNoNodes())
			tile.clientModelData.setNodes(model.createTickableNodes());
		tile.clientModelData.refresh();
		return model.getCutoutQuads();
	}

	public static MutableQuad[] getEngineQuads(TileAkutoEngineBase engine, float partialTicks) {
		if(engine instanceof TileAkutoEngine)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.X1), engine, partialTicks);
		else if(engine instanceof TileAkutoEngine8)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.X8), engine, partialTicks);
		else if(engine instanceof TileAkutoEngine32)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.X32), engine, partialTicks);
		else if(engine instanceof TileAkutoEngine128)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.X128), engine, partialTicks);
		else if(engine instanceof TileAkutoEngine512)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.X512), engine, partialTicks);
		else if(engine instanceof TileAkutoEngine2048)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.X2048), engine, partialTicks);
		else if(engine instanceof TileSuperEngine)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.SUPER), engine, partialTicks);
		else if(engine instanceof TileSuperEngine2)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.SUPER2), engine, partialTicks);
		else if(engine instanceof TileFinalEngine)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.FINAL), engine, partialTicks);
		else
			return null;
	}
}
