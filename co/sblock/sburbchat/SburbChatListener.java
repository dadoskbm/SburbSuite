package co.sblock.sburbchat;

import java.io.*;
import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SburbChatListener implements Listener
{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		try
		{
			User.addPlayer(event.getPlayer());
		}
		catch (IOException e)
		{
			Logger.getLogger("Minecraft").severe(SburbChat.getInstance().prefix() + "Error loading userdata for " + event.getPlayer().getName() + ":");
			e.printStackTrace();
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
	{
		if (User.getUser(event.getPlayer().getName()) != null)
		{
			event.setCancelled(true);
			User.getUser(event.getPlayer().getName()).chat(event);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		try
		{
			User.removePlayer(event.getPlayer());
		}
		catch (IOException e)
		{
			Logger.getLogger("Minecraft").severe(SburbChat.getInstance().prefix() + "Error saving userdata for " + event.getPlayer().getName() + ":");
			e.printStackTrace();
		}
	}
}
