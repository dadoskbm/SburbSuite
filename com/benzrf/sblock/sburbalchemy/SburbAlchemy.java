package com.benzrf.sblock.sburbalchemy;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.benzrf.sblock.sburbalchemy.machines.Cruxtruder;
import com.benzrf.sblock.sburbalchemy.machines.Machine;

public class SburbAlchemy extends JavaPlugin implements Listener
{
	@Override
	public void onEnable()
	{
		this.getServer().getPluginManager().registerEvents(this, this);
		SburbAlchemy.instance = this;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		new Cruxtruder(((Player) sender).getLocation(), false);
		return true;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_AIR))
		{
			return;
		}
		else if (!machines.containsKey(event.getClickedBlock().getLocation()))
		{
			return;
		}
		else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			event.setCancelled(machines.get(event.getClickedBlock().getLocation()).onRightClick(event.getPlayer(), event.getClickedBlock()) || event.isCancelled());
		}
		else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK))
		{
			event.setCancelled(machines.get(event.getClickedBlock().getLocation()).onLeftClick(event.getPlayer(), event.getClickedBlock()) || event.isCancelled());
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
	
	public void removeMachine(Location[] blocks, Machine m)
	{
		for (Location block : blocks)
		{
			this.machines.remove(block);
		}
	}
	
	private Map<Location, Machine> machines = new HashMap<Location, Machine>();
	public static SburbAlchemy instance;
}
