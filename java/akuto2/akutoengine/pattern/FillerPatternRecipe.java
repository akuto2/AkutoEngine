package akuto2.akutoengine.pattern;

import java.util.HashMap;
import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FillerPatternRecipe{
	public int recipeWidth;
	public int recipeHeight;
	public ItemStack input[];
	public FillerPatternCore output;
	public static LinkedList<FillerPatternRecipe> recipes = new LinkedList<FillerPatternRecipe>();

	public FillerPatternRecipe(int width, int height, FillerPatternCore pattern, ItemStack... aItemStacks) {
		recipeWidth = width;
		recipeHeight = height;
		output = pattern;
		input = aItemStacks;
	}

	public boolean matches(IInventory inventory) {
		for(int i = 0; i <= 3 - recipeWidth; i++) {
			for(int j = 0; j <= 3 - recipeHeight; j++) {
				if(checkMatch(inventory, i, j, true)) {
					return true;
				}
				if(checkMatch(inventory, i, j, false)){
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkMatch(IInventory inventory, int i, int j, boolean flag) {
		for(int k = 0; k < 3; k++) {
			for(int l = 0; l < 3; l++) {
				int i1 = k - i;
				int j1 = l - j;
				ItemStack stack = null;

				if(i1 >= 0 && j1 >= 0 && i1 < recipeWidth && j1 < recipeHeight) {
					if(flag) {
						stack = input[recipeWidth - i1 - 1 + j1 * recipeWidth];
					}
					else {
						stack = input[i1 + j1 * recipeWidth];
					}
				}

				ItemStack stack1 = inventory.getStackInSlot(k + l * 3);
				if((stack1 != null) || (stack != null)) {
					if(((stack1 == null) && (stack != null)) || ((stack1 != null) && (stack == null))) {
						return false;
					}
					if((stack1.getItem() != stack.getItem())) {
						return false;
					}
					if((stack.getItemDamage() != 32767) && (stack.getItemDamage() != stack1.getItemDamage())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static void addRecipe(FillerPatternCore pattern, ItemStack stack) {
		recipes.add(new FillerPatternRecipe(1, 1, pattern, stack));
	}

	public static void addRecipe(FillerPatternCore pattern, Object... objects) {
		String string = "";
		int i = 0;
		int width = 0;
		int height = 0;

		if(objects[i] instanceof String[]) {
			String aString[] = (String[])objects[i++];

			for(int l = 0; l < aString.length; l++) {
				String string1 = aString[l];
				height++;
				width = string1.length();
				string = (new StringBuilder()).append(string).append(string1).toString();
			}
		}
		else {
			while(objects[i] instanceof String) {
				String string2 = (String)objects[i++];
				height++;
				width = string2.length();
				string = (new StringBuilder()).append(string).append(string2).toString();
			}
		}
		HashMap hashMap;
		for(hashMap = new HashMap(); i < objects.length; i += 2) {
			Character character = (Character)objects[i];
			ItemStack stack = null;
			if((objects[i + 1] instanceof Item)) {
				stack = new ItemStack((Item)objects[i + 1]);
			}
			else if((objects[i + 1] instanceof Block)) {
				stack = new ItemStack((Block)objects[i + 1], 1, 32767);
			}
			else if((objects[i + 1] instanceof ItemStack)) {
				stack = (ItemStack)objects[i + 1];
			}
			hashMap.put(character, stack);
		}
		ItemStack[] ingredients = new ItemStack[width * height];
		for(int i1 = 0; i1 < width * height; i1++) {
			char c = string.charAt(i1);
			if(hashMap.containsKey(Character.valueOf(c))) {
				ingredients[i1] = ((ItemStack) hashMap.get(Character.valueOf(c))).copy();
			}
			else {
				ingredients[i1] = null;
			}
		}
		recipes.add(new FillerPatternRecipe(width, height, pattern, ingredients));
	}

	public static FillerPatternCore findMatchingRecipe(IInventory inventory) {
		for(int i = 0; i < recipes.size(); i++) {
			FillerPatternRecipe recipe = recipes.get(i);
			if(recipe.matches(inventory)) {
				return recipe.output;
			}
		}
		return null;
	}
}
