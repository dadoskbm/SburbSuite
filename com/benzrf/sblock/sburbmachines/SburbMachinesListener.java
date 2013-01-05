package com.benzrf.sblock.sburbmachines;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.benzrf.sblock.sburbmachines.machines.Transmaterializer;

public class SburbMachinesListener implements Listener
{
	public SburbMachinesListener()
	{
		this.pl = SburbMachines.instance;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		try
		{
			Block b = event.getClickedBlock();
			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				if (!this.pl.machines.containsKey(b.getLocation()) & !event.isCancelled() && b.getType().equals(Material.LEVER))
				{
					if (b.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.IRON_BLOCK) && b.getLocation().subtract(0, 2, 0).getBlock().getType().equals(Material.GOLD_BLOCK))
					{
						b.setTypeId(0);
						b.getLocation().subtract(0, 1, 0).getBlock().setTypeId(0);
						b.getWorld().createExplosion(b.getLocation(), 0);
						Transmaterializer t = new Transmaterializer(b.getLocation().subtract(0, 2, 0));
						this.pl.addMachine(t);
					}
				}
				event.setCancelled(this.pl.machines.get(b.getLocation()).onRightClick(event.getPlayer(), b) || event.isCancelled());
			}
			else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK))
			{
				event.setCancelled(this.pl.machines.get(b.getLocation()).onLeftClick(event.getPlayer(), b) || event.isCancelled());
			}
		}
		catch (NullPointerException e)
		{
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		if (this.pl.machines.containsKey(event.getBlock().getLocation()))
		{
			event.setCancelled(this.pl.machines.get(event.getBlock().getLocation()).onBreak(event.getPlayer(), event.getBlock()) || event.isCancelled());
		}
	}
	
	// bleh
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event)
	{
		for (Block b : event.blockList())
		{
			if (this.pl.machines.containsKey(b.getLocation())){this.pl.machines.get(b.getLocation()).onOtherEvent(event);}
		}
	}
	
	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent event)
	{
		if (this.pl.machines.containsKey(event.getBlock().getLocation())){this.pl.machines.get(event.getBlock().getLocation()).onOtherEvent(event);}
	}
	
	@EventHandler
	public void onBlockPistonExtend(BlockPistonExtendEvent event)
	{
		for (Block b : event.getBlocks())
		{
			if (this.pl.machines.containsKey(b.getLocation())){this.pl.machines.get(b.getLocation()).onOtherEvent(event);}
		}
	}
	
	@EventHandler
	public void onBlockPistonRetract(BlockPistonRetractEvent event)
	{
		if (this.pl.machines.containsKey(event.getRetractLocation())){this.pl.machines.get(event.getRetractLocation()).onOtherEvent(event);}
	}
	
	private void blockEvent(BlockEvent event)
	{
		if (this.pl.machines.containsKey(event.getBlock().getLocation())){this.pl.machines.get(event.getBlock().getLocation()).onOtherEvent(event);}
	}
	
	@EventHandler public void onBlockFade(BlockFadeEvent event){this.blockEvent(event);}
	@EventHandler public void onBlockIgnite(BlockIgniteEvent event){this.blockEvent(event);}
	@EventHandler public void onBlockBurn(BlockBurnEvent event){this.blockEvent(event);}
	
	final SburbMachines pl;
}
