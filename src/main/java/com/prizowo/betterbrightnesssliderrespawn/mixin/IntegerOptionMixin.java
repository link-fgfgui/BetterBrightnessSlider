package com.prizowo.betterbrightnesssliderrespawn.mixin;

import com.prizowo.betterbrightnesssliderrespawn.Betterbrightnesssliderrespawn;
import net.caffeinemc.mods.sodium.api.config.ConfigState;
import net.caffeinemc.mods.sodium.api.config.StorageEventHandler;
import net.caffeinemc.mods.sodium.api.config.option.ControlValueFormatter;
import net.caffeinemc.mods.sodium.api.config.option.OptionBinding;
import net.caffeinemc.mods.sodium.api.config.option.OptionImpact;
import net.caffeinemc.mods.sodium.api.config.option.Range;
import net.caffeinemc.mods.sodium.api.config.option.SteppedValidator;
import net.caffeinemc.mods.sodium.client.config.structure.IntegerOption;
import net.caffeinemc.mods.sodium.client.config.value.ConstantValue;
import net.caffeinemc.mods.sodium.client.config.value.DependentValue;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Extends sodium's brightness/gamma slider range beyond the vanilla 0-100%
 * by replacing the IntegerOption's validator with an extended Range.
 *
 * <p>The gamma option stores its value as an integer percentage (0-100 in
 * vanilla), and the binding multiplies by 0.01D to convert to the double
 * gamma value. This mixin widens the validator's range so values outside
 * vanilla's bounds are accepted by both the slider and the validator.</p>
 */
@Pseudo
@Mixin(IntegerOption.class)
public abstract class IntegerOptionMixin {
    @Shadow
    @Mutable
    private DependentValue<? extends SteppedValidator> validator;

    @Inject(at = @At("RETURN"), method = "<init>", remap = false)
    private void brightnessslider$onInit(
            ResourceLocation id,
            Collection<ResourceLocation> dependencies,
            Component name,
            DependentValue<Boolean> enabled,
            StorageEventHandler storage,
            Function<Integer, Component> tooltipProvider,
            OptionImpact impact,
            Set<ResourceLocation> flags,
            DependentValue<Integer> defaultValue,
            Boolean controlHiddenWhenDisabled,
            OptionBinding<Integer> binding,
            Consumer<ConfigState> applyHook,
            DependentValue<? extends SteppedValidator> validator,
            ControlValueFormatter valueFormatter,
            CallbackInfo info) {
        if (id.equals(ResourceLocation.parse("sodium:general.gamma"))) {
            int min = (int) (Betterbrightnesssliderrespawn.min * 100);
            int max = (int) (Betterbrightnesssliderrespawn.max * 100);
            int step = (int) (Betterbrightnesssliderrespawn.interval * 100);
            this.validator = new ConstantValue<>(new Range(min, max, step));
        }
    }
}
