package akuto2.akutoengine.pattern;

import akuto2.akutoengine.tiles.TileFillerEX;
import net.minecraft.item.ItemStack;

public class FillerFillAll extends FillerPatternCore{
	public FillerFillAll() {
		super(0);
	}

	@Override
	public boolean iteratePattern(TileFillerEX fillerEX, ItemStack stack) {
		return fill(fillerEX, stack);
	}

	@Override
	public void initialize(TileFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.setPower(16, 512);
	}
}
