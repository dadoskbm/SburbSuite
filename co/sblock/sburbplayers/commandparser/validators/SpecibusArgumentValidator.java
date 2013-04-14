package co.sblock.sburbplayers.commandparser.validators;

import co.sblock.common.commandparser.validators.ArgumentValidator;
import co.sblock.sburbplayers.SburbPlayers;


public class SpecibusArgumentValidator implements ArgumentValidator
{
	@Override
	public boolean isArgumentValid(String arg)
	{
		return SburbPlayers.getInstance().getAbstrata().containsKey(arg);
	}
}
