package com.brittank88.adinfinitum.api.registry.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to assign an {@link io.wispforest.owo.itemgroup.gui.ItemGroupTab ItemGroupTab} (by numerical index)
 * to an {@link net.minecraft.item.Item Item} (or {@link net.minecraft.item.BlockItem BlockItem}).
 *
 * @author Brittank88
 * @see io.wispforest.owo.itemgroup.OwoItemGroup OwoItemGroup
 * @see io.wispforest.owo.itemgroup.gui.ItemGroupTab ItemGroupTab
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AssignedTab {
    /**
     * Default tab index that the {@link net.minecraft.item.Item Item} is assigned to if no tab is specified.
     *
     * @see AssignedTab#tab()
     */
    int DEFAULT_TAB_INDEX = 0;

    /**
     * @return The index of the {@link io.wispforest.owo.itemgroup.gui.ItemGroupTab ItemGroupTab} to assign the {@link net.minecraft.item.Item Item} to.
     *         Defaults to {@link AssignedTab#DEFAULT_TAB_INDEX}.
     */
    int tab() default DEFAULT_TAB_INDEX;
}
