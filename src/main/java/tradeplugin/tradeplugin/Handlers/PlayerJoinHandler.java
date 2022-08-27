package tradeplugin.tradeplugin.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.*;
import tradeplugin.tradeplugin.TradePlugin;

public class PlayerJoinHandler implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        if(!player.getPersistentDataContainer().has(TradePlugin.money, PersistentDataType.INTEGER))
        {
            player.getPersistentDataContainer().set(TradePlugin.money, PersistentDataType.INTEGER, 1000);

        }
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard s = manager.getNewScoreboard();

        Objective objective = s.registerNewObjective("Bank", Criteria.create("Money"), (ChatColor.BLUE + "Money"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score = objective.getScore("Money "+ TradePlugin.getMoney(player) );
        player.setScoreboard(score.getScoreboard());
        score.setScore(1);

    }

}
