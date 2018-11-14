package Akuto2Mod.Blocks;

import Akuto2Mod.Akuto2Core;
import Akuto2Mod.TileEntity.TileEMCBuilder;
import buildcraft.builders.BlockBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEMCBuilder extends BlockBuilder {
	public BlockEMCBuilder() {
		setCreativeTab(Akuto2Core.tabAkutoEngine);
		setBlockName("emcBuilder");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		if(!world.isRemote) {
			entityplayer.openGui(Akuto2Core.instance, 3, world, x, y, z);
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEMCBuilder();
	}
}
