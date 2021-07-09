package systems.ogd.destinycontrol.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import systems.ogd.destinycontrol.Destiny;
import systems.ogd.destinycontrol.user.Usermeta;
import systems.ogd.destinycontrol.utils.MessageUtils;

import java.util.Locale;
import java.util.Optional;

public class DestinyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return executeCommand(resolvePlayer((Player) sender), label.toLowerCase(Locale.ROOT), args);
    }

    private Usermeta resolvePlayer(Player sender) {
        Destiny.getDestiny().getLog().debug(" » Resolving User " + sender.getDisplayName());

        Optional<Usermeta> optionalUsermeta = Destiny.getDestiny().getFs().getUserdata().stream().filter(
                usermeta1 -> usermeta1.getUuid().toString().equals(sender.getUniqueId().toString())).findFirst();

        if (!optionalUsermeta.isPresent()) {
            Destiny.getDestiny().getLog().debug(" » User not present in config!");
            return null;
        }

        Destiny.getDestiny().getLog().debug(" » User present in config.");

        return optionalUsermeta.get();
    }

    private boolean executeCommand(Usermeta sender, String label, String[] args) {
        if (label.equals("help") || label.equals("ver") || label.equals("version") || label.equals("icanhasbukkit")){
            sendHelpMessage(sender);
            return true;
        }

        return false;
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
