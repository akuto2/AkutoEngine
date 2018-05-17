package Akuto2Mod.Renderer;

import org.lwjgl.opengl.GL11;

import buildcraft.core.Box;
import buildcraft.core.Box.Kind;
import buildcraft.core.LaserData;
import buildcraft.core.internal.IBoxProvider;
import buildcraft.core.render.RenderLaser;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderBoxProvider extends TileEntitySpecialRenderer{

	private static final ResourceLocation STRIPES = new ResourceLocation("buildcraftcore:textures/laserBeams/stripes.png");

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
		GL11.glPushMatrix();
	    GL11.glPushAttrib(8192);
	    GL11.glEnable(2884);
	    GL11.glDisable(2896);
	    GL11.glEnable(3042);
	    GL11.glBlendFunc(770, 771);
	    GL11.glTranslated(-tileEntity.xCoord, -tileEntity.yCoord, -tileEntity.zCoord);
	    GL11.glTranslated(x, y, z);

	    if(tileEntity instanceof IBoxProvider) {
	    	Box box = ((IBoxProvider)tileEntity).getBox();
	    	if(box.isVisible) {
	    		GL11.glPushMatrix();
	    	    GL11.glDisable(2896);

	    	    box.createLaserData();
	    	    for (LaserData l : box.lasersData)
	    	    {
	    	      l.update();
	    	      l.isGlowing = true;
	    	      GL11.glPushMatrix();
	    	      GL11.glTranslated(0.5D, 0.5D, 0.5D);
	    	      RenderLaser.doRenderLaser(TileEntityRendererDispatcher.instance.field_147553_e, l, getTexture(box.kind));
	    	      GL11.glPopMatrix();
	    	    }
	    	    GL11.glEnable(2896);
	    	    GL11.glPopMatrix();
	    	}
	    }

	    GL11.glPopAttrib();
	    GL11.glPopMatrix();
	}

	private ResourceLocation getTexture(Kind kind) {
		switch(kind) {
		case STRIPES:
			return STRIPES;
		default:
			return null;
		}
	}

}
