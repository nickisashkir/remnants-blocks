package net.remnants.moreslabs.block;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.resources.Identifier;
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

        this.bottomState = PolymerBlockResourceUtils.requestBlock(
                BlockModelType.SLAB_BOTTOM, PolymerBlockModel.of(bottomModel));
        this.topState = PolymerBlockResourceUtils.requestBlock(
                BlockModelType.getSlab(SlabType.TOP, false), PolymerBlockModel.of(topModel));
        this.bottomWaterloggedState = PolymerBlockResourceUtils.requestBlock(
                BlockModelType.SLAB_BOTTOM_WATERLOGGED, PolymerBlockModel.of(bottomModel));
        this.topWaterloggedState = PolymerBlockResourceUtils.requestBlock(
                BlockModelType.SLAB_TOP_WATERLOGGED, PolymerBlockModel.of(topModel));
        this.doubleState = PolymerBlockResourceUtils.requestBlock(
                BlockModelType.FULL_BLOCK, PolymerBlockModel.of(doubleModel));
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
