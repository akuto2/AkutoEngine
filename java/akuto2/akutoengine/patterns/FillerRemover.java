package akuto2.akutoengine.patterns;

import javax.annotation.Nonnull;

import akuto2.akutoengine.tiles.TileEntityFillerEX;
import net.minecraft.item.ItemStack;

public class FillerRemover extends FillerPatternCore{
	public FillerRemover() {
		super(2);
	}

	@Override
	public boolean iteratePattern(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		return emptyDU(fillerEX, stack);
	}

	@Override
	public void initialize(TileEntityFillerEX fillerEX) {
		fillerEX.initRotationPosition();
		fillerEX.setPower(32, 2);
	}
}
