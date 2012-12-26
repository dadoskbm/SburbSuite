package com.benzrf.sblock.sburbmachines;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.World;

import com.benzrf.sblock.sburbchat.SburbChat;
import com.benzrf.sblock.sburbchat.User;

public class Slocation extends Location
{
	public Slocation(World world, double x, double y, double z)
	{
		super(world, x, y, z);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
	}
	
	private 
}
