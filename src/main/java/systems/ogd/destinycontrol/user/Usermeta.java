package systems.ogd.destinycontrol.user;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Usermeta {

    private UUID uuid;
    private int permissions;

    public Usermeta(List<Integer> dataBuf) {
        int ri = 0;

        int x;

        StringBuilder bobTheBuilder = new StringBuilder();

        while ((x = dataBuf.get(ri)) != 0b10010011) {
            bobTheBuilder.append((char) x);

            ri++;
        }

        ri++;

        uuid = UUID.fromString(bobTheBuilder.toString());
        permissions = dataBuf.get(ri);
    }

    public Usermeta(UUID uuid, int permissions){
        this.uuid = uuid;
        this.permissions = permissions;
    }

    public ArrayList<Integer> export(){
        ArrayList<Integer> buffer = new ArrayList<>();

        char[] uuid = getUuid().toString().toCharArray();

        for(char i : uuid){
            buffer.add((int) i);
        }

        buffer.add(0b10010011);

        buffer.add(permissions);
        buffer.add(0b10000001);

        return buffer;
    }
}

