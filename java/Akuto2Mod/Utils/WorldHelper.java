package Akuto2Mod.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class WorldHelper {
	private static Minecraft mc = null;

	public static Minecraft getMinecraft() {
		if(mc == null) {
			mc = Minecraft.getMinecraft();
		}
		return mc;
	}

	public static void updateBlock(int x, int y, int z) {
		getMinecraft().renderGlobal.markBlockForRenderUpdate(x, y, z);
	}

	public static boolean checkChunkBlock(World world, int x, int y, int z, int r) {
		return world.checkChunksExist(x - r, y - r, z - r, x + r, y + r, z + r);
	}

	public static boolean setBlockToAir(World world, int x, int y, int z) {
		if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000)
        {
            if (y < 0)
            {
                return false;
            }
            else if (y >= 256)
            {
                return false;
            }
            else
            {
                Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
                boolean flag = chunk.func_150807_a(x & 15, y, z & 15, Blocks.air, 0);
                world.theProfiler.startSection("checkLight");
                world.func_147451_t(x, y, z);
                world.theProfiler.endSection();
                return flag;
            }
        }
        else
        {
            return false;
        }
	}
}
