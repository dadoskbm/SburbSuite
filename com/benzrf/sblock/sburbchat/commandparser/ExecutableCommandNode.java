package com.benzrf.sblock.sburbchat.commandparser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;

import com.benzrf.sblock.sburbchat.User;

public class ExecutableCommandNode extends CommandNode
{
	public ExecutableCommandNode(String name, CommandNode parent, String commandName, ArgumentType... args)
	{
		super(name, parent);
		this.argTypes = Arrays.asList(args);
		for(ArgumentType argtype : argTypes)
		{
			if(argtype.mergeArguments() && argTypes.indexOf(argtype) != argTypes.size() - 1)
				throw new IllegalArgumentException("Argument type " + argtype + " must be the last argument in the command.");
		}
		Class<?>[] cArgTypes = new Class<?>[argTypes.size()];
		for (int i = 0; i < argTypes.size(); i++)
		{
			cArgTypes[i] = this.argTypes.get(i).getReturnType();
		}
		try
		{
			this.command = User.class.getMethod(commandName, cArgTypes);
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
	}
	
	public List<ArgumentType> getNeededArguments()
	{
		return this.argTypes;
	}
	
	public String generateArgumentListErrorMessage()
	{
		String msg = "/" + getFullName() + " ";
		for (ArgumentType at : this.argTypes)
		{
			msg += "<" + at.getHumanName() + ChatColor.RED + ">" + " ";
		}
		msg = msg.substring(0, msg.length() - 1);
		return msg;
	}
	
	public void invoke(List<String> args, User u)
	{
		Object[] oArgs = new Object[args.size()];
		for (int i = 0; i < args.size(); i++)
		{
			oArgs[i] = this.argTypes.get(i).convertArgument(args.get(i));
		}
		try
		{
			command.invoke(u, oArgs);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}
	
	private List<ArgumentType> argTypes;
	private Method command;
}
