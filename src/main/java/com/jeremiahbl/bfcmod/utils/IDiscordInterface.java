package com.jeremiahbl.bfcmod.utils;

import com.jeremiahbl.bfcmod.config.IReloadable;
import com.mojang.authlib.GameProfile;

public interface IDiscordInterface extends IReloadable {
	/**
	 * Connects to the discord API
	 * 
	 * @return true if connected
	 */
	public boolean connectAPI();
	/**
	 * Registers a discord event listener
	 * 
	 * @param list
	 */
	public void registerDiscordListener(IDiscordListener list);
	/**
	 * Should be called when a player sends a chat message
	 * 
	 * @param playerProfile The player's associated GameProfile
	 * @param playerChatName The player's raw chat name
	 * @param message The player's message
	 */
	public void sendChatMessage(GameProfile playerProfile, String playerChatName, String message);

	/**
	 * Should be called when a player leaves or joins the game
	 * 
	 * @param playerProfile The player's associated GameProfile
	 * @param playerChatName The player's raw chat name
	 * @param left True if the player left the game, False if they joined the game
	 */
	public void sendPlayerMessage(GameProfile playerProfile, String playerChatName, boolean left);
	/**
	 * Should be called when a player dies
	 * 
	 * @param playerProfile The player's associated GameProfile
	 * @param playerChatName The player's raw chat name
	 * @param reason How they died
	 */
	public void sendDeathMessage(GameProfile playerProfile, String playerChatName, String reason);
	/** Disconnects from the discord API */
	public void disconnectAPI();
}
