package com.benzrf.sblock.sburbchat.commandparser.converters;

import com.benzrf.sblock.common.commandparser.converters.ArgumentConverter;
import com.benzrf.sblock.sburbchat.User;

public class UserArgumentConverter implements ArgumentConverter
{
	@Override
	public Object convertArgument(String arg)
	{
		return User.getUser(arg);
	}
}
