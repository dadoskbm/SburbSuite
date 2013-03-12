package com.benzrf.sblock.sburbchat.commandparser.converters;

import com.benzrf.sblock.common.commandparser.PrivilegeLevel;
import com.benzrf.sblock.common.commandparser.converters.ArgumentConverter;

/**
 * Converts privilege level arguments <br/>
 * 
 * Check y9ur privilege!
 * 
 * @author FireNG
 *
 */
public class PrivilegeArgumentConverter implements ArgumentConverter
{

	/* (non-Javadoc)
	 * @see com.benzrf.sblock.sburbchat.commandparser.converters.ArgumentConverter#convertArgument(java.lang.String)
	 */
	@Override
	public Object convertArgument(String arg)
	{
		return PrivilegeLevel.valueOf(arg.toUpperCase());
	}

}
