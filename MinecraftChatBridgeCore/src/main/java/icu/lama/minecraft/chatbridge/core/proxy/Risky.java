package icu.lama.minecraft.chatbridge.core.proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a field/method have risk when using it.
 * <p>
 * You should avoid calling these methods or referencing these fields.
 * Surround a try-catch block if you have to use them (THIS IS THE WORST SOLUTION)
 * You should never expect this will work as intended in everywhere
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Risky {
}
