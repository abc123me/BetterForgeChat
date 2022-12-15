package com.jeremiahbl.bfcmod.utils;

public interface IDiscordListener {
	public default void onDiscordReady() {};
	public void onDiscordMessageReceived(String msg, String username);
}
