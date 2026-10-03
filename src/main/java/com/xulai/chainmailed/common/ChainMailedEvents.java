package com.xulai.chainmailed.common;

import com.xulai.chainmailed.ChainMailed;
import com.xulai.chainmailed.ChainMailedConfig;
import com.xulai.chainmailed.sound.ChainMailedSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = ChainMailed.MODID)
public final class ChainMailedEvents {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, ChainMailed.MODID);

    public static final DeferredHolder<Attribute, Attribute> IMPACT_RESISTANCE = ATTRIBUTES.register(
            "impact_resistance",
            () -> new RangedAttribute("attribute.impact_resistance", 0.0D, -Double.MAX_VALUE, Double.MAX_VALUE).setSyncable(true));

    private ChainMailedEvents() {
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof ArmorItem armor)) {
            return;
        }
        Player player = event.getEntity();
        ItemStack offhand = player.getOffhandItem();
        if (ChainMailedConfig.chainmailedBlacklist.contains(armor)) {
            return;
        }

        if (!ChainMailHelper.hasChainmailed(stack) && offhand.getItem() instanceof ArmorItem chainmail
                && !armor.getMaterial().is(ArmorMaterials.CHAIN)
                && chainmail.getMaterial().is(ArmorMaterials.CHAIN)
                && chainmail.getType().getSlot() == armor.getType().getSlot()) {
            ChainMailHelper.storeChainmailed(stack, offhand, player.level().registryAccess());
            offhand.shrink(1);
            player.playSound(SoundEvents.SMITHING_TABLE_USE);
        } else if (player.isShiftKeyDown() && offhand.isEmpty() && ChainMailHelper.hasChainmailed(stack)) {
            ItemStack stored = ChainMailHelper.takeChainmailed(stack, player.level().registryAccess());
            if (stored != null) {
                player.setItemSlot(EquipmentSlot.OFFHAND, stored);
                player.playSound(SoundEvents.ANVIL_USE);
            }
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
        if (!ChainMailedConfig.soundChainmailedBlock) {
            return;
        }

        victim.playSound(ChainMailedSounds.CHAINMAIL_BLOCK.get());
        if (victim instanceof Player player) {
            player.level().playSound(null, victim, ChainMailedSounds.CHAINMAIL_BLOCK.get(), SoundSource.PLAYERS, 1.5F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onItemAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof ArmorItem armor)) {
            return;
        }
        boolean chainmail = armor.getMaterial().is(ArmorMaterials.CHAIN);
        if (!chainmail && !ChainMailHelper.hasChainmailed(stack)) {
            return;
        }

        EquipmentSlot slot = armor.getType().getSlot();
        int bonus = armorForSlot(slot);
        int defense = chainmail ? bonus : armor.getDefense() + bonus;
        ResourceLocation id = ResourceLocation.withDefaultNamespace("armor." + armor.getType().getName());
        EquipmentSlotGroup group = EquipmentSlotGroup.bySlot(slot);

        event.removeAllModifiersFor(Attributes.ARMOR);
        event.addModifier(Attributes.ARMOR, new AttributeModifier(id, defense, AttributeModifier.Operation.ADD_VALUE), group);
        event.addModifier(IMPACT_RESISTANCE, new AttributeModifier(id, ChainMailedConfig.impactResistanceChainmailedBasic, AttributeModifier.Operation.ADD_VALUE), group);
    }

    public static int armorForSlot(EquipmentSlot slot) {
        int[] table = ChainMailedConfig.armorForSlotChainmailedBasic;
        int index = slot.getIndex();
        return index >= 0 && index < table.length ? table[index] : 0;
    }
}
