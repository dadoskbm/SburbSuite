package co.sblock.sburbplayers.session;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import co.sblock.sburbmachines.machines.Machine;
import co.sblock.sburbplayers.SburbPlayers;

/**
 * The Phernalia Registry provides a "catalog" of all items purchasable with Grist. It also manages the inventory screen
 * that server players see when placing blocks and provides methods to pass that information over to the session manager,
 * and the sessions themselves.
 * @author FireNG
 *
 */
public class PhernaliaRegistry
{
	/**
     * 
     */
	private Map<String, Integer> buyPrice, sellPrice;
	private static final File REG_FILE = new File(SburbPlayers.PLUGIN_DIR + "phernalia_registry.txt");
	private static final String ITEM = "item.", MACHINE = "machine.";
	
	/**
	 * Creates a new Phernalia Registry, loading in prices from a file. If no file exists, a default will be used.
	 */
	public PhernaliaRegistry()
	{
		if(!REG_FILE.exists())
		{
			SburbPlayers.getInstance().getLogger().warning("Phernalia registry file missing, creating a default!");
			this.saveDefault();
		}
		else
		{
			buyPrice = new HashMap<String, Integer>();
			sellPrice = new HashMap<String, Integer>();
			BufferedReader reader = null;
			try
			{
				reader = new BufferedReader(new FileReader(REG_FILE));
				String line;
				while((line = reader.readLine()) != null)
				{
					try
					{
    					if(line.startsWith("#")) //Skip commented lines
    						continue;
    					String[] parts = line.split(" ");
    					if(Integer.parseInt(parts[1]) != -1)
    						buyPrice.put(parts[0], Integer.parseInt(parts[1]));
    					if(Integer.parseInt(parts[2]) != 0)
    					{
    						if(Integer.parseInt(parts[2]) < 0)
    							throw new IllegalArgumentException();
    						sellPrice.put(parts[0], Integer.parseInt(parts[2]));
    					}
					}
					catch(RuntimeException e)
					{
						SburbPlayers.getInstance().getLogger().warning("Phernalia registry: Syntax error on line '" + line + "', skipping line.");
						continue;
					}
				}
			}
			catch(IOException e)
			{
				SburbPlayers.getInstance().getLogger().severe("Failed to load Phernalia Registry file, creating a default.");
				this.useDefault();
			}
			finally
			{
				try
                {
	                reader.close();
                }
                catch (IOException e)
                {
	                e.printStackTrace();
                }
			}
		}
	}
	
	/**
	 * Initializes default price tables.
	 */
	private void useDefault()
	{
		buyPrice = new HashMap<String, Integer>();
		buyPrice.put(ITEM + Material.COBBLESTONE, 1);
		buyPrice.put(ITEM + Material.STONE, 1);
		buyPrice.put(ITEM + Material.BRICK, 2);
		buyPrice.put(ITEM + Material.GOLD_BLOCK, 100);
		
		sellPrice = new HashMap<String, Integer>();
		sellPrice.put(ITEM + Material.COBBLESTONE, 1);
		sellPrice.put(ITEM + Material.BRICK, 1);
		sellPrice.put(ITEM + Material.GOLD_BLOCK, 50);
	}
	
	/**
	 * Loads the default price tables and saves them to a file.
	 */
	private void saveDefault()
	{
		this.useDefault();
		
		List<String> keys = new ArrayList<String>();
		for(Material mat : Material.values())
		{
			keys.add(ITEM + mat);
		}
		Collections.sort(keys);
		
		String header = "#Phernalia registry prices file\n" +
						"#Defaults generated " + new Date() + "\n" +
						"#The format for each line is: <itemtype>.<item> <buy> <sell>\n" +
						"#A buy price of -1 indicates that the item cannot be bought with grist, sell prices MUST be positive but may be 0.\n";
			
		BufferedWriter writer = null;
		try
		{
    		writer = new BufferedWriter(new FileWriter(REG_FILE));
    		writer.write(header);
    		for(String key : keys)
    		{
    			writer.write(key + " " + this.getBuyPrice(key) + " " + this.getSellPrice(key) + "\n");
    		}
		}
		catch(IOException e)
		{
			SburbPlayers.getInstance().getLogger().severe("Unable to save default registry file.");
			e.printStackTrace();
		}
		finally
		{
			try
            {
				writer.flush();
	            writer.close();
            }
            catch (IOException e)
            {
	            e.printStackTrace();
            }
		}
	}
	
	/**
     * @param key
     * @return
     */
    private int getBuyPrice(String key)
    {
    	Integer value = buyPrice.get(key);
		return value != null ? value : -1;
    }

	/**
	 * Provides the grist price for the given item.
	 * @param item The item to look up
	 * @return The cost of the item, in grist. If the cost is less than 0,
	 *         the item may not be bought using grist.
	 */
	public int getBuyPrice(Material item)
	{
		return getBuyPrice(ITEM + item);
	}
	
	/**
	 * Provides the grist price for the given machine.
	 * @param machine The machine to look up
	 * @return The cost of the machine, in grist. If the cost is less than 0,
	 * 		   the machine may not be bought using grist.
	 */
	public int getBuyPrice(Machine machine)
	{
		return 0;//TODO NYI
	}

	/**
     * @param key
     * @return
     */
    private int getSellPrice(String key)
    {
        Integer value = sellPrice.get(key);
        return value != null ? value : 0;
    }

	/**
	 * Returns the sale price of the given item.
     * @param type
     * @return
     */
    public int getSellPrice(Material type)
    {
	    return this.getSellPrice(ITEM + type);
    }

	/**
     * Returns a set of items that may be bought with grist.
     * @return The set of items.
     */
    public ItemStack[] getAvailableItems()
    {
    	Class type = Void.TYPE;
    	Set<String> keys = buyPrice.keySet();
    	List<ItemStack> items = new ArrayList<ItemStack>();
    	for(String key : keys)
    	{
    		String[] arr = key.split("\\.");
    		if(arr[0].equals("block"))
    			items.add(new ItemStack(Material.valueOf(arr[1])));
    	}
    	return items.toArray(new ItemStack[items.size()]);
    }
}
