package co.sblock.sburbchat.channel.channels;

import java.util.Set;

import co.sblock.common.commandparser.PrivilegeLevel;
import co.sblock.sburbchat.User;
import co.sblock.sburbchat.channel.AccessLevel;
import co.sblock.sburbchat.channel.ChannelType;


public interface Channel
{
	public String getName();
	public String getPrefix(User sender);
	public String getJoinChatMessage(User sender);
	public String getLeaveChatMessage(User sender);
	public AccessLevel getSAcess();
	public AccessLevel getLAcess();
	public Set<User> getUsers();
	public ChannelType getType();
	
	public void addAlias(String name, User sender);
	public void removeAlias(String name, User sender);
	
	public boolean userJoin(User sender);
	public void userLeave(User sender);
	
	/**
	 * Sends a single message to this channel.
	 * @param message Message to display
	 * @param sender User that originated the message
	 * @author FireNG
	 */
	public void setChat(String message, User sender);
	
	public void setNick(String nick, User sender);
	public void removeNick(User sender);
	
	public void setOwner(String name, User sender);
	public void addMod(User user, User sender);
	public void addMod(String user);
	public void removeMod(User user, User sender);
	
	public void kickUser(User user, User sender);
	public void banUser(User user, User sender);
	public void unbanUser(User user, User sender);
	public void muteUser(User user, User sender);
	public void unmuteUser(User user, User sender);
	public void approveUser(User user, User sender);
	public void deapproveUser(User user, User sender);
	
	public void disband(User sender);
	
	/**
	 * Sets who is allowed to use chat colors in this channel
	 * @param level Access level to use.
	 * @author FireNG
	 */
	public void setColorAccess(PrivilegeLevel level, User user);
	
	public void makeSerializable();
	public void makeUsable();
}

