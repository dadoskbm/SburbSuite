package com.benzrf.sblock.sburbchat.commandparser.validators;

import com.benzrf.sblock.common.commandparser.validators.ArgumentValidator;

public class AliasArgumentValidator implements ArgumentValidator
{
	@Override
	public boolean isArgumentValid(String arg)
	{
		return !arg.startsWith("#");
	}	
}
