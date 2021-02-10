package akuto2.akutoengine.renderer;

import org.lwjgl.opengl.GL11;

import akuto2.akutoengine.tiles.TileBuildCraftEX;
import buildcraft.core.EntityLaser;
import buildcraft.core.LaserData;
import buildcraft.core.render.RenderLaser;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;

public class RenderBuilderEX extends RenderBoxProvider{

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
		super.renderTileEntityAt(tileEntity, x, y, z, f);

		TileBuildCraftEX buildCraftEX = (TileBuildCraftEX)tileEntity;

		GL11.glPushMatrix();

	    GL11.glPushAttrib(8192);
	    GL11.glEnable(2884);
	    GL11.glEnable(2896);
	    GL11.glEnable(3042);
	    GL11.glBlendFunc(770, 771);

	    GL11.glTranslated(x, y, z);
	    GL11.glTranslated(-tileEntity.xCoord, -tileEntity.yCoord, -tileEntity.zCoord);

	    if(buildCraftEX.getPathLaser() != null) {
	    	for(LaserData l : buildCraftEX.getPathLaser()) {
	    		if(l != null) {
	    			GL11.glPushMatrix();

	    			RenderLaser.doRenderLaser(TileEntityRendererDispatcher.instance.field_147553_e, l, EntityLaser.LASER_TEXTURES[4]);

	    			GL11.glPopMatrix();
	    		}
	    	}
	    }

	    GL11.glPopAttrib();
	    GL11.glPopMatrix();
	}
}
