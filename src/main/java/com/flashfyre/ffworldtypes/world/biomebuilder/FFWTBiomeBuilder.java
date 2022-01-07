package com.flashfyre.ffworldtypes.world.biomebuilder;

import java.util.function.Consumer;

import com.mojang.datafixers.util.Pair;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.Climate.Parameter;

public abstract class FFWTBiomeBuilder {
	
	protected final Climate.Parameter FULL_RANGE = span(-1.0F, 1.0F);
	
	public void addBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumerPair) {
		this.addUndergroundBiomes(consumerPair);
        this.addOffcoastBiomes(consumerPair);
        this.addInlandBiomes(consumerPair);
	}
	
	protected abstract void addUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumerPair);

	protected abstract void addOffcoastBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumerPair);
	
	protected abstract void addInlandBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumerPair);
	
	protected final void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumerPair, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter weirdness, Climate.Parameter erosion, Climate.Parameter continentalness, float offset, ResourceKey<Biome> biomeKey) {
        consumerPair.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, point(0.0F), weirdness, offset), biomeKey));
        consumerPair.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, point(1.0F), weirdness, offset), biomeKey));
    }
	
	protected final void addUndergroundBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumerPair, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter weirdness, Climate.Parameter erosion, Climate.Parameter continentalness, float offset, ResourceKey<Biome> biomeKey) {
        consumerPair.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, span(0.2F, 0.9F), weirdness, offset), biomeKey));
    }
	
	protected final void addUndergroundBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumerPair, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter weirdness, Climate.Parameter erosion, Climate.Parameter continentalness, Climate.Parameter depth, float offset, ResourceKey<Biome> biomeKey) {
        consumerPair.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion, depth, weirdness, offset), biomeKey));
    }
	
	protected final static Climate.Parameter span(float min, float max) {
        return Climate.Parameter.span(min, max);
    }

	protected final static Climate.Parameter span(Parameter min, Parameter max) {
        return Climate.Parameter.span(min, max);
    }

	protected final static Climate.Parameter point(float point) {
        return Climate.Parameter.point(point);
    }

}
