package com.benzrf.sblock.common.commandparser;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;


public class ExecutableCommandNode extends CommandNode
{
	private Class<?>[] cArgTypes;
	private String commandName;
	
	public ExecutableCommandNode(String name, CommandNode parent, String commandName, ArgumentType... args)
	{
		super(name, parent);
		this.commandName = commandName;
		this.argTypes = Arrays.asList(args);
		for(ArgumentType argtype : argTypes)
		{
			if(argtype.mergeArguments() && argTypes.indexOf(argtype) != argTypes.size() - 1)
				throw new IllegalArgumentException("Argument type " + argtype + " must be the last argument in the command.");
		}
		this.cArgTypes = new Class<?>[argTypes.size()];
		for (int i = 0; i < argTypes.size(); i++)
		{
			this.cArgTypes[i] = this.argTypes.get(i).getReturnType();
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
	
	public void invoke(List<String> args, ExecutorClass toExecute)
	{
		Object[] oArgs = new Object[args.size()];
		for (int i = 0; i < args.size(); i++)
		{
			oArgs[i] = this.argTypes.get(i).convertArgument(args.get(i));
		}
		try
		{
			toExecute.getClass().getMethod(commandName, cArgTypes).invoke(toExecute, oArgs);
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
        catch (SecurityException e)
        {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
	}
	
	private List<ArgumentType> argTypes;
}
