package akuto2.akutoengine.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import akuto2.akutoengine.AkutoEngine;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;

public class Update {
	private UpdateInfo updateInfo = null;
	private ModContainer container = Loader.instance().activeModContainer();
	private boolean isCompleted = false;

	public boolean isCompleted() {
		return isCompleted;
	}

	public UpdateInfo getUpdateInfo() {
		return updateInfo;
	}

	public void checkUpdate() {
		new Thread("[AkutoEngine]update check") {
			public void run() {
				try {
					String receivedData;
					try {
						URL url = new URL(AkutoEngine.meta.updateUrl);
						InputStream con = url.openStream();
						receivedData = new String(ByteStreams.toByteArray(con));
						con.close();
					}
					catch(IOException exception) {
						return;
					}
					System.out.println("json null");
					List<Map<String, Object>> updateInfoList;
					try {
						updateInfoList = (List)new Gson().fromJson(receivedData, List.class);
					}
					catch(JsonSyntaxException exception) {
						return;
					}
					Map<String, String> updateInfoJson = null;
					for(Map<String, Object> map : updateInfoList) {
						updateInfoJson = (Map)map.get("1.7.10");
					}
					if(updateInfoJson == null) {
						return;
					}
					String currentVersion = Update.this.container.getVersion();
					String newVersion = (String)updateInfoJson.get("version");
					if(!currentVersion.equals(newVersion)) {
						Update.this.updateInfo = new Update.UpdateInfo();
						Update.this.updateInfo.version = ((String)updateInfoJson.get("version"));
						Update.this.updateInfo.downloadUrl = ((String)updateInfoJson.get("downloadUrl"));
					}
				}
				finally {
					Update.this.isCompleted = true;
				}
			}
		}.start();
	}

	public void notifyUpdate(ICommandSender commandSender, Side side) {
		if(updateInfo != null) {
			if(side == Side.SERVER) {
				commandSender.addChatMessage(new ChatComponentTranslation("akutoengine.update.server", new Object[] {updateInfo.version, updateInfo.downloadUrl}));
			}
			else {
				ChatStyle style = new ChatStyle();
				style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, updateInfo.downloadUrl));
				commandSender.addChatMessage(new ChatComponentTranslation("akutoengine.update.client", new Object[] {updateInfo.version}).setChatStyle(style));
			}
		}
	}

	public static class UpdateInfo {
		public String version;
		public String downloadUrl;
	}
}
