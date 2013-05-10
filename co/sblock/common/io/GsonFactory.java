package co.sblock.common.io;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * This class is used to generate a GsonBuilder that has special handling for specific Bukkit objects.
 * @author FireNG
 *
 */
public final class GsonFactory
{
	private static Gson gson;
	static
	{
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Location.class, new LocationTypeAdapter());
		builder.registerTypeAdapter(ItemStack.class, new ItemStackTypeAdapter());
		gson = builder.create();
	}
	public static Gson getGson()
	{
		return gson;
	}
	
	private static class LocationTypeAdapter implements JsonSerializer<Location>, JsonDeserializer<Location>
	{
		/* (non-Javadoc)
         * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
         */
        @Override
        public JsonElement serialize(Location loc, Type type, JsonSerializationContext context)
        {
        	JsonObject obj = new JsonObject();
        	obj.addProperty("world", loc.getWorld().getName());
        	obj.addProperty("x", loc.getBlockX());
        	obj.addProperty("y", loc.getBlockY());
        	obj.addProperty("z", loc.getBlockZ());
        	return obj;
        }
		
		/* (non-Javadoc)
         * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
         */
        @Override
        public Location deserialize(JsonElement element, Type type,JsonDeserializationContext context) throws JsonParseException
        {
        	JsonObject obj = element.getAsJsonObject();
        	World world = Bukkit.getWorld(obj.get("world").getAsString());
        	if(world != null)
        		return new Location(world, obj.get("x").getAsInt(), obj.get("y").getAsInt(), obj.get("z").getAsInt());
        	else
        		throw new IllegalArgumentException("World " + obj.get("world").getAsString() + " does not exist.");
	        
        }
		
	}
	
	private static class ItemStackTypeAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack>
	{

		private class SimpleEnchantment
		{
			int id, level;
			
			SimpleEnchantment(int id, int level)
			{
				this.id = id;
				this.level = level;
			}
		}
		/* (non-Javadoc)
         * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
         */
        @Override
        public JsonElement serialize(ItemStack item, Type type, JsonSerializationContext context)
        {
	        JsonObject obj = new JsonObject();
	        obj.addProperty("id", item.getTypeId());
	        obj.addProperty("amount", item.getAmount());
	        obj.addProperty("durability", item.getDurability());
	        obj.addProperty("data", item.getData().getData());
	        
	        JsonArray enchantments = new JsonArray();
	        for(Enchantment ench : item.getEnchantments().keySet())
	        {
	        	enchantments.add(context.serialize(new SimpleEnchantment(ench.getId(), item.getEnchantments().get(ench))));
	        }
	        
	        obj.add("enchantments", enchantments);

	        return obj;
        }

		/* (non-Javadoc)
         * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
         */
        @Override
        public ItemStack deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException
        {
	        JsonObject obj = element.getAsJsonObject();
	        ItemStack item = new ItemStack(obj.get("id").getAsInt(), obj.get("amount").getAsInt(), obj.get("durability").getAsShort());
	        item.setData(new MaterialData(item.getType(), obj.get("data").getAsByte()));
	        Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
	        JsonArray arr = obj.get("enchantments").getAsJsonArray();
	        for(JsonElement elem : arr)
	        {
	        	SimpleEnchantment enchant = context.deserialize(elem, SimpleEnchantment.class);
	        	enchantments.put(Enchantment.getById(enchant.id), enchant.level);
	        }
	        item.addEnchantments(enchantments);
	        return item;
        }
		
	}
}
