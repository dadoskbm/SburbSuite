package co.sblock.sburbchat.commandparser.validators;

import co.sblock.common.commandparser.validators.ArgumentValidator;
import co.sblock.sburbchat.User;


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
