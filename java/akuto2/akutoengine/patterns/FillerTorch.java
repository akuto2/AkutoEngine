package akuto2.akutoengine.patterns;

import javax.annotation.Nonnull;

import akuto2.akutoengine.config.AkutoEngineConfig;
import akuto2.akutoengine.tiles.TileEntityFillerEX;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class FillerTorch extends FillerPatternCore{
	public FillerTorch() {
		super(10);
	}

	@Override
	public boolean iteratePattern(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		return set(fillerEX, stack);
	}

	@Override
	public void initialize(TileEntityFillerEX fillerEX) {
		fillerEX.initRotationPosition();
		fillerEX.mx *= AkutoEngineConfig.filler.intervalTorch;
		fillerEX.mz *= AkutoEngineConfig.filler.intervalTorch;
		fillerEX.setPower(32, 16);
	}

	private boolean set(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		if(stack.isEmpty()) {
			return false;
		}
		while(isRange(fillerEX.sz, fillerEX.ez, fillerEX.cz, fillerEX.mz)) {
			while(isRange(fillerEX.sx, fillerEX.ex, fillerEX.cx, fillerEX.mx)) {
				if(isFilling(fillerEX.getWorld(), new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz))) {
					setBlock(fillerEX, new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz), stack);
					fillerEX.cx += fillerEX.mx;
					return false;
				}
				fillerEX.cx += fillerEX.mx;;
			}
			fillerEX.cx = fillerEX.sx;
			fillerEX.cz += fillerEX.mz;
		}
		fillerEX.cz = fillerEX.sz;
		return true;
	}
}
