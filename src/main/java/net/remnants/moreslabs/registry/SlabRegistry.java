package net.remnants.moreslabs.registry;

import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.remnants.moreslabs.MoreSlabs;
import net.remnants.moreslabs.SlabTypes;
import net.remnants.moreslabs.block.PolymerSlabBlock;
import net.remnants.moreslabs.block.PolymerWallBlock;

import java.util.EnumMap;
import java.util.Map;

public class SlabRegistry {
    private static final Map<SlabTypes, PolymerSlabBlock> BLOCKS = new EnumMap<>(SlabTypes.class);
    private static final Map<SlabTypes, BlockItem> ITEMS = new EnumMap<>(SlabTypes.class);

    // Walls
    public static PolymerWallBlock POLISHED_DIORITE_WALL;
    public static BlockItem POLISHED_DIORITE_WALL_ITEM;

    public static void register() {
        registerSlabs();
        registerWalls();
    }

    private static void registerSlabs() {
        for (SlabTypes type : SlabTypes.values()) {
            Identifier blockId = Identifier.fromNamespaceAndPath(MoreSlabs.MODID, type.getSlabId());

            // Create block with properties copied from the source block
            BlockBehaviour.Properties props = BlockBehaviour.Properties.ofFullCopy(type.getDisplayBlock());
            PolymerSlabBlock block = new PolymerSlabBlock(type, props);
            Registry.register(BuiltInRegistries.BLOCK, blockId, block);
            BLOCKS.put(type, block);

            // Create block item with custom model
            Identifier itemModelId = Identifier.fromNamespaceAndPath(MoreSlabs.MODID, type.getSlabId());
            BlockItem item = new PolymerBlockItem(block, new Item.Properties(), Items.PETRIFIED_OAK_SLAB) {
                @Override
                public Identifier getPolymerItemModel(ItemStack stack, PacketContext ctx, HolderLookup.Provider lookup) {
                    return itemModelId;
                }
            };
            Registry.register(BuiltInRegistries.ITEM, blockId, item);
            ITEMS.put(type, item);
        }
    }

    private static void registerWalls() {
        Identifier wallId = Identifier.fromNamespaceAndPath(MoreSlabs.MODID, "polished_diorite_wall");

        // Maps to diorite_wall for shape/collision; resource pack overrides diorite_wall textures
        BlockBehaviour.Properties props = BlockBehaviour.Properties.ofFullCopy(Blocks.DIORITE_WALL);
        POLISHED_DIORITE_WALL = new PolymerWallBlock(Blocks.DIORITE_WALL, props);
        Registry.register(BuiltInRegistries.BLOCK, wallId, POLISHED_DIORITE_WALL);

        Identifier itemModelId = Identifier.fromNamespaceAndPath(MoreSlabs.MODID, "polished_diorite_wall");
        POLISHED_DIORITE_WALL_ITEM = new PolymerBlockItem(POLISHED_DIORITE_WALL, new Item.Properties(), Items.DIORITE_WALL) {
            @Override
            public Identifier getPolymerItemModel(ItemStack stack, PacketContext ctx, HolderLookup.Provider lookup) {
                return itemModelId;
            }
        };
        Registry.register(BuiltInRegistries.ITEM, wallId, POLISHED_DIORITE_WALL_ITEM);
    }

    public static PolymerSlabBlock getBlock(SlabTypes type) {
        return BLOCKS.get(type);
    }

    public static BlockItem getItem(SlabTypes type) {
        return ITEMS.get(type);
    }
}
