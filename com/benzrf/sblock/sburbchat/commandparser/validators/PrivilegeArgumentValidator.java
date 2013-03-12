package com.benzrf.sblock.sburbchat.commandparser.validators;

import com.benzrf.sblock.common.commandparser.PrivilegeLevel;
import com.benzrf.sblock.common.commandparser.validators.ArgumentValidator;

/**
 * Validates privilege level inputs.
 * 
 * <tt><div style = "color:#ff0000">
 * Excuse me sir, 6ut this c9de that y9u have written ridicules a seri9us issue. Y9u see, privilege is n9 laughing matter.
 * Many tr9lls (And humans, f9r that matter) a6use their privilege t9 keep themselves in a "higher" p9siti9n than 9thers in
 * s9ciety. Take y9urself, f9r example. Pr9grammers have a privilege 9ver 9thers as-</div> ERROR: java.io.SocketException: read timed out
 * </tt>
 * 
 * @author FireNG
 *
 */
public class PrivilegeArgumentValidator implements ArgumentValidator
{

	/* (non-Javadoc)
	 * @see com.benzrf.sblock.sburbchat.commandparser.validators.ArgumentValidator#isArgumentValid(java.lang.String)
	 */
	@Override
	public boolean isArgumentValid(String arg)
	{
		for(PrivilegeLevel p : PrivilegeLevel.values())
		{
			if(p.toString().equalsIgnoreCase(arg))
				return true;
		}
		return false;
	}

}
