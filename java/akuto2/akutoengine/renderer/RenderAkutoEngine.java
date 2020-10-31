package akuto2.akutoengine.renderer;

import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngineBase;
import akuto2.akutoengine.utils.models.EngineModels;
import buildcraft.lib.client.model.MutableQuad;
import buildcraft.lib.client.render.tile.RenderEngine_BC8;

public class RenderAkutoEngine extends RenderEngine_BC8<TileEntityAkutoEngineBase>{
	public static final RenderAkutoEngine INSTANCE = new RenderAkutoEngine();

	@Override
	protected MutableQuad[] getEngineModel(TileEntityAkutoEngineBase engine, float partialTicks) {
		return EngineModels.getEngineQuads(engine, partialTicks);
	}
}
