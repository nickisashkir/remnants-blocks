package net.remnants.moreslabs;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.api.ResourcePackBuilder;

/**
 * Generates resource pack assets programmatically during pack creation.
 *
 * Generated assets:
 * - Invisible petrified_oak_slab blockstate (base block clients see)
 * - Slab block models (bottom/top variants per type)
 * - Custom grass slab model with tintindex + side overlay
 * - Item definitions (what players see in inventory)
 */
public class ResourcePackGenerator {

    public static void register() {
        PolymerResourcePackUtils.RESOURCE_PACK_CREATION_EVENT.register(builder -> {
            generateInvisibleSlab(builder);
            generateGrassSlabTemplates(builder);
            for (SlabTypes type : SlabTypes.values()) {
                generateBlockModels(builder, type);
                generateItemDefinition(builder, type);
            }
        });
    }

    private static void generateInvisibleSlab(ResourcePackBuilder builder) {
        builder.addStringData("assets/minecraft/models/block/more_slabs_invisible.json", """
                {
                  "textures": {
                    "particle": "minecraft:block/stone"
                  }
                }
                """);

        builder.addStringData("assets/minecraft/blockstates/petrified_oak_slab.json", """
                {
                  "variants": {
                    "type=bottom,waterlogged=false": { "model": "minecraft:block/more_slabs_invisible" },
                    "type=bottom,waterlogged=true": { "model": "minecraft:block/more_slabs_invisible" },
                    "type=double,waterlogged=false": { "model": "minecraft:block/more_slabs_invisible" },
                    "type=double,waterlogged=true": { "model": "minecraft:block/more_slabs_invisible" },
                    "type=top,waterlogged=false": { "model": "minecraft:block/more_slabs_invisible" },
                    "type=top,waterlogged=true": { "model": "minecraft:block/more_slabs_invisible" }
                  }
                }
                """);
    }

    /**
     * Generates custom grass slab templates with tintindex on top face and
     * side overlay for proper biome coloring in the item model.
     */
    private static void generateGrassSlabTemplates(ResourcePackBuilder builder) {
        String ns = MoreSlabs.MODID;

        // Bottom grass slab with tintindex + side overlay
        builder.addStringData("assets/" + ns + "/models/block/grass_block_slab.json", GRASS_SLAB_BOTTOM);

        // Top grass slab
        builder.addStringData("assets/" + ns + "/models/block/grass_block_slab_top.json", GRASS_SLAB_TOP);
    }

    // Custom grass slab model (bottom) with tintindex on top face and side overlay
    private static final String GRASS_SLAB_BOTTOM = """
            {
              "parent": "minecraft:block/block",
              "textures": {
                "bottom": "minecraft:block/dirt",
                "top": "minecraft:block/grass_block_top",
                "side": "minecraft:block/grass_block_side",
                "overlay": "minecraft:block/grass_block_side_overlay",
                "particle": "minecraft:block/dirt"
              },
              "elements": [
                {
                  "from": [0, 0, 0],
                  "to": [16, 8, 16],
                  "faces": {
                    "down":  { "uv": [0, 0, 16, 16], "texture": "#bottom", "cullface": "down" },
                    "up":    { "uv": [0, 0, 16, 16], "texture": "#top", "cullface": "up", "tintindex": 0 },
                    "north": { "uv": [0, 8, 16, 16], "texture": "#side", "cullface": "north" },
                    "south": { "uv": [0, 8, 16, 16], "texture": "#side", "cullface": "south" },
                    "west":  { "uv": [0, 8, 16, 16], "texture": "#side", "cullface": "west" },
                    "east":  { "uv": [0, 8, 16, 16], "texture": "#side", "cullface": "east" }
                  }
                },
                {
                  "from": [0, 0, 0],
                  "to": [16, 8, 16],
                  "faces": {
                    "north": { "uv": [0, 8, 16, 16], "texture": "#overlay", "cullface": "north", "tintindex": 0 },
                    "south": { "uv": [0, 8, 16, 16], "texture": "#overlay", "cullface": "south", "tintindex": 0 },
                    "west":  { "uv": [0, 8, 16, 16], "texture": "#overlay", "cullface": "west", "tintindex": 0 },
                    "east":  { "uv": [0, 8, 16, 16], "texture": "#overlay", "cullface": "east", "tintindex": 0 }
                  }
                }
              ]
            }
            """;

