package systems.ogd.destinycontrol.fs;

import java.util.ArrayList;
import java.util.List;

public class FileBufferUtils {
    protected boolean isValid(ArrayList<Integer> buf){
        return buf.size() > 0;
    }

    protected List<List<Integer>> readSectors(ArrayList<Integer> buf){
        List<List<Integer>> sec = new ArrayList<>();

        List<Integer> subbuffer = new ArrayList<>();

        for(int c : buf){
            if(c == 0b10000000) {
                sec.add(subbuffer);
                subbuffer = new ArrayList<>();
            }else{
                subbuffer.add(c);
            }

        }

        return sec;
    }
}
