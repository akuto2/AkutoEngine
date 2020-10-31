package akuto2.akutoengine.config;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;

import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class FinalTypeEnabledFactory implements IConditionFactory{
	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		return () -> AkutoEngineConfig.recipes.isFinalType;
	}
}
