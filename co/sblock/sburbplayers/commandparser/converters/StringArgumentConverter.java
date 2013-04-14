package co.sblock.sburbplayers.commandparser.converters;

import co.sblock.common.commandparser.converters.ArgumentConverter;

public class StringArgumentConverter implements ArgumentConverter
{
	@Override
	public Object convertArgument(String arg)
	{
		return arg;
	}
}
