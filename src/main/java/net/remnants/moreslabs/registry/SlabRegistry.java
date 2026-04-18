package net.remnants.moreslabs.registry;

import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.remnants.moreslabs.MoreSlabs;
import net.remnants.moreslabs.SlabTypes;
import net.remnants.moreslabs.block.PolymerSlabBlock;

import java.util.EnumMap;
import java.util.Map;

public class SlabRegistry {
    private static final Map<SlabTypes, PolymerSlabBlock> BLOCKS = new EnumMap<>(SlabTypes.class);
    private static final Map<SlabTypes, BlockItem> ITEMS = new EnumMap<>(SlabTypes.class);

    public static void register() {
        for (SlabTypes type : SlabTypes.values()) {
            Identifier blockId = Identifier.fromNamespaceAndPath(MoreSlabs.MODID, type.getSlabId());
            ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, blockId);

            // Set ID on properties before constructing -- 26.1 needs this for loot table resolution
            BlockBehaviour.Properties props = BlockBehaviour.Properties.ofFullCopy(type.getDisplayBlock())
                    .setId(blockKey);
            PolymerSlabBlock block = new PolymerSlabBlock(type, props);
            Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
            BLOCKS.put(type, block);

            // Create block item with custom model
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, blockId);
            Identifier itemModelId = blockId;
            BlockItem item = new PolymerBlockItem(block, new Item.Properties().setId(itemKey), Items.PETRIFIED_OAK_SLAB) {
                @Override
                public Identifier getPolymerItemModel(ItemStack stack, PacketContext ctx, HolderLookup.Provider lookup) {
                    return itemModelId;
                }
            };
            Registry.register(BuiltInRegistries.ITEM, itemKey, item);
            ITEMS.put(type, item);
        }
    }

    public static PolymerSlabBlock getBlock(SlabTypes type) {
        return BLOCKS.get(type);
    }

    public static BlockItem getItem(SlabTypes type) {
        return ITEMS.get(type);
    }
}
