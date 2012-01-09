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
        int minutesLeft = plugin.getMinutesToRestartLeft();
        if(minutesLeft == 1) {
            sender.sendMessage("The next restart is in 1 minute.");
        } else {
            sender.sendMessage("The next restart is in "+minutesLeft+" minutes.");
        }
        return true;
    }
}
