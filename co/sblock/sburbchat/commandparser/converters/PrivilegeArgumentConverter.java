package co.sblock.sburbchat.commandparser.converters;

import co.sblock.common.commandparser.PrivilegeLevel;
import co.sblock.common.commandparser.converters.ArgumentConverter;


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
