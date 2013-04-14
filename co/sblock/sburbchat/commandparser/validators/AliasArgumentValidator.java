package co.sblock.sburbchat.commandparser.validators;

import co.sblock.common.commandparser.validators.ArgumentValidator;

public class AliasArgumentValidator implements ArgumentValidator
{
	@Override
	public boolean isArgumentValid(String arg)
	{
		return !arg.startsWith("#");
	}	
}
