package systems.ogd.destinycontrol.utils;

import org.bukkit.entity.Player;
import systems.ogd.destinycontrol.Destiny;
import systems.ogd.destinycontrol.user.Usermeta;

import java.util.Optional;

public class PlayerUtils {
    public static Usermeta resolvePlayer(Player sender) {
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
}
