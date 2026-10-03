package com.xulai.chainlined.advancement;

import com.xulai.chainlined.Chainlined;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ChainlinedTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS =
            DeferredRegister.create(Registries.TRIGGER_TYPE, Chainlined.MODID);

    public static final DeferredHolder<CriterionTrigger<?>, AnvilChainmailTrigger> ANVIL_CHAINMAIL =
            TRIGGERS.register("anvil_chainmail", AnvilChainmailTrigger::new);

    private ChainlinedTriggers() {
    }
}