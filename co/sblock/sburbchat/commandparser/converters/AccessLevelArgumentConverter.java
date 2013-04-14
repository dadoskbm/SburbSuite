package co.sblock.sburbchat.commandparser.converters;

import co.sblock.common.commandparser.converters.ArgumentConverter;
import co.sblock.sburbchat.channel.AccessLevel;


public class AccessLevelArgumentConverter implements ArgumentConverter
{
	@Override
	public Object convertArgument(String arg)
	{
		return AccessLevel.valueOf(arg.toUpperCase());
	}
}
