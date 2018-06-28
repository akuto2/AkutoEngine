package Akuto2Mod.Pattern;

import Akuto2Mod.TileEntity.TileFillerEX;
import net.minecraft.item.ItemStack;

public class FillerEraser extends FillerPatternCore{
	public FillerEraser() {
		super(1);
	}


	@Override
	public boolean iteratePattern(TileFillerEX fillerEX, ItemStack stack) {
		return erase(fillerEX, stack);
	}

	@Override
	public void initialize(TileFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.cy = fillerEX.ey;
		fillerEX.setPower(24, 256);
	}
}
