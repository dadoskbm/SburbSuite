package com.benzrf.sblock.sburbmachines.machines;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Designix extends Machine
{
	public Designix(){}
	public Designix(Location base)
	{
		this.base = base.getBlock().getLocation();
	}
	
	@Override
	public boolean onLeftClick(Player p, Block b)
	{
		return false;
	}
	
	@Override
	public boolean onRightClick(Player p, Block b)
	{
		return false;
	}
	
	@Override
	public boolean onBreak(Player p, Block b)
	{
		return false;
	}
	
	@Override
	public Location[] getBlocks()
	{
		return this.blocks;
	}
	
	@Override
	public void makeSerializable()
	{
		this.sbase = Machine.lts(this.base);
		this.sblocks = new String[this.blocks.length];
		for (int i = 0; i < this.blocks.length; i++)
		{
			this.sblocks[i] = Machine.lts(this.blocks[i]);
		}
	}
	
	@Override
	public void makeUsable()
	{
		this.base = Machine.stl(this.sbase);
		this.blocks = new Location[this.sblocks.length];
		for (int i = 0; i < this.sblocks.length; i++)
		{
			this.blocks[i] = Machine.stl(this.sblocks[i]);
		}
	}
	
	transient private Location base;
	private String sbase;
	transient private Location[] blocks;
	private String[] sblocks;
	
	private static final long serialVersionUID = 3516288349999983742L;
}

