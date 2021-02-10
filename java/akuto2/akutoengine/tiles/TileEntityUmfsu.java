package akuto2.akutoengine.tiles;

import ic2.core.block.wiring.TileEntityElectricBlock;

public class TileEntityUmfsu extends TileEntityElectricBlock{
	public TileEntityUmfsu(){
		super(5, 2048000, 200000000);
	}

	@Override
	public String getInventoryName(){
		return "UMFSU";
	}
}
