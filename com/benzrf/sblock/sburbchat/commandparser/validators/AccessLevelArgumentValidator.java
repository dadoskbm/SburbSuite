package com.benzrf.sblock.sburbchat.commandparser.validators;

import com.benzrf.sblock.sburbchat.channel.AccessLevel;

public class AccessLevelArgumentValidator implements ArgumentValidator
{
	@Override
	public boolean isArgumentValid(String arg)
	{
		for (AccessLevel al : AccessLevel.values())
		{
			if (arg.toUpperCase().equals(al.toString()))
			{
				return true;
			}
		}
		return false;
	}
}
