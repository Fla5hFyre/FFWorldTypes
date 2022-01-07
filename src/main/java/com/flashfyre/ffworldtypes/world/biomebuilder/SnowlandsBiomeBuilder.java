package com.flashfyre.ffworldtypes.world.biomebuilder;

import java.util.function.Consumer;

import com.flashfyre.ffworldtypes.world.FFWTBiomes;
import com.mojang.datafixers.util.Pair;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.Climate.ParameterPoint;

public class SnowlandsBiomeBuilder extends FFWTBiomeBuilder {
	
	private final Climate.Parameter smallIslandPeakContRange = span(-1.0F, -0.7F);
	private final Climate.Parameter smallIslandSlopeContRange = span(-0.7F, -0.6F);
	private final Climate.Parameter smallIslandInlandContRange = span(-0.6F, -0.45F);
	private final Climate.Parameter smallIslandCoastContRange = span(-0.45F, -0.4F);
	private final Climate.Parameter coastContRange = span(0.0F, 0.05F);
	private final Climate.Parameter nearInlandContRange = span(0.05F, 0.2F);
	private final Climate.Parameter midInlandContRange = span(0.2F, 0.5F);
	private final Climate.Parameter farInlandContRange = span(0.5F, 1.0F); // saddle valleys start here
	//private final Climate.Parameter fullContRange = span(coastContRange, farInlandContRange);
	//private final Climate.Parameter fullContRangeNoCoast = span(nearInlandContRange, farInlandContRange);
	
	private final Climate.Parameter[] erosionRanges = {
			span(-1.0F, -0.5F),
			span(-0.5F, 0.0F),
			span(0.0F, 0.5F),
			span(0.5F, 1.0F) 
	};
	
	private final Climate.Parameter[] landWeirdnessRanges = {
			span(-1.0F, -0.7F), // basin 0
			span(-0.7F, -0.5F), // peak 1
			span(-0.5F, -0.35F), // high 2
			span(-0.35F, -0.15F), // mid 3
			span(-0.15F, -0.1F), // low 4
			span(-0.1F, 0.1F), // valley 5
			span(0.1F, 0.15F), // low 6
			span(0.15F, 0.35F), // mid 7
			span(0.35F, 0.5F), // high 8
			span(0.5F, 0.7F), // peak 9
			span(0.7F, 1.0F) // basin 10
	};	
	private final Climate.Parameter oceanContRanges[] = {
			span(-0.4F, -0.25F),
			span(-0.25F, -0.15F),
			span(-0.15F, 0.0F)
	};
	
