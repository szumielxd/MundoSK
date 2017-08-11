package com.pie.tlatoani.ProtocolLib;

import ch.njol.skript.lang.ExpressionType;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.pie.tlatoani.Mundo;
import com.pie.tlatoani.Util.Logging;
import com.pie.tlatoani.Util.Registration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Tlatoani on 8/8/17.
 */
public class PacketManager {
    private static Map<String, PacketType> packetTypesByName;
    
    public static void load() {
        Logging.info("You've discovered the amazing realm of ProtocolLib packet syntaxes!");
        String pLibVersion = Bukkit.getPluginManager().getPlugin("ProtocolLib").getDescription().getVersion();
        if (!pLibVersion.substring(0, 1).equals("4") || pLibVersion.substring(0, 3).equals("4.0")) {
            Logging.info("Your version of ProtocolLib is " + pLibVersion);
            Logging.info("MundoSK requires that you run at least version 4.1 of ProtocolLib");
            Logging.info("If you are running at least version 4.1 of ProtocolLib, please post a message on MundoSK's thread on forums.skunity.com");
        }
        packetTypesByName = createNameToPacketTypeMap();
        Registration.registerEnum(PacketType.class, "packettype", new PacketType[0], packetTypesByName.entrySet().toArray(new Map.Entry[0]));
        Registration.registerType(PacketContainer.class, "packet");
        Registration.registerEffect(EffSendPacket.class, "send packet %packet% to %player%", "send %player% packet %packet%");
        Registration.registerEffect(EffReceivePacket.class, "rec(ei|ie)ve packet %packet% from %player%"); //Included incorrect spelling to avoid wasted time
        Registration.registerEffect(EffPacketInfo.class, "packet info %packet%");
        Registration.registerEvent("Packet Event", EvtPacketEvent.class, MundoPacketEvent.class, "packet event %packettypes%");
        Registration.registerEventValue(MundoPacketEvent.class, PacketContainer.class, MundoPacketEvent::getPacket);
        Registration.registerEventValue(MundoPacketEvent.class, PacketType.class, MundoPacketEvent::getPacketType);
        Registration.registerEventValue(MundoPacketEvent.class, Player.class, MundoPacketEvent::getPlayer);
        Registration.registerExpression(ExprTypeOfPacket.class, PacketType.class, ExpressionType.SIMPLE, "packettype of %packet%", "%packet%'s packettype");
        Registration.registerExpression(ExprNewPacket.class, PacketContainer.class, ExpressionType.PROPERTY, "new %packettype% packet");
        Registration.registerExpression(ExprJSONObjectOfPacket.class, JSONObject.class, ExpressionType.PROPERTY,
                "(%-string%" + ExprJSONObjectOfPacket.getConverterNamesPattern(true) + ") pjson %number% of %packet%",
                "(%-string%" + ExprJSONObjectOfPacket.getConverterNamesPattern(false) + ") array pjson %number% of %packet%");
        Registration.registerExpression(ExprObjectOfPacket.class, Object.class, ExpressionType.PROPERTY,
                "(0¦%-classinfo/string%" + ExprObjectOfPacket.getConverterNamesPattern(true) + ") pinfo %number% of %packet%",
                "(0¦%-classinfo/string%" + ExprObjectOfPacket.getConverterNamesPattern(false) + ") array pinfo %number% of %packet%");
        Registration.registerExpression(ExprPrimitiveOfPacket.class, Number.class, ExpressionType.PROPERTY, "(0¦byte|1¦short|2¦int|3¦long|4¦float|5¦double) pnum %number% of %packet%");
        Registration.registerExpression(ExprPrimitiveArrayOfPacket.class, Number.class, ExpressionType.PROPERTY, "(0¦int|1¦byte) array pnum %number% of %packet%");
        Registration.registerExpression(ExprEntityOfPacket.class, Entity.class, ExpressionType.PROPERTY,
                "%world% pentity %number% of %packet%",
                "%world% pentity array %number% of %packet%");
        Registration.registerExpression(ExprEnumOfPacket.class, String.class, ExpressionType.PROPERTY, "%string% penum %number% of %packet%");
    }

    public PacketType getPacketTypeFromName(String name) {
        return packetTypesByName.get(name.toLowerCase());
    }

    public static void onPacketEvent(PacketType packetType, Consumer<PacketEvent> handler) {
        onPacketEvent(packetType, ListenerPriority.NORMAL, handler);
    }

    public static void onPacketEvent(PacketType packetType, ListenerPriority priority, Consumer<PacketEvent> handler) {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Mundo.INSTANCE, priority, packetType) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                handler.accept(event);
            }

            @Override
            public void onPacketSending(PacketEvent event) {
                handler.accept(event);
            }
        });
    }

    public static void onPacketEvent(PacketType[] packetTypes, Consumer<PacketEvent> handler) {
        onPacketEvent(packetTypes, ListenerPriority.NORMAL, handler);
    }

    public static void onPacketEvent(PacketType[] packetTypes, ListenerPriority priority, Consumer<PacketEvent> handler) {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Mundo.INSTANCE, priority, packetTypes) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                handler.accept(event);
            }

            @Override
            public void onPacketSending(PacketEvent event) {
                handler.accept(event);
            }
        });
    }
    
    public static void sendPacket(Player player, Object exceptLoc, PacketContainer packet) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            Logging.reportException(exceptLoc, e);
        }
    }
    
    public static void sendPacket(Player[] players, Object exceptLoc, PacketContainer packet) {
        try {
            for (Player player : players) {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            }
        } catch (InvocationTargetException e) {
            Logging.reportException(exceptLoc, e);
        }
    }

    public static void sendPacket(Iterable<Player> players, Object exceptLoc, PacketContainer packet) {
        try {
            for (Player player : players) {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            }
        } catch (InvocationTargetException e) {
            Logging.reportException(exceptLoc, e);
        }
    }

    public static Map<String, PacketType> createNameToPacketTypeMap() {
        Map<String, PacketType> packetTypesByName = new HashMap<>();
        addPacketTypes(packetTypesByName, PacketType.Play.Server.getInstance().iterator(), "PLAY", true);
        addPacketTypes(packetTypesByName, PacketType.Play.Client.getInstance().iterator(), "PLAY", false);
        addPacketTypes(packetTypesByName, PacketType.Handshake.Server.getInstance().iterator(), "HANDSHAKE", true);
        addPacketTypes(packetTypesByName, PacketType.Handshake.Client.getInstance().iterator(), "HANDSHAKE", false);
        addPacketTypes(packetTypesByName, PacketType.Login.Server.getInstance().iterator(), "LOGIN", true);
        addPacketTypes(packetTypesByName, PacketType.Login.Client.getInstance().iterator(), "LOGIN", false);
        addPacketTypes(packetTypesByName, PacketType.Status.Server.getInstance().iterator(), "STATUS", true);
        addPacketTypes(packetTypesByName, PacketType.Status.Client.getInstance().iterator(), "STATUS", false);
        return packetTypesByName;
    }

    public static void addPacketTypes(Map<String, PacketType> map, Iterator<PacketType> packetTypeIterator, String prefix, Boolean isServer) {
        while (packetTypeIterator.hasNext()) {
            PacketType current = packetTypeIterator.next();
            String fullname = prefix + "_" + (isServer ? "SERVER" : "CLIENT") + "_" + current.name().toUpperCase();
            map.put(fullname, current);
        }
    }
}