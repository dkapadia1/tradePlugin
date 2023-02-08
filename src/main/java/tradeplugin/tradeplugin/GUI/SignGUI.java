package tradeplugin.tradeplugin.GUI;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
                        int blockx = event.getPacket().getBlockPositionModifier().read(0).getX();
                        int blocky = event.getPacket().getBlockPositionModifier().read(0).getY();
                        int blockz = event.getPacket().getBlockPositionModifier().read(0).getZ();
                        if (blockx != v.getBlockX()) {event.getPlayer().sendMessage( v.getBlockX() + " x" + blockx);return;}
                        if (blocky != v.getBlockY()) {event.getPlayer().sendMessage( v.getBlockY() + " y" + blocky);return;}
                        if (blockz != v.getBlockZ()) {event.getPlayer().sendMessage( v.getBlockZ() + " z" + blockz);return;}

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
            packet53.getBlockPositionModifier().write(0, new BlockPosition(x, y, z));
           packet53.getBlockData().write(0, WrappedBlockData.createData(Material.OAK_SIGN));
            //packet53.getIntegers().write(1, x).write(2, y).write(3, z);
            packets.add(packet53);
            /* PacketContainer packet130 = protocolManager.createPacket(PacketType.Play.Server.UPDATE_SIGN);
            packet130.getBlockPositionModifier().write(0, new BlockPosition(x, y, z));
            packet130.getStringArrays().write(0, defaultText);
            packets.add(packet130);*/
        }

        PacketContainer packet133 = protocolManager.createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        packet133.getBlockPositionModifier().write(0, new BlockPosition(x, y, z));
        packets.add(packet133);
        
        if (defaultText != null) {
            PacketContainer packet53 = protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
            packet53.getBlockPositionModifier().write(0, new BlockPosition(x, y, z));
            packet53.getBlockData().write(0, WrappedBlockData.createData(Material.BEDROCK));
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