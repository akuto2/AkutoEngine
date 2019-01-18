package Akuto2Mod.Items;

import org.w3c.dom.Document;

import Akuto2Mod.Client.ClientProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import lib.api.books.BookData;
import lib.api.books.BookDataStore;

public class ManualInfo {
	BookData filler = new BookData();

	public ManualInfo() {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		filler = initManual(filler, "item.manual.filler", "", side == Side.CLIENT ? ClientProxy.filler : null);
	}

	public BookData initManual(BookData data, String name, String toolTip, Document doc) {
		data.unlocalizedName = name;
		data.toolTip = toolTip;
		data.modID = "akutoengine";
		data.document = doc;
		BookDataStore.addBook(data);
		return data;
	}
}
