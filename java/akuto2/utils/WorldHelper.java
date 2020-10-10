package akuto2.utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class WorldHelper {
	public static boolean setBlockToAir(World world, BlockPos pos) {
		if(world.isValid(pos) && !world.isRemote) {
			Chunk chunk = world.getChunkFromBlockCoords(pos);
			pos = pos.toImmutable();

			IBlockState state = chunk.setBlockState(pos, Blocks.AIR.getDefaultState());

			if(state == null) {
				return false;
			}
			else {
				world.profiler.startSection("checkLight");
				world.checkLight(pos);
				world.profiler.endSection();
				return true;
			}
		}
		else {
			return false;
		}
	}
}
