package akuto2.utils.properties;

import akuto2.utils.enums.EnumEngineType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;

public class AkutoEngineProperty {
	public static final IProperty<EnumEngineType> ENGINE_TYPE = PropertyEnum.create("type", EnumEngineType.class);
}
