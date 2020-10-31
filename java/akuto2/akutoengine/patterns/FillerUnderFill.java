package akuto2.akutoengine.patterns;

import javax.annotation.Nonnull;

import akuto2.akutoengine.tiles.TileEntityFillerEX;
import net.minecraft.item.ItemStack;

public class FillerUnderFill extends FillerPatternCore{
	public FillerUnderFill() {
		super(6);
	}

	@Override
	public boolean iteratePattern(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		return fill(fillerEX, stack);
	}

	@Override
	public void initialize(TileEntityFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.setPower(16, 256);
		fillerEX.ey = fillerEX.sy - 1;
		fillerEX.sy = 1;
		fillerEX.cy = 1;
	}
}
