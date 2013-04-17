package co.sblock.sburbplayers.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import co.sblock.sburbmachines.machines.Machine;

/**
 * The Phernalia Registry provides a "catalog" of all items purchasable with Grist. It also manages the inventory screen
 * that server players see when placing blocks and provides methods to pass that information over to the session manager,
 * and the sessions themselves.
 * @author FireNG
 *
 */
public class PhernaliaRegistry
{
	private Map<String, Integer> prices;
	
	/**
	 * Creates a new Phernalia Registry, loading in prices from a file. If no file exists, a default will be used.
	 */
	public PhernaliaRegistry()
	{
		prices = new HashMap<String, Integer>();
		prices.put("block." + Material.COBBLESTONE.name(), 1);
		prices.put("block." + Material.STONE.name(), 1);
		prices.put("block." + Material.BRICK.name(), 2);
		prices.put("block." + Material.GOLD_BLOCK.name(), 100);
	}
	
	/**
	 * Provides the grist price for the given item.
	 * @param item The item to look up
	 * @return The cost of the item, in grist. If the cost is less than 0,
	 *         the item may not be bought using grist.
	 */
	public int getPrice(Material item)
	{
		Integer value = prices.get("block." + item.name());
		return value != null ? value : -1;
	}
	
	/**
	 * Returns a set of items that may be bought with grist.
	 * @return The set of items.
	 */
	public ItemStack[] getAvailableItems()
	{
		Set<String> keys = prices.keySet();
		List<ItemStack> items = new ArrayList<ItemStack>();
		for(String key : keys)
		{
			String[] arr = key.split("\\.");
			if(arr[0].equals("block"))
				items.add(new ItemStack(Material.valueOf(arr[1])));
		}
		return items.toArray(new ItemStack[items.size()]);
	}
	
	/**
	 * Provides the grist price for the given machine.
	 * @param machine The machine to look up
	 * @return The cost of the machine, in grist. If the cost is less than 0,
	 * 		   the machine may not be bought using grist.
	 */
	public int getPrice(Machine machine)
	{
		return 0; //TODO NYI
	}
}
