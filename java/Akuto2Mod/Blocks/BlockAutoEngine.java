package Akuto2Mod.Blocks;

import java.util.List;

import Akuto2Mod.TileEntity.Engine.TileAutoEngine;
import Akuto2Mod.TileEntity.Engine.TileAutoEngine128;
import Akuto2Mod.TileEntity.Engine.TileAutoEngine2048;
import Akuto2Mod.TileEntity.Engine.TileAutoEngine32;
import Akuto2Mod.TileEntity.Engine.TileAutoEngine512;
import Akuto2Mod.TileEntity.Engine.TileAutoEngine8;
import Akuto2Mod.TileEntity.Engine.TileFinalEngine;
import Akuto2Mod.TileEntity.Engine.TileSuperEngine;
import Akuto2Mod.TileEntity.Engine.TileSuperEngine2;
import buildcraft.core.BlockEngine;
import buildcraft.core.lib.engines.TileEngineBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockAutoEngine extends BlockEngine {
	private static IIcon woodIcon;

	public BlockAutoEngine() {

	}

	@Override
	public TileEntity createTileEntity(World world, int metadata){
		if (metadata == 0)
			return new TileAutoEngine();
		else if (metadata == 1)
			return new TileAutoEngine8();
		else if (metadata == 2)
			return new TileAutoEngine32();
		else if (metadata == 3)
			return new TileAutoEngine128();
		else if (metadata == 4)
			return new TileAutoEngine512();
		else if (metadata == 5)
			return new TileAutoEngine2048();
		else if (metadata == 6)
			return new TileSuperEngine();
		else if (metadata == 7)
			return new TileSuperEngine2();
		else if (metadata == 8)
			return new TileFinalEngine();
		else
			return null;
	}
	@Override
	@SuppressWarnings("unchecked")
	public void getSubBlocks(Item item, CreativeTabs par2CreativeTabs, List arraylist ){
		arraylist.add(new ItemStack(this, 1, 0));
		arraylist.add(new ItemStack(this, 1, 1));
		arraylist.add(new ItemStack(this, 1, 2));
		arraylist.add(new ItemStack(this, 1, 3));
		arraylist.add(new ItemStack(this, 1, 4));
		arraylist.add(new ItemStack(this, 1, 5));
		arraylist.add(new ItemStack(this, 1, 6));
		arraylist.add(new ItemStack(this, 1, 7));
		arraylist.add(new ItemStack(this, 1, 8));
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9){
		TileEngineBase tileengine = (TileEngineBase)world.getTileEntity(x, y, z);

		if(!world.isRemote && entityplayer.getCurrentEquippedItem() != null){
			Item itemID = entityplayer.getCurrentEquippedItem().getItem();
		}

		return super.onBlockActivated(world, x, y, z, entityplayer, par6, par7, par8, par9);

	}

	@Override
	public void onPostBlockPlaced(World world, int x, int y, int z, int par5) {
	}

	@Override
	@SideOnly(Side.CLIENT)

	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		super.registerBlockIcons(par1IconRegister);
		this.woodIcon = par1IconRegister.registerIcon("akutoengine:icon");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return this.woodIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTexturePrefix(int meta, boolean addPrefix) {
		return "buildcraftcore:textures/blocks/engineWood";
	}
}
