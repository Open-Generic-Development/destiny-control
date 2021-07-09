package systems.ogd.destinycontrol.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtils {
    public static void sendMessage(Player player, String message){
        player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Destiny" + ChatColor.GRAY + "] " + message);
    }
}
