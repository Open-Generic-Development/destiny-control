package systems.ogd.destinycontrol.fs;

import lombok.Getter;
import systems.ogd.destinycontrol.Destiny;
import systems.ogd.destinycontrol.kingdoms.Kingdom;
import systems.ogd.destinycontrol.user.Usermeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            Destiny.getDestiny().getLog().debug(" » Buffer Invalid");

            writeInitial();

            Destiny.getDestiny().getLog().debug(" » Saving");

            fileSystem.save();
        }else
            Destiny.getDestiny().getLog().debug(" » Buffer Valid");

        Destiny.getDestiny().getLog().debug(" » Analyzing Data");

        List<List<Integer>> sectors = readSectors(buf);

        bufPart1 = sectors.get(0);
        bufPart2 = sectors.get(1);
    }

    private void writeInitial() {
        Destiny.getDestiny().getLog().debug(" » Generating example data");

        Usermeta user = new Usermeta(UUID.randomUUID(), 42);

        ArrayList<Usermeta> users = new ArrayList<>();
        users.add(user);

        Destiny.getDestiny().getLog().debug(" » Writing Data to Cache");

        getBufPart2().addAll(user.export());
        getBufPart1().addAll(new Kingdom("Name", 'A', users, user).export());

        this.buf.addAll(getBufPart1());

        this.buf.add(0b10000000);

        this.buf.addAll(getBufPart2());
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
