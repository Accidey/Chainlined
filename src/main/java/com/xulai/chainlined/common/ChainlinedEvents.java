package com.xulai.chainlined.common;

import com.xulai.chainlined.Chainlined;
import com.xulai.chainlined.ChainlinedConfig;
import com.xulai.chainlined.advancement.ChainlinedTriggers;
import com.xulai.chainlined.sound.ChainlinedSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = Chainlined.MODID)
public final class ChainlinedEvents {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, Chainlined.MODID);

    public static final DeferredHolder<Attribute, Attribute> IMPACT_RESISTANCE = ATTRIBUTES.register(
            "impact_resistance",
            () -> new RangedAttribute("attribute.impact_resistance", 0.0D, -Double.MAX_VALUE, Double.MAX_VALUE).setSyncable(true));

    private ChainlinedEvents() {
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        if (!(left.getItem() instanceof ArmorItem armor)) {
            return;
        }
        if (ChainlinedConfig.chainmailedBlacklist.contains(armor)) {
            return;
        }
        if (ChainlinedHelper.hasChainmailed(left)) {
            return;
        }
        if (armor.getMaterial().is(ArmorMaterials.CHAIN)) {
            return;
        }
        if (!(right.getItem() instanceof ArmorItem chainmail)
                || !chainmail.getMaterial().is(ArmorMaterials.CHAIN)
                || chainmail.getType().getSlot() != armor.getType().getSlot()) {
            return;
        }
        ItemStack output = left.copy();
        ChainlinedHelper.storeChainmailed(output, right, event.getPlayer().level().registryAccess());
        event.setOutput(output);
        event.setCost(ChainlinedConfig.anvilExperienceCost);
        event.setMaterialCost(1);
        if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
            ChainlinedTriggers.ANVIL_CHAINMAIL.get().trigger(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack crafted = event.getCrafting();
        if (!(crafted.getItem() instanceof ArmorItem craftedArmor)) {
            return;
        }
        if (ChainlinedHelper.hasChainmailed(crafted)) {
            return;
        }
        if (ChainlinedConfig.chainmailedBlacklist.contains(craftedArmor)) {
            return;
        }
        Container matrix = event.getInventory();
        for (int i = 0; i < matrix.getContainerSize(); i++) {
            ItemStack ingredient = matrix.getItem(i);
            if (!(ingredient.getItem() instanceof ArmorItem ingredientArmor)) {
                continue;
            }
            if (ingredientArmor.getType().getSlot() != craftedArmor.getType().getSlot()) {
                continue;
            }
            if (!ChainlinedHelper.hasChainmailed(ingredient)) {
                continue;
            }
            ItemStack snapshot = ChainlinedHelper.copyChainmailed(ingredient, event.getEntity().level().registryAccess());
            if (snapshot.isEmpty()) {
                continue;
            }
            ChainlinedHelper.storeChainmailed(crafted, snapshot, event.getEntity().level().registryAccess());
            return;
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        LivingEntity victim = event.getEntity();
        AttributeInstance resistance = victim.getAttribute(IMPACT_RESISTANCE);
        if (resistance == null) {
            return;
        }
        double blocked = resistance.getValue();
        if (blocked <= 0.0D) {
            return;
        }

        DamageSource source = event.getSource();
        if (!source.is(DamageTypes.MOB_ATTACK) && !source.is(DamageTypes.PLAYER_ATTACK) && !source.is(DamageTypes.GENERIC)) {
            return;
        }

        event.setNewDamage((float) Math.max(event.getNewDamage() - blocked, 1.0F));
        if (!ChainlinedConfig.soundChainmailedBlock) {
            return;
        }

        victim.playSound(ChainlinedSounds.CHAINMAIL_BLOCK.get());
        if (victim instanceof Player player) {
            player.level().playSound(null, victim, ChainlinedSounds.CHAINMAIL_BLOCK.get(), SoundSource.PLAYERS, 1.5F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onItemAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof ArmorItem armor)) {
            return;
        }
        boolean chainmail = armor.getMaterial().is(ArmorMaterials.CHAIN);
        if (!chainmail && !ChainlinedHelper.hasChainmailed(stack)) {
            return;
        }

        EquipmentSlot slot = armor.getType().getSlot();
        int bonus = armorForSlot(slot);
        int defense = chainmail ? bonus : armor.getDefense() + bonus;
        ResourceLocation id = ResourceLocation.withDefaultNamespace("armor." + armor.getType().getName());
        EquipmentSlotGroup group = EquipmentSlotGroup.bySlot(slot);

        event.removeAllModifiersFor(Attributes.ARMOR);
        event.addModifier(Attributes.ARMOR, new AttributeModifier(id, defense, AttributeModifier.Operation.ADD_VALUE), group);
        event.addModifier(IMPACT_RESISTANCE, new AttributeModifier(id, ChainlinedConfig.impactResistanceChainmailedBasic, AttributeModifier.Operation.ADD_VALUE), group);
    }

    public static int armorForSlot(EquipmentSlot slot) {
        int[] table = ChainlinedConfig.armorForSlotChainmailedBasic;
        int index = slot.getIndex();
        return index >= 0 && index < table.length ? table[index] : 0;
    }
}