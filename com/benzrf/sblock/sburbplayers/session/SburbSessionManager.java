/**
 * 
 */
package com.benzrf.sblock.sburbplayers.session;

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
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.benzrf.sblock.sburbplayers.SburbPlayer;
import com.benzrf.sblock.sburbplayers.SburbPlayers;
import com.google.gson.Gson;

/**
 * Master class that manages all sessions currently in progress.
 * @author FireNG
 *
 */
public final class SburbSessionManager
{
	private Set<SburbSession> sessions;
	private Map<SburbPlayer, SburbSession> clients, servers;
	public static final String SESSIONS_DIR = SburbPlayers.PLUGIN_DIR + "sessions/";
	
	public SburbSessionManager() throws IOException
	{
		Gson gson = new Gson();
		sessions = new HashSet<SburbSession>();
		clients = new HashMap<SburbPlayer, SburbSession>();
		servers = new HashMap<SburbPlayer, SburbSession>();
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
		System.out.println("Entries: " + entries.length);
		for(File file : entries)
		{
			SburbSession newSession = gson.fromJson(new FileReader(file), SburbSession.class);
			System.out.printf("Entry %s produces JSON object %s", file.getName(), newSession);
			newSession.loadRefs();
			sessions.add(newSession);
			clients.put(newSession.getClientPlayer(), newSession);
			servers.put(newSession.getServerPlayer(), newSession);
		}
	}
	
	/**
	 * Returns the session object of which the given player is the client
	 * @param name Player to search
	 * @return The player's client session, or null if the player is not a client in any session.
	 */
	SburbSession getClientSession(SburbPlayer name)
	{
		return clients.get(name);
	}
	
	/**
	 * Returns the session object of which the given player is the server
	 * @param name Player to search
	 * @return The player's server session, or null if the player is not a server in any session.
	 */
	SburbSession getServerSession(SburbPlayer name)
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
				Logger.getLogger("Sburb").severe("Error saving session file!: " + e.toString());
			}
		}
	}

	/**
	 * Starts a new session
	 * @param client Player who will act as the client in this session
	 * @param server Player who will act as the server in this session
	 */
    public void startSession(SburbPlayer client, SburbPlayer server)
    {
    	SburbSession session;
	    try
        {
	        session = new SburbSession(client,server);
        }
        catch (IOException e)
        {
	        client.sendMessage("Your Sburb session could not be started.");
	        server.sendMessage("Your Sburb session could not be started.");
	        e.printStackTrace();
	        return;
        }
	    
	    sessions.add(session);
	    clients.put(client, session);
	    servers.put(server, session);
	    session.init();
	    
    }
    /**
     * Sends a mark to the client player's session class, to be interpreted.
     * @param location Marking location
     * @param clientPlayer Client player issuing the mark.
     * @return True if mark was used, and the marking block should NOT be placed.
     */
    public boolean sendMark(Location location, SburbPlayer clientPlayer)
    {
    	SburbSession clientSession = clients.get(clientPlayer);
    	if(clientSession != null && clientSession.receiveMark(location))
    		return true;
    	else
    		return false;
    }
    

	/**
	 * Kills the session where the given player is the client
	 * @param clientPlayerToKill Client player of the session to kill
	 * @see com.benzrf.sblock.sburbplayers.SburbPlayer#killSession(Player)
	 */
    
    public void killSession(Player clientPlayerToKill)
    {
	    SburbSession sessionToKill = this.clients.get(SburbPlayers.getInstance().getPlayer(clientPlayerToKill.getName()));
	    sessions.remove(sessionToKill);
	    clients.remove(sessionToKill.getClientPlayer());
	    servers.remove(sessionToKill.getServerPlayer());
	    sessionToKill.kill();
    }

	/**
	 * @param sburbPlayer
	 */
    public void teleport(SburbPlayer serverPlayer)
    {
    	SburbSession serverSession = servers.get(serverPlayer);
    	if(serverSession != null)
    	{
    	    if(!servers.get(serverPlayer).isServerInEditMode())
    	    	servers.get(serverPlayer).teleportIn();
    	    else
    	    	servers.get(serverPlayer).teleportOut();
    	}
    	else
    		serverPlayer.sendMessage("You are not a server player in a session");
	    
    }
}
