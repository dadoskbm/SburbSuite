package co.sblock.sburbchat.channel.channels;

import java.util.*;

import org.bukkit.ChatColor;

import co.sblock.sburbchat.User;
import co.sblock.sburbchat.channel.AccessLevel;
import co.sblock.sburbchat.channel.ChannelType;


public class NickChannel extends NormalChannel
{
	public NickChannel(){}
	public NickChannel(String name, AccessLevel listeningAccess, AccessLevel sendingAccess, String creator)
	{
		super(name, listeningAccess, sendingAccess, creator);
	}
	
	
	@Override
	public void setNick(String nick, User sender)
	{
		if (!this.nickMap.containsValue(nick))
		{
			this.nickMap.put(sender.getName(), nick);
			this.sendToAll(ChatColor.YELLOW + sender.getName() + " has set their nick to " + ChatColor.DARK_BLUE + nick + ChatColor.YELLOW + "!", sender);
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "The nick " + ChatColor.DARK_BLUE + nick + ChatColor.RED + " is already taken in channel " + ChatColor.GOLD + this.name + ChatColor.RED + "!");
		}
	}
	
	@Override
	public void removeNick(User sender)
	{
		if (this.nickMap.containsKey(sender.getName()))
		{
			this.nickMap.remove(sender.getName());
			this.sendToAll(ChatColor.YELLOW + sender.getName() + " no longer has a nick!", sender);
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "You don't have a nick in channel " + ChatColor.GOLD + this.name + ChatColor.RED + "!");
		}
	}
	
	@Override
	public ChannelType getType()
	{
		return ChannelType.NICK;
	}
	
	protected Map<String, String> nickMap = new HashMap<String, String>();
	
	private static final long serialVersionUID = -6365694572913986352L;
}

