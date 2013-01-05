package com.benzrf.sblock.sburbmachines.machines;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import com.benzrf.sblock.sburbmachines.SburbMachines;

public abstract class Machine implements Serializable
{
	public abstract boolean onLeftClick(Player p, Block b);
	public abstract boolean onRightClick(Player p, Block b);
	public abstract boolean onBreak(Player p, Block b);
	public void onOtherEvent(Event event)
	{
		if (event instanceof Cancellable)
		{
			((Cancellable) event).setCancelled(true);
		}
	}
	
	public void tick(){}
	
	public abstract Location[] getBlocks();
	
	public void makeSerializable(){}
	public void makeUsable(){}
	
	static int[] getLocationDifference(Location l1, Location l2)
	{
		return new int[] {(int) (l1.getX() - l2.getX()), (int) (l1.getY() - l2.getY()), (int) (l1.getZ() - l2.getZ())};
	}
	
	static Location addLocationDifference(Location l, int[] a)
	{
		return l.add(a[0], a[1], a[2]);
	}
	
	static String lts(Location l)
	{
		String out = "";
		out = out + l.getWorld().getName() + ",";
		out = out + l.getBlockX() + ",";
		out = out + l.getBlockY() + ",";
		out = out + l.getBlockZ();
		return out;
	}
	
	static Location stl(String s)
	{
		String[] parts = s.split(",");
		try
		{
			return new Location(SburbMachines.instance.getServer().getWorld(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
		}
		return null;
	}
	
	private static final long serialVersionUID = 7803062027010131046L;
}
