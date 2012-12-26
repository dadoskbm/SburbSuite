package com.benzrf.sblock.sburbmachines.machines;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Cruxtruder extends Machine
{
	public Cruxtruder(Location base, boolean tick)
	{
		this.base = base.getBlock().getLocation();
		this.tick = tick;
		
		Location[] blocks = new Location[Cruxtruder.iron.length + 2];
		for (int i = 0; i < Cruxtruder.iron.length; i++)
		{
			blocks[i + 2] = this.base.clone().add(Cruxtruder.iron[i][0], Cruxtruder.iron[i][1], Cruxtruder.iron[i][2]);
			blocks[i + 2].getBlock().setType(Material.IRON_BLOCK);
		}
//		blocks[0] = this.base.clone().add(Cruxtruder.sign[0], Cruxtruder.sign[1], Cruxtruder.sign[2]);
//		blocks[0].getBlock().setType(Material.SIGN);
		blocks[0] = this.base.clone().add(Cruxtruder.diamond[0], Cruxtruder.diamond[1], Cruxtruder.diamond[2]);
		blocks[0].getBlock().setType(Material.DIAMOND_BLOCK);
		blocks[1] = this.base.clone().add(Cruxtruder.top[0], Cruxtruder.top[1], Cruxtruder.top[2]);
		blocks[1].getBlock().setType(Material.STEP);
		
		this.blocks = blocks;
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
		if (!this.broken && Arrays.equals(Machine.getLocationDifference(b.getLocation(), this.base), Cruxtruder.top))
		{
			this.broken = true;
			return false;
		}
		else if (this.broken && Arrays.equals(Machine.getLocationDifference(b.getLocation(), this.base), Cruxtruder.diamond))
		{
			this.base.getWorld().dropItem(this.base.clone().add(Cruxtruder.top[0], Cruxtruder.top[1], Cruxtruder.top[2]), new ItemStack(Material.SKULL_ITEM));
		}
		return true;
	}
	
	@Override
	public Location[] getBlocks()
	{
		return this.blocks;
	}
	
	@Override
	public void tick()
	{
		if (!tick) return;
	}
	
	private Location base;
	private Location[] blocks;
	private boolean tick;
	private boolean broken = false;
	
	static final int[][] iron = {
		{0, 0, 0},
		{1, 0, 0}, {0, 0, 1}, {-1, 0, 0}, {0, 0, -1},
		{1, 0, 1}, {1, 0, -1}, {-1, 0, 1}, {-1, 0, -1},
		{2, 0, -1}, {2, 0, 0}, {2, 0, 1},
		{-2, 0, -1}, {-2, 0, 0}, {-2, 0, 1},
		{-1, 0, 2}, {0, 0, 2}, {1, 0, 2},
		{-1, 0, -2}, {0, 0, -2}, {1, 0, -2},
		
		{0, 1, 0},
		{1, 1, 0}, {0, 1, 1}, {-1, 1, 0}, {0, 1, -1},
		{1, 1, 1}, {1, 1, -1}, {-1, 1, 1}, {-1, 1, -1},
		
		{1, 2, 0}, {0, 2, 1}, {-1, 2, 0}, {0, 2, -1}
	};
	static final int[] sign = {0, 1, 2};
	static final int[] diamond = {0, 2, 0};
	static final int[] top = {0, 3, 0};
	
	private static final long serialVersionUID = -3516283273370627172L;
}
