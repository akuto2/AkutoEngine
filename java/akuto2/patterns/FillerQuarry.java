package akuto2.patterns;

import akuto2.tiles.TileEntityFillerEX;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class FillerQuarry extends FillerPatternCore{
	public FillerQuarry() {
		super(14);
	}

	@Override
	public boolean iteratePattern(TileEntityFillerEX fillerEX, ItemStack stack) {
		if(fillerEX.now == 0 && quarry(fillerEX)) {
			fillerEX.now++;
		}
		if(fillerEX.now == 1 && flatBedrock(fillerEX)) {
			return true;
		}
		return false;
	}

	@Override
	public void initialize(TileEntityFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.cy = fillerEX.sy - 1;
	}

	private boolean quarry(TileEntityFillerEX fillerEX) {
		while(fillerEX.cy > 0 && fillerEX.cy <= 255) {
			while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
				while(fillerEX.cx >= fillerEX.sx && fillerEX.cx <= fillerEX.ex) {
					if(isErase(fillerEX)) {
						eraseBlock(fillerEX.getWorld(), new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz));
						fillerEX.cx++;
						return false;
					}
					fillerEX.cx++;
				}
				fillerEX.cx = fillerEX.sx;
				fillerEX.cz++;
			}
			fillerEX.cz = fillerEX.sz;
			fillerEX.cy--;
		}
		fillerEX.cy = fillerEX.sy - 1;
		return true;
	}

	private boolean flatBedrock(TileEntityFillerEX fillerEX) {
		int y = 0;
		while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
			while(fillerEX.cx >= fillerEX.sx && fillerEX.cx <= fillerEX.ex) {
				if(isAir(fillerEX.getWorld().getBlockState(new BlockPos(fillerEX.cx, y, fillerEX.cz)).getBlock())) {
					fillerEX.getWorld().setBlockState(new BlockPos(fillerEX.cx, y, fillerEX.cz), Blocks.BEDROCK.getDefaultState());
					fillerEX.cx++;
					return false;
				}
				fillerEX.cx++;
			}
			fillerEX.cx = fillerEX.sx;
			fillerEX.cz++;
		}
		fillerEX.cz = fillerEX.sz;
		return true;
	}
}
