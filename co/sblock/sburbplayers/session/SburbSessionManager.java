/**
 * 
 */
package co.sblock.sburbplayers.session;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import co.sblock.sburbplayers.SburbPlayer;
import co.sblock.sburbplayers.SburbPlayers;

import com.google.gson.Gson;

/**
 * Master class that manages all sessions currently in progress.
 * @author FireNG
 *
 */
public final class SburbSessionManager implements Listener
{
	private Set<SburbSession> sessions;
	private Map<String, SburbSession> clients, servers;
	public static final String SESSIONS_DIR = SburbPlayers.PLUGIN_DIR + "sessions/";
	static PhernaliaRegistry registry = new PhernaliaRegistry();
	
	public SburbSessionManager() throws IOException
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, SburbPlayers.getInstance());
		Gson gson = new Gson();
		sessions = new HashSet<SburbSession>();
		clients = new HashMap<String, SburbSession>();
		servers = new HashMap<String, SburbSession>();
		File[] entries = new File(SburbSessionManager.SESSIONS_DIR).listFiles(new FileFilter(){

			@Override
            public boolean accept(File file)
            {
	            if(file.getName().matches("s_.+_.+\\.sps"))
	            	return true;
	            else
	            	return false;
            }
			
		});
		Logger.getLogger("Minecraft").info("Loading " + entries.length + " active session(s)");
		for(File file : entries)
		{
			SburbSession newSession = gson.fromJson(new FileReader(file), SburbSession.class);
			if(newSession == null)
			{
				Logger.getLogger("Minecraft").severe("File " + file.getName() + " is corrupt, failed to load session.");
				continue;
			}
			newSession.loadSession();
			sessions.add(newSession);
			clients.put(newSession.getClientName(), newSession);
			servers.put(newSession.getServerName(), newSession);
		}
	}
	
	/**
	 * Returns the session object of which the given player is the client
	 * @param name Player to search
	 * @return The player's client session, or null if the player is not a client in any session.
	 */
	SburbSession getClientSession(String name)
	{
		return clients.get(name);
	}
	
	/**
	 * Returns the session object of which the given player is the server
	 * @param name Player to search
	 * @return The player's server session, or null if the player is not a server in any session.
	 */
	SburbSession getServerSession(String name)
	{
		return servers.get(name);
	}
	
	/**
	 * Saves all sessions and performs other cleanup as necessary.
	 */
	public void shutdown()
	{
		for(SburbSession session : sessions)
		{
			try
			{
				session.saveSession();
			}
			catch(IOException e)
			{
				Logger.getLogger("Minecraft").severe("Error saving session file!: " + e.toString());
			}
		}
	}

	/**
	 * Starts a new session
	 * @param client Player who will act as the client in this session
	 * @param server Player who will act as the server in this session
	 * @param caller Administrator performing this action
	 */
    public void startSession(String client, String server, SburbPlayer caller)
    {
    	SburbSession session;
	    try
        {
	        session = new SburbSession(client,server);
        }
        catch (IOException e)
        {
	        SburbPlayers.getInstance().getPlayer(client).sendMessage("Your Sburb session could not be started.");
	        SburbPlayers.getInstance().getPlayer(server).sendMessage("Your Sburb session could not be started.");
	        e.printStackTrace();
	        return;
        }
	    
	    Logger.getLogger("Sburb").info("ADMIN ACTION: " + caller.getName() + " started a session (Client: " + session.getClientName() + ", Server: " + session.getServerName() + ")");
	    sessions.add(session);
	    clients.put(client, session);
	    servers.put(server, session);
	    session.init();
	    
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
    	if(registry.getPrice(event.getBlock().getType()) < 0)
    		event.setCancelled(true);
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
		Block eventBlock = event.getBlock();
		SburbSession clientSession = clients.get(event.getPlayer().getName()),
					 serverSession = servers.get(event.getPlayer().getName());
		
		if(serverSession != null && serverSession.isServerInEditMode())
		{
			int price = registry.getPrice(eventBlock.getType());
			if(price < 0)
			{
				serverSession.sendToClient("You are not allowed to place that item.");
				event.setCancelled(true);
				return;
			}
			else if(clientSession.getGrist() < price)
			{
				serverSession.sendToClient("This object costs " + ChatColor.RED + price + SburbSession.STD_COLOR + " grist. (Your total: "  + ChatColor.RED + serverSession.getGrist() + SburbSession.STD_COLOR + ")");
				event.setCancelled(true);
				return;
			}
			else
			{
				serverSession.adjustGrist(price * -1);
			}
		}
		if(clientSession != null && eventBlock.getType() == Material.COBBLESTONE && clientSession.receiveMark(eventBlock.getLocation()))
			event.setCancelled(true);
    }    
    

	/**
	 * Kills the session where the given player is the client
	 * @param clientPlayerToKill Client player of the session to kill
	 * @param caller Administrator issuing this command
	 * @see co.sblock.sburbplayers.SburbPlayer#killSession(Player)
	 */
    
    public void killSession(Player clientPlayerToKill, SburbPlayer caller)
    {
	    SburbSession sessionToKill = this.clients.get(clientPlayerToKill.getName());
	    Logger.getLogger("Sburb").info("ADMIN ACTION: " + caller.getName() + " killed a session (Client: " + sessionToKill.getClientName() + ", Server: " + sessionToKill.getServerName() + ")");
	    sessions.remove(sessionToKill);
	    clients.remove(sessionToKill.getClientName());
	    servers.remove(sessionToKill.getServerName());
	    sessionToKill.kill();
    }

	/**
	 * @param sburbPlayer
	 */
    public void teleport(SburbPlayer serverPlayer)
    {
    	SburbSession serverSession = servers.get(serverPlayer.getName());
    	if(serverSession != null)
    	{
    	    if(!servers.get(serverPlayer.getName()).isServerInEditMode())
    	    	servers.get(serverPlayer.getName()).teleportIn();
    	    else
    	    	servers.get(serverPlayer.getName()).teleportOut();
    	}
    	else
    		serverPlayer.sendMessage("You are not a server player in a session");
	    
    }

	/**
	 * Called when a player joins the server. When a player joins, if they are a client player currently in edit mode, the location
	 * checker should be enabled and all necessary powers are given to them.
	 * @param event
	 */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
	    SburbSession sSession = servers.get(event.getPlayer().getName()),
	    		     cSession = clients.get(event.getPlayer().getName());
	    if(sSession != null)
	    	sSession.onServerPlayerJoin();
	    if(cSession != null)
	    	cSession.onClientPlayerJoin();
	    
    }

    
}
