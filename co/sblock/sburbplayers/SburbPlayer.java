package co.sblock.sburbplayers;

import java.io.Serializable;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import co.sblock.common.commandparser.ExecutorClass;
import co.sblock.sburbplayers.session.SburbSessionManager;


public class SburbPlayer implements Serializable, ExecutorClass
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
		this.player.sendMessage(SburbPlayers.getInstance().prefix() + s);
	}
	
	public void getInfo(Player p)
	{
		SburbPlayer sp = SburbPlayers.getInstance().getPlayer(p.getName());
		if (sp != null)
		{
			this.sendMessage(ChatColor.YELLOW + p.getName() + ChatColor.GREEN + " is a " + sp.sclass + " of " + sp.aspect + " who dreams on " + sp.cplanet + " and resides on " + sp.mplanet + " while in the Medium.");
		}
	}
	
	/**
	 * Called when the player requests a new Sburb session
	 * @param other Other player
	 * @param playerType Player type of the player executing the command, either "client" or "server"
	 */
	public void startSession(Player client, Player server)
	{
		if(player.hasPermission("sburbplayers.manageSessions"))
		{
    		SburbPlayers.getInstance().getSessionManager().startSession(client.getName(), server.getName(), this);
		}
		else
			this.sendMessage(ChatColor.RED + "You do not have permission to do that!");
	}
	
	public void teleport()
	{
		SburbPlayers.getInstance().getSessionManager().teleport(this);
	}
	
	
	/**
	 * Kills the session where the given player is the client
	 * @param clientPlayerToKill Client player of the session to kill
	 */
	public void killSession(Player clientPlayerToKill)
	{
		if(this.player.hasPermission("sburbplayers.manageSessions"))
		{
			SburbPlayers.getInstance().getSessionManager().killSession(clientPlayerToKill, this);
		}
		else
			this.sendMessage(ChatColor.RED + "You do not have permission to do that!");
	}
	
	public void setSpecibus(String s)
	{
		if (this.abstratus == null || this.abstratus.isEmpty())
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
		if (this.abstratus == null || this.abstratus.isEmpty())
		{
			this.sendMessage(ChatColor.RED + "You don't have a " + ChatColor.BLUE + "KIND ABSTRATUS" + ChatColor.RED + " allocated to your " + ChatColor.GRAY + "STRIFE SPECIBUS" + ChatColor.RED + "!");
			return;
		}
		Inventory i = Bukkit.createInventory(this.player, 9, "Strife Deck");
		if (this.weapons != null) i.setContents(this.weapons);
		this.player.openInventory(i);
	}
	
	/**
	 * @return The player's name.
	 */
	public String getName()
	{
		return player.getName();
	}
	
	/**
	 * @return The Bukkit Player object associated with this SburbPlayer
	 */
    public Player asBukkitPlayer()
    {
	    return player;
    }
	
	/**
	 * Sets the Bukkit player object associated with this SburbPlayer. This should be called right after
	 * deserialization, but never be called in any other situation
	 * @param p Player to set
	 */
	void setBukkitPlayer(Player p)
	{
	    this.player = p;
	    
	}
	
	public void retrieveItem()
	{
	}
	
	/**
	 * @return true if the player is currently in bed.
	 */
	public boolean isInBed()
	{
		return inBed;
	}
	/**
	 * @param If the player is currently in bed
	 */
	public void setInBed(boolean inBed)
	{
		this.inBed = inBed;
	}
	/**
	 * @return the player's sleeping location
	 */
	public String getSleepingLocation()
	{
		return sleepingloc;
	}
	/**
	 * @param The player's sleeping location
	 */
	public void setSleepingLocation(String sleepingloc)
	{
		this.sleepingloc = sleepingloc;
	}
	/**
	 * @return the player's dreaming location
	 */
	public String getDreamingLocation()
	{
		return dreamingloc;
	}
	/**
	 * @param The player's dreaming location
	 */
	public void setDreamingLocation(String dreamingloc)
	{
		this.dreamingloc = dreamingloc;
	}

	private transient Player player;
	SClass sclass;
	Aspect aspect;
	MPlanet mplanet;
	CPlanet cplanet;
	String bed;
	private transient boolean inBed = false;
	private String sleepingloc = "";
	private String dreamingloc = "";
	ItemStack[] weapons = new ItemStack[9];
	private ItemStack[] knock = new ItemStack[9];
	String abstratus;
	private static final long serialVersionUID = -6947763764629711601L;
	
}
