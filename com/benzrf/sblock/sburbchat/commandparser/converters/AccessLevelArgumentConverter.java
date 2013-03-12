package com.benzrf.sblock.sburbchat.commandparser.converters;

import com.benzrf.sblock.common.commandparser.converters.ArgumentConverter;
import com.benzrf.sblock.sburbchat.channel.AccessLevel;

public class AccessLevelArgumentConverter implements ArgumentConverter
{
	@Override
	public Object convertArgument(String arg)
	{
		return AccessLevel.valueOf(arg.toUpperCase());
	}
}
