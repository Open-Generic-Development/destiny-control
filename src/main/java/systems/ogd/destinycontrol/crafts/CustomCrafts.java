package systems.ogd.destinycontrol.crafts;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import systems.ogd.destinycontrol.Destiny;

public class CustomCrafts {
    public static void registerCrafts() {
        ShapelessRecipe quarzWayback = new ShapelessRecipe(
                new NamespacedKey(Destiny.getDestiny(), "QuarzBack"),
                new ItemStack(Material.QUARTZ, 4)
        );

        quarzWayback.addIngredient(Material.QUARTZ_BLOCK);

        Bukkit.addRecipe(quarzWayback);
    }
}
