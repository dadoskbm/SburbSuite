package com.benzrf.sblock.sburbalchemy.machines;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class Machine
{
	public abstract boolean onLeftClick(Player p, Block b);
	public abstract boolean onRightClick(Player p, Block b);
	public abstract boolean onBreak(Player p, Block b);
	
	public void tick(){}
	
	static int[] getLocationDifference(Location l1, Location l2)
	{
		return new int[] {(int) (l1.getX() - l2.getX()), (int) (l1.getY() - l2.getY()), (int) (l1.getZ() - l2.getZ())};
	}
}
