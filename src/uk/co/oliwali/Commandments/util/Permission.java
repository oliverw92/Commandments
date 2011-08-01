package uk.co.oliwali.Commandments.util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import uk.co.oliwali.Commandments.Commandments;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * Permissions handler
 * Supports multiple permissions systems
 * @author oliverw92
 */
public class Permission {
	
	private Commandments plugin;
	private static PermissionPlugin handler = PermissionPlugin.OP;
	private static PermissionHandler permissionPlugin;
	
	/**
	 * Check permissions plugins, deciding which one to use
	 * @param instance
	 */
	public Permission(Commandments instance) {
		plugin = instance;
        Plugin permissions = plugin.getServer().getPluginManager().getPlugin("Permissions");
        
        if (permissions != null) {
        	permissionPlugin = ((Permissions)permissions).getHandler();
        	handler = PermissionPlugin.PERMISSIONS;
        	Util.info("Using Permissions for user permissions");
        }
        else {
        	Util.info("No permission handler detected, only ops can use commands");
        }
	}
	
	/**
	 * Private method for checking a users permission level.
	 * Permission checks from other classes should go through a separate method for each node.
	 * @param sender
	 * @param node
	 * @return true if the user has permission, false if not
	 */
	private static boolean hasPermission(CommandSender sender, String node) {
		if (!(sender instanceof Player))
			return true;
		Player player = (Player)sender;
		switch (handler) {
			case PERMISSIONS:
				return permissionPlugin.has(player, node);
			case OP:
				return player.isOp();
		}
		return false;
	}
	
	/**
	 * Permission to be notified of rule breaks
	 * @param player
	 * @return
	 */
	public static boolean notify(CommandSender player) {
		return hasPermission(player, "commandments.notify");
	}
	
	/**
	 * Check if a player is in a group
	 * @param world
	 * @param player
	 * @param group
	 * @return
	 */
	public static boolean inSingleGroup(String world, String player, String group) {
		switch (handler) {
			case PERMISSIONS:
				return permissionPlugin.inSingleGroup(world, player, group);
		}
		return false;
	}
	
	/**
	 * Enumeration containing supported permission systems
	 * @author oliverw92
	 */
	private enum PermissionPlugin {
		PERMISSIONS,
		OP
	}

}
