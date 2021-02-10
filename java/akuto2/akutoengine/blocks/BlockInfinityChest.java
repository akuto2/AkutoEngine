package akuto2.akutoengine.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
	private IIcon bottomIcon;

	public BlockInfinityChest() {
		super(Material.glass);
		setHardness(0.3F);
		setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return AxisAlignedBB.getBoundingBox(x + 0.0625F, y, z + 0.0625F, x + 0.9375F, y + 0.875F, z + 0.9375F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return null;
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
	public int quantityDropped(Random p_149745_1_) {
		return 0;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack stack) {
		int meta = determineOrientation(world, x, y, z, (EntityPlayer)entityLivingBase);
		world.setBlockMetadataWithNotify(x, y, z, meta, 3);
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {

	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		return true;
	}

	public int determineOrientation(World world, int x, int y, int z, EntityPlayer player) {
		int face = MathHelper.floor_double((player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;
		return (face == 0) ? 2 : ((face == 1) ? 5 : ((face == 2) ? 3 : ((face == 3) ? 4 : 3)));
	}

	private void dropInfinityChestWithNBT(World world, int x, int y, int z) {

	}
}
