package com.minesnap.restarter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Handler for the /rsset command.
 */
public class RSSetCommand implements CommandExecutor {
    private final RestarterPlugin plugin;

    public RSSetCommand(RestarterPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length != 1)
            return false;

        int minutes;
        try {
            minutes = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            sender.sendMessage("Could not parse number of minutes.");
            return false;
        }

        plugin.scheduleRestart(minutes);
        if(minutes == 1) {
            sender.sendMessage("Restart scheduled 1 minute from now.");
        } else {
            sender.sendMessage("Restart scheduled "+minutes+" minutes from now.");
        }
        return true;
    }
}
