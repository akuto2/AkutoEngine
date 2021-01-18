package akuto2.akutoengine.compat.pe;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;

import akuto2.akutoengine.compat.Compat;
import akuto2.akutoengine.compat.pe.items.ItemKleinStarsEX;
import lib.utils.RecipeRegister;
import lib.utils.Register;
import moze_intel.projecte.gameObjs.ObjHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

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

	public static void registerRecipes(IForgeRegistry<IRecipe> registry) {
		RecipeRegister.addRecipe(registry, "akutoengine", "kleinstarex0", new ItemStack(kleinStarsEX, 1, 0), "xxx", "x x", "xxx", 'x', new ItemStack(ObjHandler.kleinStars, 1, 5));
		RecipeRegister.addRecipe(registry, "akutoengine", "kleinstarex1", new ItemStack(kleinStarsEX, 1, 1), " x ", "xxx", " x ", 'x', new ItemStack(kleinStarsEX, 1, 0));
	}

	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		return () -> Compat.pe;
	}

}
