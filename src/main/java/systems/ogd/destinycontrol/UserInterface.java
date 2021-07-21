package systems.ogd.destinycontrol;

import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import lombok.Getter;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import systems.ogd.destinycontrol.kingdoms.Kingdom;
import systems.ogd.destinycontrol.kingdoms.KingdomInvitation;
import systems.ogd.destinycontrol.user.Usermeta;
import systems.ogd.destinycontrol.utils.MessageUtils;
import systems.ogd.destinycontrol.utils.PlayerUtils;

import java.lang.management.BufferPoolMXBean;
import java.util.Objects;

import static systems.ogd.destinycontrol.utils.PlayerUtils.resolvePlayer;

@Getter
public class UserInterface {
    private final Player player;
    private final Usermeta meta;

    public UserInterface(Player player, Usermeta meta) {
        this.player = player;
        this.meta = meta;

        SGMenu userInterfaceMenu = Destiny.getDestiny().getUiManager().create("&aDestiny &c(Page {currentPage}/{maxPage})", 4, "UI");

        SGButton createNewKingdomButton = new SGButton(
                new ItemBuilder(Material.NETHER_STAR).name("§e* Create new Kingdom").build()
        ).withListener((InventoryClickEvent event) -> UserInterface.openKingdomWizard(((Player) event.getWhoClicked())));

        SGButton acceptKingdomInvitation = new SGButton(
                meta.getInvitation() == null ?
                        new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name("§cNo Invitation to Accept").build() :
                        new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).name("§aAccept Invitation")
                                .lore(
                                        "Kingdom: " + meta.getInvitation().getName(),
                                        "By: " + meta.getInvitation().getInvitator()
                                )
                                .build()
        ).withListener((InventoryClickEvent event) -> {
            Usermeta usermeta = Objects.requireNonNull(resolvePlayer((Player) event.getWhoClicked()));
            Player user = (Player) event.getWhoClicked();
            if (Objects.requireNonNull(resolvePlayer((Player) event.getWhoClicked())).getInvitation() != null) {
                KingdomInvitation invitation = usermeta.getInvitation();
                Kingdom destiny = Destiny.getDestiny().getFs().getKingdoms().get(invitation.getKingdomId());

                destiny.getUsers().add(usermeta);
                Destiny.getDestiny().getFs().save();

                usermeta.setInvitation(null);
                user.closeInventory();
                user.kickPlayer("§eYou are now the Member of a kingdom. Please reconnect.");
            }
        });

        SGButton alterKingdom = new SGButton(
                Destiny.getDestiny().getFs().getKingdoms().stream().anyMatch(kingdom -> kingdom.getLeader().getUuid().toString().equals(meta.getUuid().toString())) ?
                        new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).name("§aAlter your Kingdom").build() :
                        new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name("§cYou are not a King or Queen").build()
        ).withListener((InventoryClickEvent event) -> {
            Usermeta usermeta = Objects.requireNonNull(resolvePlayer((Player) event.getWhoClicked()));
            Player user = (Player) event.getWhoClicked();

            if (Objects.requireNonNull(event.getInventory().getItem(14)).getType() == Material.GREEN_STAINED_GLASS_PANE) {
                //Not needed, already checked
                //noinspection OptionalGetWithoutIsPresent
                openAlterKingdom(user, usermeta, Destiny.getDestiny().getFs().getKingdoms().stream().filter(kingdom -> kingdom.getLeader().getUuid().toString().equals(meta.getUuid().toString())).findFirst().get());
            }
        });

        SGButton invite = new SGButton(
                Destiny.getDestiny().getFs().getKingdoms().stream().anyMatch(kingdom -> kingdom.getLeader().getUuid().toString().equals(meta.getUuid().toString())) ?
                        new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).name("§aInvite Someone to your Kingdom").build() :
                        new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name("§cYou are not a King or Queen").build()
        ).withListener((InventoryClickEvent event) -> {
            Player user = (Player) event.getWhoClicked();

            if (Objects.requireNonNull(event.getInventory().getItem(14)).getType() == Material.GREEN_STAINED_GLASS_PANE) {
                //Not needed, already checked
                //noinspection OptionalGetWithoutIsPresent
                sendInvite(user.getName(), Destiny.getDestiny().getFs().getKingdoms().stream().filter(kingdom -> kingdom.getLeader().getUuid().toString().equals(meta.getUuid().toString())).findFirst().get());
            }
        });

        userInterfaceMenu.setButton(0, 12, createNewKingdomButton);
        userInterfaceMenu.setButton(0, 13, acceptKingdomInvitation);
        userInterfaceMenu.setButton(0, 14, alterKingdom);

        player.openInventory(userInterfaceMenu.getInventory());
    }

    private void sendInvite(String inviter, Kingdom kingdom) {
        AnvilGUI.Builder gui = new AnvilGUI.Builder();
        gui.plugin(Destiny.getDestiny());
        gui.title("§eWho do you want to invite?");
        gui.text("...");
        gui.onComplete((player1, entry) -> {
            Player user = Bukkit.getPlayer(entry);
            if (user == null) {
                return AnvilGUI.Response.text("Incorrect User");
            }

            if (Destiny.getDestiny().getFs().getKingdoms().stream().noneMatch(kingdom1 -> kingdom1.getUsers().stream().anyMatch(usermeta -> usermeta.getUuid().toString().equals(user.getUniqueId().toString())))) {
                Usermeta usermeta = PlayerUtils.resolvePlayer(user);

                assert usermeta != null;
                usermeta.setInvitation(new KingdomInvitation(kingdom.getName(), Destiny.getDestiny().getFs().getKingdoms().indexOf(kingdom), inviter));

                MessageUtils.sendMessage(player1, "§eSent Invitation");

                return AnvilGUI.Response.close();
            }

            return AnvilGUI.Response.text("This user is already in a Kingdom");
        });

        gui.open(Bukkit.getPlayer(inviter));
    }

    private void openAlterKingdom(Player user, Usermeta usermeta, Kingdom kingdom) {

    }

    private static void openKingdomWizard(Player king) {

    }
}

/*
 *  P1:
 *      Kingdoms
 *          new
 *          accept
 *          alter
 *          invite
 *          leave
 *      AFK
 *  P2:
 *      Clear Chat
 *      Kick Player
 *      Ban Player
 *      Warn Player
 *      Teamchat
 *      Warn Kingdom
 *  P3:
 *      Promote Member
 *      Demote Member
 *      Grant Admin Privileges
 *      Remove Admin Privileges
 *      Alter Kingdom
 *      Remove Kingdom
 *
 * 0 0 0 0 0 0 0 0 0
 * 0 0 0 x x x 0 0 0
 * 0 0 0 x x x 0 0 0
 * 0 0 0 0 0 0 0 0 0
 *
 */
