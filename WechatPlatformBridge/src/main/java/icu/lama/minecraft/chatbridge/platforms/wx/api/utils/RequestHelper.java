package icu.lama.minecraft.chatbridge.platforms.wx.api.utils;

import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;

public class RequestHelper {
    private final static Gson GSON = new Gson();
    // Some magic text from UOS ver wechat
    private final static String UOS_MAGIC = "Go8FCIkFEokFCggwMDAwMDAwMRAGGvAESySibk50w5Wb3uTl2c2h64jVVrV7gNs06GFlWplHQbY/5FfiO++1yH4ykCyNPWKXmco+wfQzK5R98D3so7rJ5LmGFvBLjGceleySrc3SOf2Pc1gVehzJgODeS0lDL3/I/0S2SSE98YgKleq6Uqx6ndTy9yaL9qFxJL7eiA/R3SEfTaW1SBoSITIu+EEkXff+Pv8NHOk7N57rcGk1w0ZzRrQDkXTOXFN2iHYIzAAZPIOY45Lsh+A4slpgnDiaOvRtlQYCt97nmPLuTipOJ8Qc5pM7ZsOsAPPrCQL7nK0I7aPrFDF0q4ziUUKettzW8MrAaiVfmbD1/VkmLNVqqZVvBCtRblXb5FHmtS8FxnqCzYP4WFvz3T0TcrOqwLX1M/DQvcHaGGw0B0y4bZMs7lVScGBFxMj3vbFi2SRKbKhaitxHfYHAOAa0X7/MSS0RNAjdwoyGHeOepXOKY+h3iHeqCvgOH6LOifdHf/1aaZNwSkGotYnYScW8Yx63LnSwba7+hESrtPa/huRmB9KWvMCKbDThL/nne14hnL277EDCSocPu3rOSYjuB9gKSOdVmWsj9Dxb/iZIe+S6AiG29Esm+/eUacSba0k8wn5HhHg9d4tIcixrxveflc8vi2/wNQGVFNsGO6tB5WF0xf/plngOvQ1/ivGV/C1Qpdhzznh0ExAVJ6dwzNg7qIEBaw+BzTJTUuRcPk92Sn6QDn2Pu3mpONaEumacjW4w6ipPnPw+g2TfywJjeEcpSZaP4Q3YV5HG8D6UjWA4GSkBKculWpdCMadx0usMomsSS/74QgpYqcPkmamB4nVv1JxczYITIqItIKjD35IGKAUwAA==";

    private static final CookieStore cookieStore = new CookieStore();

    public static HttpRequestResult post(String url, Object data) throws Exception {
        HttpsURLConnection connection = (HttpsURLConnection) (new URL(url)).openConnection();

        connection.setRequestProperty("Cookie", cookieStore.toString());
        connection.setRequestProperty("extspam", UOS_MAGIC);
        connection.setRequestProperty("client-version", "2.0.0");

        connection.setDoInput(true);
        connection.setDoOutput(true);

        connection.getOutputStream().write(GSON.toJson(data).getBytes(StandardCharsets.UTF_8));
        return readRemote(connection);
    }

    public static HttpRequestResult get(String url) throws Exception {
        HttpsURLConnection connection = (HttpsURLConnection) (new URL(url)).openConnection();

        connection.setRequestProperty("Cookie", cookieStore.toString());
        connection.setRequestProperty("extspam", UOS_MAGIC);
        connection.setRequestProperty("client-version", "2.0.0");

        connection.setDoInput(true);
        connection.setDoOutput(true);

        return readRemote(connection);
    }

    private static HttpRequestResult readRemote(HttpsURLConnection connection) throws IOException {
        cookieStore.update(connection.getHeaderField("Set-Cookie"));
        InputStream remote = connection.getInputStream();

        byte[] response = new byte[connection.getContentLength()];
        int read = remote.read(response);
        while (read < connection.getContentLength()) {
            read += remote.read(response, read, connection.getContentLength() - read);
        }

        remote.close();
        connection.disconnect();

        return new HttpRequestResult(connection.getResponseCode(), response);
    }

    public static void setCookieStore(String cookie) { cookieStore.update(cookie); }

    public static void setSkipSSLCheck() throws Exception {

        TrustManager[] sslTrustAll = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, sslTrustAll, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
}
