package Akuto2Mod.TileEntity;

import Akuto2Mod.BuildCraft.BptEMCBuilder;
import Akuto2Mod.Utils.EMCWorldSave;
import buildcraft.builders.ItemBlueprintStandard;
import buildcraft.builders.TileBuilder;
import buildcraft.core.blueprints.Blueprint;
import buildcraft.core.blueprints.BlueprintBase;
import buildcraft.core.blueprints.BptBuilderBase;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEMCBuilder extends TileBuilder{
	private static EMCWorldSave worldSave;
	private static boolean initData = false;
	public static final String fileName = "ak_world";

	@Override
	public void initialize() {
		super.initialize();
		if(!initData) {
			worldSave = getWorldSaveData(worldObj);
		}
	}

	public EMCWorldSave getWorldSaveData(World world) {
		if((!initData) && (world != null) && (worldSave == null)) {
			worldSave = (EMCWorldSave)world.loadItemData(EMCWorldSave.class, "ak_world");
			if(worldSave == null) {
				worldSave = new EMCWorldSave("ak_world");
				worldSave.markDirty();
				world.setItemData("ak_world", worldSave);
			}
			initData = true;
		}
		return worldSave;
	}

	@Override
	public BptBuilderBase instanciateBluePrintBuilder(int x, int y, int z, ForgeDirection o) {
		BlueprintBase bpt = instanciateBlueprint();
		if(bpt == null) {
			return null;
		}
		bpt = bpt.adjustToWorld(worldObj, x, y, z, o);
		if(bpt != null) {
			if((getStackInSlot(0).getItem() instanceof ItemBlueprintStandard)) {
				return new BptEMCBuilder((Blueprint)bpt, worldObj, x, y, z, worldSave);
			}
		}
		return null;
	}
}
