package icu.lama.minecraft.chatbridge.core.proxy.minecraft;

public class Location {
    private final int x;
    private final int y;
    private final int z;
    private final double yaw;
    private final double pitch;
    private final IProxyWorld world;

    public Location(int x, int y, int z, double yaw, double pitch, IProxyWorld world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public IProxyWorld getWorld() {
        return world;
    }
}
