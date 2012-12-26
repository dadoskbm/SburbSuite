package com.benzrf.sblock.sburbchat;

import static com.benzrf.sblock.sburbchat.commandparser.ArgumentType.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.benzrf.sblock.sburbchat.channel.AccessLevel;
import com.benzrf.sblock.sburbchat.channel.ChannelManager;
import com.benzrf.sblock.sburbchat.channel.ChannelType;
import com.benzrf.sblock.sburbchat.commandparser.CommandNode;
import com.benzrf.sblock.sburbchat.commandparser.CommandParser;
import com.benzrf.sblock.sburbchat.commandparser.ExecutableCommandNode;

public class SburbChat extends JavaPlugin
{
	@Override
	public void onDisable()
	{
		for (Player p : this.getServer().getOnlinePlayers())
		{
			try
			{
				User.removePlayer(p);
			}
			catch (IOException e)
			{
				Logger.getLogger("Minecraft").severe(this.prefix + "Error saving userdata for " + p.getName() + ":");
				e.printStackTrace();
			}
		}
		try
		{
			this.cm.writeChannels("plugins/SburbChat/", "channels.scd");
		}
		catch (IOException e)
		{
			Logger.getLogger("Minecraft").warning(this.prefix + "Error writing channels!");
		}
		try
		{
			this.cm.writeAliases("plugins/SburbChat/aliases.scd");
		}
		catch (FileNotFoundException e)
		{
			Logger.getLogger("Minecraft").warning(this.prefix + "Could not write aliases file!");
		}
		catch (IOException e)
		{
			Logger.getLogger("Minecraft").warning(this.prefix + "Error writing aliases file!");
		}
	}

	@Override
	public void onEnable()
	{
		this.cm.newChannel("#", ChannelType.NORMAL, AccessLevel.PUBLIC, AccessLevel.PUBLIC, "benzrf")
		.addMod("Dublek");
		
		this.commandRoot = buildCommandTree();
		instance = this;
		this.getServer().getPluginManager().registerEvents(listener, this);
		try
		{
			this.cm.readChannels("plugins/SburbChat/", "channels.scd");
		}
		catch (IOException e)
		{
			Logger.getLogger("Minecraft").warning(this.prefix + "Error reading channels file!");
		}
		catch (ClassNotFoundException e)
		{
			Logger.getLogger("Minecraft").warning(this.prefix + "Error reading channels file!");
		}
		try
		{
			this.cm.readAliases("plugins/SburbChat/aliases.scd");
		}
		catch (IOException e)
		{
			Logger.getLogger("Minecraft").warning(this.prefix + "Error reading aliases file!");
		}
		catch (ClassNotFoundException e)
		{
			Logger.getLogger("Minecraft").warning(this.prefix + "Error reading aliases file!");
		}
		for (Player p : this.getServer().getOnlinePlayers())
		{
			try
			{
				User.addPlayer(p);
			}
			catch (IOException e)
			{
				Logger.getLogger("Minecraft").severe(SburbChat.getInstance().prefix() + "Error loading userdata for " + p.getName() + ":");
				e.printStackTrace();
			}
		}
	}
	
	private CommandNode buildCommandTree()
	{
		CommandNode root = new CommandNode("sc");
		new ExecutableCommandNode("c", root, "setCurrent", CHANNEL);
		new ExecutableCommandNode("current", root, "setCurrent", CHANNEL);
		new ExecutableCommandNode("l", root, "addListening", CHANNEL);
		new ExecutableCommandNode("listen", root, "addListening", CHANNEL);
		new ExecutableCommandNode("r", root, "removeListening", CHANNEL);
		new ExecutableCommandNode("remove", root, "removeListening", CHANNEL);
		new ExecutableCommandNode("m", root, "toggleMute");
		new ExecutableCommandNode("mute", root, "toggleMute");
		new ExecutableCommandNode("w", root, "listUsers");
		new ExecutableCommandNode("who", root, "listUsers");
		new ExecutableCommandNode("ch", root, "listChannels");
		new ExecutableCommandNode("channels", root, "listChannels");
		
		CommandNode alias = new CommandNode("alias", root);
		new ExecutableCommandNode("new", alias, "addAlias", ALIAS);
		new ExecutableCommandNode("del", alias, "removeAlias", ALIAS);
		
		CommandNode channel = new CommandNode("channel", root);
		new ExecutableCommandNode("new", channel, "newChannel", NEW_CHANNEL, CHANNEL_TYPE, ACCESS_LEVEL, ACCESS_LEVEL);
		new ExecutableCommandNode("mod", channel, "addMod", USER);
		new ExecutableCommandNode("demod", channel, "removeMod", USER);
		new ExecutableCommandNode("disband", channel, "disband");
		
		CommandNode mod = new CommandNode("mod", root);
		new ExecutableCommandNode("kick", mod, "kick", USER);
		new ExecutableCommandNode("ban", mod, "ban", USER);
		new ExecutableCommandNode("unban", mod, "unban", USER);
		new ExecutableCommandNode("mute", mod, "mute", USER);
		new ExecutableCommandNode("unmute", mod, "unmute", USER);
		new ExecutableCommandNode("approve", mod, "approve", USER);
		new ExecutableCommandNode("deapprove", mod, "deapprove", USER);
		
		CommandNode nick = new CommandNode("nick", root);
		new ExecutableCommandNode("set", nick, "setNick", NICK);
		new ExecutableCommandNode("remove", nick, "removeNick");
		
		CommandNode info = new CommandNode("info", root);
		new ExecutableCommandNode("l", info, "getListeningChannels");
		new ExecutableCommandNode("listchannels", info, "getListeningChannels");
		
		return root;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if (sender.getName().equals("CONSOLE"))
		{
			sender.sendMessage(this.prefix + ChatColor.RED + "Only players can use SburbChat commands!");
			return true;
		}
		if (User.getUser(sender.getName()) == null)
		{
			sender.sendMessage(this.prefix + ChatColor.RED + "You are not listed as a user for some reason! Seek help immediately!");
			return true;
		}
		CommandParser.runCommand(args, this.commandRoot, User.getUser(sender.getName()));
		return true;
	}
	
	public ChannelManager getChannelManager()
	{
		return cm;
	}
	
	public static SburbChat getInstance()
	{
		return instance;
	}
	
	public String prefix()
	{
		return prefix;
	}
	
	private String prefix = ChatColor.WHITE + "[" + ChatColor.GREEN + "Sburb" + ChatColor.YELLOW + "Chat" + ChatColor.WHITE + "] ";
	private ChannelManager cm = new ChannelManager();
	private CommandNode commandRoot;
	private SburbChatListener listener = new SburbChatListener();
	private static SburbChat instance; 
}