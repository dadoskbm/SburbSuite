package com.benzrf.sblock.sburbmachines;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.bukkit.Location;
import org.bukkit.World;

public class SLocation extends Location
{
	public SLocation(World world, double x, double y, double z)
	{
		super(world, x, y, z);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
	}
	
//	private 
}
