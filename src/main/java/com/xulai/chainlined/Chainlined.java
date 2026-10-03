package com.xulai.chainlined;

import com.mojang.logging.LogUtils;
import com.xulai.chainlined.advancement.ChainlinedTriggers;
import com.xulai.chainlined.common.ChainlinedEvents;
import com.xulai.chainlined.sound.ChainlinedSounds;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(Chainlined.MODID)
public class Chainlined {
    public static final String MODID = "chainlined";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Chainlined(IEventBus modEventBus, ModContainer modContainer) {
        ChainlinedSounds.SOUND_EVENTS.register(modEventBus);
        ChainlinedEvents.ATTRIBUTES.register(modEventBus);
        ChainlinedTriggers.TRIGGERS.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, ChainlinedConfig.SPEC);
    }
}