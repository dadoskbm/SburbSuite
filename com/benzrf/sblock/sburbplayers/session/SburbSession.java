package com.benzrf.sblock.sburbplayers.session;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

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
	private static final ChatColor SERVER_COLOR = ChatColor.GOLD, CLIENT_COLOR = ChatColor.AQUA, STD_COLOR = ChatColor.YELLOW;
    private String cName, sName;
    //private transient SburbPlayer client, server;
    private transient Location[] cuboidPoints;
    private int[][] rawPoints;
    private int[] rawServerLocation;
    private String worldName, serverLocationWorldName;
    private transient File sessionFile;
    private transient BukkitTask locationChecker;
    private int totalArea = -1;
    private int groundFloorLevel = -1;
    private transient Location serverPrevLocation = null;
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
    SburbSession(String client, String server) throws IOException
    {
    	this.cName = client;
    	this.sName = server;
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
    	cuboidPoints = new Location[2];
    	isActive = true;
    	sendToClient("Starting a new Sburb session with " + SERVER_COLOR + sName);
    	sendToClient("You are the " + CLIENT_COLOR + "CLIENT");
    	sendToServer("Starting a new Sburb session with " + CLIENT_COLOR + cName);
    	sendToServer("You are the " + SERVER_COLOR + "SERVER");
    	
    	sendToServer("Please wait while " + CLIENT_COLOR + cName + STD_COLOR + " sets his/her house boundaries.");
    	sendToClient("You need to set the boundaries of your house.");
    	sendToClient("Place a " + ChatColor.RED + "cobblestone block " + STD_COLOR + "in " 
				 + ChatColor.RED + "one corner " + STD_COLOR + "of your house ");
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
    			sendToClient("Place a " + ChatColor.RED + "cobblestone block " + STD_COLOR + "in " 
    					 + ChatColor.RED + "the opposite corner " + STD_COLOR + "of your house ");
    		}
    		else if(cuboidPoints[1] == null)
    		{
    			cuboidPoints[1] = location;
    			sendToClient("Location 2 set to (" + SburbPlayers.lts(location) + ")");
    			sendToClient("Place a " + ChatColor.RED + "cobblestone block " + STD_COLOR + "on " 
    					 + ChatColor.RED + "the ground floor " + STD_COLOR + "of your house ");
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
    	Location teleportLocation = serverPrevLocation == null ? getOpenLocation() : serverPrevLocation;
    	serverPrevLocation = Bukkit.getPlayer(sName).getLocation();
    	if(teleportLocation != null)
    	{
    		Bukkit.getPlayer(sName).teleport(teleportLocation);
    		serverInEditMode = true;
    		locationChecker = this.new LocationCheckTask().runTaskTimer(SburbPlayers.getInstance(), SburbPlayers.TICKS_PER_SECOND, SburbPlayers.TICKS_PER_SECOND);
    		sendToServer("Teleported in");
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
    	locationChecker = null;
    	Location prevLoc = Bukkit.getPlayer(sName).getLocation();
    	Bukkit.getPlayer(sName).teleport(serverPrevLocation);
    	serverPrevLocation = prevLoc; //Save location for when server returns to house.
    	sendToServer("Teleported out");
    }
    
    /**
	 * Called by the session manager when the server player joins. If a server player was in edit mode when they
	 * last logged out, the location checker must be reinitialized.
	 */
	void onServerPlayerJoin()
	{
	    if(serverInEditMode)
	    {
	    	locationChecker = this.new LocationCheckTask().runTaskTimer(SburbPlayers.getInstance(), SburbPlayers.TICKS_PER_SECOND, SburbPlayers.TICKS_PER_SECOND);
	    }
	    
	}
	
	/**
	 * Called by the session manager when the client player joins. If setup of the session is not complete, then the session must
	 * be reinitialized.
	 */
	void onClientPlayerJoin()
	{
		if(cuboidPoints == null)
			init(); //Boundaries still need to be set.
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
    			Location thisLoc = new Location(Bukkit.getPlayer(cName).getWorld(), x + 0.5D, groundFloorLevel + 1, z + 0.5D);
    			if(thisLoc.getBlock().getType() == Material.AIR
    					&& thisLoc.add(0, 1, 0).getBlock().getType() == Material.AIR)
    			{
    				return thisLoc;
    			}
    		}
    	}
    	
    	return null;
    }

	/**
	 * @return The client player's name.
	 */
	String getClientName()
	{
	    return cName;
	}

	/**
	 * @return The server player's name.
	 */
	String getServerName()
	{
		return sName;
	}

	/**
	 * This method loads in references to transient class values that could not be saved in a file. It is imperative that this method
	 * be called right after deserialization.
	 */
	void loadSession()
	{
		if(sessionFile == null)
			sessionFile = new File(SburbSessionManager.SESSIONS_DIR + "s_" + cName + "_" + sName + ".sps");
		if(cuboidPoints == null && rawPoints != null)
		{
			cuboidPoints = new Location[2];
			for(int i = 0; i < cuboidPoints.length; i++)
				cuboidPoints[i] = new Location(Bukkit.getWorld(worldName), rawPoints[i][0], rawPoints[i][1], rawPoints[i][2]);
		}
		if(rawServerLocation != null)
		{
			serverPrevLocation = new Location(Bukkit.getWorld(serverLocationWorldName), rawServerLocation[0], rawServerLocation[1], rawServerLocation[2]);
		}
	
	}

	/**
	 * Prepares this session for serialization, serializes it, and saves it to the proper file.
	 * @throws IOException if an IOException occurs
	 */
	void saveSession() throws IOException
    {
		if(cuboidPoints != null && cuboidPoints[0] != null && cuboidPoints[1] != null)
		{
			rawPoints = new int[2][3];
    		for(int i = 0; i < rawPoints.length; i++)
    		{
    			rawPoints[i][0] = cuboidPoints[i].getBlockX();
    			rawPoints[i][1] = cuboidPoints[i].getBlockY();
    			rawPoints[i][2] = cuboidPoints[i].getBlockZ();
    		}
    		worldName = cuboidPoints[0].getWorld().getName();
		}
		if(serverPrevLocation != null)
		{
			serverLocationWorldName = serverPrevLocation.getWorld().getName();
			rawServerLocation = new int[3];
			rawServerLocation[0] = serverPrevLocation.getBlockX();
			rawServerLocation[1] = serverPrevLocation.getBlockY();
			rawServerLocation[2] = serverPrevLocation.getBlockZ();
		}
    	FileOutputStream out = new FileOutputStream(sessionFile);
    	byte[] json = new Gson().toJson(this).getBytes(Charset.defaultCharset());
    	if(json.length > 0)
    	{
        	out.write(json);
        	out.flush();
        	out.close();
    	}
    	else //Something went wrong with converting to JSON
    		Logger.getLogger("Minecraft").severe(sessionFile.getName() + " could not be saved, an error occured during creation of the file.");
    }
    

    
    /**
     * Sends a message to the client, with a color/text prefix.
     * @param message Message to send
     */
    void sendToClient(String message)
    {
    	Bukkit.getPlayer(cName).sendMessage("[" + SERVER_COLOR + sName + ChatColor.WHITE + "=>" + CLIENT_COLOR + cName + ChatColor.WHITE + "] " + STD_COLOR + message);
    }
    
    /**
     * Sends a message to the server, with a color/text prefix.
     * @param message Message to send
     */
    void sendToServer(String message)
    {
    	Bukkit.getPlayer(sName).sendMessage("[" + SERVER_COLOR + sName + ChatColor.WHITE + "=>" + CLIENT_COLOR + cName + ChatColor.WHITE + "] " + STD_COLOR + message);
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
    	if(locationChecker != null)
    		locationChecker.cancel();
    	isActive = false;
    	sendToBoth(ChatColor.RED + "Your Sburb session has been terminated by a server administrator.");
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
        	if(Bukkit.getPlayer(sName) == null)
        	{
        		this.cancel();
        	}
        	else if(!isInsideHouse(Bukkit.getPlayer(sName).getLocation()))
	        {	
	        	sendToServer(ChatColor.RED + "GET THE FUCK BACK!");
	        	Bukkit.getPlayer(sName).teleport(getOpenLocation());
	        }
	        
        }
    	
    }
}
