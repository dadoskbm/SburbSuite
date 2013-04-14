package co.sblock.sburbplayers.commandparser.converters;

import co.sblock.common.commandparser.converters.ArgumentConverter;
import co.sblock.sburbplayers.SburbPlayers;


public class PlayerArgumentConverter implements ArgumentConverter
{
	@Override
	public Object convertArgument(String arg)
	{
		return SburbPlayers.getInstance().getServer().getPlayer(arg);
	}
}
