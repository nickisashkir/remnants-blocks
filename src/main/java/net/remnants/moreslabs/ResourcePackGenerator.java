package net.remnants.moreslabs;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.api.ResourcePackBuilder;

/**
 * Generates resource pack assets programmatically during pack creation.
 *
 * Generated assets:
 * - Block models (bottom, top, double variants per type)
 * - Custom grass slab model with tintindex + side overlay
 * - Item definitions (what players see in inventory)
 *
 * The polymer-blocks module handles block state allocation internally --
 * we just need to provide the model files it references.
 */
public class ResourcePackGenerator {

    public static void register() {
        PolymerResourcePackUtils.RESOURCE_PACK_CREATION_EVENT.register(builder -> {
            for (SlabTypes type : SlabTypes.values()) {
                generateBlockModels(builder, type);
                generateItemDefinition(builder, type);
            }
        });
    }

    private static void generateBlockModels(ResourcePackBuilder builder, SlabTypes type) {
        String id = type.getSlabId();
        String bottom = type.getBottomTexturePath();
        String top = type.getTopTexturePath();
        String side = type.getSideTexturePath();

        if (type == SlabTypes.GRASS_BLOCK) {
            // Grass slab uses custom models with tintindex + side overlay for biome coloring
            builder.addStringData("assets/" + MoreSlabs.MODID + "/models/block/" + id + ".json", GRASS_SLAB_BOTTOM);
            builder.addStringData("assets/" + MoreSlabs.MODID + "/models/block/" + id + "_top.json", GRASS_SLAB_TOP);
            builder.addStringData("assets/" + MoreSlabs.MODID + "/models/block/" + id + "_double.json", GRASS_BLOCK_FULL);
            return;
        }

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
                    "up":    { "uv": [0, 0, 16, 16], "texture": "#top", "tintindex": 0 },
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
                    "down":  { "uv": [0, 0, 16, 16], "texture": "#bottom" },
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

    // Full grass block model (double slab state)
    private static final String GRASS_BLOCK_FULL = """
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
                  "to": [16, 16, 16],
                  "faces": {
                    "down":  { "uv": [0, 0, 16, 16], "texture": "#bottom", "cullface": "down" },
                    "up":    { "uv": [0, 0, 16, 16], "texture": "#top", "cullface": "up", "tintindex": 0 },
                    "north": { "uv": [0, 0, 16, 16], "texture": "#side", "cullface": "north" },
                    "south": { "uv": [0, 0, 16, 16], "texture": "#side", "cullface": "south" },
                    "west":  { "uv": [0, 0, 16, 16], "texture": "#side", "cullface": "west" },
                    "east":  { "uv": [0, 0, 16, 16], "texture": "#side", "cullface": "east" }
                  }
                },
                {
                  "from": [0, 0, 0],
                  "to": [16, 16, 16],
                  "faces": {
                    "north": { "uv": [0, 0, 16, 16], "texture": "#overlay", "cullface": "north", "tintindex": 0 },
                    "south": { "uv": [0, 0, 16, 16], "texture": "#overlay", "cullface": "south", "tintindex": 0 },
                    "west":  { "uv": [0, 0, 16, 16], "texture": "#overlay", "cullface": "west", "tintindex": 0 },
                    "east":  { "uv": [0, 0, 16, 16], "texture": "#overlay", "cullface": "east", "tintindex": 0 }
                  }
                }
              ]
            }
            """;

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