	private final Climate.Parameter[] icynessRanges = {
	        span(-1.0F, -0.5F), // index 0
	        span(-0.5F, -0.1F), // index 1
	        span(-0.1F, 0.1F), // index 2
	        span(0.1F, 0.5F), // index 3
	        span(0.5F, 1.0F) // index 4
	};
	private final Climate.Parameter[] vegetationRanges = {
	    	span(-1.0F, -0.5F),
	    	span(-0.5F, -0.1F),
		    span(-0.1F, 0.1F),
		    span(0.1F, 0.5F),
		    span(0.5F, 1.0F)
	};
	
	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] normalBiomes = new ResourceKey[][]{
		{Biomes.MEADOW, Biomes.MEADOW, Biomes.TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA},
		{Biomes.SNOWY_PLAINS, Biomes.MEADOW, Biomes.TAIGA, FFWTBiomes.SNOWY_GIANT_PINE_TAIGA, FFWTBiomes.SNOWY_GIANT_SPRUCE_TAIGA},
		{Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS},
		{Biomes.ICE_SPIKES, Biomes.ICE_SPIKES, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, FFWTBiomes.SNOWY_GIANT_SPRUCE_TAIGA},
		{Biomes.ICE_SPIKES, Biomes.ICE_SPIKES, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA}
	};
	
	@SuppressWarnings("unchecked")
	private final ResourceKey<Biome>[][] basinBiomes = new ResourceKey[][]{
		{Biomes.MEADOW, 	Biomes.MEADOW,				Biomes.MEADOW,				Biomes.TAIGA, 				Biomes.TAIGA}, // least icy
		{Biomes.MEADOW, 	Biomes.MEADOW,				Biomes.MEADOW,				Biomes.TAIGA, 				Biomes.TAIGA},
		{Biomes.ICE_SPIKES,	FFWTBiomes.MOUNTAIN_SPRINGS,FFWTBiomes.MOUNTAIN_SPRINGS,FFWTBiomes.MOUNTAIN_SPRINGS,Biomes.GROVE},
		{Biomes.ICE_SPIKES, FFWTBiomes.MOUNTAIN_SPRINGS,FFWTBiomes.MOUNTAIN_SPRINGS,FFWTBiomes.MOUNTAIN_SPRINGS,Biomes.GROVE},
		{Biomes.ICE_SPIKES, Biomes.ICE_SPIKES,			FFWTBiomes.MOUNTAIN_SPRINGS,Biomes.GROVE, 				Biomes.GROVE} // most icy
		// least vegetation														// most vegetation
	};
	
	@Override
	protected void addOffcoastBiomes(Consumer<Pair<ParameterPoint, ResourceKey<Biome>>> consumerPair) {
		this.addSurfaceBiome(consumerPair, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, oceanContRanges[0], 0.0F, Biomes.FROZEN_OCEAN);
		this.addSurfaceBiome(consumerPair, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, oceanContRanges[1], 0.0F, Biomes.DEEP_FROZEN_OCEAN);
		this.addSurfaceBiome(consumerPair, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, this.FULL_RANGE, oceanContRanges[2], 0.0F, Biomes.FROZEN_OCEAN);
	}

	@Override
	protected void addUndergroundBiomes(Consumer<Pair<ParameterPoint, ResourceKey<Biome>>> consumerPair) {
		this.addUndergroundBiome(consumerPair, span(0.6F, 1.0F), FULL_RANGE, FULL_RANGE, FULL_RANGE, FULL_RANGE, span(0.2F, 0.6F), 0.0F, FFWTBiomes.ICY_CAVES); // spawn in high icyness
		this.addUndergroundBiome(consumerPair, FULL_RANGE, span(0.6F, 1.0F), FULL_RANGE, FULL_RANGE, FULL_RANGE, 0.0F, Biomes.LUSH_CAVES); // spawn in high vegetation
		this.addUndergroundBiome(consumerPair, FULL_RANGE, FULL_RANGE, FULL_RANGE, FULL_RANGE, span(0.7F, 1.0F), 0.0F, Biomes.DRIPSTONE_CAVES); // spawn in high continentalness
	}

	@Override
	protected void addInlandBiomes(Consumer<Pair<ParameterPoint, ResourceKey<Biome>>> consumerPair) {		
		for (int i = 0; i < this.icynessRanges.length; ++i) {
            Climate.Parameter icyness = this.icynessRanges[i];
            for (int j = 0; j < this.vegetationRanges.length; ++j) {
                Climate.Parameter vegetation = this.vegetationRanges[j];
                
                for (int e = 0; e < this.erosionRanges.length; ++e) {
                	Climate.Parameter erosion = this.erosionRanges[e];
                	ResourceKey<Biome> normalBiome = pickNormalBiome(i, j);
                	ResourceKey<Biome> beachBiome = pickBeachBiome(e, i, j);
                    ResourceKey<Biome> slopeBiome = pickSlopeBiome(i, j);
                    ResourceKey<Biome> mountainBiome = pickMountainBiome(e);
                    ResourceKey<Biome> basinBiome = pickBasinBiome(i, j, false);
                    ResourceKey<Biome> weirdBasinBiome = pickBasinBiome(i, j, true);
                    ResourceKey<Biome> coastalRiverBiome = pickCoastalRiverBiome(i, j);
                    ResourceKey<Biome> inlandRiverBiome = pickInlandRiverBiome(e, i, j);
                    ResourceKey<Biome> glacierOrNormalBiome = maybePickGlacier(i, j);
                    ResourceKey<Biome> geoValleyOrNormalBiome = maybePickGeoValley(e, i, j);
                    
                    // island biomes
                    this.addSurfaceBiome(consumerPair, icyness, vegetation, this.FULL_RANGE, erosion, smallIslandCoastContRange, 0.0F, beachBiome);
                    this.addSurfaceBiome(consumerPair, icyness, vegetation, this.FULL_RANGE, erosion, smallIslandInlandContRange, 0.0F, normalBiome);
                    this.addSurfaceBiome(consumerPair, icyness, vegetation, this.FULL_RANGE, erosion, smallIslandSlopeContRange, 0.0F, slopeBiome);
                    this.addSurfaceBiome(consumerPair, icyness, vegetation, this.FULL_RANGE, erosion, smallIslandPeakContRange, 0.0F, mountainBiome);
                    
                    // positive basins
                    this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[0], erosion, span(0.1F, 1.0F), 0.0F, basinBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[0], erosion, span(0.0F, 0.1F), 0.0F, slopeBiome);
            		
            		// positive peaks
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[1], erosion, farInlandContRange, 0.0F, mountainBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[1], erosion, midInlandContRange, 0.0F, mountainBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[1], erosion, nearInlandContRange, 0.0F, normalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[1], erosion, coastContRange, 0.0F, normalBiome);
            		
            		// positive high
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[2], erosion, farInlandContRange, 0.0F, slopeBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[2], erosion, midInlandContRange, 0.0F, slopeBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[2], erosion, nearInlandContRange, 0.0F, normalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[2], erosion, coastContRange, 0.0F, normalBiome);
            		
            		// positive mid
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[3], erosion, farInlandContRange, 0.0F, normalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[3], erosion, midInlandContRange, 0.0F, normalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[3], erosion, nearInlandContRange, 0.0F, normalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[3], erosion, coastContRange, 0.0F, normalBiome);
            		
            		// positive low
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[4], erosion, span(0.8F, 1.0F), 0.0F, normalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[4], erosion, span(0.5F, 0.8F), 0.0F, glacierOrNormalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[4], erosion, midInlandContRange, 0.0F, geoValleyOrNormalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[4], erosion, nearInlandContRange, 0.0F, normalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[4], erosion, coastContRange, 0.0F, normalBiome);
            		
            		// valley
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[5], erosion, span(0.8F, 1.0F), 0.0F, normalBiome); // saddle valley
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[5], erosion, span(0.5F, 0.8F), 0.0F, glacierOrNormalBiome); // glaciers        		
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[5], erosion, midInlandContRange, 0.0F, inlandRiverBiome); // inland rivers
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[5], erosion, nearInlandContRange, 0.0F, coastalRiverBiome); // coastal rivers
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[5], erosion, coastContRange, 0.0F, Biomes.FROZEN_OCEAN); // add frozen ocean in river mouths
            		
            		// negative low
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[6], erosion, span(0.8F, 1.0F), 0.0F, normalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[6], erosion, span(0.5F, 0.8F), 0.0F, glacierOrNormalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[6], erosion, midInlandContRange, 0.0F, geoValleyOrNormalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[6], erosion, nearInlandContRange, 0.0F, normalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[6], erosion, coastContRange, 0.0F, normalBiome);
            		
            		// negative mid
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[7], erosion, farInlandContRange, 0.0F, normalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[7], erosion, midInlandContRange, 0.0F, normalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[7], erosion, nearInlandContRange, 0.0F, normalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[7], erosion, coastContRange, 0.0F, normalBiome);
            		
            		// negative high
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[8], erosion, farInlandContRange, 0.0F, slopeBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[8], erosion, midInlandContRange, 0.0F, slopeBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[8], erosion, nearInlandContRange, 0.0F, normalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[8], erosion, coastContRange, 0.0F, normalBiome);
            		
            		// negative peaks
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[9], erosion, farInlandContRange, 0.0F, mountainBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[9], erosion, midInlandContRange, 0.0F, mountainBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[9], erosion, nearInlandContRange, 0.0F, normalBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[9], erosion, coastContRange, 0.0F, normalBiome);
            		
            		// negative basins
                    this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[10], erosion, span(0.1F, 1.0F), 0.0F, weirdBasinBiome);
            		this.addSurfaceBiome(consumerPair, icyness, vegetation, landWeirdnessRanges[10], erosion, span(0.0F, 0.1F), 0.0F, slopeBiome);
                }
            }
		}		
	}
	
	private ResourceKey<Biome> maybePickGeoValley(int erosionIndex, int icynessIndex, int vegetationindex) {
		if(erosionIndex > 0 && erosionIndex < 3) {
			return FFWTBiomes.GEOTHERMAL_VALLEY;
		}
		return (erosionIndex > 0 && erosionIndex < 3) ? FFWTBiomes.GEOTHERMAL_VALLEY : pickNormalBiome(icynessIndex, vegetationindex);
	}

	/**
	 * Picks the biome to be used in coastlines.
	 */
	private ResourceKey<Biome> pickBeachBiome(int erosionIndex, int icynessIndex, int vegetationindex) {
		return (erosionIndex == 1 || erosionIndex == 3) ? Biomes.SNOWY_BEACH : this.pickNormalBiome(icynessIndex, vegetationindex);
	}

	/**
	 * Picks the biome to be used in "normal" parts of the terrain.
	 */
	private ResourceKey<Biome> pickNormalBiome(int icynessIndex, int vegetationIndex) {
		return normalBiomes[icynessIndex][vegetationIndex];
	}
	
	/**
	 * Picks the biome to be used in "slope" parts of the terrain.
	 */
	private ResourceKey<Biome> pickSlopeBiome(int icynessIndex, int vegetationIndex) {
		return vegetationIndex > 2 ? Biomes.GROVE : (icynessIndex == 0 ? Biomes.MEADOW : Biomes.SNOWY_SLOPES);
	}
	
	/**
	 * Picks the biome to be used in mountain peak terrain.
	 */
	private ResourceKey<Biome> pickMountainBiome(int erosionIndex) {
		return erosionIndex <= 1 ? Biomes.JAGGED_PEAKS : Biomes.FROZEN_PEAKS;
	}
	
	/**
	 * Picks the biome to be used in mountain basin terrain.
	 */
	private ResourceKey<Biome> pickBasinBiome(int icynessIndex, int vegetationIndex, boolean isWeird) {
		return isWeird ? basinBiomes[icynessIndex][this.vegetationRanges.length-vegetationIndex-1] : basinBiomes[icynessIndex][vegetationIndex];
	}
	
	/**
	 * Picks the biome to be used in a river valley area (low-ish continentalness).
	 */
	private ResourceKey<Biome> pickInlandRiverBiome(int erosionIndex, int icynessIndex, int vegetationIndex) {
		if(erosionIndex > 0 && erosionIndex < 3) {
			return FFWTBiomes.GEOTHERMAL_VALLEY;
		}
		return icynessIndex == 0 ? Biomes.RIVER : (icynessIndex <= 2 ? FFWTBiomes.COLD_RIVER : Biomes.FROZEN_RIVER);
	}
	
	/**
	 * Picks the biome to be used in a river valley area (low continentalness).
	 */
	private ResourceKey<Biome> pickCoastalRiverBiome(int icynessIndex, int vegetationIndex) {
		return icynessIndex == 0 ? Biomes.RIVER : FFWTBiomes.COLD_RIVER;
	}
	
	/**
	 * Picks the biome to be used in a saddle valley area (quite high continentalness).
	 */
	private ResourceKey<Biome> maybePickGlacier(int icynessIndex, int vegetationIndex) {
		return icynessIndex >= 2 ? FFWTBiomes.GLACIER : normalBiomes[icynessIndex][vegetationIndex];
	}

}
