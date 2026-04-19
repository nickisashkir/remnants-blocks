package net.remnants.moreslabs.compat;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.remnants.moreslabs.block.PolymerSlabBlock;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.JadeIds;
import snownee.jade.api.StreamServerDataProvider;
import snownee.jade.api.WailaPlugin;

/**
 * Jade integration -- pushes the correct block display name to Jade clients via
 * Jade's own server-to-client protocol. Overrides the block name slot (CORE_OBJECT_NAME),
 * so clients see "Dirt Slab" etc. instead of "Waxed Cut Copper Slab" (the underlying
 * polymer-blocks allocation that the vanilla client thinks it's rendering).
 *
 * <p>Clients need only Jade installed (no Polymer companion required). The server plugin
 * is registered via the "jade" entrypoint in fabric.mod.json.
 */
@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(SlabNameProvider.INSTANCE, PolymerSlabBlock.class);
    }

    /**
     * Streams the real server-side block's display name to the Jade client.
     */
    public static final class SlabNameProvider implements StreamServerDataProvider<BlockAccessor, Component> {
        public static final SlabNameProvider INSTANCE = new SlabNameProvider();

        @Override
        public Component streamData(BlockAccessor accessor) {
            // The server-side block IS our PolymerSlabBlock, whose getName() returns
            // the correct translation component.
            return accessor.getBlockState().getBlock().getName();
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, Component> streamCodec() {
            return ComponentSerialization.STREAM_CODEC;
        }

        @Override
        public Identifier getUid() {
            // Override the default block-name slot so Jade uses our component instead.
            return JadeIds.CORE_OBJECT_NAME;
        }
    }
}
