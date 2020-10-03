package akuto2.renderer;

import akuto2.tiles.engines.TileEntityAkutoEngineBase;
import akuto2.utils.models.EngineModels;
import buildcraft.lib.client.model.MutableQuad;
import buildcraft.lib.client.render.tile.RenderEngine_BC8;

public class RenderAkutoEngine extends RenderEngine_BC8<TileEntityAkutoEngineBase>{
	public static final RenderAkutoEngine INSTANCE = new RenderAkutoEngine();

	@Override
	protected MutableQuad[] getEngineModel(TileEntityAkutoEngineBase engine, float partialTicks) {
		return EngineModels.getEngineQuads(engine, partialTicks);
	}
}
