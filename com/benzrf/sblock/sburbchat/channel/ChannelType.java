package com.benzrf.sblock.sburbchat.channel;

import com.benzrf.sblock.sburbchat.channel.channels.*;

@SuppressWarnings("rawtypes")
public enum ChannelType
{
	NORMAL(NormalChannel.class),
	RP(RPChannel.class),
	NICK(NickChannel.class),
	TMP(TmpChannel.class);
	
	private ChannelType(Class c)
	{
		channelType = c;
	}
	
	@SuppressWarnings("unchecked")
	public Channel newChannel(String name, AccessLevel listeningAccess, AccessLevel sendingAccess, String creator)
	{
		try
		{
			return (Channel) channelType.getConstructor(String.class, AccessLevel.class, AccessLevel.class, String.class).newInstance(name, listeningAccess, sendingAccess, creator);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public Class<?> getType()
	{
		return channelType;
	}
	
	private Class channelType;
}
