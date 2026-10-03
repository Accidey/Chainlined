package com.xulai.chainmailed;

import com.mojang.logging.LogUtils;
import com.xulai.chainmailed.common.ChainMailedEvents;
import com.xulai.chainmailed.sound.ChainMailedSounds;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(ChainMailed.MODID)
public class ChainMailed {
    public static final String MODID = "chainmailed";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ChainMailed(IEventBus modEventBus, ModContainer modContainer) {
        ChainMailedSounds.SOUND_EVENTS.register(modEventBus);
        ChainMailedEvents.ATTRIBUTES.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, ChainMailedConfig.SPEC);
    }
}
