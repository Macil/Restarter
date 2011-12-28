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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        sender.sendMessage("/rsset received");
        return true;
    }
}
