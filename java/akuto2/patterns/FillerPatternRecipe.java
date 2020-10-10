package akuto2.patterns;

import java.util.HashMap;
import java.util.LinkedList;

import javax.annotation.Nonnull;

import akuto2.gui.container.inventory.InventoryFillerEXCrafting;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;

/**
 * フィラーのパターン用のレシピ管理クラス
 */
public class FillerPatternRecipe {
	public int recipeWidth;
	public int recipeHeight;
	public NonNullList<ItemStack> input;
	public FillerPatternCore output;
	public static LinkedList<FillerPatternRecipe> recipes = new LinkedList();

	public FillerPatternRecipe(int width, int height, FillerPatternCore pattern, @Nonnull ItemStack... stacks) {
		this(width, height, pattern, getInputNonNullList(width, height, stacks));
	}

	public FillerPatternRecipe(int width, int height, FillerPatternCore pattern, NonNullList<ItemStack> stacks) {
		recipeWidth = width;
		recipeHeight = height;
		output = pattern;
		input = stacks;
	}

	private static NonNullList<ItemStack> getInputNonNullList(int width, int height,@Nonnull ItemStack... stacks){
		NonNullList<ItemStack> list = NonNullList.withSize(width * height, ItemStack.EMPTY);

		int x = 0;
		for(ItemStack stack : stacks) {
			list.set(x++, stack);
		}

		return list;
	}

	public boolean matches(NonNullList<ItemStack> craft) {
		for(int i = 0; i <= 3 - recipeWidth; i++) {
			for(int j = 0; j <= 3 - recipeHeight; j++) {
				if(checkMatch(craft, i, j, true)) {
					return true;
				}
				if(checkMatch(craft, i, j, false)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean matches(IInventory inventory) {
		NonNullList<ItemStack> stacks = NonNullList.withSize(9, ItemStack.EMPTY);

		for(int x = 0; x < stacks.size(); x++) {
			stacks.set(x, inventory.getStackInSlot(x));
		}

		return matches(stacks);
	}

	private boolean checkMatch(NonNullList<ItemStack> craft, int i, int j, boolean flag) {
		for(int k = 0; k < 3; k++) {
			for(int l = 0; l < 3; l++) {
				int i1 = k - i;
				int j1 = l - j;
				ItemStack stack = ItemStack.EMPTY;

				if(i1 >= 0 && j1 >= 0 && i1 < recipeWidth && j1 < recipeHeight) {
					if(flag) {
						stack = input.get(recipeWidth - i1 - 1 + j1 * recipeWidth);
					}
					else {
						stack = input.get(i1 + j1 * recipeWidth);
					}
				}

				ItemStack stack1 = craft.get(k + l * 3);
				if(!stack1.isEmpty() || !stack.isEmpty()) {
					if((stack1.isEmpty() && !stack.isEmpty()) || (!stack1.isEmpty() && stack.isEmpty())) {
						return false;
					}
					if(stack1.getItem() != stack.getItem()) {
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

	public static void addRecipe(FillerPatternCore pattern,@Nonnull ItemStack stack) {
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
			ItemStack stack = ItemStack.EMPTY;
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
				ingredients[i1] = ((ItemStack)hashMap.get(Character.valueOf(c))).copy();
			}
			else {
				ingredients[i1] = ItemStack.EMPTY;
			}
		}
		recipes.add(new FillerPatternRecipe(width, height, pattern, ingredients));
	}

	public static FillerPatternCore findMatchingRecipe(NonNullList<ItemStack> stacks) {
		for(int i = 0; i < recipes.size(); i++) {
			FillerPatternRecipe recipe = recipes.get(i);
			if(recipe.matches(stacks)) {
				return recipe.output;
			}
		}
		return null;
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

	public static NonNullList<ItemStack> getRemainingItems(InventoryFillerEXCrafting fillerEX){
		NonNullList<ItemStack> nonNullList = NonNullList.withSize(9, ItemStack.EMPTY);

		for(FillerPatternRecipe recipe : recipes) {
			if(recipe.matches(fillerEX)) {
				for(int i = 0; i < nonNullList.size(); i++) {
					ItemStack stack = fillerEX.getStackInSlot(i);

					nonNullList.set(i, ForgeHooks.getContainerItem(stack));
				}
				return nonNullList;
			}
		}

		for(int i = 0; i < nonNullList.size(); i++) {
			nonNullList.set(i, fillerEX.getStackInSlot(i));
		}

		return nonNullList;
	}
}
