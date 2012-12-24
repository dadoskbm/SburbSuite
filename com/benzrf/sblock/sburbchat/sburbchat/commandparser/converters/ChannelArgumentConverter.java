package com.benzrf.sblock.sburbchat.commandparser.converters;

import com.benzrf.sblock.sburbchat.SburbChat;

public class ChannelArgumentConverter implements ArgumentConverter
{
	@Override
	public Object convertArgument(String arg)
	{
		return SburbChat.getInstance().getChannelManager().getChannel(arg);
	}
}
