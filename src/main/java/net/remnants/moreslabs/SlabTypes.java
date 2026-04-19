package net.remnants.moreslabs;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * Registry of all slab types this mod provides.
 *
 * <p>Entries fall into three flavors:
 * <ul>
 *   <li><b>Plain slabs</b> -- use the vanilla block's textures directly (dirt, concrete, terracotta, etc.)</li>
 *   <li><b>Tinted grass slabs</b> -- share the vanilla grass_block textures but baked with a specific RGB tint.
 *       The resource pack generator pre-bakes the texture, so no biome tinting is required.</li>
 * </ul>
 */
public enum SlabTypes {
    // ------------------------------- Natural -------------------------------
    DIRT("dirt", Blocks.DIRT, "Dirt"),
    COARSE_DIRT("coarse_dirt", Blocks.COARSE_DIRT, "Coarse Dirt"),
    PODZOL("podzol", Blocks.PODZOL, "Podzol"),

    // ------------------------------- Grass variants (baked tints) ---------
    // Colors chosen to match distinctive Terralith biomes
    NATURAL_GRASS("natural_grass", Blocks.GRASS_BLOCK, "Grass", 0x79C05A),
    SCARLET_GRASS("scarlet_grass", Blocks.GRASS_BLOCK, "Scarlet Grass", 0x901222),
    AMETHYST_GRASS("amethyst_grass", Blocks.GRASS_BLOCK, "Amethyst Grass", 0x70D484),
    MIRAGE_GRASS("mirage_grass", Blocks.GRASS_BLOCK, "Mirage Grass", 0x2AB2C7),
    LAVENDER_GRASS("lavender_grass", Blocks.GRASS_BLOCK, "Lavender Grass", 0x9BE0BC),
    WARPED_GRASS("warped_grass", Blocks.GRASS_BLOCK, "Warped Grass", 0xBEAFDD),
    MOONLIGHT_GRASS("moonlight_grass", Blocks.GRASS_BLOCK, "Moonlight Grass", 0x9EB2E1),
    EMERALD_PEAKS_GRASS("emerald_peaks_grass", Blocks.GRASS_BLOCK, "Emerald Peaks Grass", 0x6783AF),
    SAKURA_GRASS("sakura_grass", Blocks.GRASS_BLOCK, "Sakura Grass", 0x79CF85),
    AUTUMN_GRASS("autumn_grass", Blocks.GRASS_BLOCK, "Autumn Grass", 0xDFD43D),
    DESERT_GRASS("desert_grass", Blocks.GRASS_BLOCK, "Desert Grass", 0xDCAC56),
    ASHEN_GRASS("ashen_grass", Blocks.GRASS_BLOCK, "Ashen Grass", 0xC0BBAC),

    // ------------------------------- Stone -----------------------------------
    AMETHYST_BLOCK("amethyst_block", Blocks.AMETHYST_BLOCK, "Amethyst"),
    OBSIDIAN("obsidian", Blocks.OBSIDIAN, "Obsidian"),
    CRYING_OBSIDIAN("crying_obsidian", Blocks.CRYING_OBSIDIAN, "Crying Obsidian"),

    // ------------------------------- Concrete (16) ---------------------------
    WHITE_CONCRETE("white_concrete", Blocks.WHITE_CONCRETE, "White Concrete"),
    ORANGE_CONCRETE("orange_concrete", Blocks.ORANGE_CONCRETE, "Orange Concrete"),
    MAGENTA_CONCRETE("magenta_concrete", Blocks.MAGENTA_CONCRETE, "Magenta Concrete"),
    LIGHT_BLUE_CONCRETE("light_blue_concrete", Blocks.LIGHT_BLUE_CONCRETE, "Light Blue Concrete"),
    YELLOW_CONCRETE("yellow_concrete", Blocks.YELLOW_CONCRETE, "Yellow Concrete"),
    LIME_CONCRETE("lime_concrete", Blocks.LIME_CONCRETE, "Lime Concrete"),
    PINK_CONCRETE("pink_concrete", Blocks.PINK_CONCRETE, "Pink Concrete"),
    GRAY_CONCRETE("gray_concrete", Blocks.GRAY_CONCRETE, "Gray Concrete"),
    LIGHT_GRAY_CONCRETE("light_gray_concrete", Blocks.LIGHT_GRAY_CONCRETE, "Light Gray Concrete"),
    CYAN_CONCRETE("cyan_concrete", Blocks.CYAN_CONCRETE, "Cyan Concrete"),
    PURPLE_CONCRETE("purple_concrete", Blocks.PURPLE_CONCRETE, "Purple Concrete"),
    BLUE_CONCRETE("blue_concrete", Blocks.BLUE_CONCRETE, "Blue Concrete"),
    BROWN_CONCRETE("brown_concrete", Blocks.BROWN_CONCRETE, "Brown Concrete"),
    GREEN_CONCRETE("green_concrete", Blocks.GREEN_CONCRETE, "Green Concrete"),
    RED_CONCRETE("red_concrete", Blocks.RED_CONCRETE, "Red Concrete"),
    BLACK_CONCRETE("black_concrete", Blocks.BLACK_CONCRETE, "Black Concrete"),

