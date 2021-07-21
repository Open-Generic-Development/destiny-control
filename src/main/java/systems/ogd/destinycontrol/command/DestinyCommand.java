package systems.ogd.destinycontrol.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import systems.ogd.destinycontrol.UserInterface;
import systems.ogd.destinycontrol.user.Usermeta;
import systems.ogd.destinycontrol.utils.MessageUtils;

import java.util.Locale;
import java.util.Objects;

import static systems.ogd.destinycontrol.utils.PlayerUtils.resolvePlayer;

public class DestinyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return executeCommand(resolvePlayer((Player) sender), label.toLowerCase(Locale.ROOT), args);
    }

    private boolean executeCommand(Usermeta sender, String label, String[] args) {
        if (label.equals("help") || label.equals("ver") || label.equals("version") || label.equals("icanhasbukkit")){
            sendHelpMessage(sender);
            return true;
        }

        Player player = Bukkit.getPlayer(sender.getUuid());
        new UserInterface(Objects.requireNonNull(player), sender);

        return true;
    }

    private void sendHelpMessage(Usermeta sender) {
        Player player = Bukkit.getPlayer(sender.getUuid());

        if(sender.getPermissions() >= 0){
            assert player != null;
            MessageUtils.sendMessage(player, "Hello! Welcome to Minecraft Destiny!");
            MessageUtils.sendMessage(player, "To get started use /ctrl or /destiny\n");
            MessageUtils.sendMessage(player, "This server runs on git+Paper1.17");
            MessageUtils.sendMessage(player, "Active Resource Handlers [3]: Vanilla, CavesAndCliffs, DestinyContol");
        }

        if(sender.getPermissions() >= 30){
            assert player != null;
            MessageUtils.sendMessage(player, "Welcome back Kanrisha!");
        }
    }
}
