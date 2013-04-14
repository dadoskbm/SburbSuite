package co.sblock.sburbchat.commandparser.validators;

import org.bukkit.ChatColor;

import co.sblock.common.commandparser.validators.ArgumentValidator;
import co.sblock.sburbchat.SburbChat;


//import com.benzrf.sburb.sburbchat.SburbChat;

public class ChannelArgumentValidator implements ArgumentValidator
{
	@Override
	public boolean isArgumentValid(String arg)
	{
		if (/*arg.startsWith("#") && */!arg.contains(String.valueOf(ChatColor.COLOR_CHAR)) && !arg.contains("/"))
		{
			if (SburbChat.getInstance().getChannelManager().getChannel(arg) != null)
			{
				return true;
			}
		}
		return false;
	}
}
