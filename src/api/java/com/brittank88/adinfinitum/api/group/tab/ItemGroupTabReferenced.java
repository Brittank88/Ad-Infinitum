package com.brittank88.adinfinitum.api.group.tab;

import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;

public class ItemGroupTabReferenced {

    private final OwoItemGroup parentGroup;

    public ItemGroupTabReferenced(Icon icon, String name, TagKey<Item> contentTag, Identifier texture, OwoItemGroup parentGroup) {
        // super(icon, name, contentTag, texture);
        this.parentGroup = parentGroup;
    }

    public OwoItemGroup getParentGroup() { return parentGroup; }
}
