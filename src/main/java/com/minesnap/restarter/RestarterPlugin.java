package com.minesnap.restarter;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Restarter plugin for Bukkit
 *
 * @author AgentME
 */
public class RestarterPlugin extends JavaPlugin {
    static final Logger logger = Logger.getLogger("Minecraft.Restarter");
    PluginDescriptionFile pdfFile;

    public void onDisable() {
        // NOTE: All registered events are automatically unregistered
        // when a plugin is disabled
        logger.info(pdfFile.getName()+" disabled.");
    }

    public void onEnable() {
        // Register our commands
        getCommand("rsquery").setExecutor(new RSQueryCommand(this));
        getCommand("rsdelay").setExecutor(new RSDelayCommand(this));

        pdfFile = getDescription();
        logger.info(pdfFile.getName()+" version "+pdfFile.getVersion()+" is enabled!");
    }
}
