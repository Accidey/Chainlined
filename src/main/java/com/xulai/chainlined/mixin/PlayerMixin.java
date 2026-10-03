package com.xulai.chainlined.mixin;

import com.xulai.chainlined.ChainlinedConfig;
import com.xulai.chainlined.common.ChainlinedHelper;
import com.xulai.chainlined.sound.ChainlinedSounds;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Redirect(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"))
    private List<LivingEntity> chainmailed$skipReinforcedTargets(Level level, Class<LivingEntity> entityClass, AABB area) {
        List<LivingEntity> candidates = level.getEntitiesOfClass(entityClass, area);
        List<LivingEntity> sweepTargets = new ArrayList<>(candidates.size());
        for (LivingEntity candidate : candidates) {
            if (!ChainlinedHelper.hasFullChainmailedSet(candidate)) {
                sweepTargets.add(candidate);
            } else if (ChainlinedConfig.soundChainmailedBlock) {
                candidate.playSound(ChainlinedSounds.CHAINMAIL_BLOCK.get());
            }
        }
        return sweepTargets;
    }
}