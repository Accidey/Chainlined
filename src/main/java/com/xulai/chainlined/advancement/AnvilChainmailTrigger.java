package com.xulai.chainlined.advancement;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

public class AnvilChainmailTrigger extends SimpleCriterionTrigger<AnvilChainmailTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, instance -> true);
    }

    public record TriggerInstance() implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = Codec.unit(TriggerInstance::new);

        @Override
        public Optional<ContextAwarePredicate> player() {
            return Optional.empty();
        }
    }
}