package Akuto2Mod.TileEntity;

import ic2.core.block.wiring.TileEntityElectricBlock;

public class TileEntityHumfsu extends TileEntityElectricBlock {
	public TileEntityHumfsu(){
		super(6, 8192000, 800000000);
	}

	@Override
	public String getInventoryName(){
		return "HUMFSU";
	}
}
