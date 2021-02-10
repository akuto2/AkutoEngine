package akuto2.akutoengine.proxies;

import akuto2.akutoengine.pattern.FillerPatternCore;
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
