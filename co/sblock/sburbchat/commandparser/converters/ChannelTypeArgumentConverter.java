package co.sblock.sburbchat.commandparser.converters;

import co.sblock.common.commandparser.converters.ArgumentConverter;
import co.sblock.sburbchat.channel.ChannelType;


public class ChannelTypeArgumentConverter implements ArgumentConverter
{
	@Override
	public Object convertArgument(String arg)
	{
		return ChannelType.valueOf(arg.toUpperCase());
	}
}
