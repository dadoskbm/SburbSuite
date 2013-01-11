package com.benzrf.sblock.sburbchat.commandparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;

import com.benzrf.sblock.sburbchat.User;

public class CommandParser
{
	public static void runCommand(String[] command, CommandNode root, User sender)
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
					ExecutableCommandNode node = (ExecutableCommandNode) c;
					int remaining = lCommand.size() - lCommand.indexOf(next) - 1; 
					if ((!node.getNeededArguments().contains(ArgumentType.MESSAGE) && node.getNeededArguments().size() != remaining )
							|| node.getNeededArguments().size() > remaining)
					{
						sender.sendMessage(ChatColor.RED + node.generateArgumentListErrorMessage());
						return;
					}
					else
					{
						Iterator<ArgumentType> iAt = node.getNeededArguments().iterator();
						List<String> args = new ArrayList<String>();
						ArgumentType nextAt;
						while (iCommand.hasNext())
						{
							next = iCommand.next();
							nextAt = iAt.next();
							if (!nextAt.isArgumentValid(next))
							{
								sender.sendMessage(ChatColor.RED + next + " is not a valid " + nextAt.getHumanName() + ChatColor.RED + "!");
								return;
							}
							if(nextAt.mergeArguments())
							{
								StringBuilder sb = new StringBuilder();
								for(String word : lCommand.subList(lCommand.indexOf(next), lCommand.size()))
								{
									sb.append(word + " ");
								}
								args.add(sb.toString().trim());
								break;
							}
							args.add(next);
							
						}
						node.invoke(args, sender);
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
