package akuto2.compat;

import akuto2.compat.pe.ProjectE;
import lib.utils.Register;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Compat {
	public static boolean ic2 = false;
	public static boolean pe = false;

	public static void census() {
		ic2 = Loader.isModLoaded("ic2");
		pe = Loader.isModLoaded("projecte");
	}

	public static void registerBlock(Register register) {
		if(pe) {
			ProjectE.registerBlock(register);
		}
	}

	public static void registerItem(Register register) {
		if(pe) {
			ProjectE.registerItem(register);
		}
	}

	public static void registerTileEntity() {
		if(pe) {
			ProjectE.registerTileEntity();
		}
	}

	@SideOnly(Side.CLIENT)
	public static void registerModel() {
		if(pe) {
			ProjectE.registerModel();
		}
	}
}
