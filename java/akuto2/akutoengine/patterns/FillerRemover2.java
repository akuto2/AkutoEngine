package akuto2.akutoengine.patterns;

import javax.annotation.Nonnull;

import akuto2.akutoengine.tiles.TileEntityFillerEX;
import net.minecraft.item.ItemStack;

public class FillerRemover2 extends FillerPatternCore{
	public FillerRemover2() {
		super(3);
	}

	@Override
	public boolean iteratePattern(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		return emptyUD(fillerEX, stack);
	}

	@Override
	public void initialize(TileEntityFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.cy = fillerEX.ey;
		fillerEX.setPower(32, 2);
	}
}
