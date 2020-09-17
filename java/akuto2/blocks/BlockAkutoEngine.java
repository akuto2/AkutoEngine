package akuto2.blocks;

import akuto2.tiles.engines.TileAkutoEngine;
import akuto2.tiles.engines.TileAkutoEngine128;
import akuto2.tiles.engines.TileAkutoEngine2048;
import akuto2.tiles.engines.TileAkutoEngine32;
import akuto2.tiles.engines.TileAkutoEngine512;
import akuto2.tiles.engines.TileAkutoEngine8;
import akuto2.tiles.engines.TileFinalEngine;
import akuto2.tiles.engines.TileSuperEngine;
import akuto2.tiles.engines.TileSuperEngine2;
import akuto2.utils.enums.EnumEngineType;
import akuto2.utils.properties.AkutoEngineProperty;
import buildcraft.lib.engine.BlockEngineBase_BC8;
import buildcraft.lib.tile.TileBC_Neptune;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class BlockAkutoEngine extends BlockEngineBase_BC8<EnumEngineType>{
	public BlockAkutoEngine() {
		super(Material.WOOD, "");
		setUnlocalizedName("akutoengine");
	}

	@Override
	public IProperty<EnumEngineType> getEngineProperty() {
		return AkutoEngineProperty.ENGINE_TYPE;
	}

	@Override
	public EnumEngineType getEngineType(int meta) {
		return EnumEngineType.fromMeta(meta);
	}

	@Override
	public String getUnlocalizedName(EnumEngineType type) {
		return super.getUnlocalizedName() + "." + type.name;
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for(EnumEngineType engine : EnumEngineType.VALUES) {
			items.add(new ItemStack(this, 1, engine.ordinal()));
		}
	}

	@Override
	public TileBC_Neptune createTileEntity(World world, IBlockState state) {
		int meta = state.getValue(getEngineProperty()).ordinal();
		switch(meta) {
		case 0:	return new TileAkutoEngine();
		case 1: return new TileAkutoEngine8();
		case 2: return new TileAkutoEngine32();
		case 3: return new TileAkutoEngine128();
		case 4: return new TileAkutoEngine512();
		case 5: return new TileAkutoEngine2048();
		case 6: return new TileSuperEngine();
		case 7: return new TileSuperEngine2();
		case 8: return new TileFinalEngine();
		default: return null;
		}
	}
}
