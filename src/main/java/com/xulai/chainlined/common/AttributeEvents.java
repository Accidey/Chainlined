package com.xulai.chainlined.common;

import com.xulai.chainlined.Chainlined;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

@EventBusSubscriber(modid = Chainlined.MODID)
public final class AttributeEvents {
    private AttributeEvents() {
    }

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ChainlinedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.PIGLIN, ChainlinedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.PIGLIN_BRUTE, ChainlinedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.ZOMBIFIED_PIGLIN, ChainlinedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.WITHER_SKELETON, ChainlinedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.SKELETON, ChainlinedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.STRAY, ChainlinedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.HUSK, ChainlinedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.DROWNED, ChainlinedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.ZOMBIE, ChainlinedEvents.IMPACT_RESISTANCE);
    }
}