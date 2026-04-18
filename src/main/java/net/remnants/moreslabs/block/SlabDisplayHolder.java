package net.remnants.moreslabs.block;

import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.BlockBoundAttachment;
import eu.pb4.polymer.virtualentity.api.attachment.HolderAttachment;
import eu.pb4.polymer.virtualentity.api.elements.BlockDisplayElement;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.Vec3;
import net.remnants.moreslabs.SlabTypes;
import org.joml.Vector3f;

/**
 * Manages the BlockDisplayElement that gives each slab its correct visual appearance.
 * Uses the vanilla block's model (e.g. dirt, grass_block, concrete) scaled to slab dimensions.
 * For grass slabs, the client applies biome tinting to the display entity automatically.
 */
public class SlabDisplayHolder extends ElementHolder {
    private final BlockDisplayElement display;
    private SlabType currentSlabType;

    public SlabDisplayHolder(SlabTypes slabType, BlockState initialState) {
        this.display = new BlockDisplayElement(slabType.getDisplayBlock().defaultBlockState());
        this.currentSlabType = initialState.getValue(SlabBlock.TYPE);
        applyTransform(currentSlabType);
        this.addElement(display);
    }

    @Override
    public void notifyUpdate(HolderAttachment.UpdateType type) {
        super.notifyUpdate(type);
        if (type == BlockBoundAttachment.BLOCK_STATE_UPDATE) {
            var attachment = getAttachment();
            if (attachment instanceof BlockBoundAttachment bba) {
                SlabType newType = bba.getBlockState().getValue(SlabBlock.TYPE);
                if (newType != currentSlabType) {
                    currentSlabType = newType;
                    applyTransform(newType);
                    display.startInterpolation();
                }
            }
        }
    }

    private void applyTransform(SlabType type) {
        switch (type) {
            case BOTTOM -> {
                display.setScale(new Vector3f(1f, 0.5f, 1f));
                display.setOffset(Vec3.ZERO);
            }
            case TOP -> {
                display.setScale(new Vector3f(1f, 0.5f, 1f));
                display.setOffset(new Vec3(0, 0.5, 0));
            }
            case DOUBLE -> {
                display.setScale(new Vector3f(1f, 1f, 1f));
                display.setOffset(Vec3.ZERO);
            }
        }
    }
}
