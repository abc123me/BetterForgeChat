package com.jeremiahbl.bfcmod.utils;

import com.jeremiahbl.bfcmod.config.IReloadable;

public interface IDiscordInterface extends IReloadable {
	public void sendMessage(String str);
	public void disconnectAPI();
	public void connectAPI();
	public void registerDiscordListener(IDiscordListener list);
}
