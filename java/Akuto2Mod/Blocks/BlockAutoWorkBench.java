package Akuto2Mod.Blocks;

import Akuto2Mod.Akuto2Core;
import Akuto2Mod.TileEntity.TileAutoWorkBench;
import buildcraft.api.transport.IItemPipe;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAutoWorkBench extends BlockContainer {

	public BlockAutoWorkBench() {
		super(Material.wood);
		setHardness(3.0F);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hitX, float hitY, float hitZ) {
		if(player.getHeldItem() != null) {
			if(player.getCurrentEquippedItem().getItem() instanceof IItemPipe) {
				return false;
			}
		}

		if(!world.isRemote) {
			player.openGui(Akuto2Core.instance, 2, world, x, y, z);
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileAutoWorkBench();
	}
}
