package tradeplugin.tradeplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;
import tradeplugin.tradeplugin.Commands.AddMoney;
import tradeplugin.tradeplugin.Commands.Trade;
import tradeplugin.tradeplugin.GUI.TradeGUI;
import tradeplugin.tradeplugin.Handlers.PlayerJoinHandler;

public final class TradePlugin extends JavaPlugin {
    PlayerJoinHandler ph;
    public static NamespacedKey money;
    public TradeGUI tGUI;
    public Trade trade;
    public static TradePlugin plugin;
    @Override
    public void onEnable() {
        // Plugin startup logic
        money = new NamespacedKey(this, "money");
        plugin = this;
        ph = new PlayerJoinHandler();
        tGUI = new TradeGUI();
        tGUI.plugin = this;
        trade = new Trade();
        trade.plugin =this;
        AddMoney AddMoneyCommand = new AddMoney();
        AddMoneyCommand.plugin = this;
        Bukkit.getPluginManager().registerEvents(ph,this);
        Bukkit.getPluginManager().registerEvents(tGUI,this);
        getCommand("trade").setExecutor(trade);
        getCommand("AddMoney").setExecutor(AddMoneyCommand);
    }
    public static Integer getMoney(Player p)
    {
        return(p.getPersistentDataContainer().get(money, PersistentDataType.INTEGER));

    }
    public static void setMoney(Player player, int deltamoney)
    {
        player.getPersistentDataContainer().set(money, PersistentDataType.INTEGER, getMoney(player) + deltamoney) ;
        updateMoney(player);
    }
    public static void updateMoney(Player player)
    {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard s = manager.getNewScoreboard();

        Objective objective = s.registerNewObjective("Bank", Criteria.create("Money"), (ChatColor.BLUE + "Money"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score = objective.getScore("Money "+ TradePlugin.getMoney(player) );
        player.setScoreboard(score.getScoreboard());
        score.setScore(1);
    }
    public static void updateMoney(Player player, String addon)
    {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard s = manager.getNewScoreboard();

        Objective objective = s.registerNewObjective("Bank", Criteria.create("Money"), (ChatColor.BLUE + "Money"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score = objective.getScore("Money "+ TradePlugin.getMoney(player) + addon);
        player.setScoreboard(score.getScoreboard());
        score.setScore(1);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