    // Custom grass slab model (top) with tintindex
    private static final String GRASS_SLAB_TOP = """
            {
              "parent": "minecraft:block/block",
              "textures": {
                "bottom": "minecraft:block/dirt",
                "top": "minecraft:block/grass_block_top",
                "side": "minecraft:block/grass_block_side",
                "overlay": "minecraft:block/grass_block_side_overlay",
                "particle": "minecraft:block/dirt"
              },
              "elements": [
                {
                  "from": [0, 8, 0],
                  "to": [16, 16, 16],
                  "faces": {
                    "down":  { "uv": [0, 0, 16, 16], "texture": "#bottom", "cullface": "down" },
                    "up":    { "uv": [0, 0, 16, 16], "texture": "#top", "cullface": "up", "tintindex": 0 },
                    "north": { "uv": [0, 0, 16, 8], "texture": "#side", "cullface": "north" },
                    "south": { "uv": [0, 0, 16, 8], "texture": "#side", "cullface": "south" },
                    "west":  { "uv": [0, 0, 16, 8], "texture": "#side", "cullface": "west" },
                    "east":  { "uv": [0, 0, 16, 8], "texture": "#side", "cullface": "east" }
                  }
                },
                {
                  "from": [0, 8, 0],
                  "to": [16, 16, 16],
                  "faces": {
                    "north": { "uv": [0, 0, 16, 8], "texture": "#overlay", "cullface": "north", "tintindex": 0 },
                    "south": { "uv": [0, 0, 16, 8], "texture": "#overlay", "cullface": "south", "tintindex": 0 },
                    "west":  { "uv": [0, 0, 16, 8], "texture": "#overlay", "cullface": "west", "tintindex": 0 },
                    "east":  { "uv": [0, 0, 16, 8], "texture": "#overlay", "cullface": "east", "tintindex": 0 }
                  }
                }
              ]
            }
            """;

    private static void generateBlockModels(ResourcePackBuilder builder, SlabTypes type) {
        String id = type.getSlabId();

        // Grass slab uses custom templates generated above -- skip standard generation
        if (type == SlabTypes.GRASS_BLOCK) {
            return;
        }

        String bottom = type.getBottomTexturePath();
        String top = type.getTopTexturePath();
        String side = type.getSideTexturePath();

        builder.addStringData("assets/" + MoreSlabs.MODID + "/models/block/" + id + ".json",
                slabModel("minecraft:block/slab", bottom, top, side));

        builder.addStringData("assets/" + MoreSlabs.MODID + "/models/block/" + id + "_top.json",
                slabModel("minecraft:block/slab_top", bottom, top, side));
    }

    private static void generateItemDefinition(ResourcePackBuilder builder, SlabTypes type) {
        String id = type.getSlabId();
        String modelRef = MoreSlabs.MODID + ":block/" + id;

        if (type.needsBiomeTint()) {
            builder.addStringData("assets/" + MoreSlabs.MODID + "/items/" + id + ".json", """
                    {
                      "model": {
                        "type": "minecraft:model",
                        "model": "%s",
                        "tints": [
                          {
                            "type": "minecraft:grass",
                            "temperature": 0.8,
                            "downfall": 0.4
                          }
                        ]
                      }
                    }
                    """.formatted(modelRef));
        } else {
            builder.addStringData("assets/" + MoreSlabs.MODID + "/items/" + id + ".json", """
                    {
                      "model": {
                        "type": "minecraft:model",
                        "model": "%s"
                      }
                    }
                    """.formatted(modelRef));
        }
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
}
