package net.remnants.moreslabs;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;

/**
 * Generates resource pack assets programmatically during pack creation.
 * This avoids maintaining dozens of near-identical JSON files.
 *
 * Generated assets:
 * - Invisible petrified_oak_slab blockstate (base block clients see)
 * - Slab block models (bottom/top variants per type)
 * - Item definitions (what players see in inventory)
 */
public class ResourcePackGenerator {

    public static void register() {
        PolymerResourcePackUtils.RESOURCE_PACK_CREATION_EVENT.register(builder -> {
            generateInvisibleSlab(builder);
            for (SlabTypes type : SlabTypes.values()) {
                generateBlockModels(builder, type);
                generateItemDefinition(builder, type);
            }
            generatePolishedDioriteWall(builder);
        });
    }

    private static void generateInvisibleSlab(eu.pb4.polymer.resourcepack.api.ResourcePackBuilder builder) {
        // Empty model (no visible elements, just particle texture for break effects)
        builder.addStringData("assets/minecraft/models/block/more_slabs_invisible.json", """
                {
                  "textures": {
                    "particle": "minecraft:block/stone"
                  }
                }
                """);

        // Override petrified_oak_slab blockstate to use the invisible model
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

    private static void generateBlockModels(eu.pb4.polymer.resourcepack.api.ResourcePackBuilder builder, SlabTypes type) {
        String id = type.getSlabId();
        String bottom = type.getBottomTexturePath();
        String top = type.getTopTexturePath();
        String side = type.getSideTexturePath();

        // Bottom slab model
        builder.addStringData("assets/" + MoreSlabs.MODID + "/models/block/" + id + ".json",
                slabModel("minecraft:block/slab", bottom, top, side));

        // Top slab model
        builder.addStringData("assets/" + MoreSlabs.MODID + "/models/block/" + id + "_top.json",
                slabModel("minecraft:block/slab_top", bottom, top, side));

        // Double slab model (full block)
        builder.addStringData("assets/" + MoreSlabs.MODID + "/models/block/" + id + "_double.json",
                cubeModel(bottom, top, side));
    }

    private static void generateItemDefinition(eu.pb4.polymer.resourcepack.api.ResourcePackBuilder builder, SlabTypes type) {
        String id = type.getSlabId();
        String modelRef = MoreSlabs.MODID + ":block/" + id;

        if (type.needsBiomeTint()) {
            // Grass slab item with tint source for a natural green color
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

    private static void generatePolishedDioriteWall(eu.pb4.polymer.resourcepack.api.ResourcePackBuilder builder) {
        String texture = "minecraft:block/polished_diorite";

        // Override diorite_wall models to use polished diorite texture
        builder.addStringData("assets/minecraft/models/block/diorite_wall_post.json",
                wallModel("minecraft:block/template_wall_post", texture));
        builder.addStringData("assets/minecraft/models/block/diorite_wall_side.json",
                wallModel("minecraft:block/template_wall_side", texture));
        builder.addStringData("assets/minecraft/models/block/diorite_wall_side_tall.json",
                wallModel("minecraft:block/template_wall_side_tall", texture));

        // Wall item model
        builder.addStringData("assets/minecraft/models/block/diorite_wall_inventory.json",
                wallModel("minecraft:block/wall_inventory", texture));

        // Item definition for polished diorite wall
        builder.addStringData("assets/" + MoreSlabs.MODID + "/items/polished_diorite_wall.json", """
                {
                  "model": {
                    "type": "minecraft:model",
                    "model": "minecraft:block/diorite_wall_inventory"
                  }
                }
                """);
    }

    private static String wallModel(String parent, String texture) {
        return """
                {
                  "parent": "%s",
                  "textures": {
                    "wall": "%s"
                  }
                }
                """.formatted(parent, texture);
    }

    private static String cubeModel(String bottom, String top, String side) {
        if (bottom.equals(top) && top.equals(side)) {
            // Simple cube with one texture
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
