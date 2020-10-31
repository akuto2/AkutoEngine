package akuto2.akutoengine.patterns;

import javax.annotation.Nonnull;

import akuto2.akutoengine.tiles.TileEntityFillerEX;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class FillerHoe extends FillerPatternCore{
	public FillerHoe() {
		super(13);
	}

	@Override
	public boolean iteratePattern(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		return fillerHoe(fillerEX);
	}

	@Override
	public void initialize(TileEntityFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.cy = fillerEX.sy - 1;
	}

	private boolean fillerHoe(TileEntityFillerEX fillerEX) {
		while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
			while(fillerEX.cx >= fillerEX.sx && fillerEX.cx <= fillerEX.ex) {
				if(isDirt(fillerEX.getWorld().getBlockState(new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz)).getBlock())) {
					fillerEX.getWorld().setBlockState(new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz), Blocks.FARMLAND.getDefaultState());
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

	private boolean isDirt(Block block) {
		return block == Blocks.DIRT || block == Blocks.GRASS;
	}
}
