package com.benzrf.sblock.sburbchat.channel.channels;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.benzrf.sblock.sburbchat.User;
import com.benzrf.sblock.sburbchat.channel.AccessLevel;
import com.benzrf.sblock.sburbchat.channel.ChannelType;

public class RPChannel extends NickChannel
{
	public RPChannel(){}
	public RPChannel(String name, AccessLevel listeningAccess, AccessLevel sendingAccess, String creator)
	{
		super(name, listeningAccess, sendingAccess, creator);
	}
	
	@Override
	public void setChat(AsyncPlayerChatEvent event, User sender)
	{
		if (this.muteList.contains(sender.getName()))
		{
			sender.sendMessage(ChatColor.RED + "You are muted in channel " + ChatColor.GOLD + this.name + ChatColor.RED + "!");
			return;
		}
		if (!this.nickMap.containsKey(sender.getName()))
		{
			sender.sendMessage(ChatColor.RED + "You must have a canon nick to chat in channel " + ChatColor.GOLD + this.name + ChatColor.RED + "!");
			return;
		}
		switch (this.sendingAccess)
		{
		case PUBLIC:
			break;
		case PRIVATE:
			if (this.modList.contains(sender.getName()))
			{
				break;
			}
			else
			{
				return;
			}
		case REQUEST:
			if (approvedList.contains(sender.getName()))
			{
				break;
			}
			else
			{
				return;
			}
		}
		String msg = event.getMessage();
		msg = sender.hasPermission("sburbchat.chatcolor") ? msg.replaceAll("&([0-9a-fk-op])", ChatColor.COLOR_CHAR + "$1") : msg;
		this.sendToAll(this.getChatPrefix(sender, msg) + ((msg.startsWith("\\#") || msg.startsWith("#")) ? canonNicks.get(this.nickMap.get(sender.getName())).applyColor(msg.substring(1)) : canonNicks.get(this.nickMap.get(sender.getName())).apply(msg)));
	}
	
	@Override
	public void setNick(String nick, User sender)
	{
		for (String s : canonNicks.keySet())
		{
			if (nick.equalsIgnoreCase(ChatColor.stripColor(s.toLowerCase())))
			{
				nick = s;
				if (!this.nickMap.containsValue(nick))
				{
					this.nickMap.put(sender.getName(), nick);
					this.sendToAll(ChatColor.YELLOW + sender.getName() + " has set their nick to " + ChatColor.DARK_BLUE + nick + ChatColor.YELLOW + "!");
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "The nick " + ChatColor.DARK_BLUE + nick + ChatColor.RED + " is already taken in channel " + ChatColor.GOLD + this.name + "!");
				}
				return;
			}
		}
		sender.sendMessage(ChatColor.RED + "The nick " + ChatColor.DARK_BLUE + nick + ChatColor.RED + " is not canon!");
	}
	
	@Override
	public ChannelType getType()
	{
		return ChannelType.RP;
	}
	
	static Map<String, Quirker> canonNicks = new HashMap<String, Quirker>();
	static
	{
		canonNicks.put(ChatColor.BLUE + "John", new Quirker(ChatColor.BLUE.toString(), "", null));
		canonNicks.put(ChatColor.LIGHT_PURPLE + "Rose", new Quirker(ChatColor.LIGHT_PURPLE.toString(), "", null));
		canonNicks.put(ChatColor.DARK_RED + "Dave", new Quirker(ChatColor.DARK_RED.toString(), "", null));
		canonNicks.put(ChatColor.GREEN + "Jade", new Quirker(ChatColor.GREEN.toString(), "", null));
		canonNicks.put(ChatColor.DARK_GRAY + "Karkat", new Quirker(ChatColor.DARK_GRAY.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.karkat(msg);}}));//"a", "A", "b", "B", "c", "C", "d", "D", "e", "E", "f", "F", "g", "G", "h", "H", "i", "I", "j", "J", "k", "K", "l", "L", "m", "M", "n", "N", "o", "O", "p", "P", "q", "Q", "r", "R", "s", "S", "t", "T", "u", "U", "v", "V", "w", "W", "x", "X", "y", "Y", "z", "Z"));
		canonNicks.put(ChatColor.DARK_PURPLE + "Gamzee", new Quirker(ChatColor.DARK_PURPLE.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.gamzee(msg);}}));
		canonNicks.put(ChatColor.DARK_AQUA + "Terezi", new Quirker(ChatColor.DARK_AQUA.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.terezi(msg);}}));
		canonNicks.put(ChatColor.YELLOW + "Sollux", new Quirker(ChatColor.YELLOW.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.sollux(msg);}}));
		canonNicks.put(ChatColor.GOLD + "Tavros", new Quirker(ChatColor.GOLD.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.tavros(msg);}}));
		canonNicks.put(ChatColor.RED + "Aradia", new Quirker(ChatColor.RED.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.aradia(msg);}}));
		canonNicks.put(ChatColor.DARK_GREEN + "Nepeta", new Quirker(ChatColor.DARK_GREEN.toString() + ":33 < ", "", new Quirkf(){String apply(String msg){return Quirkf.nepeta(msg);}}));
		canonNicks.put(ChatColor.BLUE + "Vriska", new Quirker(ChatColor.BLUE.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.vriska(msg);}}));
		canonNicks.put(ChatColor.DARK_BLUE + "Equius", new Quirker(ChatColor.DARK_BLUE.toString() + "D --> ", "", new Quirkf(){String apply(String msg){return Quirkf.equius(msg);}}));
		canonNicks.put(ChatColor.DARK_GREEN + "Kanaya", new Quirker(ChatColor.DARK_GREEN.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.kanaya(msg);}}));
		canonNicks.put(ChatColor.DARK_PURPLE + "Eridan", new Quirker(ChatColor.DARK_PURPLE.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.eridan(msg);}}));
		canonNicks.put(ChatColor.DARK_PURPLE + "Feferi", new Quirker(ChatColor.DARK_PURPLE.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.feferi(msg);}}));
		canonNicks.put(ChatColor.BLUE + "Jane", new Quirker(ChatColor.BLUE.toString(), "", null));
		canonNicks.put(ChatColor.LIGHT_PURPLE + "Roxy", new Quirker(ChatColor.LIGHT_PURPLE.toString(), "", null));
		canonNicks.put(ChatColor.GOLD + "Dirk", new Quirker(ChatColor.GOLD.toString(), "", null));
		canonNicks.put(ChatColor.DARK_GREEN + "Jake", new Quirker(ChatColor.DARK_GREEN.toString(), "", null));
	}
	
	private static final long serialVersionUID = -4463076541248313240L;
}
class Quirker
{
	public Quirker(String prefix, String suffix, Quirkf quirk)
	{
		this.prefix = prefix;
		this.color = prefix.substring(0, 2);
		this.suffix = suffix;
		this.quirk = quirk;
	}
	
