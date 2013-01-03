package com.benzrf.sblock.sburbmachines.machines;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.benzrf.sblock.sburbmachines.SburbMachines;


public class Transmaterializer extends Machine
{
	public Transmaterializer(){};
	public Transmaterializer(Location base)
	{
		this.base = base.getBlock().getLocation();
		
		Location[] blocks = new Location[Transmaterializer.iron.length + Transmaterializer.doubleslab.length + Transmaterializer.slab.length + Transmaterializer.glass.length + Transmaterializer.emerald.length + 4];
		blocks = this.setBlocks(Transmaterializer.iron, Material.IRON_BLOCK, blocks, 4);
		blocks = this.setBlocks(Transmaterializer.doubleslab, Material.DOUBLE_STEP, blocks, Transmaterializer.iron.length + 4);
		blocks = this.setBlocks(Transmaterializer.slab, Material.STEP, blocks, Transmaterializer.iron.length + Transmaterializer.doubleslab.length + 4);
		blocks = this.setBlocks(Transmaterializer.glass, Material.GLASS, blocks, Transmaterializer.iron.length + Transmaterializer.doubleslab.length + Transmaterializer.slab.length + 4);
		blocks = this.setBlocks(Transmaterializer.emerald, Material.EMERALD_BLOCK, blocks, Transmaterializer.iron.length + Transmaterializer.doubleslab.length + Transmaterializer.slab.length + Transmaterializer.glass.length + 4);
		blocks[0] = Machine.addLocationDifference(this.base.clone(), Transmaterializer.gold);
		blocks[0].getBlock().setType(Material.GOLD_BLOCK);
		blocks[1] = Machine.addLocationDifference(this.base.clone(), Transmaterializer.chest);
		blocks[1].getBlock().setType(Material.CHEST);
		blocks[2] = Machine.addLocationDifference(this.base.clone(), Transmaterializer.button1);
		blocks[2].getBlock().setTypeIdAndData(Material.STONE_BUTTON.getId(), (byte) 1, true);
		blocks[3] = Machine.addLocationDifference(this.base.clone(), Transmaterializer.button2);
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
		try
		{
			if (Arrays.equals(Machine.getLocationDifference(b.getLocation(), this.base), Transmaterializer.button1) || Arrays.equals(Machine.getLocationDifference(b.getLocation(), this.base), Transmaterializer.button2))
			{
				if (this.blockFromDifference(Transmaterializer.signx).getType().equals(Material.WALL_SIGN) &&
					this.blockFromDifference(Transmaterializer.signy).getType().equals(Material.WALL_SIGN) &&
					this.blockFromDifference(Transmaterializer.signz).getType().equals(Material.WALL_SIGN))
				{
					Location target = new Location(this.base.getWorld(), 0, 0, 0);
					target.setX(Float.parseFloat(((Sign) blockFromDifference(Transmaterializer.signx).getState()).getLine(0)));
					target.setY(Float.parseFloat(((Sign) blockFromDifference(Transmaterializer.signy).getState()).getLine(0)));
					target.setZ(Float.parseFloat(((Sign) blockFromDifference(Transmaterializer.signz).getState()).getLine(0)));
					if (this.base.distance(target) < 300)
					{
						Inventory i = ((Chest) this.blockFromDifference(chest).getState()).getInventory();
						if (i.contains(Material.GLOWSTONE) && i.contains(Material.REDSTONE))
						{
							i.getItem(i.first(Material.GLOWSTONE)).setAmount(i.getItem(i.first(Material.GLOWSTONE)).getAmount() - 1);
							i.getItem(i.first(Material.REDSTONE)).setAmount(i.getItem(i.first(Material.GLOWSTONE)).getAmount() - 1);
							if (Arrays.equals(Machine.getLocationDifference(b.getLocation(), this.base), Transmaterializer.button1))
							{
								Arrow a = this.base.getWorld().spawnArrow(base.clone().add(0.5, 1, 0.5), new Vector(), 0, 0);
								for (Entity e : a.getNearbyEntities(1, 1, 1))
								{
									e.teleport(target);
								}
								a.remove();
							}
							else
							{
								Arrow a = this.base.getWorld().spawnArrow(target, new Vector(), 0, 0);
								for (Entity e : a.getNearbyEntities(5, 5, 5))
								{
									e.teleport(base.clone().add(0.5, 1, 0.5));
								}
								a.remove();
							}
//							SburbMachines.instance.getServer().broadcastMessage("AWESOME");
						}
					}
					else
					{
//						SburbMachines.instance.getServer().broadcastMessage("TOO FAR");
						return false;
					}
				}
				else
				{
//					SburbMachines.instance.getServer().broadcastMessage("SIGNS NOT SET");
				}
			}
		}
		catch (NumberFormatException e)
		{
//			SburbMachines.instance.getServer().broadcastMessage("INVALID COORDS");
		}
		return false;
	}
	
	private Block blockFromDifference(int[] a)
	{
		return Machine.addLocationDifference(this.base.clone(), a).getBlock();
	}
	
	@Override
	public boolean onBreak(Player p, Block b)
	{
		if (Arrays.equals(Machine.getLocationDifference(b.getLocation(), this.base), Transmaterializer.gold))
		{
			this.base.getWorld().createExplosion(this.base, 0);
			SburbMachines.instance.deleteMachine(this);
			this.base.getWorld().dropItem(this.base, new ItemStack(Material.GOLD_BLOCK));
		}
		return true;
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
	static final int[] signx = {-1, 2, -1};
	static final int[] signy = {0, 2, -1};
	static final int[] signz = {1, 2, -1};
	
	private static final long serialVersionUID = 5899573315323701783L;
}
