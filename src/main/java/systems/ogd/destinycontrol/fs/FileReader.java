package systems.ogd.destinycontrol.fs;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FileReader extends FileBufferUtils {
    private final ArrayList<Integer> buf;
    private final FileSystem fileSystem;

    private List<Integer> bufPart1 = new ArrayList<>();
    private List<Integer> bufPart2 = new ArrayList<>();

    public FileReader(ArrayList<Integer> buf, FileSystem fileSystem) {
        this.buf = buf;
        this.fileSystem = fileSystem;
    }

    public void start(){
        if(!isValid(buf)){
            writeInitial();

            fileSystem.save();
        }

        List<List<Integer>> sectors = readSectors(buf);

        bufPart1 = sectors.get(0);
        bufPart2 = sectors.get(1);
    }

    private void writeInitial() {
        this.buf.add(0b10000000);
        this.buf.add(0b10000000);
    }
}

/*
 * Prefixe
 * Permissions
 * /kingdom [new,accept,alter,invite,leave]
 * Custom Craft: Quarz Wayback
 *
 * 0 0 0 0 0 0 0 0
 *
 * Control -> 0: Normal, 1: Skip
 *
 */
