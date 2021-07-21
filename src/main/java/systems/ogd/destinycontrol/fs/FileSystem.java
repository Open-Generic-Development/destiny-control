package systems.ogd.destinycontrol.fs;

import lombok.Getter;
import systems.ogd.destinycontrol.Destiny;
import systems.ogd.destinycontrol.fs.exceptions.CriticalFileError;
import systems.ogd.destinycontrol.kingdoms.Kingdom;
import systems.ogd.destinycontrol.user.Usermeta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class FileSystem {
    private final File file;
    private final Destiny destinycontrol;
    private final FileReader fileReader;

    private final ArrayList<Kingdom> kingdoms = new ArrayList<>();
    private final ArrayList<Usermeta> userdata = new ArrayList<>();

    public FileSystem(Destiny destinycontrol) throws IOException {
        this.destinycontrol = destinycontrol;

        Destiny.getDestiny().getLog().debug("Creating File Structure");

        File dataFolder = this.destinycontrol.getDataFolder();

        if (dataFolder.mkdirs()) {
            this.destinycontrol.getLog().info("Created new Data Folder");
        }else
            Destiny.getDestiny().getLog().debug(" » Data Folder exists");

        file = new File(dataFolder, "data.bin");

        if (!file.exists()) {
            if (!file.createNewFile()) {
                this.destinycontrol.getLog().exception(new CriticalFileError("Cannot create Binary file"));
            }else
                Destiny.getDestiny().getLog().debug(" » New file created");
        }else
            Destiny.getDestiny().getLog().debug(" » Binary File exists");


        try {
            Destiny.getDestiny().getLog().debug(" » Writing initial Data");

            // create a reader
            FileInputStream fis = new FileInputStream(this.file);

            // read one byte at a time
            int read;

            Destiny.getDestiny().getLog().debug(" » Creating and Reading into Buffer");

            ArrayList<Integer> bytes = new ArrayList<>();

            while ((read = fis.read()) != -1) {
                bytes.add(read);
            }

            Destiny.getDestiny().getLog().debug(" » Close File Handle");

            // close the reader
            fis.close();

            Destiny.getDestiny().getLog().debug(" » Analyze Buffer");

            fileReader = new FileReader(bytes, this);
            fileReader.start();

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new CriticalFileError();
        }

    }

    public void save() {
        try {
            // create a writer
            FileOutputStream fos = new FileOutputStream(getFile());

            fileReader.getBufPart1().add(0b00001111);

            Destiny.getDestiny().getLog().debug(" » Writing Data to File");

            // write data to file
            for (int c : fileReader.getBufPart1()) {
                fos.write(c);
            }

            fos.write(0b10000000);

            for (int c : fileReader.getBufPart2()) {
                fos.write(c);
            }

            fos.write(0b10000000);

            Destiny.getDestiny().getLog().debug(" » Close the stream");

            // close the writer
            fos.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new CriticalFileError();
        }

    }

    public void loadKingdoms() {
        kingdoms.clear();

        // Prepare Kingdoms
        List<Integer> buf1 = fileReader.getBufPart1();
        List<Integer> dataBuf = new ArrayList<>();

        for(int c : buf1){
            if(c == 0b10000001){
                kingdoms.add(new Kingdom(dataBuf));
                dataBuf.clear();
            }else{
                dataBuf.add(c);
            }
        }

    }

    public void loadUserdata() {
        userdata.clear();

        // Prepare Kingdoms
        List<Integer> buf2 = fileReader.getBufPart2();
        List<Integer> dataBuf = new ArrayList<>();

        for(int c : buf2){
            if(c == 0b10000001){
                userdata.add(new Usermeta(dataBuf));
                dataBuf.clear();
            }else{
                dataBuf.add(c);
            }
        }
    }

    public void exportLiveData() {
        for (Usermeta user : userdata) {
            fileReader.getBufPart2().addAll(user.export());
        }

        for (Kingdom kingdom : kingdoms) {
            fileReader.getBufPart1().addAll(kingdom.export());
        }
    }
}
