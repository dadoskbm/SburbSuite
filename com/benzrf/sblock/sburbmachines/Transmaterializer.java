package com.benzrf.sblock.sburbmachines;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.benzrf.sblock.sburbmachines.machines.Machine;

public class Transmaterializer extends Machine
{
	public Transmaterializer(Location base)
	{
		this.base = base.getBlock().getLocation();
		
		Location[] blocks = new Location[Transmaterializer.iron.length + Transmaterializer.doubleslab.length + Transmaterializer.slab.length + Transmaterializer.glass.length + Transmaterializer.emerald.length + 4];
		blocks = this.setBlocks(Transmaterializer.iron, Material.IRON_BLOCK, blocks, 4);
		blocks = this.setBlocks(Transmaterializer.doubleslab, Material.DOUBLE_STEP, blocks, Transmaterializer.iron.length + 4);
		blocks = this.setBlocks(Transmaterializer.slab, Material.STEP, blocks, Transmaterializer.iron.length + Transmaterializer.doubleslab.length + 4);
		blocks = this.setBlocks(Transmaterializer.glass, Material.GLASS, blocks, Transmaterializer.iron.length + Transmaterializer.doubleslab.length + Transmaterializer.slab.length + 4);
		blocks = this.setBlocks(Transmaterializer.emerald, Material.EMERALD_BLOCK, blocks, Transmaterializer.iron.length + Transmaterializer.doubleslab.length + Transmaterializer.glass.length + Transmaterializer.glass.length + 4);
		blocks[0] = this.base.clone().add(Transmaterializer.gold[0], Transmaterializer.gold[1], Transmaterializer.gold[2]);
		blocks[0].getBlock().setType(Material.GOLD_BLOCK);
		blocks[1] = this.base.clone().add(Transmaterializer.chest[0], Transmaterializer.chest[1], Transmaterializer.chest[2]);
		blocks[1].getBlock().setType(Material.CHEST);
		blocks[2] = this.base.clone().add(Transmaterializer.button1[0], Transmaterializer.button1[1], Transmaterializer.button1[2]);
		blocks[2].getBlock().setTypeIdAndData(Material.STONE_BUTTON.getId(), (byte) 1, true);
		blocks[3] = this.base.clone().add(Transmaterializer.button2[0], Transmaterializer.button2[1], Transmaterializer.button2[2]);
		blocks[3].getBlock().setTypeIdAndData(Material.STONE_BUTTON.getId(), (byte) 2, true);
		
		this.blocks = blocks;
	}
	
	private Location[] setBlocks(int[][] blocks, Material block, Location[] array, int offset)
	{
		for (int i = 0; i < blocks.length; i++)
		{
			array[i + offset] = this.base.clone().add(blocks[i][0], blocks[i][1], blocks[i][2]);
			array[i + offset].getBlock().setType(block);
		}
		return array;
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
		return true;
	}
	
	@Override
	public Location[] getBlocks()
	{
		return this.blocks;
	}
	
	private Location base;
	private Location[] blocks;
	
	static final int[][] iron = {
		{2, 0, -2}, {2, 0, -1}, {2, 0, 0}, {2, 0, 1}, {2, 0, 2},
		{-2, 0, -2}, {-2, 0, -1}, {-2, 0, 0}, {-2, 0, 1}, {-2, 0, 2},
		{-1, 0, 2}, {0, 0, 2}, {1, 0, 2},
		{-1, 0, -2}, {0, 0, -2}, {1, 0, -2},
		{-2, 1, -2}, {-1, 1, -2}, {0, 1, -2}, {1, 1, -2}, {2, 1, -2},
		{2, 1, -1}, {-2, 1, -1},
		{2, 1, 1}
	};
	static final int[][] doubleslab = {
		{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}, {1, 0, 1}, {-1, 0, -1}, {-1, 0, 1}, {1, 0, -1}
	};
	static final int[][] slab = {
		{2, 1, 2}, {-2, 1, 2}, {2, 2, -2}, {-2, 2, -2}
	};
	static final int[][] glass = {
		{-1, 2, -2}, {0, 2, -2}, {1, 2, -2}
	};
	static final int[][] emerald = {
		{-2, 1, 0}, {-2, 2, 0}, {-2, 3, 0},
		{2, 1, 0}, {2, 2, 0}, {2, 3, 0},
		{-1, 4, 0}, {1, 4, 0}
	};
	static final int[] gold = {0, 0, 0};
	static final int[] chest = {-2, 1, 1};
	static final int[] button1 = {-1, 2, 0};
	static final int[] button2 = {1, 2, 0};
	
	private static final long serialVersionUID = 5899573315323701783L;
}
