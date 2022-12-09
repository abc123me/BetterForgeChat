package com.jeremiahbl.bfcmod.utils;

public interface IDiscordListener {
	public void onDiscordReady();
	public void onMessageReceived(String msg, String username);
}
