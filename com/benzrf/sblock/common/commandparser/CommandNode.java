package com.benzrf.sblock.common.commandparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandNode
{
	public CommandNode(String name)
	{
		this.name = name;
		this.parent = null;
	}
	
	public CommandNode(String name, CommandNode parent)
	{
		this.name = name;
		this.parent = parent;
		this.parent.addChild(this);
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean hasChild(String name)
	{
		return children.containsKey(name);
	}
	
	public CommandNode getChild(String name)
	{
		return children.get(name);
	}
	
	public String getFullName()
	{
		if (this.parent == null)
		{
			return this.name;
		}
		else
		{
			return this.parent.getFullName() + " " + this.name;
		}
	}
	
	public void runCommand(String[] command, ExecutorClass toExecute, CommandSender sender)
	{
		if(command.length == 0 || !children.containsKey(command[0]))
			sender.sendMessage(ChatColor.RED + generateChildListErrorMessage());
		else
			children.get(command[0]).runCommand(Arrays.copyOfRange(command, 1, command.length), toExecute, sender);
	}
	
	public String generateChildListErrorMessage()
	{
		String msg = "/" + getFullName() + " {";
		List<String> sorted = new ArrayList<String>();
		sorted.addAll(children.keySet());
		Collections.sort(sorted);
		for (String childname : sorted)
		{
			msg += childname + ChatColor.RED + "|";
		}
		msg = msg.substring(0, msg.length() - 1);
		msg += "}";
		return msg;
	}
	
	private void addChild(CommandNode child)
	{
		children.put(child.getName(), child);
	}
	
	private String name;
	private Map<String, CommandNode> children = new HashMap<String, CommandNode>();
	private CommandNode parent;
}
