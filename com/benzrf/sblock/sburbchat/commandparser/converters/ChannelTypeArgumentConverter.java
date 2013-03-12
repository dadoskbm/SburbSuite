package com.benzrf.sblock.sburbchat.commandparser.converters;

import com.benzrf.sblock.common.commandparser.converters.ArgumentConverter;
import com.benzrf.sblock.sburbchat.channel.ChannelType;

public class ChannelTypeArgumentConverter implements ArgumentConverter
{
	@Override
	public Object convertArgument(String arg)
	{
		return ChannelType.valueOf(arg.toUpperCase());
	}
}
