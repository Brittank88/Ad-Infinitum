package com.brittank88.adastra.client.resource;

import com.brittank88.adastra.AdAstra;
import com.brittank88.adastra.annotation.DisplayName;
import com.brittank88.adastra.group.AdAstraGroups;
import com.brittank88.adastra.util.RegistryUtil;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.lang.JLang;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import java.lang.reflect.Field;

/**
 * Registration class for the {@link RuntimeResourcePack RPP} used by Ad Astra.
 *
 * @author brittank88
 */
@Environment(EnvType.CLIENT)
public abstract class AdAstraRRP {

    public static final String RESOURCE_PACK_ID = AdAstra.MOD_ID + ":rrp";

    public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create(RESOURCE_PACK_ID);
    public static final JLang LANG = JLang.lang();

    public static void registerPack() {
        RESOURCE_PACK.addLang(AdAstra.id("en_us"), LANG);

        RRPCallback.BEFORE_VANILLA.register(a -> a.add(RESOURCE_PACK));

        AdAstra.LOGGER.info("RRP registered!");
    }

    public static <T extends ItemGroup> void handleItemGroup(T itemGroup) {
        String groupIdentifier = itemGroup.getName().replace(':', '.');

        LANG.entry("itemGroup." + groupIdentifier, "Ad Astra | " + WordUtils.capitalizeFully(StringUtils.removeStart(groupIdentifier, "ad-astra.group.")));

        if (itemGroup instanceof OwoItemGroup ig)
            ig.tabs.forEach(t -> LANG.entry(
                    t.getTranslationKey(groupIdentifier),
                    ig.equals(AdAstraGroups.GROUP_SINGULARITIES) ? t.name().toUpperCase() : WordUtils.capitalizeFully(t.name()))
            );
    }

    public static void handleBlockRegistryContainer(String namespace, Block value, String identifier, Field field) {

        // Language registration.
        DisplayName displayNameAnnotation = field.getAnnotation(DisplayName.class);
        String displayName = displayNameAnnotation == null ? RegistryUtil.registryToDisplayName(identifier) : displayNameAnnotation.name();
        LANG.entry("block." + namespace + "." + identifier, displayName);

        // Commonly used identifiers.
        Identifier blockModelTextureIdentifier = new Identifier(namespace, "block/" + identifier);
        Identifier blockStateIdentifier = new Identifier(namespace, identifier);

        // Blockstate registration.
        RESOURCE_PACK.addBlockState(
                JState.state(
                        JState.variant(
                                JState.model(blockModelTextureIdentifier)
                        )
                ),
                blockStateIdentifier
        );

        // Model registration.
        RESOURCE_PACK.addModel(
                JModel.model("block/cube_all").textures(
                        JModel.textures().var("all", blockModelTextureIdentifier.toString())
                ),
                blockModelTextureIdentifier
        );
    }

    public static void handleItemRegistryContainer(String namespace, Item item, String identifier, Field field) {
        DisplayName displayNameAnnotation = field.getAnnotation(DisplayName.class);
        registerItem(namespace, identifier, item, displayNameAnnotation == null ? RegistryUtil.registryToDisplayName(identifier) : displayNameAnnotation.name());
    }

    private static void registerItem(String namespace, String identifier, Item item, String displayName) {

        LANG.entry("item." + namespace + "." + identifier, displayName);

        // Model registration.
        String strID = namespace + ":item/" + identifier;
        RESOURCE_PACK.addModel(
                item instanceof BlockItem
                        ? JModel.model().parent(namespace + ":block/" + identifier)
                        : JModel.model("minecraft:item/generated").textures(
                        JModel.textures().var("layer0", strID)
                ),
                new Identifier(strID)
        );
    }
}
