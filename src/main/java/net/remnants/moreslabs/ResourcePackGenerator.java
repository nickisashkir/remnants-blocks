package net.remnants.moreslabs;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.api.ResourcePackBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Generates resource pack assets programmatically during pack creation.
 *
 * <p>Generated assets:
 * <ul>
 *   <li>Block models (bottom, top, double variants per slab)</li>
 *   <li>Item model definitions (the tooltip shown in inventory)</li>
 *   <li>Baked, tinted grass textures for each grass color variant</li>
 * </ul>
 */
public class ResourcePackGenerator {

    private static final String GRASS_TOP_SOURCE = "assets/" + MoreSlabs.MODID + "/textures/source/grass_block_top.png";
    private static final String GRASS_OVERLAY_SOURCE = "assets/" + MoreSlabs.MODID + "/textures/source/grass_block_side_overlay.png";

    public static void register() {
        PolymerResourcePackUtils.RESOURCE_PACK_CREATION_EVENT.register(builder -> {
            bakeGrassTextures(builder);
            for (SlabTypes type : SlabTypes.values()) {
                generateBlockModels(builder, type);
                generateItemDefinition(builder, type);
            }
        });
    }

    // ------------------------- Grass texture baking -------------------------

    private static void bakeGrassTextures(ResourcePackBuilder builder) {
        byte[] topSource = loadResource(GRASS_TOP_SOURCE);
        byte[] overlaySource = loadResource(GRASS_OVERLAY_SOURCE);
        if (topSource == null || overlaySource == null) {
            MoreSlabs.LOGGER.warn("Grass source textures missing from classpath; grass tint variants will not render correctly.");
            return;
        }

        for (SlabTypes type : SlabTypes.values()) {
            if (!type.isTintedGrass()) continue;

            try {
                // 1. Baked top texture (dirt-like grass top, tinted to variant color)
                byte[] topTinted = tintPng(topSource, type.getTintColor());
                builder.addData("assets/" + MoreSlabs.MODID + "/textures/block/" + type.getId() + "_top.png", topTinted);

                // 2. Side texture is dirt base with a tinted grass overlay blended on top
                byte[] overlayTinted = tintPng(overlaySource, type.getTintColor());
                byte[] dirtBytes = loadVanillaTexture("assets/minecraft/textures/block/dirt.png");
                byte[] sideComposite = dirtBytes != null
                        ? blendOver(dirtBytes, overlayTinted)
                        : overlayTinted; // fallback: just the tinted overlay
                builder.addData("assets/" + MoreSlabs.MODID + "/textures/block/" + type.getId() + "_side.png", sideComposite);

            } catch (IOException e) {
                MoreSlabs.LOGGER.error("Failed to bake grass textures for {}", type, e);
            }
        }
    }

