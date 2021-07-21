package systems.ogd.destinycontrol;

import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import systems.ogd.destinycontrol.user.Usermeta;

@Getter
public class UserInterface {
    private final Player player;
    private final Usermeta meta;

    public UserInterface(Player player, Usermeta meta){
        this.player = player;
        this.meta = meta;

        SGMenu userInterfaceMenu = Destiny.getDestiny().getUiManager().create("&aDestiny &c(Page {currentPage}/{maxPage})", 3, "UI");

        SGButton myAwesomeButton = new SGButton(
                new ItemBuilder(Material.ACACIA_BUTTON).build()
        ).withListener((InventoryClickEvent event) -> {
            event.getWhoClicked().sendMessage("Hello, world!");
        });

        userInterfaceMenu.setButton(0, 0, myAwesomeButton);

        player.openInventory(userInterfaceMenu.getInventory());
    }
}
