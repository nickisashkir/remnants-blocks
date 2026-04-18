package net.remnants.moreslabs.block;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.virtualentity.api.BlockWithElementHolder;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.remnants.moreslabs.SlabTypes;

public class PolymerSlabBlock extends SlabBlock implements PolymerBlock, BlockWithElementHolder {
    private final SlabTypes slabType;

    public PolymerSlabBlock(SlabTypes slabType, Properties properties) {
        super(properties);
        this.slabType = slabType;
    }

    public SlabTypes getSlabType() {
        return slabType;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext ctx) {
        return Blocks.PETRIFIED_OAK_SLAB.defaultBlockState()
                .setValue(TYPE, state.getValue(TYPE))
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
    }

    @Override
    public BlockState getPolymerBreakEventBlockState(BlockState state, PacketContext ctx) {
        // Show correct break particles (dirt particles for dirt slab, etc.)
        return slabType.getDisplayBlock().defaultBlockState();
    }

    @Override
    public ElementHolder createElementHolder(ServerLevel level, BlockPos pos, BlockState state) {
        return new SlabDisplayHolder(slabType, state);
    }

    @Override
    public boolean tickElementHolder(ServerLevel level, BlockPos pos, BlockState state) {
        return false;
    }
}
