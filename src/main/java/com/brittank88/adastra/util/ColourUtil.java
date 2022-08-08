package com.brittank88.adastra.util;

import com.brittank88.adastra.AdAstra;
import de.androidpit.colorthief.ColorThief;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.math.ColorHelper;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

public abstract class ColourUtil {

    private static final Map<Sprite, Integer> DOMINANT_RGB_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    public static int extractProminentRGB(SpriteIdentifier spriteId) {
        return extractProminentRGB(spriteId.getSprite());
    }

    public static int extractProminentRGB(Sprite sprite) {
        return DOMINANT_RGB_CACHE.computeIfAbsent(sprite, s -> {
            try (InputStream inputStream = new ByteArrayInputStream(s.images[0].getBytes())) {
                int[] dominantRGB = ColorThief.getColorMap(ImageIO.read(inputStream), 8)
                        .vboxes
                        .get(0)
                        .avg(false);
                return ColorHelper.Argb.getArgb(255, dominantRGB[0], dominantRGB[1], dominantRGB[2]);
            } catch (Exception e) {
                AdAstra.LOGGER.error("Error extracting dominant RGB for sprite " + sprite.getId(), e);
                return 0xFFFFFFFF;
            }
        });
    }
}
