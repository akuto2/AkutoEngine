package akuto2.akutoengine.blocks;

import java.util.List;
import java.util.Random;

import akuto2.akutoengine.AkutoEngine;
import akuto2.akutoengine.tiles.TilePumpEX;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockPumpEX extends BlockContainer {

	@SideOnly(Side.CLIENT)
	private IIcon side[];
	@SideOnly(Side.CLIENT)
	private IIcon top[];
	@SideOnly(Side.CLIENT)
	private IIcon bottom[];
	private static int type = 5;

	public BlockPumpEX() {
		super(Material.iron);
		setHardness(5.0F);
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	public Item getItemDropped(int par1, Random random, int par3) {
		return Item.getItemFromBlock(AkutoEngine.pumpEX);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TilePumpEX();
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tabs, List arrayList) {
		for(int i = 0; i < type; ++i) {
			arrayList.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if((tileEntity instanceof TilePumpEX)) {
			((TilePumpEX)tileEntity).onNeighborBlockChange(block);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register) {
		side = new IIcon[type];
		top = new IIcon[type];
		bottom = new IIcon[type];

		for(int i = 0; i < type; i++) {
			side[i] = register.registerIcon("akutoengine:pump/pumpex" + (i + 1) + "_side");
			top[i] = register.registerIcon("akutoengine:pump/pumpex" + (i + 1) + "_top");
			bottom[i] = register.registerIcon("akutoengine:pump/pumpex" + (i + 1) + "_bottom");
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		if(side == 0) {
			return bottom[meta];
		}
		if(side == 1) {
			return top[meta];
		}
		return this.side[meta];
	}
}
