package net.remnants.moreslabs;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;

/**
 * Generates resource pack assets programmatically during pack creation.
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
        });
    }

    private static void generateInvisibleSlab(eu.pb4.polymer.resourcepack.api.ResourcePackBuilder builder) {
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

    private static void generateBlockModels(eu.pb4.polymer.resourcepack.api.ResourcePackBuilder builder, SlabTypes type) {
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

    private static void generateItemDefinition(eu.pb4.polymer.resourcepack.api.ResourcePackBuilder builder, SlabTypes type) {
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
