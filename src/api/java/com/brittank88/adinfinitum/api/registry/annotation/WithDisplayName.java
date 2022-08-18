package com.brittank88.adinfinitum.api.registry.annotation;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the display name of the {@link java.lang.reflect.Field field} that is being registered.
 *
 * @author Brittank88
 * @see io.wispforest.owo.registration.reflect.SimpleFieldProcessingSubject SimpleFieldProcessingSubject
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithDisplayName {
    /** @return The display name for the {@link java.lang.reflect.Field Field} being processed. */
    @Nullable String name();
}
