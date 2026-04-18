package net.remnants.moreslabs;

import com.mojang.logging.LogUtils;
import net.remnants.moreslabs.registry.SlabRegistry;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class MoreSlabs implements ModInitializer {
    public static final String MODID = "remnants_blocks";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        SlabRegistry.register();
        PolymerResourcePackUtils.addModAssets(MODID);
        PolymerResourcePackUtils.markAsRequired();
        ResourcePackGenerator.register();
        LOGGER.info("Remnants More Slabs loaded - {} slab types registered", SlabTypes.values().length);
    }
}
