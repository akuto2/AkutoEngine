package Akuto2Mod.Blocks;

import java.util.List;

import Akuto2Mod.TileEntity.TileEntityHumfsu;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.block.TileEntityBlock;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockHUMFSU extends BlockUMFSU {
	@SideOnly(Side.CLIENT)
	private IIcon top;
	@SideOnly(Side.CLIENT)
	private IIcon output;
	@SideOnly(Side.CLIENT)
	private IIcon input;

	public BlockHUMFSU(){
		super();
		setHardness(1.5F);
		setStepSound(soundTypeMetal);
		setBlockName("HUMFSU");
	}

	@Override
	public IIcon getIcon(IBlockAccess iBlockAccess, int x, int y, int z, int side){
		TileEntity tile = iBlockAccess.getTileEntity(x, y, z);
		if((tile instanceof TileEntityBlock)){
			switch (new Short(((TileEntityBlock)tile).getFacing()).intValue()) {
			case 0:
				switch(side){
				case 2:
					return this.top;
				case 3:
					return this.top;
				case 0:
					return this.output;
				}
				return this.input;
			case 1:
				switch(side){
				case 2:
					return this.top;
				case 3:
					return this.top;
				case 1:
					return this.output;
				}
				return this.input;
			case 2:
		        switch (side)
		        {
		        case 0:
		          return this.top;
		        case 1:
		          return this.top;
		        case 2:
		          return this.output;
		        }
		        return this.input;
		      case 3:
		        switch (side)
		        {
		        case 0:
		          return this.top;
		        case 1:
		          return this.top;
		        case 3:
		          return this.output;
		        }
		        return this.input;
		      case 4:
		        switch (side)
		        {
		        case 0:
		          return this.top;
		        case 1:
		          return this.top;
		        case 4:
		          return this.output;
		        }
		        return this.input;
		      case 5:
		        switch (side)
		        {
		        case 0:
		          return this.top;
		        case 1:
		          return this.top;
		        case 5:
		          return this.output;
		        }
		        return this.input;
			}
			return this.input;
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata){
	    switch (side){
	    case 0:
	      return this.top;
	    case 1:
	      return this.top;
	    case 3:
	      return this.output;
	    }
	    return this.input;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister){
		this.top = iconRegister.registerIcon("akutoengine:humfsu_top");
		this.output = iconRegister.registerIcon("akutoengine:humfsu_output");
		this.input = iconRegister.registerIcon("akutoengine:humfsu_input");
	}

	@Override
	public TileEntity createTileEntity(World world, int mete){
		return new TileEntityHumfsu();
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List stackList){
		ItemStack zeroStack = new ItemStack(this, 1, 0);
	    StackUtil.getOrCreateNbtData(zeroStack).setInteger("energy", 0);
	    stackList.add(zeroStack);
	    ItemStack fullStack = new ItemStack(this, 1, 1);
	    StackUtil.getOrCreateNbtData(fullStack).setInteger("energy", 800000000);
	    stackList.add(fullStack);
	}
}
