package uk.co.oliwali.Commandments.util;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

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
	private static PermissionPlugin handler = PermissionPlugin.BUKKITPERMS;
	private static PermissionHandler permissionPlugin;
	private static PermissionManager permissionsEx;
	
	/**
	 * Check permissions plugins, deciding which one to use
	 * @param instance
	 */
	public Permission(Commandments instance) {
		plugin = instance;
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
        	handler = PermissionPlugin.PERMISSIONSEX;
        	permissionsEx = PermissionsEx.getPermissionManager();
        	Util.info("Using PermissionsEx for user permissions");
		}
        else if (Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
        	permissionPlugin = ((Permissions)plugin.getServer().getPluginManager().getPlugin("Permissions")).getHandler();
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
			case PERMISSIONSEX:
				return permissionsEx.has(player, node);
			case PERMISSIONS:
				return permissionPlugin.has(player, node);
			case BUKKITPERMS:
				return player.hasPermission(node);
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
	public static boolean inGroup(World world, Player player, String group) {
		return inGroup(world.getName(), player.getName(), group);
	}
	public static boolean inGroup(String world, String player, String group) {
		switch (handler) {
			case PERMISSIONSEX:
				return permissionsEx.getUser(player).inGroup(group);
			case PERMISSIONS:
				return permissionPlugin.inGroup(world, player, group);
		}
		return false;
	}
	
	/**
	 * Enumeration containing supported permission systems
	 * @author oliverw92
	 */
	private enum PermissionPlugin {
		PERMISSIONSEX,
		PERMISSIONS,
		BUKKITPERMS
	}

}