	public String apply(String msg)
	{
		return prefix + (quirk == null ? msg : quirk.apply(msg)) + suffix;
	}
	
	public String applyColor(String msg)
	{
		return color + msg;
	}
	
	private Quirkf quirk;
	private String prefix;
	private String suffix;
	private String color;
}
class Quirkf
{
	String apply(String msg)
	{
		return null;
	}
	
	static String karkat(String msg)
	{
		return msg.toUpperCase();
	}
	
	static String gamzee(String msg)
	{
		boolean odd = true;
		char[] c = msg.toCharArray();
		for (int i = 0; i < c.length; i++)
		{
			if (odd)
			{
				if (c[i] == 'o')
				{
					try
					{
						if (!(c[i - 1] == ':' && (c[i + 1] == ')' || c[i + 1] == '(')))
						{
							c[i] = Character.toUpperCase(c[i]);
						}
					}
					catch (ArrayIndexOutOfBoundsException e)
					{
					}
				}
				else
				{
					c[i] = Character.toUpperCase(c[i]);
				}
			}
			else
			{
				c[i] = Character.toLowerCase(c[i]);
			}
			odd = !odd;
		}
		return new String(c);
	}
	
	static String terezi(String msg)
	{
		return msg.toUpperCase().replace('A', '4').replace('I', '1').replace('E', '3').replace("'", "");
	}
	
	static String sollux(String msg)
	{
		return msg.replace("i", "ii").replaceAll("(?<=(^| ))(too|to)(?=($| ))", "two").replace('s', '2');
	}
	
	static String tavros(String msg)
	{
		char[] c = msg.toCharArray();
		for (int i = 0; i < c.length; i++)
		{
			if (c[i] == '.')
			{
				c[i] = ',';
			}
			else
			{
				c[i] = (Character.isUpperCase(c[i]) ? Character.toLowerCase(c[i]) : Character.toUpperCase(c[i]));
			}
		}
		c[0] = Character.toLowerCase(c[0]);
		return new String(c);
	}
	
	static String aradia(String msg)
	{
		return msg.toLowerCase().replace('o', '0').replace("'", "");
	}
	
	static String nepeta(String msg)
	{
		return msg.replaceAll("(e|E)(e|E)", "33");
	}
	
	static String vriska(String msg)
	{
		return msg.replace('b', '8').replace('B', '8').replace("ate", "8").replaceAll("(.)\\\\8", "$1$1$1$1$1$1$1$1");
	}
	
	static String equius(String msg)
	{
		return msg.replace('x', '%').replace("loo", "100").replace("lue", "100").replaceAll("(s|S)(t|T)(r|R)(o|O)(n|N)(g|G)", "STRONG").replace("nay", "neigh");
	}
	
	static String kanaya(String msg)
	{
		String[] caps = msg.replace("'", "").split(" ");
		StringBuilder sb = new StringBuilder();
		for (String s : caps)
		{
			sb.append(Character.toUpperCase(s.charAt(0))).append(s.toLowerCase().substring(1)).append(" ");
		}
		return sb.toString();
	}
	
	static String eridan(String msg)
	{
		 return msg.toLowerCase().replaceAll("(?<!w)w(?!w)", "ww").replaceAll("(?<!v)v(?!v)", "vv").replaceAll("(?<=[^ ])ing(?=( |$))", "in");
	}
	
	static String feferi(String msg)
	{
		return msg.replace("h", ")(").replace("H", ")(").replace("E", "-E");
	}
}
