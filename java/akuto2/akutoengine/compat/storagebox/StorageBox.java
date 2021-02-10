package akuto2.akutoengine.compat.storagebox;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

public class StorageBox {
	public static Item storageBox;

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new StorageBoxEvent());
	}

	public static void post() {
		storageBox = GameRegistry.findItem("net.minecraft.storagebox.mod_StorageBox", "Storage Box");
	}
}
