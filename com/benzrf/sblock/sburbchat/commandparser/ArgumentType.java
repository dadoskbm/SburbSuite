package com.benzrf.sblock.sburbchat.commandparser;

import org.bukkit.ChatColor;

import com.benzrf.sblock.sburbchat.User;
import com.benzrf.sblock.sburbchat.channel.AccessLevel;
import com.benzrf.sblock.sburbchat.channel.ChannelType;
import com.benzrf.sblock.sburbchat.channel.channels.Channel;
import com.benzrf.sblock.sburbchat.commandparser.converters.*;
import com.benzrf.sblock.sburbchat.commandparser.validators.*;

public enum ArgumentType
{
	ALIAS(ChatColor.BLUE + "aliasname", new AliasArgumentValidator(), new StringArgumentConverter(), String.class),
	MESSAGE("message", new StringArgumentValidator(), new StringArgumentConverter(), String.class, true),
	NICK("nick", new StringArgumentValidator(), new StringArgumentConverter(), String.class),
	PRIVILEGE_LEVEL("level", new PrivilegeArgumentValidator(), new PrivilegeArgumentConverter(), PrivilegeLevel.class),
	CHANNEL(ChatColor.GOLD + "#channelname", new ChannelArgumentValidator(), new ChannelArgumentConverter(), Channel.class),
	NEW_CHANNEL(ChatColor.GOLD + "#channelname", new NewChannelArgumentValidator(), new StringArgumentConverter(), String.class),
	USER("username", new UserArgumentValidator(), new UserArgumentConverter(), User.class),
	ACCESS_LEVEL("accesslevel", new AccessLevelArgumentValidator(), new AccessLevelArgumentConverter(), AccessLevel.class),
	CHANNEL_TYPE("channeltype", new ChannelTypeArgumentValidator(), new ChannelTypeArgumentConverter(), ChannelType.class);
	
	private ArgumentType(String humanName, ArgumentValidator av, ArgumentConverter ac, Class<?> rt)
	{
		this(humanName, av, ac, rt, false);
	}
	
	private ArgumentType(String humanName, ArgumentValidator av, ArgumentConverter ac, Class<?> rt, boolean merge)
	{
		this.humanName = humanName;
		this.av = av;
		this.ac = ac;
		this.rt = rt;
		this.merge = merge;
	}
	
	/**
	 * Returns whether to "merge" the remaining arguments in the command into this one. Used for messages
	 * @return true if the remaining arguments in the command should be merged with this one.
	 * @author FireNG
	 */
	public boolean mergeArguments()
	{
		return merge;
	}
	
	public String getHumanName()
	{
		return this.humanName;
	}
	
	public boolean isArgumentValid(String arg)
	{
		return av.isArgumentValid(arg);
	}
	
	public Object convertArgument(String arg)
	{
		return ac.convertArgument(arg);
	}
	
	public Class<?> getReturnType()
	{
		return rt;
	}
	
	private String humanName;
	private ArgumentValidator av;
	private ArgumentConverter ac;
	private Class<?> rt;
	private boolean merge;
}
