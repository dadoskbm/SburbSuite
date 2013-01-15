package com.benzrf.sblock.sburbmachines.machines;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
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
		setBlocks(true);
	}
	
	private void setBlocks(boolean remove)
	{
		final int single = 5;
		Location[] blocks = new Location[Transmaterializer.iron.length + Transmaterializer.doubleslab.length + Transmaterializer.slab.length + Transmaterializer.glass.length + Transmaterializer.emerald.length + single];
		blocks = this.setBlocks(Transmaterializer.iron, Material.IRON_BLOCK, blocks, single, remove);
		blocks = this.setBlocks(Transmaterializer.doubleslab, Material.DOUBLE_STEP, blocks, Transmaterializer.iron.length + single, remove);
		blocks = this.setBlocks(Transmaterializer.slab, Material.STEP, blocks, Transmaterializer.iron.length + Transmaterializer.doubleslab.length + single, remove);
		blocks = this.setBlocks(Transmaterializer.glass, Material.GLASS, blocks, Transmaterializer.iron.length + Transmaterializer.doubleslab.length + Transmaterializer.slab.length + single, remove);
		blocks = this.setBlocks(Transmaterializer.emerald, Material.EMERALD_BLOCK, blocks, Transmaterializer.iron.length + Transmaterializer.doubleslab.length + Transmaterializer.slab.length + Transmaterializer.glass.length + single, remove);
		this.setBlock(Transmaterializer.gold, Material.GOLD_BLOCK, blocks, 0, (byte) 0, remove);
		this.setBlock(Transmaterializer.chest, Material.CHEST, blocks, 1, (byte) 0, remove);
		this.setBlock(Transmaterializer.button1, Material.STONE_BUTTON, blocks, 2, (byte) 1, remove);
		this.setBlock(Transmaterializer.button2, Material.STONE_BUTTON, blocks, 3, (byte) 2, remove);
		this.setBlock(Transmaterializer.wool, Material.WOOL, blocks, 4, (byte) 0, remove);
		this.blocks = blocks;
	}
	private Location[] setBlocks(int[][] blocks, Material block, Location[] array, int offset, boolean remove)
	{
		for (int i = 0; i < blocks.length; i++)
		{
			array[i + offset] = this.base.clone().add(blocks[i][0], blocks[i][1], blocks[i][2]);
			if (remove)
			{
				SburbMachines.instance.setBlock(array[i + offset].getBlock(), block.getId(), (byte) 0, false);
			}
			else
			{
				array[i + offset].getBlock().setType(block);
			}
		}
		return array;
	}
	private void setBlock(int[] loc, Material block, Location[] array, int index, byte data, boolean remove)
	{
		array[index] = Machine.addLocationDifference(this.base.clone(), loc);
		if (remove)
		{
			SburbMachines.instance.setBlock(array[index].getBlock(), block.getId(), data, false);
		}
		else
		{
			array[index].getBlock().setTypeIdAndData(block.getId(), data, false);
		}
	}
	
	@Override
	public boolean onLeftClick(Player p, Block b)
	{
		if (this.isBlock(b, Transmaterializer.chest))
		{
			Inventory inv = ((Chest) this.blockFromDifference(Transmaterializer.chest).getState()).getInventory();
			int add;
			for (int i = 0; i < inv.getContents().length; i++)
			{
				ItemStack is = inv.getContents()[i];
				if (is != null && Transmaterializer.fuels.containsKey(is.getType()))
				{
					add = Transmaterializer.fuels.get(is.getType()) * is.getAmount();
					if ((this.fuel + add) <= Transmaterializer.maxFuel)
					{
						inv.clear(i);
						this.fuel += add;
					}
				}
			}
			this.updateWool();
		}
		return false;
	}
	
	@Override
	public boolean onRightClick(Player p, Block b)
	{
		try
		{
			if (this.isBlock(b, Transmaterializer.button1) || this.isBlock(b, Transmaterializer.button2))
			{
				if (this.blockFromDifference(Transmaterializer.signx).getType().equals(Material.WALL_SIGN) &&
					this.blockFromDifference(Transmaterializer.signy).getType().equals(Material.WALL_SIGN) &&
					this.blockFromDifference(Transmaterializer.signz).getType().equals(Material.WALL_SIGN))
				{
					Location target = new Location(this.base.getWorld(), 0, 0, 0);
					target.setX(Float.parseFloat(((Sign) blockFromDifference(Transmaterializer.signx).getState()).getLine(0)));
					target.setY(Float.parseFloat(((Sign) blockFromDifference(Transmaterializer.signy).getState()).getLine(0)));
					target.setZ(Float.parseFloat(((Sign) blockFromDifference(Transmaterializer.signz).getState()).getLine(0)));
					if (this.base.distance(target) < 1000)
					{
						int icost = (int) Math.ceil(this.base.distance(target) / 200);
						int cost = (int) Math.ceil(this.base.distance(target) / 200) + 1;
						Location from;
						Location to;
						List<Callable<Entity>> es;
						if (Arrays.equals(Machine.getLocationDifference(b.getLocation(), this.base), Transmaterializer.button1))
						{
							to = this.base.clone().add(0, 1, 0);
							from = target;
						}
						else
						{
							from = this.base.clone().add(0, 1, 0);
							to = target;
						}
						if (from.getBlock().getType().equals(Material.CHEST) && SburbMachines.instance.lwc.canAccessProtection(p, SburbMachines.instance.lwc.findProtection(from.getBlock())))
						{
							es = this.getFromChest(from, cost, icost);
						}
						else
						{
							es = this.getFromLoc(from, cost, icost);
						}
						if (to.getBlock().getType().equals(Material.CHEST) && SburbMachines.instance.lwc.canAccessProtection(p, SburbMachines.instance.lwc.findProtection(to.getBlock())))
						{
							this.sendToChest(es, to, cost, icost);
						}
						else
						{
							this.sendToLoc(es, to, cost, icost);
						}
						this.updateWool();
					}
				}
			}
			else if (this.isBlock(b, Transmaterializer.wool) && p.getItemInHand() != null && p.getItemInHand().getType().equals(Material.COMPASS))
			{
				p.sendMessage("[" + ChatColor.GREEN + "Sburb" + ChatColor.GRAY + "Machines" + ChatColor.WHITE + "] " + ChatColor.GOLD + this.fuel + ChatColor.GREEN + " remaining!");
			}
		}
		catch (NumberFormatException e){}
		return false;
	}
	private List<Callable<Entity>> getFromLoc(Location l, int cost, int icost)
	{
		Arrow a = this.base.getWorld().spawnArrow(base.clone().add(0.5, 1, 0.5), new Vector(), 0, 0);
		List<Callable<Entity>> es = new ArrayList<Callable<Entity>>();
		int lfuel = this.fuel;
		for (Entity e : a.getNearbyEntities(1.5, 1.5, 1.5))
		{
			if ((e instanceof Item && ((lfuel -= icost) > 0)))
			{
				final Entity fe = e;
				es.add(new Callable<Entity>() {
					@Override
					public Entity call()
					{
						return fe;
					}
				});
			}
			else if ((lfuel -= cost) > 0)
			{
				final Entity fe = e;
				es.add(new Callable<Entity>() {
					@Override
					public Entity call()
					{
						return fe;
					}
				});
			}
			else
			{
				a.remove();
				return es;
			}
		}
		a.remove();
		return es;
	}
	private List<Callable<Entity>> getFromChest(Location l, int cost, int icost)
	{
		final Inventory inv = ((Chest) l.getBlock().getState()).getBlockInventory();
		List<Callable<Entity>> es = new ArrayList<Callable<Entity>>();
		int lfuel = this.fuel;
		for (int i = 0; i < inv.getContents().length; i++)
		{
			ItemStack is = inv.getContents()[i];
			if (is != null && ((lfuel -= icost) > 0))
			{
				final ItemStack fis = is;
				final int fi = i;
				es.add(new Callable<Entity>(){
					@Override
					public Entity call()
					{
						inv.clear(fi);
						return Transmaterializer.this.base.getWorld().dropItem(base.clone().add(0, 1, 0), fis);
					}
				});
			}
			else if (is != null)
			{
				return es;
			}
		}
		return es;
	}
	private void sendToLoc(List<Callable<Entity>> es, Location l, int cost, int icost)
	{
		for (Callable<Entity> ec : es)
		{
			try
			{
				Entity e = ec.call();
				if (e instanceof Item)
				{
					this.fuel -= icost;
					e.teleport(l);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	private void sendToChest(List<Callable<Entity>> es, Location l, int cost, int icost)
	{
		Inventory inv = ((Chest) l.getBlock().getState()).getInventory();
		for (int i = 0; i < inv.getContents().length; i++)
		{
			if (inv.getItem(i) == null)
			{
				Entity e;
				try
				{
					e = es.remove(0).call();
					if (e instanceof Item)
					{
						inv.setItem(i, ((Item) e).getItemStack());
						e.remove();
					}
				}
				catch (IndexOutOfBoundsException ex)
				{
					return;
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}
	
	private boolean isBlock(Block b, int[] a)
	{
		return Arrays.equals(Machine.getLocationDifference(b.getLocation(), this.base), a);
	}
	
	private Block blockFromDifference(int[] a)
	{
		return Machine.addLocationDifference(this.base.clone(), a).getBlock();
	}
	
	private void updateWool()
	{
		this.blockFromDifference(Transmaterializer.wool).setData((byte) (this.fuel == Transmaterializer.maxFuel ? 0x5 : (this.fuel > (Transmaterializer.maxFuel / 2) ? 0x4 : (this.fuel > 0 ? 0xE : 0x0))));
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
	
	private int fuel = 0;
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
	static final int[] wool = {0, 4, 0};
	static final int[] signx = {-1, 2, -1};
	static final int[] signy = {0, 2, -1};
	static final int[] signz = {1, 2, -1};
	
	static final int maxFuel = 612;
	static final Map<Material, Integer> fuels = new HashMap<Material, Integer>();
	static
	{
		fuels.put(Material.REDSTONE, 1);
		fuels.put(Material.GLOWSTONE, 3);
		fuels.put(Material.BLAZE_POWDER, 5);
	}
	
	private static final long serialVersionUID = 5899573315323701783L;
}
