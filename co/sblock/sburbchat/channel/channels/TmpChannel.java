package co.sblock.sburbchat.channel.channels;

import org.bukkit.ChatColor;

import co.sblock.sburbchat.SburbChat;
import co.sblock.sburbchat.User;
import co.sblock.sburbchat.channel.AccessLevel;
import co.sblock.sburbchat.channel.ChannelType;


public class TmpChannel extends NormalChannel
{
	public TmpChannel(){}
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
	
	@Override
	public ChannelType getType()
	{
		return ChannelType.TMP;
	}
	
	private static final long serialVersionUID = 3909818601954361239L;
}

