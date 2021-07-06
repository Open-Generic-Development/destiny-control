package systems.ogd.destinycontrol.kingdoms;

import lombok.Getter;
import lombok.Setter;
import systems.ogd.destinycontrol.Destiny;
import systems.ogd.destinycontrol.user.Usermeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Kingdom {
    private String name;
    private char color;
    private ArrayList<Usermeta> users = new ArrayList<>();
    private Usermeta leader;

    public Kingdom(List<Integer> dataBuf) {
        int ri = 0;
        StringBuilder bobTheBuilder = new StringBuilder();
        int x;

        while ((x = dataBuf.get(ri)) != 0b10000010) {
            bobTheBuilder.append((char) x);

            ri++;
        }

        name = bobTheBuilder.toString();

        ri++;

        color = (char) (int) dataBuf.get(ri);

        ri++;

        do {
            Destiny.getDestiny().getLog().debug("Waiting for Sequence 10000100");
            bobTheBuilder = new StringBuilder();

            while ((x = dataBuf.get(ri)) != 0b10000011) {
                bobTheBuilder.append((char) x);

                ri++;
            }

            ri++;

            UUID uuid = UUID.fromString(bobTheBuilder.toString());

            Destiny.getDestiny().getLog().debug("Read String UUID finished");
            Destiny.getDestiny().getLog().debug(" » Raw:    '" + bobTheBuilder + "'");
            Destiny.getDestiny().getLog().debug(" » Parsed: '" + uuid + "'");

            for (Usermeta user : Destiny.getDestiny().getFs().getUserdata()) {
                Destiny.getDestiny().getLog().debug("Comparing User Unique IDs");
                Destiny.getDestiny().getLog().debug(" » local {" + uuid + "}");
                Destiny.getDestiny().getLog().debug(" » remote{" + user.getUuid() + "}");
                if (user.getUuid().toString().equals(uuid.toString())) {
                    Destiny.getDestiny().getLog().debug(" » TRUE");
                    users.add(user);
                }else
                    Destiny.getDestiny().getLog().debug(" » FALSE");
            }
        }
        while (dataBuf.get(ri) != 0b10000100);

        ri++;

        leader = users.get(dataBuf.get(ri));
    }

    public Kingdom(String name, char color, ArrayList<Usermeta> users, Usermeta leader) {
        this.name = name;
        this.color = color;
        this.users = users;
        this.leader = leader;
    }

    public ArrayList<Integer> export() {
        ArrayList<Integer> buffer = new ArrayList<>();

        for (char c : name.toCharArray()) {
            buffer.add((int) c);
        }

        buffer.add(0b10000010);

        buffer.add((int) color);

        for (Usermeta user : users) {
            char[] uuid = user.getUuid().toString().toCharArray();

            for (char i : uuid) {
                buffer.add((int) i);
            }

            buffer.add(0b10000011);
        }

        buffer.add(0b10000100);

        buffer.add(users.indexOf(leader));

        buffer.add(0b10000001);

        return buffer;
    }
}
