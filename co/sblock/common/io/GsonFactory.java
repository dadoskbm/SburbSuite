package co.sblock.common.io;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

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
	        //return new JsonPrimitive(String.format("%s,%d,%d,%d", loc.getWorld().getName(),loc.getBlockX(),loc.getBlockY(),loc.getBlockZ()));
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
        		
//	        String[] raw = element.getAsString().split(",");
//	        World world = Bukkit.getWorld(raw[0]);
//	        if(world != null)
//	        	return new Location(world, Integer.parseInt(raw[1]), Integer.parseInt(raw[2]), Integer.parseInt(raw[3]));
//	        else
//	        	throw new IllegalArgumentException("World " + raw[0] + " does not exist.");
	        
        }
		
	}
	
	private static class ItemStackTypeAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack>
	{

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
	        obj.add("enchantments", context.serialize(item.getEnchantments()));

	        return obj;
        }

		/* (non-Javadoc)
         * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
         */
        @SuppressWarnings("unchecked")
        @Override
        public ItemStack deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException
        {
	        JsonObject obj = element.getAsJsonObject();
	        ItemStack item = new ItemStack(obj.get("id").getAsInt(), obj.get("amount").getAsInt(), obj.get("durability").getAsShort());
	        item.setData(new MaterialData(item.getType(), obj.get("data").getAsByte()));
	        item.addEnchantments((Map<Enchantment, Integer>)context.deserialize(obj.get("enchantments"), Map.class));
	        return item;
        }
		
	}
}
