package com.benzrf.sblock.sburbplayers.commandparser.converters;

import com.benzrf.sblock.common.commandparser.converters.ArgumentConverter;

public class StringArgumentConverter implements ArgumentConverter
{
	@Override
	public Object convertArgument(String arg)
	{
		return arg;
	}
}
