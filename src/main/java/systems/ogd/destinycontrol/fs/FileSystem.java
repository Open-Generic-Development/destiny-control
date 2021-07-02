package systems.ogd.destinycontrol.fs;

import lombok.Getter;
import systems.ogd.destinycontrol.Destiny;
import systems.ogd.destinycontrol.fs.exceptions.CriticalFileError;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

@Getter
public class FileSystem {
    private final File file;
    private final Destiny destinycontrol;
    private final FileReader fileReader;

    public FileSystem(Destiny destinycontrol) throws IOException {
        this.destinycontrol = destinycontrol;

        File dataFolder = this.destinycontrol.getDataFolder();

        if (dataFolder.mkdirs()) {
            this.destinycontrol.getLog().info("Created new Data Folder");
        }

        file = new File(dataFolder, "data.bin");

        if (!file.exists()) {
            if (!file.createNewFile()) {
                this.destinycontrol.getLog().exception(new CriticalFileError("Cannot create Binary file"));
            }
        }


        try {
            // create a reader
            FileInputStream fis = new FileInputStream(this.file);

            // read one byte at a time
            int read;

            ArrayList<Integer> bytes = new ArrayList<>();

            while ((read = fis.read()) != -1) {
                bytes.add(read);
            }

            // close the reader
            fis.close();

            fileReader = new FileReader(bytes, this);
            fileReader.start();

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new CriticalFileError();
        }

        // throw new CriticalFileError();
    }

    public void save() {
        try {
            // create a writer
            FileOutputStream fos = new FileOutputStream(getFile());

            fileReader.getBufPart1().add(0b00001111);

            // write data to file
            for (int c : fileReader.getBufPart1()) {
                fos.write(c);
            }

            fos.write(0b10000000);

            for (int c : fileReader.getBufPart2()) {
                fos.write(c);
            }

            fos.write(0b10000000);

            // close the writer
            fos.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new CriticalFileError();
        }

    }
}