    // ------------------------------- Terracotta (plain + 16) -----------------
    TERRACOTTA("terracotta", Blocks.TERRACOTTA, "Terracotta"),
    WHITE_TERRACOTTA("white_terracotta", Blocks.WHITE_TERRACOTTA, "White Terracotta"),
    ORANGE_TERRACOTTA("orange_terracotta", Blocks.ORANGE_TERRACOTTA, "Orange Terracotta"),
    MAGENTA_TERRACOTTA("magenta_terracotta", Blocks.MAGENTA_TERRACOTTA, "Magenta Terracotta"),
    LIGHT_BLUE_TERRACOTTA("light_blue_terracotta", Blocks.LIGHT_BLUE_TERRACOTTA, "Light Blue Terracotta"),
    YELLOW_TERRACOTTA("yellow_terracotta", Blocks.YELLOW_TERRACOTTA, "Yellow Terracotta"),
    LIME_TERRACOTTA("lime_terracotta", Blocks.LIME_TERRACOTTA, "Lime Terracotta"),
    PINK_TERRACOTTA("pink_terracotta", Blocks.PINK_TERRACOTTA, "Pink Terracotta"),
    GRAY_TERRACOTTA("gray_terracotta", Blocks.GRAY_TERRACOTTA, "Gray Terracotta"),
    LIGHT_GRAY_TERRACOTTA("light_gray_terracotta", Blocks.LIGHT_GRAY_TERRACOTTA, "Light Gray Terracotta"),
    CYAN_TERRACOTTA("cyan_terracotta", Blocks.CYAN_TERRACOTTA, "Cyan Terracotta"),
    PURPLE_TERRACOTTA("purple_terracotta", Blocks.PURPLE_TERRACOTTA, "Purple Terracotta"),
    BLUE_TERRACOTTA("blue_terracotta", Blocks.BLUE_TERRACOTTA, "Blue Terracotta"),
    BROWN_TERRACOTTA("brown_terracotta", Blocks.BROWN_TERRACOTTA, "Brown Terracotta"),
    GREEN_TERRACOTTA("green_terracotta", Blocks.GREEN_TERRACOTTA, "Green Terracotta"),
    RED_TERRACOTTA("red_terracotta", Blocks.RED_TERRACOTTA, "Red Terracotta"),
    BLACK_TERRACOTTA("black_terracotta", Blocks.BLACK_TERRACOTTA, "Black Terracotta");

    private final String id;
    private final Block displayBlock;
    private final String displayName;
    private final int tintColor; // 0 = no tint, otherwise RGB

    SlabTypes(String id, Block displayBlock, String displayName) {
        this(id, displayBlock, displayName, 0);
    }

    SlabTypes(String id, Block displayBlock, String displayName, int tintColor) {
        this.id = id;
        this.displayBlock = displayBlock;
        this.displayName = displayName;
        this.tintColor = tintColor;
    }

    public String getId() {
        return id;
    }

    public String getSlabId() {
        return id + "_slab";
    }

    public Block getDisplayBlock() {
        return displayBlock;
    }

    public String getDisplayName() {
        return displayName + " Slab";
    }

    /**
     * Whether this slab uses baked-tint grass textures (one of the grass color variants).
     */
    public boolean isTintedGrass() {
        return tintColor != 0 && displayBlock == Blocks.GRASS_BLOCK;
    }

    public int getTintColor() {
        return tintColor;
    }

    /**
     * Returns the texture identifier for the top face. For tinted grass variants this points
     * to our mod's generated texture; otherwise the vanilla texture.
     */
    public String getTopTexturePath() {
        if (isTintedGrass()) {
            return MoreSlabs.MODID + ":block/" + id + "_top";
        }
        return "minecraft:block/" + id;
    }

    public String getSideTexturePath() {
        if (isTintedGrass()) {
            return MoreSlabs.MODID + ":block/" + id + "_side";
        }
        return "minecraft:block/" + id;
    }

    public String getBottomTexturePath() {
        if (isTintedGrass()) {
            return "minecraft:block/dirt";
        }
        return "minecraft:block/" + id;
    }
}
