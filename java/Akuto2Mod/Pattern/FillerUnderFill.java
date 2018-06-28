package Akuto2Mod.Pattern;

import Akuto2Mod.TileEntity.TileFillerEX;
import net.minecraft.item.ItemStack;

public class FillerUnderFill extends FillerPatternCore{

	public FillerUnderFill() {
		super(6);
	}

	@Override
	public boolean iteratePattern(TileFillerEX fillerEX, ItemStack stack) {
		return fill(fillerEX, stack);
	}

	@Override
	public void initialize(TileFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.setPower(16, 256);
		fillerEX.ey = fillerEX.sy - 1;
		fillerEX.sy = 1;
		fillerEX.cy = 1;
	}
}
