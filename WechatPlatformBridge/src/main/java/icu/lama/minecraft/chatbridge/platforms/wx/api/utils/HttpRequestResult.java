package icu.lama.minecraft.chatbridge.platforms.wx.api.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.nio.charset.StandardCharsets;

public class HttpRequestResult {
    private static final Gson GSON = new Gson();

    private int statusCode;
    private final byte[] data;

    public HttpRequestResult(int statusCode, byte[] data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public <T> T deserialize(TypeToken<T> type) {
        return GSON.fromJson(new String(this.data, StandardCharsets.UTF_8), type);
    }

    @Override public String toString() {
        return new String(data, StandardCharsets.UTF_8);
    }
}
