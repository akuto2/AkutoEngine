package akuto2.patterns;

import javax.annotation.Nonnull;

import akuto2.tiles.TileEntityFillerEX;
import net.minecraft.item.ItemStack;

public class FillerEraser extends FillerPatternCore{
	public FillerEraser() {
		super(1);
	}

	@Override
	public boolean iteratePattern(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		return erase(fillerEX, stack);
	}

	@Override
	public void initialize(TileEntityFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.cy = fillerEX.ey;
		fillerEX.setPower(24, 256);
	}
}
