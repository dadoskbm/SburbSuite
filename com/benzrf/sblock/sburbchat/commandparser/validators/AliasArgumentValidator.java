package com.benzrf.sblock.sburbchat.commandparser.validators;

public class AliasArgumentValidator implements ArgumentValidator
{
	@Override
	public boolean isArgumentValid(String arg)
	{
		return !arg.startsWith("#");
	}	
}
