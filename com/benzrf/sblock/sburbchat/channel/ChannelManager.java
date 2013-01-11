package com.benzrf.sblock.sburbchat.channel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;

import com.benzrf.sblock.sburbchat.User;
import com.benzrf.sblock.sburbchat.channel.channels.Channel;
import com.google.gson.Gson;

public class ChannelManager
{
	public Channel getChannel(String name)
	{
		if (name.startsWith("#"))
		{
			return this.cMap.get(name.toLowerCase());
		}
		else
		{
			return this.cMap.get(this.aMap.get(name.toLowerCase()));
		}
	}
	
	public void disbandChannel(String name)
	{
		this.cMap.remove(name);
		for (String a : this.aMap.keySet())
		{
			if (this.aMap.get(a).equals(name)) this.aMap.remove(a);
		}
	}
	
	public Channel registerChannel(Channel c)
	{
		this.cMap.put(c.getName(), c);
		return c;
	}
	
	public Channel newChannel(String name, ChannelType type, AccessLevel listeningAccess, AccessLevel sendingAccess, User creator)
	{
		if (this.cMap.containsKey(name))
		{
			creator.sendMessage(ChatColor.RED + "Channel " + ChatColor.GOLD + name + ChatColor.RED + " already exists!");
			return null;
		}
		else
		{
			Channel c = type.newChannel(name, listeningAccess, sendingAccess, creator.getName());
			this.cMap.put(name.toLowerCase(), c);
			creator.sendMessage(ChatColor.GREEN + "Created channel " + ChatColor.GOLD + name + ChatColor.GREEN + "!");
			return c;
		}
	}
	
	public Channel newChannel(String name, ChannelType type, AccessLevel listeningAccess, AccessLevel sendingAccess, String creator)
	{
		if (this.cMap.containsKey(name))
		{
			return null;
		}
		else
		{
			Channel c = type.newChannel(name, listeningAccess, sendingAccess, creator);
			this.cMap.put(name.toLowerCase(), c);
			return c;
		}
	}
	
	public Collection<Channel> getChannels()
	{
		return this.cMap.values();
	}
	
	public Channel newAlias(String name, Channel c, User sender)
	{
		if (!this.aMap.containsKey(name.toLowerCase()))
		{
			this.aMap.put(name.toLowerCase(), c.getName());
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
		if (this.aMap.containsKey(name.toLowerCase()) && c.equals(this.cMap.get(this.aMap.get(name.toLowerCase()))))
		{
			this.aMap.remove(name.toLowerCase());
			sender.sendMessage(ChatColor.GREEN + "Alias " + ChatColor.BLUE + name + ChatColor.GREEN + " has been removed from channel " + ChatColor.GOLD + c.getName() + ChatColor.GREEN + ".");
		}
		else if (!c.equals(this.aMap.get(name.toLowerCase())))
		{
			sender.sendMessage(ChatColor.RED + "Alias " + ChatColor.BLUE + name + ChatColor.RED + " is not an alias for channel " + ChatColor.GOLD + c.getName() + ChatColor.RED + "!");
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "Alias " + ChatColor.BLUE + name + ChatColor.RED + " does not exist!");
		}
		return c;
	}
	
	public void writeAliases(String path) throws IOException
	{
		BufferedWriter w = new BufferedWriter(new FileWriter(path));
		w.write(this.gson.toJson(this.aMap));
		w.flush();
		w.close();
	}
	
	@SuppressWarnings("unchecked")
	public void readAliases(String path) throws IOException, ClassNotFoundException
	{
		this.aMap = this.gson.fromJson(this.readFile(path), HashMap.class);
	}
	
	public void writeChannels(String path, String list) throws IOException
	{
		BufferedWriter w;
		HashMap<String, String> cm = new HashMap<String, String>();
		this.cMap.remove("#");
		for (Channel c : this.cMap.values())
		{
			c.makeSerializable();
			w = new BufferedWriter(new FileWriter(path + "c_" + c.getName() + ".scd"));
			w.write(this.gson.toJson(c));
			w.flush();
			w.close();
			cm.put(c.getName(), c.getType().toString());
		}
		w = new BufferedWriter(new FileWriter(path + list));
		w.write(this.gson.toJson(cm));
		w.flush();
		w.close();
	}
	
	@SuppressWarnings("unchecked")
	public void readChannels(String path, String list) throws IOException, ClassNotFoundException
	{
		HashMap<String, String> cm = this.gson.fromJson(this.readFile(path + list), HashMap.class);
		for (String c : cm.keySet())
		{
			Channel ch = (Channel) this.gson.fromJson(this.readFile(path + "c_" + c + ".scd"), ChannelType.valueOf(cm.get(c)).getType());
			ch.makeUsable();
			this.registerChannel(ch);
		}
	}
	
	private String readFile(String path) throws IOException
	{
		String file;
		FileInputStream stream = new FileInputStream(new File(path));
		try
		{
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			file = Charset.defaultCharset().decode(bb).toString();
		}
		finally
		{
			stream.close();
		}
		return file;
	}
	
	private Map<String, Channel> cMap = new HashMap<String, Channel>();
	private Map<String, String> aMap = new HashMap<String, String>();
	private Gson gson = new Gson();
}
