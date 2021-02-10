package akuto2.akutoengine.pattern;

import akuto2.akutoengine.tiles.TileFillerEX;
import net.minecraft.item.ItemStack;

public class FillerFlattener extends FillerPatternCore{

	public FillerFlattener() {
		super(4);
	}

	@Override
	public boolean iteratePattern(TileFillerEX fillerEX, ItemStack stack) {
		if(fillerEX.now == 0 && erase(fillerEX, stack)) {
			fillerEX.now++;
		}
		if(fillerEX.now == 1 && fill2(fillerEX, stack)) {
			return true;
		}
		return false;
	}

	@Override
	public void initialize(TileFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.ey = 255;
		fillerEX.cy = fillerEX.ey;
		fillerEX.setPower(24, 64);
	}
}
