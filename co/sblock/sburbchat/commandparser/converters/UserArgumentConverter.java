package co.sblock.sburbchat.commandparser.converters;

import co.sblock.common.commandparser.converters.ArgumentConverter;
import co.sblock.sburbchat.User;


public class UserArgumentConverter implements ArgumentConverter
{
	@Override
	public Object convertArgument(String arg)
	{
		return User.getUser(arg);
	}
}
