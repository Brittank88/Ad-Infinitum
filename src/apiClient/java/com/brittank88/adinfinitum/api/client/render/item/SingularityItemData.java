package com.brittank88.adinfinitum.api.client.render.item;

import com.brittank88.adinfinitum.AdInfinitum;
import com.brittank88.adinfinitum.api.registry.singularity.SingularityItem;
import com.brittank88.adinfinitum.util.WordUtils;
import com.brittank88.adinfinitum.util.client.ColourUtil;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.function.ToFloatFunction;
import net.minecraft.util.registry.Registry;

import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.ToIntFunction;

public record SingularityItemData(
        Function<ItemStack, String>           displayName         ,
        Function<ItemStack, SpriteIdentifier> baseSpriteIdentifier,
        Function<ItemStack, SpriteIdentifier> coreSpriteIdentifier,
        ToFloatFunction<ItemStack>            baseRotationSpeed   ,
        ToFloatFunction<ItemStack>            coreRotationSpeed   ,
        ToIntFunction  <ItemStack>            colourFunction
) {

    public static class Builder {

        /**
         * The display name of the singularity.
         * <br /><br />
         * By default, this is determined from the singularity's material's registry ID.
         */
        private final Function<ItemStack, String> displayName = stack ->
                stack.getItem() instanceof SingularityItem si
                        ? WordUtils.capitalizeFully(
                                Registry.ITEM.getId(si.getMaterial().getItem())
                                        .getPath()
                                        .replaceFirst(" (block|ingot|dust|nugget|ore|rod|powder)", "")
                        ) + " [MK-" + si.getTierNumeral() + ']'
                        : "Unknown Singularity";

        /**
         * The singularity base {@link SpriteIdentifier}.
         * <br /><br />
         * By default, this is the sprite provided by Ad Infinitum.
         */
        private Function<ItemStack, SpriteIdentifier> baseSpriteIdentifier = stack -> new SpriteIdentifier(
                PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, AdInfinitum.id("textures/item/singularity/base.png")
        );

        /**
         * The singularity core {@link SpriteIdentifier}.
         * <br /><br />
         * By default, this is the sprite provided by Ad Infinitum.
         */
        private Function<ItemStack, SpriteIdentifier> coreSpriteIdentifier = stack -> new SpriteIdentifier(
                PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, AdInfinitum.id("textures/item/singularity/core.png")
        );

        /**
         * The singularity base rotation speed in degrees/tick - by default, 1.
         * <br />
         * <br />Negative values will rotate the sprite counter-clockwise.
         * <br />Positive values will rotate the sprite clockwise.
         * <br />A speed of 0 will not rotate the sprite.
         */
        private ToFloatFunction<ItemStack> baseRotationSpeed = stack -> 1f;

        /**
         * The singularity core rotation speed in degrees/tick - by default, -0.5.
         * <br />
         * <br />Negative values will rotate the sprite counter-clockwise.
         * <br />Positive values will rotate the sprite clockwise.
         * <br />A speed of 0 will not rotate the sprite.
         */
        private ToFloatFunction<ItemStack> coreRotationSpeed = stack -> 0.5f;

        /**
         * The colour of the singularity as an ARGB integer.
         * <br /><br />
         * The colour is determined by the most dominant colour of the {@link SingularityItem}'s {@link ItemStack material}'s {@link net.minecraft.client.texture.Sprite Sprite}.
         */
        private ToIntFunction<ItemStack> colourFunction = stack -> stack.getItem() instanceof SingularityItem si
                ? ColourUtil.extractProminentRGB(new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Registry.ITEM.getId(si.getMaterial().getItem())))
                : ColourUtil.DEFAULT_COLOUR;

        public Builder setBaseSpriteIdentifier(Function<ItemStack, SpriteIdentifier> baseSpriteIdentifier) {
            this.baseSpriteIdentifier = baseSpriteIdentifier;
            return this;
        }

        public Builder setCoreSpriteIdentifier(Function<ItemStack, SpriteIdentifier> coreSpriteIdentifier) {
            this.coreSpriteIdentifier = coreSpriteIdentifier;
            return this;
        }

        public Builder setBaseRotationSpeed(ToFloatFunction<ItemStack> baseRotationSpeed) {
            this.baseRotationSpeed = baseRotationSpeed;
            return this;
        }

        public Builder setCoreRotationSpeed(ToFloatFunction<ItemStack> coreRotationSpeed) {
            this.coreRotationSpeed = coreRotationSpeed;
            return this;
        }

        public Builder setColour(int colour) { return setColour(stack -> colour); }
        public Builder setColour(IntSupplier colourSupplier) { return setColour(stack -> colourSupplier.getAsInt()); }
        public Builder setColour(ToIntFunction<ItemStack> colourFunction) {
            this.colourFunction = colourFunction;
            return this;
        }

        public SingularityItemData build() {
            return new SingularityItemData(displayName, baseSpriteIdentifier, coreSpriteIdentifier, baseRotationSpeed, coreRotationSpeed, colourFunction);
        }
    }
}
