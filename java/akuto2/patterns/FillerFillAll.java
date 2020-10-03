package akuto2.patterns;

import akuto2.tiles.TileEntityFillerEX;
import net.minecraft.item.ItemStack;

public class FillerFillAll extends FillerPatternCore{
	public FillerFillAll() {
		super(0);
	}

	@Override
	public boolean iteratePattern(TileEntityFillerEX fillerEX, ItemStack stack) {
		return fill(fillerEX, stack);
	}

	@Override
	public void initialize(TileEntityFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.setPower(16, 512);
	}
}
