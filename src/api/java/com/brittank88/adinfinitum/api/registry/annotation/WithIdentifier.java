package com.brittank88.adinfinitum.api.registry.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifier for the {@link net.minecraft.util.Identifier Identifier} that the {@link java.lang.reflect.Field field} should be registered with.
 *
 * @author Brittank88
 * @see net.minecraft.util.Identifier Identifier
 * @see io.wispforest.owo.registration.reflect.SimpleFieldProcessingSubject SimpleFieldProcessingSubject
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WithIdentifier {
    /** @return The namespace of the {@link net.minecraft.util.Identifier Identifier}. */
    String namespace() default "ad-infinitum";
    /** @return The path of the {@link net.minecraft.util.Identifier Identifier}. */
    String path() default "en_us";
}
