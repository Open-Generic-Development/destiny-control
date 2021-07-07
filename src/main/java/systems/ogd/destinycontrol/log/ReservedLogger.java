package systems.ogd.destinycontrol.log;


import lombok.Getter;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservedLogger {
    @Getter
    private final Logger base;
    private boolean debug;

    @Getter
    private boolean released = true;

    private final ArrayList<LogEntry> messages = new ArrayList<>();

    public ReservedLogger(Logger base, boolean debug) {
        this.base = base;
        this.debug = debug;
    }

    public void log(Level level, String msg) {
        if (released) {
            base.log(level, msg);
        } else {
            messages.add(new LogEntry(level, msg));
        }
    }

    public void halt() {
        released = false;
    }

    public void release() {
        released = true;

        for (LogEntry entry : messages) {
            base.log(entry.getLevel(), entry.getMsg());
        }

        messages.clear();
    }

    public void debug(String msg) {
        if (debug)
            log(Level.INFO, "[" + LogUtils.ANSI_BLUE + "DEBUG" + LogUtils.ANSI_RESET  + "] " +  msg);
    }

    public void info(String msg) {
        log(Level.INFO, msg);
    }

    public void warn(String msg) {
        log(Level.WARNING, msg);
    }

    public void exception(Exception exc) {
        log(Level.SEVERE,
                "An " + LogUtils.ANSI_RED + "Error" + LogUtils.ANSI_RESET + " occured: " + exc.toString());
    }

    @Getter
    private static class LogEntry {
        private final Level level;
        private final String msg;

        public LogEntry(Level level, String msg) {

            this.level = level;
            this.msg = msg;
        }
    }
}
