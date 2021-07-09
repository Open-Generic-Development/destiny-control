package systems.ogd.destinycontrol;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import systems.ogd.destinycontrol.command.DestinyCommand;
import systems.ogd.destinycontrol.fs.FileSystem;
import systems.ogd.destinycontrol.log.LogUtils;
import systems.ogd.destinycontrol.log.ReservedLogger;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

@Getter
public final class Destiny extends JavaPlugin {

    @Getter
    private static Destiny destiny;

    private FileSystem fs;
    private ReservedLogger log;

    public void onEnable() {
        // Plugin startup logic
        destiny = this;

        log = new ReservedLogger(getLogger(), true); // TODO before merging
        log.getBase().setLevel(Level.FINEST);

        doCleanup();

        initFs();
        loadUserData();
        loadKingdomData();


        Objects.requireNonNull(getCommand("destiny")).setExecutor(new DestinyCommand());
    }

    private void doCleanup() {
        log.halt();
        LogUtils.servLog0("Cleaning Up");
        try {
            boolean firstStart = !(getDataFolder().exists() && new File(getDataFolder(), "data.bin").exists());

            if (firstStart){
                log.info(" » It appears that you just installed the plugin (or removed the config of)");
                log.info("    -- " + LogUtils.ANSI_CYAN + "DestinyContol" + LogUtils.ANSI_RESET + " --");
                log.info(" » by Chris B. | not sponsored by RAID Shadow Legends");
                log.info(" » Please remember to set the 'tab-complete' property in your spigot.yml file to -1");
                log.info(" » to prevent tabbing. Also please use /ctrl to set up your admin account!");

                log.debug(" » Whoah! You enabled Debug Mode at first start!");
                log.debug(" » Welcome back master!");

                if (!new File(getDataFolder(), "data.bin").exists()) {
                    log.debug(" » We will generate a »fresh« config just for you!");
                }else{
                    log.debug(" » WTF there is a file in a non-existing folder. Nevermind!");
                }

                log.debug(" » checking for abandoned teams");

                for (Team team : Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getTeams()){
                    if(team.getName().startsWith("KNG_")){
                        log.debug("Remove entries of " + team.getName());

                        for (String entry : team.getEntries()) {
                            team.removeEntry(entry);
                        }
                    }
                }

            }

        }catch (Exception e) {
            LogUtils.servLog1(false);
            log.release();
            log.exception(e);
        }
        LogUtils.servLog1(true);
        log.release();
    }

    private void initFs(){
        log.halt();
        LogUtils.servLog0("Initializing FS");
        try {
            fs = new FileSystem(this);
        }catch(Exception exception){
            LogUtils.servLog1(false);
            log.release();
            log.exception(exception);
            exception.printStackTrace();
            return;
        }

        log.release();

        LogUtils.servLog1(true);
    }

    private void loadUserData(){
        log.halt();
        LogUtils.servLog0("Loading User Data");
        try {
            fs.loadUserdata();
        }catch(Exception exception){
            LogUtils.servLog1(false);
            log.release();
            log.exception(exception);
            exception.printStackTrace();
            return;
        }

        log.release();

        LogUtils.servLog1(true);
    }

    private void loadKingdomData(){
        log.halt();
        LogUtils.servLog0("Loading Kingdom Data");
        try {
            fs.loadKingdoms();
        }catch(Exception exception){
            LogUtils.servLog1(false);
            log.release();
            log.exception(exception);
            exception.printStackTrace();
            return;
        }

        log.release();

        LogUtils.servLog1(true);
    }
}
