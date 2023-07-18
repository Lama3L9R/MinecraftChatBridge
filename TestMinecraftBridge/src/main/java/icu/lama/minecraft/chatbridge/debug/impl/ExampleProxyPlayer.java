package icu.lama.minecraft.chatbridge.debug.impl;

import icu.lama.minecraft.chatbridge.core.proxy.minecraft.IProxyPlayer;
import icu.lama.minecraft.chatbridge.core.proxy.minecraft.Location;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class ExampleProxyPlayer implements IProxyPlayer {

    private final String name;

    public ExampleProxyPlayer(String name) {
        this.name = name;
    }

    @Override
    public UUID getUniqueID() {
        return UUID.fromString("ExamplePlayer:" + name);
    }

    @Override
    public String getDisplayName() {
        return "D" + name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Location getStandingLocation() {
        return new Location((int) (Math.random() * 100), (int) (Math.random() * 100), (int) (Math.random() * 100), Math.random(), Math.random(), new ExampleProxyWorld());
    }

    @Override
    public void kick() {
        System.out.println("Kick -> " + getDisplayName() + " >> null");
    }

    @Override
    public void kick(String reason) {
        System.out.println("Kick -> " + getDisplayName() + " >> " + reason);
    }

    @Override
    public void ban(Date till, String reason1, String reason2) {
        System.out.println("Ban -> " + getDisplayName() + " >> " + till + ", " + reason1 + ", " + reason2);
    }

    @Override
    public void sendMessage(String msg) {
        System.out.println("PrivateMessage -> " + getDisplayName() + " >> " + msg);
    }

    @Override
    public void sendPacket(Object data) {
        System.out.println("ObjectPacket -> " + getDisplayName() + " >> " + data.toString());
    }

    @Override
    public void sendPacket(byte[] data) {
        System.out.println("ObjectPacket -> " + getDisplayName() + " >> " + Arrays.toString(data));
    }

    @Override
    public Object unwrap() {
        return null;
    }
}
