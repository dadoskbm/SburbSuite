package com.benzrf.sblock.sburbchat.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

import com.benzrf.sblock.sburbchat.User;
import com.benzrf.sblock.sburbchat.channel.channels.Channel;

public class ChannelManager
{
	public Channel getChannel(String name)// throws NoSuchChannelException
	{
		if (name.startsWith("#"))
		{
			return cMap.get(name.toLowerCase());
		}
		else
		{
			return cMap.get(aMap.get(name.toLowerCase()));
		}
	}
	
	public void disbandChannel(String name)
	{
		cMap.remove(name);
		for (String a : aMap.keySet())
		{
			if (aMap.get(a).equals(name)) aMap.remove(a);
		}
	}
	
	public Channel registerChannel(Channel c)
	{
		cMap.put(c.getName(), c);
		return c;
	}
	
	public Channel newChannel(String name, ChannelType type, AccessLevel listeningAccess, AccessLevel sendingAccess, User creator)
	{
		if (cMap.containsKey(name))
		{
			creator.sendMessage(ChatColor.RED + "Channel " + ChatColor.GOLD + name + ChatColor.RED + " already exists!");
			return null;
		}
		else
		{
			Channel c = type.newChannel(name, listeningAccess, sendingAccess, creator.getName());
			cMap.put(name.toLowerCase(), c);
			creator.sendMessage(ChatColor.GREEN + "Created channel " + ChatColor.GOLD + name + ChatColor.GREEN + "!");
			return c;
		}
	}
	
	public Channel newChannel(String name, ChannelType type, AccessLevel listeningAccess, AccessLevel sendingAccess, String creator)
	{
		if (cMap.containsKey(name))
		{
			return null;
		}
		else
		{
			Channel c = type.newChannel(name, listeningAccess, sendingAccess, creator);
			cMap.put(name.toLowerCase(), c);
			return c;
		}
	}
	
	public Collection<Channel> getChannels()
	{
		return cMap.values();
	}
	
	public Channel newAlias(String name, Channel c, User sender)
	{
		if (!aMap.containsKey(name.toLowerCase()))
		{
			aMap.put(name.toLowerCase(), c.getName());
			sender.sendMessage(ChatColor.GREEN + "Alias " + ChatColor.BLUE + name + ChatColor.GREEN + " has been added to channel " + ChatColor.GOLD + c.getName() + ChatColor.GREEN + ".");
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "Alias " + ChatColor.BLUE + name + ChatColor.RED + " already exists!");
		}
		return c;
	}
	
	public Channel deleteAlias(String name, Channel c, User sender)
	{
		if (aMap.containsKey(name.toLowerCase()) && c.equals(cMap.get(aMap.get(name.toLowerCase()))))
		{
			aMap.remove(name.toLowerCase());
			sender.sendMessage(ChatColor.GREEN + "Alias " + ChatColor.BLUE + name + ChatColor.GREEN + " has been removed from channel " + ChatColor.GOLD + c.getName() + ChatColor.GREEN + ".");
		}
		else if (!c.equals(aMap.get(name.toLowerCase())))
		{
			sender.sendMessage(ChatColor.RED + "Alias " + ChatColor.BLUE + name + ChatColor.RED + " is not an alias for channel " + ChatColor.GOLD + c.getName() + ChatColor.RED + "!");
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "Alias " + ChatColor.BLUE + name + ChatColor.RED + " does not exist!");
		}
		return c;
	}
	
	public void writeAliases(String path) throws FileNotFoundException, IOException
	{
		ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(new File(path)));
		o.writeObject(aMap);
		o.flush();
		o.close();
	}
	
	@SuppressWarnings("unchecked")
	public void readAliases(String path) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		ObjectInputStream i = new ObjectInputStream(new FileInputStream(new File(path)));
		aMap = (Map<String, String>) i.readObject();
		i.close();
	}
	
	public void writeChannels(String path, String list) throws FileNotFoundException, IOException
	{
		ObjectOutputStream o;
		List<String> cl = new ArrayList<String>();
		cMap.remove("#");
		for (Channel c : cMap.values())
		{
			o = new ObjectOutputStream(new FileOutputStream(new File(path + "c_" + c.getName() + ".scd")));
			o.writeObject(c);
			o.flush();
			o.close();
			cl.add(c.getName());
		}
		o = new ObjectOutputStream(new FileOutputStream(new File(path + list)));
		o.writeObject(cl);
		o.flush();
		o.close();
	}
	
	@SuppressWarnings("unchecked")
	public void readChannels(String path, String list) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		ObjectInputStream i;
		i = new ObjectInputStream(new FileInputStream(new File(path + list)));
		List<String> cl = (List<String>) i.readObject();
		i.close();
		for (String c : cl)
		{
			i = new ObjectInputStream(new FileInputStream(new File(path + "c_" + c + ".scd")));
			i.readObject();
			i.close();
		}
	}
	
	private Map<String, Channel> cMap = new HashMap<String, Channel>();
	private Map<String, String> aMap = new HashMap<String, String>();
}
