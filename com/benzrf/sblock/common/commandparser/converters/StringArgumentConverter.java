package com.benzrf.sblock.common.commandparser.converters;

public class StringArgumentConverter implements ArgumentConverter
{
	@Override
	public Object convertArgument(String arg)
	{
		return arg;
	}
}
