package com.flashfyre.ffworldtypes.world.types;

import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.CubicSpline;
import net.minecraft.world.level.biome.TerrainShaper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSamplingSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.NoiseSlider;
import net.minecraft.world.level.levelgen.StructureSettings;

public class FFWTNoiseGeneratorSettings {
	
	public static ResourceKey<NoiseGeneratorSettings> register(ResourceKey<NoiseGeneratorSettings> key, NoiseGeneratorSettings settings) {
        BuiltinRegistries.register(BuiltinRegistries.NOISE_GENERATOR_SETTINGS, key.location(), settings);
        return key;
    }

	public static NoiseGeneratorSettings createSnowlandsNoiseGeneratorSettings() {
		return new NoiseGeneratorSettings(
	            new StructureSettings(true),
	            NoiseSettings.create(
	                -64, // min y
	                384, // height
	                new NoiseSamplingSettings(1.0D, 1.0D, 80.0D, 160.0D),  //xz scale, xz factor, y scale, y factor
	                new NoiseSlider(-0.078125D, 2, 8), // top slide
	                new NoiseSlider(0.1171875D, 3, 0), // bottom slide
	                1, // noise size horizontal
	                2, // noise size vertical
	                false, // island noise override (used for the end)
	                false, // amplified
	                false, // large biomes
	                createSnowlandsTerrainShaper() // terrain shaper
	            ),
	            Blocks.STONE.defaultBlockState(), // default block
	            Blocks.WATER.defaultBlockState(), // default fluid
	            SurfaceRuleData.overworld(), // surface rule source
	            63, // Sea level
	            false, // disable mob generation
	            true, // aquifers enabled
	            true, // noise caves enabled
	            true, // ore veins enabled
	            true, // noodle caves enabled
	            false // use legacy random source
	        );
	}
	
	public static TerrainShaper createSnowlandsTerrainShaper() {		
		CubicSpline<TerrainShaper.Point> valleyOffsetSpline = createValleyOffsetSpline();
		CubicSpline<TerrainShaper.Point> lowOffsetSpline = createLowOffsetSpline();
		CubicSpline<TerrainShaper.Point> peakOffsetSpline = createPeakOffsetSpline();
		
		CubicSpline<TerrainShaper.Point> offsetSpline = CubicSpline.builder(TerrainShaper.Coordinate.WEIRDNESS)
				.addPoint(-1.0F, 0.3F, 0.0F)
				.addPoint(-0.5F, peakOffsetSpline, 0.0F) // peaks
				.addPoint(-0.1F, lowOffsetSpline, 0.0F)
				.addPoint(0.0F, valleyOffsetSpline, 0.0F) // rivers
				.addPoint(0.1F, lowOffsetSpline, 0.0F)
				.addPoint(0.5F, peakOffsetSpline, 0.0F) // peaks
				.addPoint(1.0F, 0.3F, 0.0F)
				.build();
		CubicSpline<TerrainShaper.Point> factorSpline = CubicSpline.builder(TerrainShaper.Coordinate.EROSION)
				.addPoint(-1.0F, 3.0F, 0.0F)
				.addPoint(1.0F, 7.0F, 0.0F)
				.build();
		CubicSpline<TerrainShaper.Point> jaggednessSpline = CubicSpline.builder(TerrainShaper.Coordinate.WEIRDNESS)
				.addPoint(-0.7F, 0.0F, 0.0F)
				.addPoint(-0.5F, 1.0F, 0.0F) // peaks
				.addPoint(-0.3F, 0.0F, 0.0F)
				.addPoint(0.3F, 0.0F, 0.0F)
				.addPoint(0.5F, 1.0F, 0.0F) // peaks
				.addPoint(0.7F, 0.0F, 0.0F)
				.build();
		
        return new TerrainShaper(offsetSpline, factorSpline, jaggednessSpline);
    }
	
	public static CubicSpline<TerrainShaper.Point> createValleyOffsetSpline() {
		return CubicSpline.builder(TerrainShaper.Coordinate.CONTINENTS)
				.addPoint(-0.25F, -0.2F, 0.0F)
				.addPoint(0.0F, 0.2F, 0.0F)
				.addPoint(0.25F, -0.2F, 0.0F)
				.build();
	}
	
	public static CubicSpline<TerrainShaper.Point> createLowOffsetSpline() {
		return CubicSpline.builder(TerrainShaper.Coordinate.CONTINENTS)
				.addPoint(-0.2F, 0.0F, 0.0F)
				.addPoint(0.0F, 0.3F, 0.0F)
				.addPoint(0.2F, 0.0F, 0.0F)
				.build();
	}
	
	public static CubicSpline<TerrainShaper.Point> createPeakOffsetSpline() {
		return CubicSpline.builder(TerrainShaper.Coordinate.CONTINENTS)
				.addPoint(-1.0F, 0.6F, 0.0F)
				.addPoint(1.0F, 0.9F, 0.0F)
				.build();
	}
}
