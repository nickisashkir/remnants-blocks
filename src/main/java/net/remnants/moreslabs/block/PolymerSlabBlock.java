package net.remnants.moreslabs.block;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.remnants.moreslabs.MoreSlabs;
import net.remnants.moreslabs.SlabTypes;

/**
 * A Polymer-textured slab block that uses polymer-blocks to allocate real vanilla-shaped
 * slab block states with custom models. No display entities, no invisible overlays --
 * the client renders proper slab geometry with correct collision and waterlogging.
 */
public class PolymerSlabBlock extends SlabBlock implements PolymerTexturedBlock {
    private final SlabTypes slabType;
    private final BlockState bottomState;
    private final BlockState topState;
    private final BlockState bottomWaterloggedState;
    private final BlockState topWaterloggedState;
    private final BlockState doubleState;

    public PolymerSlabBlock(SlabTypes slabType, Properties properties) {
        super(properties);
        this.slabType = slabType;

        String id = slabType.getSlabId();
        Identifier bottomModel = Identifier.fromNamespaceAndPath(MoreSlabs.MODID, "block/" + id);
        Identifier topModel = Identifier.fromNamespaceAndPath(MoreSlabs.MODID, "block/" + id + "_top");
        Identifier doubleModel = Identifier.fromNamespaceAndPath(MoreSlabs.MODID, "block/" + id + "_double");

        this.bottomState = safeRequest(BlockModelType.SLAB_BOTTOM, bottomModel, id, "bottom");
        this.topState = safeRequest(BlockModelType.getSlab(SlabType.TOP, false), topModel, id, "top");
        this.bottomWaterloggedState = safeRequest(BlockModelType.SLAB_BOTTOM_WATERLOGGED, bottomModel, id, "bottom_waterlogged");
        this.topWaterloggedState = safeRequest(BlockModelType.SLAB_TOP_WATERLOGGED, topModel, id, "top_waterlogged");
        this.doubleState = safeRequest(BlockModelType.FULL_BLOCK, doubleModel, id, "double");
    }

    private static BlockState safeRequest(BlockModelType type, Identifier model, String slabId, String variant) {
        int before = PolymerBlockResourceUtils.getBlocksLeft(type);
        BlockState result = PolymerBlockResourceUtils.requestBlock(type, PolymerBlockModel.of(model));
        if (result == null) {
            String msg = String.format("[REMNANTS_BLOCKS] POOL EXHAUSTED: %s for slab '%s' variant '%s' (had %d left before request). Falling back to STONE.",
                    type, slabId, variant, before);
            MoreSlabs.LOGGER.error(msg);
            System.err.println(msg);
            return Blocks.STONE.defaultBlockState();
        }
        MoreSlabs.LOGGER.info("[REMNANTS_BLOCKS] Allocated {} for '{}' variant '{}' ({} -> {} left)",
                type, slabId, variant, before, PolymerBlockResourceUtils.getBlocksLeft(type));
        return result;
    }

    public SlabTypes getSlabType() {
        return slabType;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext ctx) {
        SlabType type = state.getValue(TYPE);
        boolean waterlogged = state.getValue(WATERLOGGED);

        return switch (type) {
            case BOTTOM -> waterlogged ? bottomWaterloggedState : bottomState;
            case TOP -> waterlogged ? topWaterloggedState : topState;
            case DOUBLE -> doubleState;
        };
    }
}
