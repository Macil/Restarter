package com.minesnap.restarter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Handler for the /rsdelay command.
 */
public class RSQueryCommand implements CommandExecutor {
    private final RestarterPlugin plugin;

    public RSQueryCommand(RestarterPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        sender.sendMessage("/rsquery received");
        return true;
    }
}
