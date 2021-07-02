package systems.ogd.destinycontrol.kingdoms;

import lombok.Getter;
import systems.ogd.destinycontrol.Destiny;
import systems.ogd.destinycontrol.user.Usermeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Kingdom {
    private final String name;
    private final char color;
    private final ArrayList<Usermeta> users = new ArrayList<>();
    private final Usermeta leader;

    public Kingdom(List<Integer> dataBuf) {
        int ri = 0;
        StringBuilder bobTheBuilder = new StringBuilder();
        int x;

        while ((x = dataBuf.get(ri)) != 0b10000010){
            bobTheBuilder.append((char) x);

            ri++;
        }

        name = bobTheBuilder.toString();

        ri++;

        color = (char) (int) dataBuf.get(ri);

        ri++;

        while (dataBuf.get(ri) != 0b10000100){
            bobTheBuilder = new StringBuilder();

            while ((x = dataBuf.get(ri)) != 0b10000011){
                bobTheBuilder.append((char) x);

                ri++;
            }

            ri++;

            UUID uuid = UUID.fromString(bobTheBuilder.toString());

            for (Usermeta user : Destiny.getDestiny().getFs().getUserdata()) {
                if(user.getUuid() == uuid){
                    users.add(user);
                }
            }
        }

        ri++;

        leader = users.get(dataBuf.get(ri));
    }
}
