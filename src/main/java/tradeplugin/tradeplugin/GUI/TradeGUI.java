package tradeplugin.tradeplugin.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import tradeplugin.tradeplugin.TradePlugin;

import java.util.Arrays;

public class TradeGUI implements Listener
{
    private final Inventory inv1;
    private final Inventory inv2;
    private int money1;
    private int money2;

    public TradePlugin plugin;

    public TradeGUI() {
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv1 = Bukkit.createInventory(null, 9, "Example");
        inv2 = Bukkit.createInventory(null, 9, "Example");

        // Put the items into the inventory
        initializeItems(inv1);
        initializeItems(inv2);
    }

    // You can call this whenever you want to put the items in
    public void initializeItems(Inventory inv) {
        inv.addItem(createGuiItem(Material.GOLD_BLOCK, "Put in Money", "§aFirst line of the lore", "§bSecond line of the lore"));
    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        ((ItemMeta) meta).setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent, boolean one) {
        if(one){ ent.openInventory(inv1);}
        else if(!one){ent.openInventory(inv1);}

    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv1) || !e.getInventory().equals(inv2)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at slot " + e.getRawSlot());
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e)
    {
        if (!e.getInventory().equals(inv1) || !e.getInventory().equals(inv2)) return;
        if(e.getInventory().equals(inv1) )
        {
        e.getPlayer().getPersistentDataContainer().set(TradePlugin.money, PersistentDataType.INTEGER,(e.getPlayer().getPersistentDataContainer().get(TradePlugin.money, PersistentDataType.INTEGER) - money1 + money2));
        }
        if(e.getInventory().equals(inv2) )
        {
            e.getPlayer().getPersistentDataContainer().set(TradePlugin.money, PersistentDataType.INTEGER,(e.getPlayer().getPersistentDataContainer().get(TradePlugin.money, PersistentDataType.INTEGER) - money2 + money1));
        }
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (!e.getInventory().equals(inv1) || !e.getInventory().equals(inv2)) {
            e.setCancelled(true);
        }
    }
}
