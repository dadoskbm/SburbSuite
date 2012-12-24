package com.benzrf.sblock.sburbplayers.commandparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;

import com.benzrf.sblock.sburbplayers.SburbPlayer;

public class CommandParser
{
	public static void runCommand(String[] command, CommandNode root, SburbPlayer sender)
	{
		List<String> lCommand = Arrays.asList(command);
		Iterator<String> iCommand = lCommand.iterator();
		CommandNode c = root;
		String next;
		while (iCommand.hasNext()) 
		{
			next = iCommand.next();
			if (c.hasChild(next))
			{
				c = c.getChild(next);
				if (c instanceof ExecutableCommandNode)
				{
					int remaining = lCommand.size() - lCommand.indexOf(next) - 1; 
					if (((ExecutableCommandNode) c).getNeededArguments().size() != remaining)
					{
						sender.sendMessage(ChatColor.RED + ((ExecutableCommandNode) c).generateArgumentListErrorMessage());
						return;
					}
					else
					{
						Iterator<ArgumentType> iAt = ((ExecutableCommandNode) c).getNeededArguments().iterator();
						List<String> args = new ArrayList<String>();
						ArgumentType nextAt;
						while (iCommand.hasNext())
						{
							next = iCommand.next();
							args.add(next);
							nextAt = iAt.next();
							if (!nextAt.isArgumentValid(next))
							{
								sender.sendMessage(ChatColor.RED + next + " is not a valid " + nextAt.getHumanName() + ChatColor.RED + "!");
								return;
							}
						}
						((ExecutableCommandNode) c).invoke(args, sender);
						return;
					}
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + c.generateChildListErrorMessage());
				return;
			}
		}
		sender.sendMessage(ChatColor.RED + c.generateChildListErrorMessage());
	}
}
