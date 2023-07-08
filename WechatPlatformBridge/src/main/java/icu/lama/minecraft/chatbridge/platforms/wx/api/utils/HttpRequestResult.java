package icu.lama.minecraft.chatbridge.platforms.wx.api.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class HttpRequestResult {
    private static final Gson GSON = new Gson();

    private int statusCode;
    private final byte[] data;
    private final HttpURLConnection conn;

    public HttpRequestResult(int statusCode, byte[] data, HttpURLConnection conn) {
        this.statusCode = statusCode;
        this.data = data;
        this.conn = conn;
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

    public HttpURLConnection getConnection() {
        return conn;
    }
}
