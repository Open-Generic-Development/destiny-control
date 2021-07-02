package systems.ogd.destinycontrol;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import systems.ogd.destinycontrol.fs.FileSystem;
import systems.ogd.destinycontrol.log.LogUtils;
import systems.ogd.destinycontrol.log.ReservedLogger;

import java.util.logging.Level;

@Getter
public final class Destiny extends JavaPlugin {

    private FileSystem fs;
    private ReservedLogger log;

    public void onEnable() {
        // Plugin startup logic
        log = new ReservedLogger(getLogger());
        log.getBase().setLevel(Level.FINEST);

        initFs();
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

        LogUtils.servLog1(true);
    }
}
