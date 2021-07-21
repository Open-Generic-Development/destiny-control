package systems.ogd.destinycontrol.kingdoms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KingdomInvitation {
    private String name;
    private int kingdomId;
    private String invitator;

    public KingdomInvitation(String name, int kingdomId, String invitator) {
        this.name = name;
        this.kingdomId = kingdomId;
        this.invitator = invitator;
    }
}
