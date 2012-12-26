package com.benzrf.sblock.sburbmachines;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.benzrf.sblock.sburbmachines.machines.Cruxtruder;
import com.benzrf.sblock.sburbmachines.machines.Machine;

public class SburbMachines extends JavaPlugin implements Listener
{
	@SuppressWarnings("unchecked")
	@Override
	public void onEnable()
	{
		try
		{
			ObjectInputStream i = new ObjectInputStream(new FileInputStream(new File("plugins/SburbMachines/machines.smd")));
			this.machines = (Map<Location, Machine>) i.readObject();
			i.close();
		}
		catch (Exception e)
		{
			Logger.getLogger("Minecraft").warning("[SburbMachines] Error reading machines file!");
		}
		this.getServer().getPluginManager().registerEvents(this, this);
		SburbMachines.instance = this;
	}
	
	@Override
	public void onDisable()
	{
		try
		{
			ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(new File("plugins/SburbMachines/machines.smd")));
			o.writeObject(this.machines);
			o.flush();
			o.close();
		}
		catch (Exception e)
		{
			Logger.getLogger("Minecraft").warning("[SburbMachines] Error reading machines file!");
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if (commandLabel.equalsIgnoreCase("crux"))
		{
			Cruxtruder c = new Cruxtruder(((Player) sender).getLocation(), false);
			this.addMachine(c.getBlocks(), c);
		}
		else if (commandLabel.equalsIgnoreCase("delmachine"))
		{
			try
			{
				Location[] blocks = this.machines.get(((Player) sender).getTargetBlock(null, 100).getLocation()).getBlocks();
				this.removeMachine(blocks);
				for (Location block : blocks)
				{
					block.getBlock().setTypeIdAndData(0, (byte) 0, false);
				}
			}
			catch (NullPointerException e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		try
		{
			Block b = event.getClickedBlock();
			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				if (!machines.containsKey(b.getLocation()) & !event.isCancelled() && b.getType().equals(Material.LEVER))
				{
					if (b.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.IRON_BLOCK) && b.getLocation().subtract(0, 2, 0).getBlock().getType().equals(Material.GOLD_BLOCK))
					{
						b.setTypeId(0);
						b.getLocation().subtract(0, 1, 0).getBlock().setTypeId(0);
						b.getWorld().createExplosion(b.getLocation(), 0);
						Transmaterializer t = new Transmaterializer(b.getLocation().subtract(0, 2, 0));
						this.addMachine(t.getBlocks(), t);
					}
				}
				event.setCancelled(machines.get(b.getLocation()).onRightClick(event.getPlayer(), b) || event.isCancelled());
			}
			else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK))
			{
				event.setCancelled(machines.get(b.getLocation()).onLeftClick(event.getPlayer(), b) || event.isCancelled());
			}
		}
		catch (NullPointerException e)
		{
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		if (machines.containsKey(event.getBlock().getLocation()))
		{
			event.setCancelled(machines.get(event.getBlock().getLocation()).onBreak(event.getPlayer(), event.getBlock()) || event.isCancelled());
		}
	}
	
	public void addMachine(Location[] blocks, Machine m)
	{
		for (Location block : blocks)
		{
			this.machines.put(block, m);
		}
	}
	
	public void removeMachine(Location[] blocks)
	{
		for (Location block : blocks)
		{
			this.machines.remove(block);
		}
	}
	
	private Map<Location, Machine> machines = new HashMap<Location, Machine>();
	public static SburbMachines instance;
}
