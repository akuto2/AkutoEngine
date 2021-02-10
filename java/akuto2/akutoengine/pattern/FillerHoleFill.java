package akuto2.akutoengine.pattern;

import akuto2.akutoengine.tiles.TileFillerEX;
import net.minecraft.item.ItemStack;

public class FillerHoleFill extends FillerPatternCore{

	public FillerHoleFill() {
		super(5);
	}

	@Override
	public boolean iteratePattern(TileFillerEX fillerEX, ItemStack stack) {
		return fill2(fillerEX, stack);
	}

	@Override
	public void initialize(TileFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.setPower(24, 64);
	}
}
