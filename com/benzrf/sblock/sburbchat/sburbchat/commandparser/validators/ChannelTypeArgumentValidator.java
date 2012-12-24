package com.benzrf.sblock.sburbchat.commandparser.validators;

import com.benzrf.sblock.sburbchat.channel.ChannelType;

public class ChannelTypeArgumentValidator implements ArgumentValidator
{
	@Override
	public boolean isArgumentValid(String arg)
	{
		for (ChannelType ct/*hur hur equius*/ : ChannelType.values())
		{
			if (arg.toUpperCase().equals(ct.toString()))
			{
				return true;
			}
		}
		return false;
	}
}
