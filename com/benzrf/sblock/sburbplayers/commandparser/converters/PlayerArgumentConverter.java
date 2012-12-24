package com.benzrf.sblock.sburbplayers.commandparser.converters;

import com.benzrf.sblock.sburbplayers.SburbPlayers;

public class PlayerArgumentConverter implements ArgumentConverter
{
	@Override
	public Object convertArgument(String arg)
	{
		return SburbPlayers.instance.getServer().getPlayer(arg);
	}
}
