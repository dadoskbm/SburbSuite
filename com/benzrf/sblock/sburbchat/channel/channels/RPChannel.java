<<<<<<< HEAD
package com.benzrf.sblock.sburbchat.channel.channels;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
		msg = sender.hasPermission("sburbchat.chatcolor") ? msg.replaceAll("&([0-9a-fk-or])", ChatColor.COLOR_CHAR + "$1") : msg;
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
		
		canonNicks.put(ChatColor.RED + "Kankri", new Quirker(ChatColor.DARK_GRAY.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.kankri(msg);}}));//"a", "A", "b", "B", "c", "C", "d", "D", "e", "E", "f", "F", "g", "G", "h", "H", "i", "I", "j", "J", "k", "K", "l", "L", "m", "M", "n", "N", "o", "O", "p", "P", "q", "Q", "r", "R", "s", "S", "t", "T", "u", "U", "v", "V", "w", "W", "x", "X", "y", "Y", "z", "Z"));
		canonNicks.put(ChatColor.DARK_PURPLE + "Kurloz", new Quirker(ChatColor.DARK_PURPLE.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.kurloz(msg);}}));
		canonNicks.put(ChatColor.DARK_AQUA + "Latula", new Quirker(ChatColor.DARK_AQUA.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.latula(msg);}}));
		canonNicks.put(ChatColor.YELLOW + "Mituna", new Quirker(ChatColor.YELLOW.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.mituna(msg);}}));
		canonNicks.put(ChatColor.GOLD + "Rufioh", new Quirker(ChatColor.GOLD.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.rufioh(msg);}}));
		canonNicks.put(ChatColor.RED + "Damara", new Quirker(ChatColor.RED.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.damara(msg);}}));
		canonNicks.put(ChatColor.DARK_GREEN + "Meulin", new Quirker(ChatColor.DARK_GREEN.toString() + "(=TωT=) ", "", new Quirkf(){String apply(String msg){return Quirkf.meulin(msg);}}));
		canonNicks.put(ChatColor.BLUE + "Arenea", new Quirker(ChatColor.BLUE.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.vriska(msg);}})); //Same as Vriska?
		canonNicks.put(ChatColor.DARK_BLUE + "Horuss", new Quirker(ChatColor.DARK_BLUE.toString() + "8=D <", "", new Quirkf(){String apply(String msg){return Quirkf.horuss(msg);}}));
		canonNicks.put(ChatColor.DARK_GREEN + "Porrim", new Quirker(ChatColor.DARK_GREEN.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.porrim(msg);}}));
		canonNicks.put(ChatColor.DARK_PURPLE + "Cronus", new Quirker(ChatColor.DARK_PURPLE.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.cronus(msg);}}));
		canonNicks.put(ChatColor.DARK_PURPLE + "Meenah", new Quirker(ChatColor.DARK_PURPLE.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.meenah(msg);}}));
		
		canonNicks.put(ChatColor.GRAY + "Calliope", new Quirker(ChatColor.GOLD.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.calliope(msg);}}));
		canonNicks.put(ChatColor.DARK_GRAY + "Caliborn", new Quirker(ChatColor.DARK_GREEN.toString(), "", new Quirkf(){String apply(String msg){return Quirkf.caliborn(msg);}}));
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
	private static String[] fakeJapanese = {"あなたのデュアルフォークを取る。二回自分自身をファック。", "あなたに性的快感を与えるために十分な厚さではない。",
		"あなたはあなたの言葉で私を退屈続けるのだろうか？または。あなたは私の服を脱ぐのだろうか？", "私はあなたの歯の間に私の乳首を感じるようにしたい。", "時々私は、そのメモリに自慰行為。",
		"性交あなたは何を話している？白痴雌犬。", "正確にどのくらいの。あなたは知りたいですか？", "あなたは私にはできません。あなたは私を性交することができない場合。",
		"いいえクソ方法。", "罰金。しかし、あなたは私に借りがある。性的な接待。", "あなたは私の胸をいつでも表示することができます。", "私のおっぱいの上にミルクを注いでください。",
		"私はそれがラフ好きです。私の体を押しつぶす。馬男。", "あなたは、幽霊のような私の性器が性的クライマックスを持っていることを確認してください。",
		"私の裸の底にあなたの汗まみれの顔をこする。", "私はあなたのアジアの女子高生です。", "あなたは私を喜ばせる必要があります。あなたのホーンを使用してください。",
		"静かにしています。黙って私をファック。", "私の体内に入る。", "あなたのセックスライフはどうですか？", "今私をファック。", "私にリンゴを養う。その後、私の髪をつかむ。",
		"私はあなたのホーンを吸うことができます。同時に、あなたは私のお尻を平手打ち。リズミカルにそれを行う。", "性的エクスタシーを体験する私たちのすべてを引き起こす。",
		"もっと私を低下させる。私はほとんどそこにいる。"};
	String apply(String msg)
	{
		return null;
	}
	
	/**
	 * Applies Caliborn's quirk to a string.
	 * @param msg
	 * @return
	 */
	static String caliborn(String msg) 
	{
		return msg.toUpperCase().replace('U', 'u').replaceAll("(\\:|\\=)(\\)|\\(|D|P|S|\\/)", "tumut");
	}

	/**
	 * Applies Calliope's quirk to a string.
	 * @param msg
	 * @return
	 */
	static String calliope(String msg)
	{
		return msg.toLowerCase().replace('u', 'U'); //TODO Force British spellings
	}

	/**
	 * Applies Meenah's quirk to a string.
	 * @param msg
	 * @return
	 */
	static String meenah(String msg) 
	{
		return msg.replace("H", ")(").replace("E","-E").replace("fucking","glubbing").replace("fuck", "glub"); //TODO More fish puns!
	}

	/**
	 * Applies Cronus's quirk to a string.
	 * @param msg
	 * @return
	 */
	static String cronus(String msg)
	{
		return msg.replace("v", "vw").replace("V","VW").replace("B", "8");
	}

	/**
	 * Applies Porrim's quirk to a string.
	 * @param msg
	 * @return
	 */
	static String porrim(String msg)
	{
		return msg.replace("o", "o+").replace("O","O+").replaceAll("(plus|PLUS)", "+");
	}

	/**
	 * Applies Horuss' quirk to a string.
	 * @param msg
	 * @return
	 */
	static String horuss(String msg) 
	{
		return msg.replace('x', '%').replace("loo", "100").replace("lue", "100").replaceAll("(s|S)(t|T)(r|R)(o|O)(n|N)(g|G)", "STRONG")
				.replace("nay", "neigh").replace("fuck", "f*ck").replace("shit", "sh*t").replace("cock", "c*ck").replace("bitch", "b*tch")
				.replace("cunt", "c*nt").replace("tits", "t*ts");
	}

	/**
	 * Applies Meulin's quirk to a string.
	 * @param msg
	 * @return
	 */
	static String meulin(String msg)
	{
		
		return msg.toUpperCase().replace("EE", "33").replace("FOR", "FUR").replace("PER", "PURR");
	}

	/**
	 * Applies Damara's quirk to a string.
	 * @param msg
	 * @return
	 */
	static String damara(String msg)
	{
		return fakeJapanese[new Random().nextInt(fakeJapanese.length)];
	}

	/**
	 * Applies Rufioh's quirk to a string.
	 * @param msg
	 * @return
	 */
	static String rufioh(String msg)
	{
		return msg.replace("i", "1").replace(",", "... ");
	}

	/**
	 * Applies Mituna's quirk to a string.
	 * @param msg
	 * @return
	 */
	static String mituna(String msg)
	{
		return msg.toUpperCase().replace("E", "3").replace("A", "4").replace("S", "5")
				.replace("O", "0").replace("T", "7").replace("I","1").replace("B", "8");
	}

	/**
	 * Applies Latula's quirk to a string.
	 * @param msg
	 * @return
	 */
	static String latula(String msg) 
	{
		return msg.toLowerCase().replace("a", "4").replace("i", "1").replace("e", "3");
	}

	/**
	 * Applies Kurloz's quirk to a string.
	 * NOTE: Kurloz speaks in mimes/images, which isn't really possible in MC, so I based his quirk
	 * on his purple text when using his telepathic ability.
	 * @param msg
	 * @return
	 */
	static String kurloz(String msg) 
	{
		return msg.toUpperCase().replaceAll("\\p{Punct}", "");
	}

	/**
	 * Applies Kankri's quirk to a string.
	 * @param msg
	 * @return
	 */
	static String kankri(String msg) 
	{
		return msg.replaceAll("(B|b)", "6").replaceAll("(O|o)", "9");
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
=======
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
		msg = sender.hasPermission("sburbchat.chatcolor") ? msg.replaceAll("&([0-9a-fk-or])", ChatColor.COLOR_CHAR + "$1") : msg;
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
>>>>>>> upstream/master
