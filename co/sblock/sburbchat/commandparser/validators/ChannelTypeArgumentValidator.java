package co.sblock.sburbchat.commandparser.validators;

import co.sblock.common.commandparser.validators.ArgumentValidator;
import co.sblock.sburbchat.channel.ChannelType;


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
