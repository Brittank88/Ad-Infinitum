package com.brittank88.adinfinitum.api.registry.annotation

/**
 * This annotation is used to assign an [ItemGroupTab][io.wispforest.owo.itemgroup.gui.ItemGroupTab] (by numerical index)
 * to an [Item][net.minecraft.item.Item] (or [BlockItem][net.minecraft.item.BlockItem]).
 *
 * @param value The index of the [ItemGroupTab][io.wispforest.owo.itemgroup.gui.ItemGroupTab] to assign the [Item][net.minecraft.item.Item] to.
 * Defaults to [AssignedTab.DEFAULT_TAB_INDEX].
 *
 * @author Brittank88
 * @see io.wispforest.owo.itemgroup.OwoItemGroup OwoItemGroup
 *
 * @see io.wispforest.owo.itemgroup.gui.ItemGroupTab ItemGroupTab
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class AssignedTab(val value: Int = 0)
