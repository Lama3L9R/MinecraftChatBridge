package icu.lama.minecraft.chatbridge.core.proxy.minecraft;

public class ProxyWorldType {
    public static final ProxyWorldType OVERWORLD = new ProxyWorldType("overworld");
    public static final ProxyWorldType NETHER = new ProxyWorldType("nether");
    public static final ProxyWorldType THE_END = new ProxyWorldType("the_end");

    private final String name;

    public ProxyWorldType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
