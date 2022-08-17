package com.brittank88.adinfinitum.client.resource;

import com.brittank88.adinfinitum.AdInfinitum;
import com.brittank88.adinfinitum.api.registry.annotation.WithDisplayName;
import com.brittank88.adinfinitum.group.AdInfinitumGroups;
import com.brittank88.adinfinitum.util.AdInfinitumUtil;
import com.brittank88.adinfinitum.util.RegistryUtil;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.lang.JLang;
import net.devtech.arrp.json.models.JModel;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import java.lang.reflect.Field;

/**
 * Registration class for the {@link RuntimeResourcePack RPP} used by Ad Infinitum.
 *
 * @author brittank88
 */
public abstract class AdInfinitumRRP {

    public static final String RESOURCE_PACK_ID = AdInfinitumUtil.MOD_ID + ":rrp";

    public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create(RESOURCE_PACK_ID);
    public static final JLang LANG_EN_US = JLang.lang();

    public static void registerPack() {
        RESOURCE_PACK.addLang(AdInfinitum.id("en_us"), LANG_EN_US);

        RRPCallback.BEFORE_VANILLA.register(a -> a.add(RESOURCE_PACK));

        AdInfinitum.LOGGER.info("RRP registered!");
    }

    public static <T extends ItemGroup> void handleItemGroup(T itemGroup) {
        String groupIdentifier = itemGroup.getName().replace(':', '.');

        // TODO: Migrate from WordUtils in Apache Commons Text to lang3.text.WordUtils in Apache Commons Lang3.
        //       Or... Just self-implement using Gravy's awesome code!
        LANG_EN_US.entry("itemGroup." + groupIdentifier, "Ad Infinitum | " + WordUtils.capitalizeFully(StringUtils.removeStart(groupIdentifier, "ad-infinitum.group.")));

        if (itemGroup instanceof OwoItemGroup ig)
            ig.tabs.forEach(t -> LANG_EN_US.entry(
                    t.getTranslationKey(groupIdentifier),
                    ig.equals(AdInfinitumGroups.GROUP_SINGULARITIES) ? t.name().toUpperCase() : WordUtils.capitalizeFully(t.name()))
            );
    }

    public static void handleBlockRegistryContainer(String namespace, Block value, String identifier, Field field) {

        // Language registration.
        WithDisplayName withDisplayNameAnnotation = field.getAnnotation(WithDisplayName.class);
        String displayName = withDisplayNameAnnotation == null ? RegistryUtil.registryToDisplayName(identifier) : withDisplayNameAnnotation.name();
        LANG_EN_US.entry("block." + namespace + "." + identifier, displayName);

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
        WithDisplayName withDisplayNameAnnotation = field.getAnnotation(WithDisplayName.class);
        registerItem(namespace, identifier, item, withDisplayNameAnnotation == null ? RegistryUtil.registryToDisplayName(identifier) : withDisplayNameAnnotation.name());
    }

    private static void registerItem(String namespace, String identifier, Item item, String displayName) {

        LANG_EN_US.entry("item." + namespace + "." + identifier, displayName);

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
