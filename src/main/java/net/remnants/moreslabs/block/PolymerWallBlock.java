package net.remnants.moreslabs.block;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WallSide;

/**
 * A Polymer wall block that maps to a vanilla wall for collision and shape.
 * The visual appearance is controlled by overriding the vanilla wall's model
 * textures in the resource pack.
 */
public class PolymerWallBlock extends WallBlock implements PolymerBlock {
    private final Block vanillaWall;

    public PolymerWallBlock(Block vanillaWall, Properties properties) {
        super(properties);
        this.vanillaWall = vanillaWall;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext ctx) {
        return vanillaWall.defaultBlockState()
                .setValue(UP, state.getValue(UP))
                .setValue(NORTH, state.getValue(NORTH))
                .setValue(SOUTH, state.getValue(SOUTH))
                .setValue(EAST, state.getValue(EAST))
                .setValue(WEST, state.getValue(WEST))
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
    }

    @Override
    public BlockState getPolymerBreakEventBlockState(BlockState state, PacketContext ctx) {
        return vanillaWall.defaultBlockState();
    }
}
