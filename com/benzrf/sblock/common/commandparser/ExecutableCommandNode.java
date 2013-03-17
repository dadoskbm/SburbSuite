package com.benzrf.sblock.common.commandparser;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


public class ExecutableCommandNode extends CommandNode
{
	private List<ArgumentType> argTypes;
	private Class<?>[] cArgTypes;
	private String commandName;
	private boolean mergeRequired = false;
	
	public ExecutableCommandNode(String name, CommandNode parent, String commandName, ArgumentType... args)
	{
		super(name, parent);
		this.commandName = commandName;
		this.argTypes = Arrays.asList(args);
		for(ArgumentType argtype : argTypes)
		{
			if(argtype.mergeArguments())
			{
				mergeRequired = true;
				if(argTypes.indexOf(argtype) != argTypes.size() - 1)
					throw new IllegalArgumentException("Argument type " + argtype + " must be the last argument in the command.");
			}
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
	
	private void mergeThenRun(String[] args, ExecutorClass toExecute, CommandSender sender)
	{
		if(args.length < argTypes.size())
		{
			sender.sendMessage(ChatColor.RED + generateArgumentListErrorMessage());
			return;
		}
		
		for(int i = 0; i < argTypes.size(); i++)
		{
			if(!argTypes.get(i).isArgumentValid(args[i]))
			{
				sender.sendMessage(ChatColor.RED + args[i] + " is not a valid " + argTypes.get(i).getHumanName() + ChatColor.RED + "!");
				return;
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for(int i = argTypes.size() - 1; i < args.length; i++)
		{
			sb.append(args[i] + " ");
		}
		args[argTypes.size() - 1] = sb.toString();
		
		
		invoke(Arrays.copyOfRange(args, 0, argTypes.size()), toExecute);
	}
	
	public void runCommand(String[] args, ExecutorClass toExecute, CommandSender sender)
	{
		if(mergeRequired)
			mergeThenRun(args,toExecute,sender);
		else
		{
			if(args.length != argTypes.size())
			{
				sender.sendMessage(ChatColor.RED + generateArgumentListErrorMessage());
				return;
			}
			
			for(int i = 0; i < argTypes.size(); i++)
			{
				if(!argTypes.get(i).isArgumentValid(args[i]))
				{
					sender.sendMessage(ChatColor.RED + args[i] + " is not a valid " + argTypes.get(i).getHumanName() + ChatColor.RED + "!");
					return;
				}
			}
			
			invoke(args, toExecute);
		}
	}
	
	public void invoke(String[] args, ExecutorClass toExecute)
	{
		Object[] oArgs = new Object[args.length];
		for (int i = 0; i < args.length; i++)
		{
			oArgs[i] = this.argTypes.get(i).convertArgument(args[i]);
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
}
