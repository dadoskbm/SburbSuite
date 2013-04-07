package com.benzrf.sblock.sburbplayers.session;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.benzrf.sblock.sburbplayers.SburbPlayer;
import com.benzrf.sblock.sburbplayers.SburbPlayers;
import com.google.gson.Gson;

/*
 * FROM Sburb Notes.txt:
 * Entry
*"The Medium" - Four 5000x5000 planets, plots for everyone on their own planet
*Entering the Medium
 *Server/Client receive a pair of books, used to start Sburb
 *Server
  *Marks area of client's house - preparation for copying to Medium
  *Server player has limited WorldEdit powers, places machines for client to use them
  *Powers are persistent after entry into Medium
  *Server teleported to client after entry
 *Client
  *Utilizes machines that player 
  *Activate Cruxtender when ready
*Use XP numbers as Countdown
*Post-Entry
 *Player's house is randomly placed into a 100x100 plot in their world.
 *Travel between worlds is forbidden until
 *Battlefield and the Veil
  *The Veil - Meteors - Players "hitch a ride" on a meteor back to Earth
*Grist is XP
*Boondollars - Some item - Emeralds?
*The Furthest Ring
 *Dream Bubbles
  *Small - Small glass sphere, purchase access with boondollars, grist, etc.
  *Large - Larger spheres for advanced builders.
 */
/**
 * This class represents a single Sburb session. 
 * 
 * A Sburb session consists of two players, a client and a server player. The server is responsible
 * for modifying the client's house and placing machines. The client executes the machines and is the
 * main one responsible for allowing both players to enter their session.<p>
 * 
 * Another main responsibility of this class is to keep track of the location of the client's house, whose boundaries
 * are defined at the time the session is initiated. It will also keep track of a "ground floor", to assist
 * the teleportation of the house to the land when their land is entered. Only blocks inside of this boundary may
 * be modified by the server player or the game throughout the duration of the session.
 * @author FireNG
 *
 */
public class SburbSession implements Serializable
{
	private static final long serialVersionUID = -7964982032724530818L;
    private String cName, sName;
    private transient SburbPlayer client, server;
    private Location[] cuboidPoints = new Location[2];
    private transient File sessionFile;
    private BukkitTask locationChecker;
    private int totalArea = -1;
    private int groundFloorLevel = -1;
    private Location serverPrevLocation = null;
    private boolean serverInEditMode = false;
    private boolean isActive = false;
    private transient boolean markingMode = false;
	
    /**
     * Constructs the Sburb session. Note that this is NOT intended to start the session, merely define one.
     * Use init() to start the session.
     * @param client The client player
     * @param server The server player
     * @throws IOException If there was an issue creating the session file.
     */
    SburbSession(SburbPlayer client, SburbPlayer server) throws IOException
    {
    	this.client = client;
    	this.server = server;
    	this.cName = client.getName();
    	this.sName = server.getName();
    	sessionFile = new File(SburbSessionManager.SESSIONS_DIR + "s_" + cName + "_" + sName + ".sps");
    	saveSession();
    }
    
    /**
     * Starts the Sburb session. At this point, the client will set house boundaries, and 
     * all Sburb-related game rules go into effect. Once this method is called, both players are
     * in the session until victory/defeat conditions are met, or the session is killed by server staff.
     */
    void init()
    {
    	isActive = true;
    	sendToClient("Starting a new Sburb session with " + ChatColor.GOLD + sName);
    	sendToClient("You are the " + ChatColor.AQUA + "CLIENT");
    	sendToServer("Starting a new Sburb session with " + ChatColor.AQUA + cName);
    	sendToServer("You are the " + ChatColor.GOLD + "SERVER");
    	
    	sendToServer("Please wait while " + cName + " sets his/her house boundaries.");
    	sendToClient("You need to set the boundaries of your house.");
    	sendToClient("Place a " + ChatColor.RED + "cobblestone block " + ChatColor.YELLOW + "in " 
				 + ChatColor.RED + "one corner " + ChatColor.YELLOW + "of your house ");
    	markingMode = true;
    	
    }
    
