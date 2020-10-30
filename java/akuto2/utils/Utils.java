package akuto2.utils;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public class Utils {
	public static int FILLEREX_GUI = 0;

	public static final long[] MAX_KLEIN_EX_EMC = new long[] { 409600000L, 2048000000L };

	public static long getKleinStarEXMaxEmc(ItemStack stack) {
		return MAX_KLEIN_EX_EMC[stack.getItemDamage()];
	}

	public static NonNullList<Ingredient> getIngredientList(Object... params) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;

		if(params[i] instanceof String[]) {
			String[] aString = (String[])params[i++];

			for(int l = 0; l < aString.length; ++l) {
				String s1 = aString[l];
				++k;
				j = s1.length();
				s = s + s1;
			}
		}
		else {
			while(params[i] instanceof String) {
				String s2 = (String)params[i++];
				++k;
				j = s2.length();
				s = s + s2;
			}
		}

		HashMap map = new HashMap();
		for( ;i < params.length; i += 2) {
			Character character = (Character)params[i];
			Ingredient ingredient = Ingredient.EMPTY;

			if(params[i + 1] instanceof Item) {
				ingredient = Ingredient.fromItem((Item)params[i + 1]);
			}
			else if(params[i + 1] instanceof Block) {
				ingredient = Ingredient.fromItem(Item.getItemFromBlock((Block)params[i + 1]));
			}
			else if(params[i + 1] instanceof ItemStack) {
				ingredient = Ingredient.fromStacks((ItemStack)params[i + 1]);
			}
			else if(params[i + 1] instanceof Ingredient) {
				ingredient = (Ingredient)params[i + 1];
			}

			map.put(character, ingredient);
		}

		NonNullList<Ingredient> nonNullList = NonNullList.withSize(j * k, Ingredient.EMPTY);
		for(int i1 = 0; i1 < j * k; ++i1) {
			char c0 = s.charAt(i1);

			if(map.containsKey(Character.valueOf(c0))) {
				nonNullList.set(i1, (Ingredient)map.get(Character.valueOf(c0)));
			}
		}
		return nonNullList;
	}
}
