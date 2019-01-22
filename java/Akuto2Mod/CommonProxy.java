package Akuto2Mod;

import Akuto2Mod.Pattern.FillerPatternCore;
import net.minecraft.world.World;

public class CommonProxy {
	public CommonProxy(){}

	public void initialize() {}

	public void registerRenderInformation(){}

	public void registerTileEntitySpecialRenderer(){}

	public void initFiller(){}

	public void registerFiller(FillerPatternCore pattern, int meta) {}

	public World getClientWorld(){
		return null;
	}

}