    boolean receiveMark(Location location)
    {
    	if(markingMode)
    	{
    		if(cuboidPoints[0] == null)
    		{
    			cuboidPoints[0] = location;
    			sendToClient("Location 1 set to (" + SburbPlayers.lts(location) + ")");
    			sendToClient("Place a " + ChatColor.RED + "cobblestone block " + ChatColor.YELLOW + "in " 
    					 + ChatColor.RED + "the opposite corner " + ChatColor.YELLOW + "of your house ");
    		}
    		else if(cuboidPoints[1] == null)
    		{
    			cuboidPoints[1] = location;
    			sendToClient("Location 2 set to (" + SburbPlayers.lts(location) + ")");
    			sendToClient("Place a " + ChatColor.RED + "cobblestone block " + ChatColor.YELLOW + "on " 
    					 + ChatColor.RED + "the ground floor " + ChatColor.YELLOW + "of your house ");
    		}
    		else if(groundFloorLevel == -1)
    		{
    			groundFloorLevel = location.getBlockY() - 1;
    			sendToClient("Ground floor set to " + groundFloorLevel);
    			sendToClient("Locations: (" + SburbPlayers.lts(cuboidPoints[0]) + ") and (" + SburbPlayers.lts(cuboidPoints[1]) + ")");
    			sendToClient("Total area: " + this.getTotalArea());
    			markingMode = false;
    		}
    		return true;
    	}
    	else
    		return false;
    }
    
    /**
     * This method loads in references to the client and server. It is imperative that this method be called right
     * after deserialization.
     */
    void loadRefs()
    {
    	if(client == null)
    		client = SburbPlayers.getInstance().getPlayer(cName);
    	if(server == null)
    		server = SburbPlayers.getInstance().getPlayer(sName);
    	if(sessionFile == null)
    		sessionFile = new File(SburbSessionManager.SESSIONS_DIR + "s_" + cName + "_" + sName + ".sps");
    }
    
    /**
     * Determines if a point is inside the client's house
     * @param loc Location to check
     * @return true if the given location is inside the boundaries of the client's house
     */
    boolean isInsideHouse(Location loc)
    {
    	if(loc.getBlockX() >= Math.min(cuboidPoints[0].getBlockX(), cuboidPoints[1].getBlockX())
    			&& loc.getBlockX() <= Math.max(cuboidPoints[0].getBlockX(), cuboidPoints[1].getBlockX())
    			&& loc.getBlockY() >= Math.min(cuboidPoints[0].getBlockY(), cuboidPoints[1].getBlockY())
    	    	&& loc.getBlockY() <= Math.max(cuboidPoints[0].getBlockY(), cuboidPoints[1].getBlockY())
    	    	&& loc.getBlockZ() >= Math.min(cuboidPoints[0].getBlockZ(), cuboidPoints[1].getBlockZ())
    	    	&& loc.getBlockZ() <= Math.max(cuboidPoints[0].getBlockZ(), cuboidPoints[1].getBlockZ()))
    		return true;
    	else
    		return false;
    }
    
    /**
     * Teleports the server player to the client's house, granting limited creative powers inside the
     * house's boundaries. However, the server is not permitted to walk out, and must teleport out in
     * order to resume normal gameplay
     */
    void teleportIn()
    {
    	serverPrevLocation = server.asBukkitPlayer().getLocation();
    	
    	Location teleportLocation = getOpenLocation();
    	if(teleportLocation != null)
    	{
    		server.asBukkitPlayer().teleport(teleportLocation);
    		serverInEditMode = true;
    		locationChecker = this.new LocationCheckTask().runTaskTimer(SburbPlayers.getInstance(), SburbPlayers.TICKS_PER_SECOND, SburbPlayers.TICKS_PER_SECOND);
    		sendToServer("Teleportation sucuessful");
    	}
    	else
    		sendToServer(ChatColor.RED + "You could not be teleported to your client's house!");
    }
    
    /**
     * Called when the server player wishes to stop editing the client's house. The server then
     * returns to their previous location and resumes normal gameplay.
     */
    void teleportOut()
    {
    	serverInEditMode = false;
    	locationChecker.cancel();
    	server.asBukkitPlayer().teleport(serverPrevLocation);
    	serverPrevLocation = null;
    	sendToServer("Teleportation sucuessful");
    }
    
    /**
     * @return true if the server is currently in "edit mode" at their client's house
     */
    boolean isServerInEditMode()
    {
    	return serverInEditMode;
    }
    
