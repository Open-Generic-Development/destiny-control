package systems.ogd.destinycontrol;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import systems.ogd.destinycontrol.fs.FileSystem;
import systems.ogd.destinycontrol.log.LogUtils;
import systems.ogd.destinycontrol.log.ReservedLogger;

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

        initFs();
        loadUserData();
        loadKingdomData();
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
