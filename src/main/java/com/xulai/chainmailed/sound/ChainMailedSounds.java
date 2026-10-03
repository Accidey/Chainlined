package com.xulai.chainmailed.sound;

import com.xulai.chainmailed.ChainMailed;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ChainMailedSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, ChainMailed.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> CHAINMAIL_BLOCK = register("chainmail_block");

    private ChainMailedSounds() {
    }

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ChainMailed.MODID, name)));
    }
}
