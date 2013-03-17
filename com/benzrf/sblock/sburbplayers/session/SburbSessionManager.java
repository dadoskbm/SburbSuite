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
	private static SburbSessionManager theManager;
	private Set<SburbSession> sessions;
	private Map<SburbPlayer, SburbSession> clients, servers;
	public static final String SESSIONS_DIR = SburbPlayers.PLUGIN_DIR + "sessions/";
	private SburbSessionManager() throws IOException
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
		for(File file : entries)
		{
			SburbSession newSession = gson.fromJson(new FileReader(file), SburbSession.class);
			newSession.loadRefs();
			sessions.add(newSession);
			clients.put(newSession.getClientPlayer(), newSession);
			servers.put(newSession.getServerPlayer(), newSession);
		}
	}
	
	SburbSession getClientSession(SburbPlayer name)
	{
		return clients.get(name);
	}
	
	SburbSession getServerSession(SburbPlayer name)
	{
		return servers.get(name);
	}
	/**
	 * @return The plugin's session manager.
	 */
	public static SburbSessionManager getSessionManager()
	{
		return theManager;
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
		theManager = null;
	}

	/**
	 * Starts a new session
	 * @param sburbPlayer
	 * @param player
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
    
    static
    {
    	try
    	{
    		theManager = new SburbSessionManager();
    	}
    	catch(IOException e)
    	{
    		Logger.getLogger("Minecraft").severe("Could not start session manager! Shutting down plugin");
    		Bukkit.getServer().getPluginManager().disablePlugin(SburbPlayers.getInstance());
    		e.printStackTrace();
    	}
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
}
