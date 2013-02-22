package com.benzrf.sblock.sburbplayers;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SburbPlayer implements Serializable
{
	public SburbPlayer()
	{
		this.weapons = new ItemStack[9];
		this.knock = new ItemStack[9];
	}
	public SburbPlayer(Player pthis, SClass sclass, Aspect aspect, MPlanet mplanet, CPlanet cplanet, String bed)
	{
		this.player = pthis;
		this.sclass = sclass;
		this.aspect = aspect;
		this.mplanet = mplanet;
		this.cplanet = cplanet;
		this.bed = bed;
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
	
	public void setSpecibus(String s)
	{
		if (this.abstratus == null || "".equals(this.abstratus))
		{
			this.abstratus = s;
			this.sendMessage(ChatColor.GREEN + "You have successfully allocated your " + ChatColor.GRAY + "STRIFE SPECIBUS" + ChatColor.GREEN + " with the " + ChatColor.BLUE + s.toUpperCase() + " ABSTRATUS" + ChatColor.GREEN + "!");
		}
		else
		{
			this.sendMessage(ChatColor.RED + "You cannot reallocate your " + ChatColor.GRAY + "STRIFE SPECIBUS" + ChatColor.RED + " with a new " + ChatColor.BLUE + "KIND ABSTRATUS" + ChatColor.RED + "!");
		}
	}
	
	public void setItem()
	{
		if (this.abstratus == null || "".equals(this.abstratus))
		{
			this.sendMessage(ChatColor.RED + "You don't have a " + ChatColor.BLUE + "KIND ABSTRATUS" + ChatColor.RED + " allocated to your " + ChatColor.GRAY + "STRIFE SPECIBUS" + ChatColor.RED + "!");
			return;
		}
		Inventory i = Bukkit.createInventory(this.player, 9, "Strife Deck");
		if (this.weapons != null) i.setContents(this.weapons);
		this.player.openInventory(i);
	}
	
	public void retrieveItem()
	{
	}
	
	public transient Player player;
	public SClass sclass;
	public Aspect aspect;
	public MPlanet mplanet;
	public CPlanet cplanet;
	public String bed;
	public transient boolean inBed = false;
	public String sleepingloc = "";
	public String dreamingloc = "";
	public ItemStack[] weapons = new ItemStack[9];
	public ItemStack[] knock = new ItemStack[9];
	public String abstratus;
	private static final long serialVersionUID = -6947763764629711601L;
}
