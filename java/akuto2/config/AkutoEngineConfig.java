package akuto2.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = "akutoengine", name = "AkutoEngine")
@EventBusSubscriber(modid = "akutoengine")
public class AkutoEngineConfig {
	public static final Recipes recipes = new Recipes();
	public static class Recipes{
		@Comment("Can create Final Type equipment in survival mode")
		public boolean isFinalType = false;
	}

	public static final Filler filler = new Filler();
	public static class Filler{
		@Comment("Torch Module Interval: 2 - 64")
		@RangeInt(min = 2, max = 64)
		public int intervalTorch = 6;
	}

	@SubscribeEvent
	public static void onConfigChanged(OnConfigChangedEvent event) {
		if(event.getModID().equals("akutoengine")) {
			ConfigManager.sync("akutoengine", Type.INSTANCE);
		}
	}
}
