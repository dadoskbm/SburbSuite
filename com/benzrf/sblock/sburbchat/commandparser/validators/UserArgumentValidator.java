package com.benzrf.sblock.sburbchat.commandparser.validators;

import com.benzrf.sblock.common.commandparser.validators.ArgumentValidator;
import com.benzrf.sblock.sburbchat.User;

public class UserArgumentValidator implements ArgumentValidator
{
	@Override
	public boolean isArgumentValid(String arg)
	{
		if (User.getUser(arg) != null)
		{
			return true;
		}
		return false;
	}
}
