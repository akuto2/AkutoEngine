package Akuto2Mod.Pattern;

import Akuto2Mod.TileEntity.TileFillerEX;
import net.minecraft.item.ItemStack;

public class FillerRemover2 extends FillerPatternCore{

	public FillerRemover2() {
		super(3);
	}

	@Override
	public boolean iteratePattern(TileFillerEX fillerEX, ItemStack stack) {
		return emptyUD(fillerEX, stack);
	}

	@Override
	public void initialize(TileFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.cy = fillerEX.ey;
		fillerEX.setPower(16, 8);
	}
}
