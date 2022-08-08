package com.brittank88.adastra.api.item;

import com.brittank88.adastra.AdAstra;
import com.brittank88.adastra.group.AdAstraGroups;
import com.brittank88.adastra.util.ColourUtil;
import com.brittank88.adastra.util.NumeralUtil;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * Abstract implementation of a singularity item.
 * <br /><br />
 * Allows for the creation of singularity items with custom properties.
 *
 * @author brittank88
 */
@SuppressWarnings("unused")
public abstract class AbstractSingularityItem extends Item implements IHaloRenderItem, BuiltinItemRendererRegistry.DynamicItemRenderer {

    public static final SpriteIdentifier DEFAULT_BASE_SPRITE_ID      = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, AdAstra.id("item/singularity/base"));
    public static final SpriteIdentifier DEFAULT_CORE_SPRITE_ID      = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, AdAstra.id("item/singularity/core"));
    public static final float            DEFAULT_BASE_ROTATION_SPEED =  1F                                                                                               ;
    public static final float            DEFAULT_CORE_ROTATION_SPEED = -0.5F                                                                                             ;
    public static final int              DEFAULT_TIER                = 1                                                                                                 ;
    public static final int              DEFAULT_MAXIMUM_TIER        = 5                                                                                                 ;
    public static final OwoItemSettings  DEFAULT_SETTINGS            = new OwoItemSettings().rarity(Rarity.COMMON).group(AdAstraGroups.GROUP_SINGULARITIES).tab(0)       ;

    private final SpriteIdentifier baseSpriteID           ;
    private final SpriteIdentifier coreSpriteID           ;
    private final float            baseSpriteRotationSpeed;
    private final float            coreSpriteRotationSpeed;
    private final int              tier                   ;
    private final ItemStack        material               ;

    private final OwoItemSettings settings;

    /**
     * Constructs for classes extending {@link AbstractSingularityItem}.
     *
     * @param settings The {@link Settings} to use.
     * @param material The {@link ItemStack} to use as the material.
     * @param tier The tier of the singularity.
     * @param baseSpriteID The {@link SpriteIdentifier} of the base sprite.
     * @param coreSpriteID The {@link SpriteIdentifier} of the core sprite.
     * @param baseSpriteRotationSpeed The speed at which the base sprite rotates (negative for CCW rotation).
     * @param coreSpriteRotationSpeed The speed at which the core sprite rotates (negative for CCW rotation).
     */
    public AbstractSingularityItem(ItemStack material, OwoItemSettings settings, int tier, SpriteIdentifier baseSpriteID, SpriteIdentifier coreSpriteID, float baseSpriteRotationSpeed, float coreSpriteRotationSpeed) {
        super(settings);
        this.settings = settings;

        this.material = material;
        this.tier = tier;
        this.baseSpriteID = baseSpriteID;
        this.coreSpriteID = coreSpriteID;
        this.baseSpriteRotationSpeed = baseSpriteRotationSpeed;
        this.coreSpriteRotationSpeed = coreSpriteRotationSpeed;

        BuiltinItemRendererRegistry.INSTANCE.register(this, this);
    }

    public AbstractSingularityItem(ItemStack material, OwoItemSettings settings, int tier, SpriteIdentifier baseSpriteID, SpriteIdentifier coreSpriteID) {
        this(material, settings, tier, baseSpriteID, coreSpriteID, DEFAULT_BASE_ROTATION_SPEED, DEFAULT_CORE_ROTATION_SPEED);
    }
    public AbstractSingularityItem(ItemStack material, OwoItemSettings settings, int tier) {
        this(material, settings, tier, DEFAULT_BASE_SPRITE_ID, DEFAULT_CORE_SPRITE_ID, DEFAULT_BASE_ROTATION_SPEED, DEFAULT_CORE_ROTATION_SPEED);
    }
    public AbstractSingularityItem(ItemStack material, OwoItemSettings settings) {
        this(material, settings, DEFAULT_TIER, DEFAULT_BASE_SPRITE_ID, DEFAULT_CORE_SPRITE_ID, DEFAULT_BASE_ROTATION_SPEED, DEFAULT_CORE_ROTATION_SPEED);
    }
    public AbstractSingularityItem(ItemStack material) {
        this(material, DEFAULT_SETTINGS, DEFAULT_TIER, DEFAULT_BASE_SPRITE_ID, DEFAULT_CORE_SPRITE_ID, DEFAULT_BASE_ROTATION_SPEED, DEFAULT_CORE_ROTATION_SPEED);
    }

    /**
     * Returns the singularity base {@link SpriteIdentifier} - by default, {@link #DEFAULT_BASE_SPRITE_ID}.
     *
     * @return The {@link SpriteIdentifier} for the base sprite.
     */
    public SpriteIdentifier getBaseSprite() { return baseSpriteID; }

    /**
     * Returns the singularity core {@link SpriteIdentifier} - by default, {@link #DEFAULT_CORE_SPRITE_ID}.
     *
     * @return The {@link SpriteIdentifier} for the core sprite.
     */
    public SpriteIdentifier getCoreSprite() { return coreSpriteID; }

    /**
     * Returns the singularity base rotation speed in degrees/tick - by default, {@link #DEFAULT_BASE_ROTATION_SPEED}.
     * <br />
     * <br />Negative values will rotate the sprite counter-clockwise.
     * <br />Positive values will rotate the sprite clockwise.
     * <br />A speed of 0 will not rotate the sprite.
     *
     * @return The rotation speed (in deg/t) of the base sprite (negative is CCW rotation).
     */
    public float getBaseSpriteRotationSpeed() { return baseSpriteRotationSpeed; }

    /**
     * Returns the singularity core rotation speed in degrees/tick - by default, {@link #DEFAULT_CORE_ROTATION_SPEED}.
     * <br />
     * <br />Negative values will rotate the sprite counter-clockwise.
     * <br />Positive values will rotate the sprite clockwise.
     * <br />A speed of 0 will not rotate the sprite.
     *
     * @return The rotation speed (in deg/t) of the core sprite (negative is CCW rotation).
     */
    public float getCoreSpriteRotationSpeed() { return coreSpriteRotationSpeed; }

    /**
     * Returns the tier of the singularity - by default, {@link #DEFAULT_TIER}.
     *
     * @return The tier of the singularity.
     */
    public int getTier() { return tier; }

    /**
     * Returns the tier of the singularity, expressed as capitalised Roman numerals.
     *
     * @return The tier of the singularity, expressed as capitalised Roman numerals.
     * @see NumeralUtil.RomanNumeral#toRoman(int)
     */
    public String getTierNumeral() { return NumeralUtil.RomanNumeral.toRoman(this.tier); }

    /**
     * A version of {@link AbstractSingularityItem#getTierNumeral()} that takes a {@link Function postProcessor} to transform the resulting Roman numerals.
     *
     * @param postProcessor The {@link Function} to transform the resulting Roman numerals.
     * @return The tier of the singularity, expressed as capitalised Roman numerals.
     * @see AbstractSingularityItem#getTierNumeral()
     */
    public String getTierNumeral(Function<String, String> postProcessor) { return postProcessor.apply(this.getTierNumeral()); }

    /**
     * Returns the material of the singularity.
     *
     * @return The material of the singularity.
     */
    public ItemStack getMaterial() { return material.copy(); }

    /**
     * Returns the colour of the singularity as an ARGB integer.
     * <br /><br />
     * The colour is determined by the most dominant colour of the {@link AbstractSingularityItem}'s {@link ItemStack material}'s {@link net.minecraft.client.texture.Sprite Sprite}.
     *
     * @return The singularity colour as an ARGB integer.
     */
    public int getColor() { return ColourUtil.extractProminentRGB(new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Registry.ITEM.getId(this.material.getItem()))); }

    /**
     * Returns the {@link net.minecraft.item.Item.Settings Settings} of the singularity's {@link Item}.
     *
     * @return The {@link net.minecraft.item.Item.Settings Settings} of the singularity's {@link Item}.
     */
    public OwoItemSettings getSettings() { return this.settings; }

    /**
     * Generates the remaining tiers, up to the default {@link #DEFAULT_MAXIMUM_TIER}. <u>Excludes tier 1.</u>
     *
     * @return The remaining tiers of singularities, up to the default {@link #DEFAULT_MAXIMUM_TIER}.
     * @see #generateUpToTier(int)
     */
    public List<? extends AbstractSingularityItem> generateRemainingTiers() { return this.generateUpToTier(DEFAULT_MAXIMUM_TIER); }

    /**
     * Generates the remaining tiers, up to the specified tier. <u>Excludes tier 1.</u>
     *
     * @param tier The maximum tier to generate.
     * @return The remaining tiers of singularities, up to the specified tier.
     */
    public abstract List<? extends AbstractSingularityItem> generateUpToTier(int tier);

    private final AtomicBoolean getTickFailed = new AtomicBoolean(false);

    /**
     * Render function for singularities.
     *
     * @param stack The {@link ItemStack}.
     * @param mode The {@link ModelTransformation.Mode}.
     * @param matrices The {@link MatrixStack}.
     * @param vertexConsumers The {@link VertexConsumerProvider}.
     * @param light The lighting integer.
     * @param overlay The overlay integer.
     */
    @Override public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        // Get the singularity's colour.
        int colour = this.getColor();
        float r = ColorHelper.Argb.getRed(colour);
        float g = ColorHelper.Argb.getGreen(colour);
        float b = ColorHelper.Argb.getBlue(colour);
        float a = ColorHelper.Argb.getAlpha(colour);

        // Get current tick for rotation.
        long ticks = 0;
        if (!getTickFailed.get()) {
            try {
                MinecraftClient mc = MinecraftClient.getInstance();
                MinecraftServer server;
                if (mc.isIntegratedServerRunning()) server = mc.getServer();
                else {
                    ClientPlayerEntity player = MinecraftClient.getInstance().player;
                    if (player == null) throw new NullPointerException("Player is null.");
                    server = player.getServer();
                }
                if (server == null) throw new NullPointerException("Server is null.");
                ticks = server.getTicks();
            } catch (NullPointerException e) {
                AdAstra.LOGGER.error("Could not get server or player ticks. Using 0 as ticks.", e);
                getTickFailed.set(true);
            }
        }

        this.renderBase(stack, mode, matrices, vertexConsumers, light, overlay, r, g, b, a, ticks);
        this.renderCore(stack, mode, matrices, vertexConsumers, light, overlay, r, g, b, a, ticks);
    }

    private void renderBase(
            ItemStack stack,
            ModelTransformation.Mode mode,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light, int overlay,
            float r, float g, float b, float a,
            long ticks
    ) {

        // Push matrices.
        matrices.push();

        // Rotate matrices.
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(ticks * this.getBaseSpriteRotationSpeed()));

        // Get current normal and position matrix.
        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
        Matrix3f normalMatrix = matrices.peek().getNormalMatrix();

        // Generate vectors for transformation.
        Vector4f v1 = new Vector4f(1, 1, 0, 1);
        Vector4f v2 = new Vector4f(0, 1, 0, 1);
        Vector4f v3 = new Vector4f(0, 0, 0, 1);
        Vector4f v4 = new Vector4f(1, 0, 0, 1);

        // Transform vectors by the position matrix.
        v1.transform(positionMatrix);
        v2.transform(positionMatrix);
        v3.transform(positionMatrix);
        v4.transform(positionMatrix);

        // Draw base sprite.
        VertexConsumer baseConsumer = this.baseSpriteID.getVertexConsumer(vertexConsumers, RenderLayer::getItemEntityTranslucentCull);
        baseConsumer.vertex(v1.getX(), v1.getY(), v1.getZ(), r, g, b, a, 1, 0, overlay, light, 0, 0, 1);
        baseConsumer.vertex(v2.getX(), v2.getY(), v2.getZ(), r, g, b, a, 0, 0, overlay, light, 0, 0, 1);
        baseConsumer.vertex(v3.getX(), v3.getY(), v3.getZ(), r, g, b, a, 0, 1, overlay, light, 0, 0, 1);
        baseConsumer.vertex(v4.getX(), v4.getY(), v4.getZ(), r, g, b, a, 1, 1, overlay, light, 0, 0, 1);

        // Pop matrices.
        matrices.pop();
    }

    private void renderCore(
            ItemStack stack,
            ModelTransformation.Mode mode,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light, int overlay,
            float r, float g, float b, float a,
            long ticks
    ) {

        // Push and rotate matrices.
        matrices.push();
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(ticks * this.getCoreSpriteRotationSpeed()));

        // Get current normal and position matrix.
        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
        Matrix3f normalMatrix = matrices.peek().getNormalMatrix();

        // Generate vectors for transformation.
        Vector4f v1 = new Vector4f(1, 1, 0.1F, 1);
        Vector4f v2 = new Vector4f(0, 1, 0.1F, 1);
        Vector4f v3 = new Vector4f(0, 0, 0.1F, 1);
        Vector4f v4 = new Vector4f(1, 0, 0.1F, 1);

        // Transform vectors by the position matrix.
        v1.transform(positionMatrix);
        v2.transform(positionMatrix);
        v3.transform(positionMatrix);
        v4.transform(positionMatrix);

        // Draw core sprite.
        VertexConsumer coreConsumer = this.coreSpriteID.getVertexConsumer(vertexConsumers, RenderLayer::getItemEntityTranslucentCull);
        coreConsumer.vertex(v1.getX(), v1.getY(), v1.getZ(), r, g, b, a, 1, 0, overlay, light, 0, 0, 1);
        coreConsumer.vertex(v2.getX(), v2.getY(), v2.getZ(), r, g, b, a, 0, 0, overlay, light, 0, 0, 1);
        coreConsumer.vertex(v3.getX(), v3.getY(), v3.getZ(), r, g, b, a, 0, 1, overlay, light, 0, 0, 1);
        coreConsumer.vertex(v4.getX(), v4.getY(), v4.getZ(), r, g, b, a, 1, 1, overlay, light, 0, 0, 1);

        // Pop matrices.
        matrices.pop();
    }
}
