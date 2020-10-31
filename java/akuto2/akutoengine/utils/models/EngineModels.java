package akuto2.akutoengine.utils.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine;
import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine128;
import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine2048;
import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine32;
import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine512;
import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine8;
import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngineBase;
import akuto2.akutoengine.tiles.engines.TileEntityFinalEngine;
import akuto2.akutoengine.tiles.engines.TileEntitySuperEngine;
import akuto2.akutoengine.tiles.engines.TileEntitySuperEngine2;
import akuto2.akutoengine.utils.enums.EnumEngineType;
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

	private static MutableQuad[] getEngineQuads(ModelHolderVariable model, TileEntityAkutoEngineBase tile, float partialTicks) {
		ENGINE_PROGRESS.value = tile.getProgressClient(partialTicks);
		ENGINE_STAGE.value = tile.getPowerStage();
		ENGINE_FACING.value = tile.getCurrentFacing();
		if(tile.clientModelData.hasNoNodes())
			tile.clientModelData.setNodes(model.createTickableNodes());
		tile.clientModelData.refresh();
		return model.getCutoutQuads();
	}

	public static MutableQuad[] getEngineQuads(TileEntityAkutoEngineBase engine, float partialTicks) {
		if(engine instanceof TileEntityAkutoEngine)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.X1), engine, partialTicks);
		else if(engine instanceof TileEntityAkutoEngine8)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.X8), engine, partialTicks);
		else if(engine instanceof TileEntityAkutoEngine32)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.X32), engine, partialTicks);
		else if(engine instanceof TileEntityAkutoEngine128)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.X128), engine, partialTicks);
		else if(engine instanceof TileEntityAkutoEngine512)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.X512), engine, partialTicks);
		else if(engine instanceof TileEntityAkutoEngine2048)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.X2048), engine, partialTicks);
		else if(engine instanceof TileEntitySuperEngine)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.SUPER), engine, partialTicks);
		else if(engine instanceof TileEntitySuperEngine2)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.SUPER2), engine, partialTicks);
		else if(engine instanceof TileEntityFinalEngine)
			return getEngineQuads(AKUTO_ENGINE.get(EnumEngineType.FINAL), engine, partialTicks);
		else
			return null;
	}
}
