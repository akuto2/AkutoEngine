package akuto2.blocks;

import akuto2.tiles.TileEntityTankEX;
import buildcraft.api.properties.BuildCraftProperties;
import buildcraft.api.transport.pipe.ICustomPipeConnection;
import buildcraft.factory.block.ITankBlockConnector;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTankEX extends Block implements ICustomPipeConnection{
	private static final IProperty<Boolean> JOINED_BELOW = BuildCraftProperties.JOINED_BELOW;
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(2 / 16D, 0 / 16D, 2 / 16D, 14 / 16D, 16 / 16D, 14 / 16D);
	private NBTTagCompound workTank = null;

	public BlockTankEX() {
		super(Material.GLASS);
		setUnlocalizedName("tankEX");
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TileEntityTankEX) {
			TileEntityTankEX tankEX = (TileEntityTankEX)tile;
			if(stack.hasTagCompound()) {
				NBTTagCompound tag = stack.getTagCompound();
				FluidStack fluid = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("tank"));
				tankEX.tank.setFluid(fluid);
			}
			tankEX.onPlacedBy(placer, stack);
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TileEntityTankEX) {
			TileEntityTankEX tankEX = (TileEntityTankEX)tile;
			return tankEX.onActivated(playerIn, hand, facing, hitX, hitY, hitZ);
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		super.getDrops(drops, world, pos, state, fortune);
		NBTTagCompound tagCompound;
		FluidStack fluid;
		if(!drops.isEmpty()) {
			if(workTank != null) {
				for(ItemStack stack : drops) {
					if(stack.getTagCompound() == null) {
						stack.setTagCompound(new NBTTagCompound());
					}
					tagCompound = stack.getTagCompound();
					tagCompound.setTag("tank", workTank);
				}
				workTank = null;
			}
			else {
				TileEntity tile = world.getTileEntity(pos);
				if(tile instanceof TileEntityTankEX) {
					fluid = ((TileEntityTankEX)tile).tank.getFluid();
					if(fluid != null) {
						for(ItemStack stack : drops) {
							if(!stack.hasTagCompound()) {
								stack.setTagCompound(new NBTTagCompound());
							}
							tagCompound = stack.getTagCompound();
							tagCompound.setTag("tank", stack.writeToNBT(new NBTTagCompound()));
						}
					}
				}
			}
		}
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if(willHarvest) {
			TileEntity tile = world.getTileEntity(pos);
			if((tile != null) && (tile instanceof TileEntityTankEX)) {
				FluidStack fluid = ((TileEntityTankEX)tile).tank.getFluid();
				if((fluid != null) && (0 < fluid.amount)) {
					System.out.println("RemovedByPlayer");
					workTank = fluid.writeToNBT(new NBTTagCompound());
				}
			}
		}
		else {
			workTank = null;
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { JOINED_BELOW });
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityTankEX();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return side.getAxis() != Axis.Y || !(blockAccess.getBlockState(pos.offset(side)).getBlock() instanceof ITankBlockConnector);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		boolean isTankBelow = worldIn.getBlockState(pos.down()).getBlock() instanceof ITankBlockConnector;
		return state.withProperty(JOINED_BELOW, isTankBelow);
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TileEntityTankEX) {
			return ((TileEntityTankEX)tile).getComparatorLevel();
		}
		return 0;
	}

	@Override
	public float getExtension(World world, BlockPos pos, EnumFacing facing, IBlockState state) {
		return facing.getAxis() == Axis.Y ? 0 : 2 / 16f;
	}
}
