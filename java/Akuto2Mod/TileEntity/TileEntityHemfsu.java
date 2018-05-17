package Akuto2Mod.TileEntity;

import ic2.core.block.wiring.TileEntityElectricBlock;

public class TileEntityHemfsu extends TileEntityElectricBlock {
	public TileEntityHemfsu(){
		super(7, 36864000, 2000000000);
	}

	@Override
	public String getInventoryName(){
		return "HEMFSU";
	}
}
