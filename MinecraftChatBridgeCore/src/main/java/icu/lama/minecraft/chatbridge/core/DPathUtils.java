package icu.lama.minecraft.chatbridge.core;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author @btnguyen2k
 * <a href="https://gist.github.com/btnguyen2k/3716451">Gist Link</a>
 */
public class DPathUtils {

    private final static Pattern PATTERN_INDEX = Pattern
            .compile("^\\[(\\d+)\\]$");

    /**
     * Extracts a value from a target object using DPath expression.
     *
     * @param target
     * @param dPath
     */
    public static Object getValue(Object target, String dPath) {
        String[] paths = dPath.split("\\.");
        Object result = target;
        for (String path : paths) {
            result = extractValue(result, path);
        }
        return result;
    }

    private static Object extractValue(Object target, String index) {
        if (target == null) {
            throw new IllegalArgumentException();
        }
        Matcher m = PATTERN_INDEX.matcher(index);
        if (m.matches()) {
            int i = Integer.parseInt(m.group(1));
            if (target instanceof Object[]) {
                return ((Object[]) target)[i];
            }
            if (target instanceof List<?>) {
                return ((List<?>) target).get(i);
            }
            throw new IllegalArgumentException("Expect an array or list!");
        }
        if (target instanceof Map<?, ?>) {
            return ((Map<?, ?>) target).get(index);
        }
        throw new IllegalArgumentException();
    }
}