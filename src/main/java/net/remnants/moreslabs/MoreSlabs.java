package net.remnants.moreslabs;

import com.mojang.logging.LogUtils;
import net.remnants.moreslabs.registry.SlabRegistry;
import eu.pb4.polymer.core.api.item.PolymerCreativeModeTabUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;

public class MoreSlabs implements ModInitializer {
    public static final String MODID = "remnants_blocks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, -1)
            .title(Component.literal("Remnants Blocks").withStyle(ChatFormatting.GOLD))
            .icon(Items.STONE_SLAB::getDefaultInstance)
            .displayItems((parameters, output) -> {
                for (SlabTypes type : SlabTypes.values()) {
                    output.accept(SlabRegistry.getItem(type));
                }
            })
            .build();

    @Override
    public void onInitialize() {
        SlabRegistry.register();
        PolymerCreativeModeTabUtils.registerPolymerCreativeModeTab(
                Identifier.fromNamespaceAndPath(MODID, "blocks"), CREATIVE_TAB);
        PolymerResourcePackUtils.addModAssets(MODID);
        PolymerResourcePackUtils.markAsRequired();
        ResourcePackGenerator.register();
        LOGGER.info("Remnants Building Blocks loaded - {} slab types registered", SlabTypes.values().length);
    }
}
