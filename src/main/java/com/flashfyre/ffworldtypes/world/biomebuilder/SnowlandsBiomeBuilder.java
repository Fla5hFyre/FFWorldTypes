package com.flashfyre.ffworldtypes.world.biomebuilder;

import java.util.function.Consumer;

import com.mojang.datafixers.util.Pair;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.Climate.ParameterPoint;

public class SnowlandsBiomeBuilder extends FFWTBiomeBuilder {
	
	private final Climate.Parameter smallIslandContRange = span(-1.0F, -0.4F);
	private final Climate.Parameter coastContRange = span(-0.0F, 0.05F);
	private final Climate.Parameter bigIslandContRange = span(-0.05F, 1.0F);
	private final Climate.Parameter[] landWeirdnessRanges = {
			span(-1.0F, -0.1F),
			span(-0.1F, 0.1F),
			span(0.1F, 1.0F)
	};	
	private final Climate.Parameter oceanContRanges[] = {
			span(-0.4F, -0.25F),
			span(-0.25F, -0.15F),
			span(-0.15F, 0.0F)
	};
	
	@Override
	protected void addOffcoastBiomes(Consumer<Pair<ParameterPoint, ResourceKey<Biome>>> consumerPair) {
		this.addSurfaceBiome(consumerPair, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, oceanContRanges[0], 0.0F, Biomes.FROZEN_OCEAN);
		this.addSurfaceBiome(consumerPair, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, oceanContRanges[1], 0.0F, Biomes.DEEP_FROZEN_OCEAN);
		this.addSurfaceBiome(consumerPair, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, oceanContRanges[2], 0.0F, Biomes.FROZEN_OCEAN);
	}

	@Override
	protected void addUndergroundBiomes(Consumer<Pair<ParameterPoint, ResourceKey<Biome>>> consumerPair) {
	}

	@Override
	protected void addInlandBiomes(Consumer<Pair<ParameterPoint, ResourceKey<Biome>>> consumerPair) {
		this.addSurfaceBiome(consumerPair, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, smallIslandContRange, 0.0F, Biomes.ICE_SPIKES);
		
		this.addSurfaceBiome(consumerPair, this.FULL_RANGE, this.FULL_RANGE, landWeirdnessRanges[0], this.FULL_RANGE, bigIslandContRange, 0.0F, Biomes.GROVE);
		this.addSurfaceBiome(consumerPair, this.FULL_RANGE, this.FULL_RANGE, landWeirdnessRanges[0], this.FULL_RANGE, coastContRange, 0.0F, Biomes.GROVE);
		this.addSurfaceBiome(consumerPair, this.FULL_RANGE, this.FULL_RANGE, landWeirdnessRanges[1], this.FULL_RANGE, bigIslandContRange, 0.0F, Biomes.FROZEN_RIVER);
		this.addSurfaceBiome(consumerPair, this.FULL_RANGE, this.FULL_RANGE, landWeirdnessRanges[1], this.FULL_RANGE, coastContRange, 0.0F, Biomes.FROZEN_OCEAN);
		this.addSurfaceBiome(consumerPair, this.FULL_RANGE, this.FULL_RANGE, landWeirdnessRanges[2], this.FULL_RANGE, bigIslandContRange, 0.0F, Biomes.GROVE);
		this.addSurfaceBiome(consumerPair, this.FULL_RANGE, this.FULL_RANGE, landWeirdnessRanges[2], this.FULL_RANGE, coastContRange, 0.0F, Biomes.GROVE);
	}

}
