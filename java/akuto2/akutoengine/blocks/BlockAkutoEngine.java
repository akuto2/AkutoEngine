package akuto2.akutoengine.blocks;

import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine;
import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine128;
import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine2048;
import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine32;
import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine512;
import akuto2.akutoengine.tiles.engines.TileEntityAkutoEngine8;
import akuto2.akutoengine.tiles.engines.TileEntityFinalEngine;
import akuto2.akutoengine.tiles.engines.TileEntitySuperEngine;
import akuto2.akutoengine.tiles.engines.TileEntitySuperEngine2;
import akuto2.akutoengine.utils.enums.EnumEngineType;
import akuto2.akutoengine.utils.properties.AkutoEngineProperty;
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
		case 0:	return new TileEntityAkutoEngine();
		case 1: return new TileEntityAkutoEngine8();
		case 2: return new TileEntityAkutoEngine32();
		case 3: return new TileEntityAkutoEngine128();
		case 4: return new TileEntityAkutoEngine512();
		case 5: return new TileEntityAkutoEngine2048();
		case 6: return new TileEntitySuperEngine();
		case 7: return new TileEntitySuperEngine2();
		case 8: return new TileEntityFinalEngine();
		default: return null;
		}
	}
}
