package akuto2.compat.pe;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;

import akuto2.compat.Compat;
import akuto2.compat.pe.items.ItemKleinStarsEX;
import lib.utils.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ProjectE implements IConditionFactory{
	public static Item kleinStarsEX;

	public static void registerBlock(Register register) {

	}

	public static void registerItem(Register register) {
		kleinStarsEX = new ItemKleinStarsEX();

		register.register(kleinStarsEX, "kleinstarex");
	}

	public static void registerTileEntity() {

	}

	@SideOnly(Side.CLIENT)
	public static void registerModel() {
		for(int i = 0; i < 2; i++) {
			ModelLoader.setCustomModelResourceLocation(kleinStarsEX, i, new ModelResourceLocation("akutoengine:kleinstarsex_" + i, "inventory"));
		}
	}

	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		return () -> Compat.pe;
	}

}
