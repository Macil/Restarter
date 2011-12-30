package com.minesnap.restarter;

import java.util.logging.Logger;
import java.util.Random;

import org.bukkit.command.PluginCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandException;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

/**
 * Restarter plugin for Bukkit
 *
 * @author AgentME
 */
public class RestarterPlugin extends JavaPlugin {
    static final Logger logger = Logger.getLogger("Minecraft.Restarter");
    PluginDescriptionFile pdfFile;
    BukkitScheduler scheduler;

    private int minutesToRestart;
    private static final int minutesToRestartDefault = 80;

    private int variance;
    private static final int varianceDefault = 0;

    private static final int TICS_PER_SECOND = 20;
    private static final String warnMessage = "Server is having a scheduled restart in one minute.";
    private static final String restartMessage = "The server is restarting and will be back in a moment!";

    private final RestarterPlugin plugin = this;

    public void onDisable() {
        // NOTE: All registered events are automatically unregistered
        // when a plugin is disabled
        logger.info("["+pdfFile.getName()+"] Disabled.");
    }

    public void onEnable() {
        // Register our commands
        PluginCommand querycmd = getCommand("rsquery");
        querycmd.setPermission("restarter.query");
        querycmd.setExecutor(new RSQueryCommand(this));

        PluginCommand delaycmd = getCommand("rsset");
        delaycmd.setPermission("restarter.set");
        delaycmd.setExecutor(new RSSetCommand(this));

        pdfFile = getDescription();
        logger.info("["+pdfFile.getName()+"] v"+pdfFile.getVersion()+" enabled.");

        scheduler = getServer().getScheduler();

        // Config stuff
        getConfig().options().copyDefaults(true);
        saveConfig();

        minutesToRestart = getConfig().getInt("minutesToRestart");
        variance = getConfig().getInt("variance");

        if(minutesToRestart <= 1) {
            minutesToRestart = minutesToRestartDefault;
            logger.severe("["+pdfFile.getName()+"] minutesToRestart value too low! Using default.");
        }

        if(variance < 0 || minutesToRestart - variance <= 1) {
            variance = varianceDefault;
            logger.severe("["+pdfFile.getName()+"] variance value is bad! Using default.");
        }

        // Apply variance. The new value of minutesToRestart will be
        // in the range of minutesToRestart ± variance.
        Random rand = new Random();
        minutesToRestart = minutesToRestart-variance + rand.nextInt(2*variance+1);

        logger.info("["+pdfFile.getName()+"] Restart scheduled in "+
                    minutesToRestart+" minutes.");
        scheduler.scheduleSyncDelayedTask(plugin, new RestartWarner(),
                                          TICS_PER_SECOND*60*(minutesToRestart-1));
    }

    private class RestartWarner implements Runnable {
        public void run() {
            getServer().broadcastMessage(ChatColor.RED+warnMessage);
            logger.info("["+pdfFile.getName()+"] "+warnMessage);

            scheduler.scheduleSyncDelayedTask(plugin, new Restarter(),
                                              TICS_PER_SECOND*60*1);
	}
    }

    private class Restarter implements Runnable {
        public void run() {
            stopServer();
        }
    }

    public boolean stopServer() {
        // log it and empty out the server first
        logger.info("["+pdfFile.getName()+"] Restarting...");
        clearServer(restartMessage);
        try {
            ConsoleCommandSender sender = getServer().getConsoleSender();
            getServer().dispatchCommand(sender, "save-all");
            getServer().dispatchCommand(sender, "stop");
        } catch (CommandException e) {
            logger.info("["+pdfFile.getName()+"] Something went wrong!");
            return false;
        }
        return true;
    }

    public void clearServer(String message) {
        getServer().broadcastMessage(ChatColor.RED+message);
        for (Player player : getServer().getOnlinePlayers()) {
            player.kickPlayer(message);
        }
    }
}
