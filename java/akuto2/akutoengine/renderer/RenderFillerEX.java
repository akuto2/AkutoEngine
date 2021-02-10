package akuto2.akutoengine.renderer;

import org.lwjgl.opengl.GL11;

import net.minecraft.tileentity.TileEntity;

public class RenderFillerEX extends RenderBuilderEX{

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
		super.renderTileEntityAt(tileEntity, x, y, z, f);

		GL11.glPushMatrix();

		GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
		GL11.glScalef(1.0004883F, 1.0004883F, 1.0004883F);
	    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

	    GL11.glPopMatrix();
	}
}
