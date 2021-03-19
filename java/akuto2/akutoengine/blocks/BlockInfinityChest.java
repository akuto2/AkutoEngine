package akuto2.akutoengine.blocks;

import java.math.BigInteger;
import java.util.Random;

import akuto2.akutoengine.AkutoEngine;
import akuto2.akutoengine.tiles.TileEntityInfinityChest;
import akuto2.akutoengine.utils.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockInfinityChest extends BlockContainer{
	@SideOnly(Side.CLIENT)
	private IIcon topIcon;
	@SideOnly(Side.CLIENT)
	private IIcon sideIcon;
	@SideOnly(Side.CLIENT)
	private IIcon frontIcon;

	public BlockInfinityChest() {
		super(Material.glass);
		setHardness(0.3F);
		setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register) {
		topIcon = register.registerIcon("akutoengine:infinitychest_top");
		sideIcon = register.registerIcon("akutoengine:infinitychest_side");
		frontIcon = register.registerIcon("akutoengine:infinitychest_front");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int face, int meta) {
		return (face < 2) ? topIcon : ((face == meta) ? frontIcon : sideIcon);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return AxisAlignedBB.getBoundingBox(x + 0.0625F, y, z + 0.0625F, x + 0.9375F, y + 0.875F, z + 0.9375F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityInfinityChest();
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack stack) {
		int meta = determineOrientation(world, x, y, z, (EntityPlayer)entityLivingBase);
		world.setBlockMetadataWithNotify(x, y, z, meta, 3);
		if(stack.hasTagCompound()) {
			TileEntityInfinityChest chest = (TileEntityInfinityChest)createNewTileEntity(world, meta);
			world.setTileEntity(x, y, z, chest);
			NBTTagCompound compound = (NBTTagCompound)stack.getTagCompound();
			ItemStack chestItem = TileEntityInfinityChest.readStackFromNBT(compound);
			BigInteger count = TileEntityInfinityChest.readCountFromNBT(compound);
			chest.setStack(chestItem, count, true);
		}
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
		dropInfinityChestWithNBT(world, x, y, z);
		super.onBlockHarvested(world, x, y, z, meta, player);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			player.openGui(AkutoEngine.instance, Utils.INFINITY_CHEST_GUI_ID, world, x, y, z);
		}
		return true;
	}

	public int determineOrientation(World world, int x, int y, int z, EntityPlayer player) {
		int face = MathHelper.floor_double((player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;
		return (face == 0) ? 2 : ((face == 1) ? 5 : ((face == 2) ? 3 : ((face == 3) ? 4 : 3)));
	}

	private void dropInfinityChestWithNBT(World world, int x, int y, int z) {
		TileEntityInfinityChest chest = (TileEntityInfinityChest)world.getTileEntity(x, y, z);
		if(!world.isRemote && chest != null) {
			ItemStack stack = new ItemStack(this, 1, 0);
			if(chest.hasStack()) {
				stack.setTagCompound(chest.setStackNBT(new NBTTagCompound()));
			}
			EntityItem drop = new EntityItem(world, x + 0.5D, y + 0.5D, z + 0.5D, stack);
			drop.motionX = 0.0D;
			drop.motionY = 0.2D;
			drop.motionZ = 0.0D;
			world.spawnEntityInWorld(drop);
		}
	}
}
