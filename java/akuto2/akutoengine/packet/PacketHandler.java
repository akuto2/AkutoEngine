package akuto2.akutoengine.packet;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {
	private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("AkutoEngine");

	public static void init() {
		INSTANCE.registerMessage(ItemCountMessage.Handler.class, ItemCountMessage.class, 0, Side.CLIENT);
	}

	public static void sendToPoint(IMessage message) {
		if(message instanceof ItemCountMessage) {
			ItemCountMessage countMessage = (ItemCountMessage)message;
			INSTANCE.sendToAllAround(countMessage, new NetworkRegistry.TargetPoint(countMessage.dim, countMessage.x, countMessage.y, countMessage.z, 16));
		}
	}
}
