package icu.lama.minecraft.chatbridge.core.config;

import icu.lama.minecraft.chatbridge.core.DPathUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlatformConfiguration {
    private Map<String, String> credentials;

    public boolean enableBinding;

    public Map<String, Object> extraContents;

    protected List<String> trustedCallers = new ArrayList<>();

    public String mainClass;

    /**
     * Get platform credentials. Only returns when caller class is in the trusted callers.
     * @return PlatformCredentials instance
     * @throws RuntimeException when caller class isn't in the trusted callers
     */
    public final String getCredentials(String path) {
        String caller = Thread.currentThread().getStackTrace()[2].getClassName();
        if (!(trustedCallers.contains(caller) || caller.equals(mainClass))) {
            throw new RuntimeException(new IllegalAccessException("Only trusted callers can access credentials"));
        }

        return (String) DPathUtils.getValue(credentials, path);
    }

    public Object get(String path) {
        return DPathUtils.getValue(extraContents, path);
    }

    public void get(String path, Callback onExists) {
        Object result = get(path);
        if (result != null) {
            onExists.run(result);
        }
    }

    public interface Callback {
        void run(Object obj);
    }
}
