package Akuto2Mod.TileEntity;

import buildcraft.factory.TileTank;

public class TileTankEX extends TileTank{
	@Override
	public boolean isInvalid(){
		if(worldObj != null){
			tank.setCapacity(2100000000);
		}
		return super.isInvalid();
	}
}
