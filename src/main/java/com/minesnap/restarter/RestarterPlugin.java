package com.minesnap.restarter;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;

import org.bukkit.command.PluginCommand;
import org.bukkit.command.CommandSender;
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
    BukkitScheduler scheduler;

    private int minutesToRestart;
    private int variance;

    private Timer timer;
    private Calendar warnTime;
    private Calendar restartTime;

    private static final int TICS_PER_SECOND = 20;
    private String warnMessage;
    private String kickMessage;

    private final RestarterPlugin plugin = this;

    public void onDisable() {
        timer.cancel();
        timer = null;
    }

    public void onEnable() {
        // Register our commands
        getCommand("rsquery").setExecutor(new RSQueryCommand(this));
        getCommand("rsset").setExecutor(new RSSetCommand(this));

        scheduler = getServer().getScheduler();

        // Config stuff
        getConfig().options().copyDefaults(true);
        saveConfig();

        minutesToRestart = getConfig().getInt("minutesToRestart");
        variance = getConfig().getInt("variance");
        warnMessage = getConfig().getString("warnMessage");
        kickMessage = getConfig().getString("kickMessage");

        if(minutesToRestart <= 1) {
            minutesToRestart = 80;
            getLogger().severe("minutesToRestart value too low! Using default.");
        }

        if(variance < 0 || minutesToRestart - variance <= 1) {
            variance = 0;
            getLogger().severe("variance value is bad! Using default.");
        }

        // Apply variance. The new value of minutesToRestart will be
        // in the range of minutesToRestart ± variance.
        Random rand = new Random();
        minutesToRestart = minutesToRestart-variance + rand.nextInt(2*variance+1);

        scheduleRestart(minutesToRestart);
    }

    public void scheduleRestart(int minutes) {
        if(timer != null) {
            timer.cancel();
        }
        timer = new Timer(true);

        if(minutes >= 1) {
            warnTime = Calendar.getInstance();
            warnTime.add(Calendar.MINUTE, minutes-1);
            timer.schedule(new RestartWarner(), warnTime.getTime());

            restartTime = (Calendar)warnTime.clone();
            restartTime.add(Calendar.MINUTE, 1);
        } else {
            warnTime = null;

            restartTime = Calendar.getInstance();
            restartTime.add(Calendar.MINUTE, minutes);
        }

        timer.schedule(new Restarter(), restartTime.getTime());

        getLogger().info("Restart scheduled in "+minutes+" minutes.");
    }

    private class RestartWarner extends TimerTask {
        public void run() {
            scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    getLogger().info("Server restart in one minute.");
                    getServer().broadcastMessage(ChatColor.RED+warnMessage);
                }
            });
	}
    }

    private class Restarter extends TimerTask {
        public void run() {
            scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    stopServer();
                }
            });
        }
    }

    public boolean stopServer() {
        // log it and empty out the server first
        getLogger().info("Restarting...");
        clearServer(kickMessage);
        getServer().shutdown();
        return true;
    }

    public void clearServer(String message) {
        getServer().broadcastMessage(ChatColor.RED+message);
        for (Player player : getServer().getOnlinePlayers()) {
            player.kickPlayer(message);
        }
    }

    /* Returns the minutesToRestart value that was read from config,
     * regardless of whether it's been changed in-game */
    public int getMinutesToRestartConfig() {
        return minutesToRestart;
    }

    /* Returns the number of minutes until the next restart from
     * now. */
    public int getMinutesToRestartLeft() {
        Calendar now = Calendar.getInstance();
        long milliDiff = restartTime.getTime().getTime()-now.getTime().getTime();
        return (int)(milliDiff / (1000*60));
    }
}
