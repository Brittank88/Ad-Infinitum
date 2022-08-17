package com.brittank88.adinfinitum.api.registry.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifier for the identifier that the {@link java.lang.reflect.Field field} should be registered with.
 *
 * @author Brittank88
 * @see io.wispforest.owo.registration.reflect.SimpleFieldProcessingSubject SimpleFieldProcessingSubject
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WithIdentifier {
    String namespace() default "ad-infinitum";
    String path() default "en_us";
}
