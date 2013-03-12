package com.benzrf.sblock.common.commandparser;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.benzrf.sblock.common.commandparser.converters.ArgumentConverter;
import com.benzrf.sblock.common.commandparser.converters.StringArgumentConverter;
import com.benzrf.sblock.common.commandparser.validators.ArgumentValidator;
import com.benzrf.sblock.common.commandparser.validators.StringArgumentValidator;
import com.benzrf.sblock.sburbchat.User;
import com.benzrf.sblock.sburbchat.channel.AccessLevel;
import com.benzrf.sblock.sburbchat.channel.ChannelType;
import com.benzrf.sblock.sburbchat.channel.channels.Channel;
import com.benzrf.sblock.sburbchat.commandparser.converters.AccessLevelArgumentConverter;
import com.benzrf.sblock.sburbchat.commandparser.converters.ChannelArgumentConverter;
import com.benzrf.sblock.sburbchat.commandparser.converters.ChannelTypeArgumentConverter;
import com.benzrf.sblock.sburbchat.commandparser.converters.PrivilegeArgumentConverter;
import com.benzrf.sblock.sburbchat.commandparser.converters.UserArgumentConverter;
import com.benzrf.sblock.sburbchat.commandparser.validators.AccessLevelArgumentValidator;
import com.benzrf.sblock.sburbchat.commandparser.validators.AliasArgumentValidator;
import com.benzrf.sblock.sburbchat.commandparser.validators.ChannelArgumentValidator;
import com.benzrf.sblock.sburbchat.commandparser.validators.ChannelTypeArgumentValidator;
import com.benzrf.sblock.sburbchat.commandparser.validators.NewChannelArgumentValidator;
import com.benzrf.sblock.sburbchat.commandparser.validators.PrivilegeArgumentValidator;
import com.benzrf.sblock.sburbchat.commandparser.validators.UserArgumentValidator;
import com.benzrf.sblock.sburbplayers.commandparser.converters.PlayerArgumentConverter;
import com.benzrf.sblock.sburbplayers.commandparser.validators.PlayerArgumentValidator;
import com.benzrf.sblock.sburbplayers.commandparser.validators.SpecibusArgumentValidator;

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
	CHANNEL_TYPE("channeltype", new ChannelTypeArgumentValidator(), new ChannelTypeArgumentConverter(), ChannelType.class),
	SPECIBUS("specibus", new SpecibusArgumentValidator(), new StringArgumentConverter(), String.class),
	PLAYER("player", new PlayerArgumentValidator(), new PlayerArgumentConverter(), Player.class);
	
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
