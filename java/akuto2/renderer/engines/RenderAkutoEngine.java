package akuto2.renderer.engines;

import akuto2.tiles.engines.TileAkutoEngineBase;
import akuto2.utils.EngineModels;
import buildcraft.lib.client.model.MutableQuad;
import buildcraft.lib.client.render.tile.RenderEngine_BC8;

public class RenderAkutoEngine extends RenderEngine_BC8<TileAkutoEngineBase>{
	public static final RenderAkutoEngine INSTANCE = new RenderAkutoEngine();

	@Override
	protected MutableQuad[] getEngineModel(TileAkutoEngineBase engine, float partialTicks) {
		return EngineModels.getEngineQuads(engine, partialTicks);
	}
}
