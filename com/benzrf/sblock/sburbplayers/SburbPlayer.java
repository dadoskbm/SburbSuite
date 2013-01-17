package com.benzrf.sblock.sburbplayers;

import java.io.Serializable;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SburbPlayer implements Serializable
{
	public SburbPlayer(Player pthis, SClass sclass, Aspect aspect, MPlanet mplanet, CPlanet cplanet, String bed)
	{
		this.player = pthis;
		this.sclass = sclass;
		this.aspect = aspect;
		this.mplanet = mplanet;
		this.cplanet = cplanet;
		this.bed = bed;
		this.dreaming = false;
	}
	
	public void sendMessage(String s)
	{
		this.player.sendMessage(SburbPlayers.instance.prefix() + s);
	}
	
	public void getInfo(Player p)
	{
		SburbPlayer sp = SburbPlayers.instance.getPlayer(p.getName());
		if (sp != null)
		{
			this.sendMessage(ChatColor.YELLOW + p.getName() + ChatColor.GREEN + " is a " + sp.sclass + " of " + sp.aspect + " who dreams on " + sp.cplanet + " and resides on " + sp.mplanet + " while in the Medium.");
		}
	}
	
	public transient Player player;
	public SClass sclass;
	public Aspect aspect;
	public MPlanet mplanet;
	public CPlanet cplanet;
	public String bed;
	public transient boolean inBed = false;
	public String sleepingloc = "";
	public boolean dreaming;
	private static final long serialVersionUID = -6947763764629711601L;
}