    /**
     * Multiply each pixel's RGB by the given color (preserving alpha).
     * Used to apply a static tint to vanilla grass textures.
     */
    private static byte[] tintPng(byte[] source, int color) throws IOException {
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(source));
        int cr = (color >> 16) & 0xFF;
        int cg = (color >> 8) & 0xFF;
        int cb = color & 0xFF;

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int argb = img.getRGB(x, y);
                int a = (argb >> 24) & 0xFF;
                if (a == 0) continue;
                int r = ((argb >> 16) & 0xFF) * cr / 255;
                int g = ((argb >> 8) & 0xFF) * cg / 255;
                int b = (argb & 0xFF) * cb / 255;
                img.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(img, "PNG", bos);
        return bos.toByteArray();
    }

    /**
     * Blend overlay on top of base (standard alpha compositing).
     */
    private static byte[] blendOver(byte[] baseBytes, byte[] overlayBytes) throws IOException {
        BufferedImage base = ImageIO.read(new ByteArrayInputStream(baseBytes));
        BufferedImage overlay = ImageIO.read(new ByteArrayInputStream(overlayBytes));
        int w = Math.min(base.getWidth(), overlay.getWidth());
        int h = Math.min(base.getHeight(), overlay.getHeight());

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int ba = base.getRGB(x, y);
                int oa = overlay.getRGB(x, y);
                int oAlpha = (oa >> 24) & 0xFF;
                if (oAlpha == 0) continue;
                int or = (oa >> 16) & 0xFF;
                int og = (oa >> 8) & 0xFF;
                int ob = oa & 0xFF;
                int br = (ba >> 16) & 0xFF;
                int bg = (ba >> 8) & 0xFF;
                int bb = ba & 0xFF;
                // Over operator: out = overlay + base * (1 - overlayAlpha)
                int r = (or * oAlpha + br * (255 - oAlpha)) / 255;
                int g = (og * oAlpha + bg * (255 - oAlpha)) / 255;
                int b = (ob * oAlpha + bb * (255 - oAlpha)) / 255;
                base.setRGB(x, y, (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(base, "PNG", bos);
        return bos.toByteArray();
    }

    private static byte[] loadResource(String path) {
        try (InputStream is = ResourcePackGenerator.class.getClassLoader().getResourceAsStream(path)) {
            return is == null ? null : is.readAllBytes();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Attempts to load a vanilla texture for compositing. Returns null if not available
     * (e.g. dedicated servers don't ship client assets) -- caller must handle fallback.
     */
    private static byte[] loadVanillaTexture(String path) {
        // Try loading from classpath (works in dev env where the client jar is merged)
        byte[] data = loadResource(path);
        if (data != null) return data;

        // Fallback: no vanilla textures available at runtime on a dedicated server.
        // We handle this by shipping the dirt texture ourselves (see source folder).
        return loadResource("assets/" + MoreSlabs.MODID + "/textures/source/dirt.png");
    }

    // ------------------------- Block + item model generation ----------------

    private static void generateBlockModels(ResourcePackBuilder builder, SlabTypes type) {
        String id = type.getSlabId();
        String bottom = type.getBottomTexturePath();
        String top = type.getTopTexturePath();
        String side = type.getSideTexturePath();

        builder.addStringData("assets/" + MoreSlabs.MODID + "/models/block/" + id + ".json",
                slabModel("minecraft:block/slab", bottom, top, side));
        builder.addStringData("assets/" + MoreSlabs.MODID + "/models/block/" + id + "_top.json",
                slabModel("minecraft:block/slab_top", bottom, top, side));
        builder.addStringData("assets/" + MoreSlabs.MODID + "/models/block/" + id + "_double.json",
                cubeModel(bottom, top, side));
    }

    private static void generateItemDefinition(ResourcePackBuilder builder, SlabTypes type) {
        String id = type.getSlabId();
        String modelRef = MoreSlabs.MODID + ":block/" + id;
        builder.addStringData("assets/" + MoreSlabs.MODID + "/items/" + id + ".json", """
                {
                  "model": {
                    "type": "minecraft:model",
                    "model": "%s"
                  }
                }
                """.formatted(modelRef));
    }

    private static String slabModel(String parent, String bottom, String top, String side) {
        return """
                {
                  "parent": "%s",
                  "textures": {
                    "bottom": "%s",
                    "top": "%s",
                    "side": "%s"
                  }
                }
                """.formatted(parent, bottom, top, side);
    }

    private static String cubeModel(String bottom, String top, String side) {
        if (bottom.equals(top) && top.equals(side)) {
            return """
                    {
                      "parent": "minecraft:block/cube_all",
                      "textures": {
                        "all": "%s"
                      }
                    }
                    """.formatted(bottom);
        }
        return """
                {
                  "parent": "minecraft:block/cube_bottom_top",
                  "textures": {
                    "bottom": "%s",
                    "top": "%s",
                    "side": "%s"
                  }
                }
                """.formatted(bottom, top, side);
    }
}
