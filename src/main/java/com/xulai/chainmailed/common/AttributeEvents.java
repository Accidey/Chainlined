package com.xulai.chainmailed.common;

import com.xulai.chainmailed.ChainMailed;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

@EventBusSubscriber(modid = ChainMailed.MODID)
public final class AttributeEvents {
    private AttributeEvents() {
    }

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ChainMailedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.PIGLIN, ChainMailedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.PIGLIN_BRUTE, ChainMailedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.ZOMBIFIED_PIGLIN, ChainMailedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.WITHER_SKELETON, ChainMailedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.SKELETON, ChainMailedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.STRAY, ChainMailedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.HUSK, ChainMailedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.DROWNED, ChainMailedEvents.IMPACT_RESISTANCE);
        event.add(EntityType.ZOMBIE, ChainMailedEvents.IMPACT_RESISTANCE);
    }
}
