package systems.ogd.destinycontrol.fs.exceptions;

public class CriticalFileError extends RuntimeException{
    public CriticalFileError(String msg){
        super(msg);
    }

    public CriticalFileError(){
        super();
    }
}
