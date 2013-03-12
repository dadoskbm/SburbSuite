package com.benzrf.sblock.sburbchat.commandparser.validators;

import org.bukkit.ChatColor;

import com.benzrf.sblock.common.commandparser.validators.ArgumentValidator;

public class NewChannelArgumentValidator implements ArgumentValidator
{
	@Override
	public boolean isArgumentValid(String arg)
	{
		return arg.startsWith("#") && !arg.contains(String.valueOf(ChatColor.COLOR_CHAR)) && !arg.contains("/");
	}
}
