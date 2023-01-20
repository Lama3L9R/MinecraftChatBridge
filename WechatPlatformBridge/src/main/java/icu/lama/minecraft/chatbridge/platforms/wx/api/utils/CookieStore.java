package icu.lama.minecraft.chatbridge.platforms.wx.api.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CookieStore {
    private static final Pattern COOKIE_SPLIT = Pattern.compile("(\\S+) ?= ?(\\S+)");
    private final HashMap<String, String> cookies = new HashMap<>();

    public CookieStore() { }

    public CookieStore(String initCookie) {
        Arrays.stream(initCookie.split(";")).forEach((it) -> {
            String[] split = it.split("=", 2);
            cookies.put(split[0].trim(), split[1].trim());
        });
    }

    @Override public String toString() {
        return cookies.entrySet().stream().map(kv -> kv.getKey() + "=" + kv.getValue()).collect(Collectors.joining(";"));
    }

    public void update(String cookie) {
        if (cookie == null) {
            return;
        }

        Arrays.stream(cookie.split(";")).forEach((it) -> {
            String[] split = it.split("=", 2);
            if (split.length < 2 || split[0].contains("Expires") || split[0].contains("Path") || split[0].contains("Domain")) {
                return;
            }
            cookies.put(split[0].trim().replace("Secure,", ""), split[1].trim());
        });
    }
}
