package akuto2.akutoengine.patterns;

import javax.annotation.Nonnull;

import akuto2.akutoengine.tiles.TileEntityFillerEX;
import net.minecraft.item.ItemStack;

public class FillerHoleFill extends FillerPatternCore{
	public FillerHoleFill() {
		super(5);
	}

	@Override
	public boolean iteratePattern(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		return fill2(fillerEX, stack);
	}

	@Override
	public void initialize(TileEntityFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.setPower(24, 64);
	}
}