    /**
     * Gets, or computes and returns, the total area of the client's house
	 * @return The area bounded by the coordinates set by the client
	 */
    private int getTotalArea()
    {
	    if(totalArea > 0)
	    	return totalArea;
	    else
	    {
	    	int lengthX = Math.abs(cuboidPoints[0].getBlockX() - cuboidPoints[1].getBlockX()) + 1;
	    	int lengthY = Math.abs(cuboidPoints[0].getBlockY() - cuboidPoints[1].getBlockY()) + 1;
	    	int lengthZ = Math.abs(cuboidPoints[0].getBlockZ() - cuboidPoints[1].getBlockZ()) + 1;
	    	totalArea = lengthX * lengthY * lengthZ;
	    	return totalArea;
	    }
    }
    
    /**
     * Returns an open location inside the client's house to teleport the client to.
     * @return A Location representing a feasible location to teleport the client, or null if no location was found.
     */
    private Location getOpenLocation()
    {
    	for(int x = Math.min(cuboidPoints[0].getBlockX(), cuboidPoints[1].getBlockY()); x <= Math.max(cuboidPoints[0].getBlockX(), cuboidPoints[1].getBlockX()); x++)
    	{
    		for(int z = Math.min(cuboidPoints[0].getBlockZ(), cuboidPoints[1].getBlockZ()); z <= Math.max(cuboidPoints[0].getBlockZ(),cuboidPoints[1].getBlockZ()); z++)
    		{
    			Location thisLoc = new Location(client.asBukkitPlayer().getWorld(), x, groundFloorLevel + 1, z);
    			if(thisLoc.getBlock().getType() == Material.AIR
    					&& thisLoc.add(0, 1, 0).getBlock().getType() == Material.AIR)
    			{
    				return thisLoc;
    			}
    		}
    	}
    	
    	return null;
    }

	void saveSession() throws IOException //FIXME Data cannot be saved in its current state. Something in the Location class is referencing itself, causing stack overflow.
    {
		try
		{
    	FileOutputStream out = new FileOutputStream(sessionFile);
    	System.out.println(new Gson().toJson(this));
    	out.write(new Gson().toJson(this).getBytes(Charset.defaultCharset()));
    	out.flush();
    	out.close();
		}catch(StackOverflowError e)
		{
			Logger.getLogger("Minecraft").severe("Stack overflow error!");
		}
    }
    
    /**
     * 
     * @return The client player
     */
    SburbPlayer getClientPlayer()
    {
    	return client;
    }
    
    /**
     * 
     * @return The server player
     */
    SburbPlayer getServerPlayer()
    {
    	return server;
    }
    
    /**
     * Sends a message to the client, with a color/text prefix.
     * @param message Message to send
     */
    void sendToClient(String message)
    {
    	this.getClientPlayer().sendMessage(ChatColor.YELLOW + message);
    }
    
    /**
     * Sends a message to the server, with a color/text prefix.
     * @param message Message to send
     */
    void sendToServer(String message)
    {
    	this.getServerPlayer().sendMessage(ChatColor.YELLOW + message);
    }
    
    /**
     * Sends a message to both session members
     * @param message Message to send
     */
    void sendToBoth(String message)
    {
    	sendToClient(message);
    	sendToServer(message);
    }
    
    /**
     * Stops the current session prematurely. Intended for server staff to kill a session for administrative
     * or technical reasons, such as a player abandonment or a bug.
     */
    void kill()
    {
    	isActive = false;
    	sendToBoth(ChatColor.RED + "Your Sburb session has been terminated");
    	if(!sessionFile.delete())
    		Logger.getLogger("Sburb").severe("Could not delete session file " + sessionFile.getName() + "!");
    	
    }
    
    /**
     * Task to intermittently check the server player's location to make sure that they have not left the boundaries of
     * the client's house
     * @author FireNG
     *
     */
    private class LocationCheckTask extends BukkitRunnable
    {

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
        @Override
        public void run()
        {
	        if(!isInsideHouse(server.asBukkitPlayer().getLocation()))
	        {	
	        	sendToServer(ChatColor.RED + "GET THE FUCK BACK!");
	        	server.asBukkitPlayer().teleport(getOpenLocation());
	        }
	        
        }
    	
    }

}
