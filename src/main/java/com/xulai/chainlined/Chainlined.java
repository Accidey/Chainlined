package com.xulai.chainlined;

import com.mojang.logging.LogUtils;
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
        modContainer.registerConfig(ModConfig.Type.COMMON, ChainlinedConfig.SPEC);
    }
}
