package tradeplugin.tradeplugin.GUI;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.comphenix.protocol.events.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.comphenix.protocol.*;
import com.comphenix.protocol.reflect.*;


public class SignGUI {

    protected ProtocolManager protocolManager;
    protected PacketAdapter packetListener;
    protected Map<String, SignGUIListener> listeners;
    protected Map<String, Vector> signLocations;



    public SignGUI(Plugin plugin) {
        protocolManager = ProtocolLibrary.getProtocolManager();
        listeners = new ConcurrentHashMap<String, SignGUIListener>();
        signLocations = new ConcurrentHashMap<String, Vector>();


        ProtocolLibrary.getProtocolManager().addPacketListener(
                packetListener =  new PacketAdapter(plugin, PacketType.Play.Client.UPDATE_SIGN)
                {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        final Player player = event.getPlayer();

                        Vector v = signLocations.remove(player.getName());
                        if (v == null) return;
                        List<Integer> list = event.getPacket().getIntegers().getValues();
                        if (list.get(0) != v.getBlockX()) return;
                        if (list.get(1) != v.getBlockY()) return;
                        if (list.get(2) != v.getBlockZ()) return;

                        final String[] lines = event.getPacket().getStringArrays().getValues().get(0);
                        final SignGUIListener response = listeners.remove(event.getPlayer().getName());
                        if (response != null) {
                            event.setCancelled(true);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                public void run() {
                                    response.onSignDone(player, lines);
                                }
                            });
                        }
                    }
                });
    }


    public void open(Player player, String[] defaultText, SignGUIListener response) {
        List<PacketContainer> packets = new ArrayList<PacketContainer>();

        int x = 1, y = 1, z = 1;
        if (defaultText != null) {
            x = player.getLocation().getBlockX();
            z = player.getLocation().getBlockZ();

            PacketContainer packet53 = new PacketContainer(PacketType.Play.Server.BLOCK_CHANGE);

            Bukkit.getLogger().info(packet53.getStructures().toString());
            packet53.getIntegers().write(1, x).write(2, y).write(3, z);

            packet53.getBlocks().write(0, Material.OAK_SIGN);
            packets.add(packet53);

            PacketContainer packet130 = protocolManager.createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
            packet130.getIntegers().write(0, x).write(1,y).write(2, z);
            packet130.getStringArrays().write(0, defaultText);
            packets.add(packet130);
        }

        PacketContainer packet133 = protocolManager.createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        packet133.getIntegers().write(0, x).write(2, z);
        packets.add(packet133);
        
        if (defaultText != null) {
            PacketContainer packet53 = protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
            packet53.getIntegers().write(0, x).write(1, 0).write(2, z);
            packet53.getBlocks().write(0, org.bukkit.Material.BEDROCK);
            packets.add(packet53);
        }

        try {
            for (PacketContainer packet : packets) {
                protocolManager.sendServerPacket(player, packet);
            }
            signLocations.put(player.getName(), new Vector(x, y, z));
            listeners.put(player.getName(), response);
        } catch (Error e) {
            e.printStackTrace();
        }

    }





    public void destroy() {
        protocolManager.removePacketListener(packetListener);
        listeners.clear();
        signLocations.clear();
    }

    public interface SignGUIListener {
        public void onSignDone(Player player, String[] lines);

    }

}