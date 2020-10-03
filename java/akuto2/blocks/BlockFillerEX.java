package akuto2.blocks;

import akuto2.AkutoEngine;
import akuto2.tiles.TileEntityFillerEX;
import akuto2.utils.Utils;
import akuto2.utils.enums.EnumWorkType;
import akuto2.utils.properties.AkutoEngineProperty;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFillerEX extends BlockContainer{
	public static final IProperty<EnumFacing> FACING = AkutoEngineProperty.BLOCK_FACING;
	public static final IProperty<EnumWorkType> WORK = AkutoEngineProperty.WORK_TYPE;

	public BlockFillerEX() {
		super(Material.IRON);
		setUnlocalizedName("fillerEX");
		setHardness(1.5F);
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(WORK, EnumWorkType.Off));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		meta |= ((EnumFacing)state.getValue(FACING)).getIndex();
		return meta;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withProperty(FACING, mirrorIn.mirror(state.getValue(FACING)));
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
		EnumFacing orientation = placer.getHorizontalFacing();
		state = state.withProperty(FACING, orientation.getOpposite());
		return state;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, WORK });
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityFillerEX();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote) {
			playerIn.openGui(AkutoEngine.instance, Utils.FILLEREX_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TileEntityFillerEX) {
			InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityFillerEX)tile);
			worldIn.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(worldIn, pos, state);
	}
}
