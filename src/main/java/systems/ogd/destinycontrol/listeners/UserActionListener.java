package systems.ogd.destinycontrol.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import systems.ogd.destinycontrol.Destiny;
import systems.ogd.destinycontrol.user.Usermeta;

import static systems.ogd.destinycontrol.utils.PlayerUtils.resolvePlayer;

public class UserActionListener implements Listener {
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        if(event.getMessage().contains(":")) event.setMessage("/help");
        if(event.getMessage().startsWith("/?")) event.setMessage("/help");
        if(event.getMessage().startsWith("/ver")) event.setMessage("/help");
        if(event.getMessage().startsWith("/version")) event.setMessage("/help");
        if(event.getMessage().startsWith("/icanhasbukkit")) event.setMessage("/help");
        if(event.getMessage().startsWith("/pl")) event.setMessage("/help");
        if(event.getMessage().startsWith("/plugins")) event.setMessage("/help");
        if(event.getMessage().startsWith("/i")) event.setMessage("/help");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Destiny.getDestiny().getLog().debug("Loading Userdata for user " + event.getPlayer().getDisplayName());
        Usermeta user = resolvePlayer(event.getPlayer());

        if(user == null){
            Destiny.getDestiny().getLog().debug(" » Creating new Entry with permission level 0");
            user = new Usermeta(event.getPlayer().getUniqueId(), 0);

            Destiny.getDestiny().getLog().debug(" » Altering User Table and saving");
            Destiny.getDestiny().getFs().getUserdata().add(user);
            Destiny.getDestiny().getFs().exportLiveData();
            Destiny.getDestiny().getFs().save();
        }
    }
}
