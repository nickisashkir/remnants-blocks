package net.remnants.moreslabs;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public enum SlabTypes {
    DIRT("dirt", Blocks.DIRT, "Dirt"),
    COARSE_DIRT("coarse_dirt", Blocks.COARSE_DIRT, "Coarse Dirt"),
    GRASS_BLOCK("grass_block", Blocks.GRASS_BLOCK, "Grass"),
    PODZOL("podzol", Blocks.PODZOL, "Podzol"),
    AMETHYST_BLOCK("amethyst_block", Blocks.AMETHYST_BLOCK, "Amethyst"),
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
    BLACK_CONCRETE("black_concrete", Blocks.BLACK_CONCRETE, "Black Concrete");

    private final String id;
    private final Block displayBlock;
    private final String displayName;

    SlabTypes(String id, Block displayBlock, String displayName) {
        this.id = id;
        this.displayBlock = displayBlock;
        this.displayName = displayName;
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
     * Returns the vanilla texture path for the block (used in model generation).
     * Most blocks use "minecraft:block/{id}" but some have special names.
     */
    public String getTexturePath() {
        return "minecraft:block/" + id;
    }

    /**
     * For grass blocks, the top texture is different from the side.
     */
    public String getTopTexturePath() {
        return switch (this) {
            case GRASS_BLOCK -> "minecraft:block/grass_block_top";
            case PODZOL -> "minecraft:block/podzol_top";
            default -> getTexturePath();
        };
    }

    public String getSideTexturePath() {
        return switch (this) {
            case GRASS_BLOCK -> "minecraft:block/grass_block_side";
            case PODZOL -> "minecraft:block/podzol_side";
            default -> getTexturePath();
        };
    }

    public String getBottomTexturePath() {
        return switch (this) {
            case GRASS_BLOCK, PODZOL -> "minecraft:block/dirt";
            default -> getTexturePath();
        };
    }

    /**
     * Whether this slab type needs grass-style biome tinting on the display entity.
     */
    public boolean needsBiomeTint() {
        return this == GRASS_BLOCK;
    }
}
