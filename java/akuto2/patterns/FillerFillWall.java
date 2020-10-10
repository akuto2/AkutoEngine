package akuto2.patterns;

import javax.annotation.Nonnull;

import akuto2.tiles.TileEntityFillerEX;
import net.minecraft.item.ItemStack;

public class FillerFillWall extends FillerPatternCore{
	public FillerFillWall() {
		super(8);
	}

	@Override
	public boolean iteratePattern(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		if(fillerEX.now == 0 && fill(fillerEX.sx, fillerEX.sy, fillerEX.sz, fillerEX.sx, fillerEX.ey, fillerEX.ez, fillerEX, stack)) {
			modeChange(fillerEX, fillerEX.sx, fillerEX.sy, fillerEX.sz);
		}
		return false;
	}

	@Override
	public void initialize(TileEntityFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.setPower(16, 512);
	}

	private void modeChange(TileEntityFillerEX fillerEX, int x, int y, int z) {
		fillerEX.cx = x;
		fillerEX.cy = y;
		fillerEX.cz = z;
		fillerEX.now++;
	}
}
