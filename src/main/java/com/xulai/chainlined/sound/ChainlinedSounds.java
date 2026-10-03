package com.xulai.chainlined.sound;

import com.xulai.chainlined.Chainlined;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ChainlinedSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, Chainlined.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> CHAINMAIL_BLOCK = register("chainmail_block");

    private ChainlinedSounds() {
    }

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Chainlined.MODID, name)));
    }
}