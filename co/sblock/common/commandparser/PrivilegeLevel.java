package co.sblock.common.commandparser;

/**
 * Represents a privilege level.
 * @author FireNG
 *
 */
public enum PrivilegeLevel 
{
	/**
	 * Operation with this privilege level may be performed by any user.
	 */
	ALL("everyone"),
	/**
	 * Operation with this privilege level is restricted to moderators only.
	 */
	MODSONLY("moderators only"),
	/**
	 * No user may perform this operation.
	 */
	NONE("nobody");
	
	private String group;
	
	private PrivilegeLevel(String group) { this.group = group; }
	
	public String group() { return group; }
}
