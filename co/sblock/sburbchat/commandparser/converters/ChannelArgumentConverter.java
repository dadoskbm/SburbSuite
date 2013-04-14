package co.sblock.sburbchat.commandparser.converters;

import co.sblock.common.commandparser.converters.ArgumentConverter;
import co.sblock.sburbchat.SburbChat;


public class ChannelArgumentConverter implements ArgumentConverter
{
	@Override
	public Object convertArgument(String arg)
	{
		return SburbChat.getInstance().getChannelManager().getChannel(arg);
	}
}
