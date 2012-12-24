package com.benzrf.sblock.sburbchat.channel.channels;

import org.bukkit.ChatColor;

import com.benzrf.sblock.sburbchat.SburbChat;
import com.benzrf.sblock.sburbchat.User;
import com.benzrf.sblock.sburbchat.channel.AccessLevel;

public class TmpChannel extends NormalChannel
{

	public TmpChannel(String name, AccessLevel listeningAccess, AccessLevel sendingAccess, String creator)
	{
		super(name, listeningAccess, sendingAccess, creator);
	}
	
	@Override
	public void userLeave(User sender)
	{
		super.userLeave(sender);
		if (this.listening.size() == 0)
		{
			sender.sendMessageFromChannel(ChatColor.AQUA + "Channel " + ChatColor.GOLD + this.name + ChatColor.AQUA + " has been disbanded.", this);
			SburbChat.getInstance().getChannelManager().disbandChannel(this.name);
		}
	}
	
	private static final long serialVersionUID = 3909818601954361239L;
}
