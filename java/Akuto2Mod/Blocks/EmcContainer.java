package Akuto2Mod.Blocks;

import java.util.List;

import Akuto2Mod.TileEntity.TileEmcContainer;
import Akuto2Mod.Utils.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class EmcContainer extends BlockContainer {
	@SideOnly(Side.CLIENT)
	private IIcon[] top;
	@SideOnly(Side.CLIENT)
	private IIcon[] side;
	@SideOnly(Side.CLIENT)
	private IIcon[] bottom;

	public EmcContainer() {
		super(Material.iron);
		setBlockName("emcContainer");
		setHardness(3.0f);
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		top = new IIcon[Utils.emcContainerType];
		side = new IIcon[Utils.emcContainerType];
		bottom = new IIcon[Utils.emcContainerType];

		for(int i = 0; i < Utils.emcContainerType; i++) {
			top[i] = register.registerIcon("akutoengine:emccontainer/" + i + "/top");
			side[i] = register.registerIcon("akutoengine:emccontainer/" + i + "/side");
			bottom[i] = register.registerIcon("akutoengine:emccontainer/" + i + "/bottom");
		}
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if(side == 0){
			return bottom[meta];
		}
		else if (side == 1){
			return top[meta];
		}
		else{
			return this.side[meta];
		}
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
		for(int i = 0; i < Utils.emcContainerType; ++i) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEmcContainer();
	}
}
