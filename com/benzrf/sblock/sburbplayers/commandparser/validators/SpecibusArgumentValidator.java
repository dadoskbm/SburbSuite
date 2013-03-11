package com.benzrf.sblock.sburbplayers.commandparser.validators;

import com.benzrf.sblock.sburbplayers.SburbPlayers;

public class SpecibusArgumentValidator implements ArgumentValidator
{
	@Override
	public boolean isArgumentValid(String arg)
	{
		return SburbPlayers.instance.abstrata.containsKey(arg);
	}
}
