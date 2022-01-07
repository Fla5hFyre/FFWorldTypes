package com.flashfyre.ffworldtypes.world;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.CubicSpline;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.TerrainShaper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSamplingSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.NoiseSlider;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

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
	            snowlands(), // surface rule source
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
		
		CubicSpline<TerrainShaper.Point> offsetSpline = CubicSpline.builder(TerrainShaper.Coordinate.CONTINENTS)
				.addPoint(-1.0F, 0.7F, 0.0F) // far inland
				.addPoint(-0.45F, 0.0F, 0.0F) // coastline
				.addPoint(-0.4F, -0.1F, 0.0F) // coastline
				.addPoint(-0.25F, -0.3F, 0.0F) // deepest ocean
				.addPoint(-0.15F, -0.3F, 0.0F) // deepest ocean
				.addPoint(0.0F, -0.1F, 0.0F) // coastline
				.addPoint(0.05F, createWeirdnessSlice(-0.15F, -0.05F, 0.35F, 0.5F, 0.35F), 0)
				.addPoint(0.1F, createWeirdnessSlice(-0.15F, -0.05F, 0.375F, 0.55F, 0.375F), 0)
				.addPoint(0.2F, createWeirdnessSlice(-0.15F, -0.05F, 0.4F, 0.6F, 0.4F), 0)
				.addPoint(0.3F, createWeirdnessSlice(-0.15F, -0.05F, 0.425F, 0.65F, 0.425F), 0)
				.addPoint(0.4F, createWeirdnessSlice(-0.15F, -0.05F, 0.45F, 0.7F, 0.45F), 0)
				.addPoint(0.5F, createWeirdnessSlice(-0.15F, -0.05F, 0.475F, 0.75F, 0.475F), 0) // glacier start
				.addPoint(0.6F, createWeirdnessSlice(0.2F, 0.25F, 0.5F, 0.8F, 0.5F), 0) // glacier start
				.addPoint(0.7F, createWeirdnessSlice(0.3F, 0.3F, 0.525F, 0.85F, 0.525F), 0)
				.addPoint(0.8F, createWeirdnessSlice(0.35F, 0.35F, 0.55F, 0.9F, 0.55F), 0)
				.addPoint(0.9F, createWeirdnessSlice(0.4F, 0.4F, 0.575F, 0.95F, 0.575F), 0)
				.addPoint(1.0F, createWeirdnessSlice(0.45F, 0.45F, 0.6F, 1.0F, 0.6F), 0)
				.build();
		CubicSpline<TerrainShaper.Point> factorSpline = CubicSpline.builder(TerrainShaper.Coordinate.CONTINENTS)
				.addPoint(0.0F, 10.0F, 0.0F)
				.addPoint(0.05F, CubicSpline.builder(TerrainShaper.Coordinate.WEIRDNESS)
						.addPoint(-0.8F, 7.0F, 0.0F)
						.addPoint(-0.6F, 12.0F, 0.0F) // more solid at peaks
						.addPoint(-0.4F, 7.0F, 0.0F)
						.addPoint(-0.2F, 7.0F, 0.0F)
						.addPoint(0.0F, 10.0F, 0.0F) // more solid at rivers
						.addPoint(0.2F, 7.0F, 0.0F)
						.addPoint(0.4F, 7.0F, 0.0F)
						.addPoint(0.6F, 12.0F, 0.0F) // more solid at peaks
						.addPoint(0.8F, 7.0F, 0.0F)
						.build(), 
						0.0F)
				.build();
				
				
		CubicSpline<TerrainShaper.Point> jaggednessSpline = createJaggednessSpline();
		
        return new TerrainShaper(offsetSpline, factorSpline, jaggednessSpline);
    }
	
	public static CubicSpline<TerrainShaper.Point> createWeirdnessSlice(float valleyOffset, float valleyEdgeOffset, float slopeOffset, float peakOffset, float basinOffset) {
		return CubicSpline.builder(TerrainShaper.Coordinate.WEIRDNESS)
				.addPoint(-0.9F, basinOffset, 0.0F)
				.addPoint(-0.6F, peakOffset, 0.0F) // peak
				.addPoint(-0.3F, slopeOffset, 0.0F)
				.addPoint(-0.1F, valleyEdgeOffset, 0.0F)
				.addPoint(0.0F, valleyOffset, 0.0F)
				.addPoint(0.1F, valleyEdgeOffset, 0.0F)
				.addPoint(0.3F, slopeOffset, 0.0F)
				.addPoint(0.6F, peakOffset, 0.0F) // peak
				.addPoint(0.9F, basinOffset, 0.0F)
				.build();
		
	}
	
	public static CubicSpline<TerrainShaper.Point> createJaggednessSpline() {
		return CubicSpline.builder(TerrainShaper.Coordinate.CONTINENTS)
				.addPoint(-0.9F, 1.0F, 0.0F)
				.addPoint(-0.7F, 0.0F, 0.0F)
				.addPoint(0.2F, 0.0F, 0.0F)
				.addPoint(0.25F, createBigIslandJaggedness(0.8F, 0.6F, 0.3F, 0.4F), 0.0F)
				.addPoint(1.0F, createBigIslandJaggedness(1.0F, 0.8F, 0.4F, 0.5F), 0.0F)
				.build();
		
	}
	
	public static CubicSpline<TerrainShaper.Point> createBigIslandJaggedness(float veryNegativeErosionJaggedness, float negativeErosionJaggedness, float positiveErosionJaggedness, float veryPositiveErosionJaggedness) {
		return CubicSpline.builder(TerrainShaper.Coordinate.WEIRDNESS)
				.addPoint(-0.7F, 0.0F, 0.0F)
				.addPoint(-0.6F, createErosionJaggednessSpline(veryNegativeErosionJaggedness, negativeErosionJaggedness, positiveErosionJaggedness, veryPositiveErosionJaggedness), 0.0F)
				.addPoint(-0.5F, 0.0F, 0.0F)
				.addPoint(0.0F, 0.0F, 0.0F)
				.addPoint(0.5F, 0.0F, 0.0F)
				.addPoint(0.6F, createErosionJaggednessSpline(veryNegativeErosionJaggedness, negativeErosionJaggedness, positiveErosionJaggedness, veryPositiveErosionJaggedness), 0.0F)
				.addPoint(0.7F, 0.0F, 0.0F)
				.build();
		
	}
	
	public static CubicSpline<TerrainShaper.Point> createErosionJaggednessSpline(float veryNegativeErosionJaggedness, float negativeErosionJaggedness, float positiveErosionJaggedness, float veryPositiveErosionJaggedness) {
		return CubicSpline.builder(TerrainShaper.Coordinate.EROSION)
				.addPoint(-1.0F, 1.0F, 0.0F)
				.addPoint(-0.1F, 0.8F, 0.0F)
				.addPoint(0.1F, 0.4F, 0.0F)
				.addPoint(1.0F, 0.5F, 0.0F)
				.build();
		
	}
	
	private static final SurfaceRules.RuleSource AIR = makeStateRule(Blocks.AIR);
	private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
	private static final SurfaceRules.RuleSource WHITE_TERRACOTTA = makeStateRule(Blocks.WHITE_TERRACOTTA);
	private static final SurfaceRules.RuleSource ORANGE_TERRACOTTA = makeStateRule(Blocks.ORANGE_TERRACOTTA);
	private static final SurfaceRules.RuleSource TERRACOTTA = makeStateRule(Blocks.TERRACOTTA);
	private static final SurfaceRules.RuleSource RED_SAND = makeStateRule(Blocks.RED_SAND);
	private static final SurfaceRules.RuleSource RED_SANDSTONE = makeStateRule(Blocks.RED_SANDSTONE);
	private static final SurfaceRules.RuleSource STONE = makeStateRule(Blocks.STONE);
	private static final SurfaceRules.RuleSource DEEPSLATE = makeStateRule(Blocks.DEEPSLATE);
	private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
	private static final SurfaceRules.RuleSource PODZOL = makeStateRule(Blocks.PODZOL);
	private static final SurfaceRules.RuleSource COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);
	private static final SurfaceRules.RuleSource MYCELIUM = makeStateRule(Blocks.MYCELIUM);
	private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
	private static final SurfaceRules.RuleSource CALCITE = makeStateRule(Blocks.CALCITE);
	private static final SurfaceRules.RuleSource GRAVEL = makeStateRule(Blocks.GRAVEL);
	private static final SurfaceRules.RuleSource SAND = makeStateRule(Blocks.SAND);
	private static final SurfaceRules.RuleSource SANDSTONE = makeStateRule(Blocks.SANDSTONE);
	private static final SurfaceRules.RuleSource PACKED_ICE = makeStateRule(Blocks.PACKED_ICE);
	private static final SurfaceRules.RuleSource SNOW_BLOCK = makeStateRule(Blocks.SNOW_BLOCK);
	private static final SurfaceRules.RuleSource POWDER_SNOW = makeStateRule(Blocks.POWDER_SNOW);
	private static final SurfaceRules.RuleSource ICE = makeStateRule(Blocks.ICE);
	private static final SurfaceRules.RuleSource WATER = makeStateRule(Blocks.WATER);
	
	public static SurfaceRules.RuleSource snowlands() {
		boolean hasSurface = true;
		boolean bedrockRoof = false;
		boolean bedrockFloor = true;
		SurfaceRules.ConditionSource surfacerules$conditionsource = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(97), 2);
		SurfaceRules.ConditionSource surfacerules$conditionsource1 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(256), 0);
		SurfaceRules.ConditionSource surfacerules$conditionsource2 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(63), -1);
		SurfaceRules.ConditionSource surfacerules$conditionsource3 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(74), 1);
		SurfaceRules.ConditionSource surfacerules$conditionsource4 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
		SurfaceRules.ConditionSource surfacerules$conditionsource5 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(63), 0);
		SurfaceRules.ConditionSource aboveWater = SurfaceRules.waterBlockCheck(-1, 0);
		SurfaceRules.ConditionSource surfacerules$conditionsource7 = SurfaceRules.waterBlockCheck(0, 0);
		SurfaceRules.ConditionSource surfacerules$conditionsource8 = SurfaceRules.waterStartCheck(-6, -1);
		SurfaceRules.ConditionSource isHole = SurfaceRules.hole();
		SurfaceRules.ConditionSource isFrozenOcean = SurfaceRules.isBiome(Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN);
		SurfaceRules.ConditionSource isSteep = SurfaceRules.steep();
		SurfaceRules.RuleSource grassOrDirt = SurfaceRules.sequence(SurfaceRules.ifTrue(aboveWater, GRASS_BLOCK), DIRT);
		SurfaceRules.RuleSource sandCeiling = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, SANDSTONE), SAND);
		SurfaceRules.RuleSource gravelCeiling = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, STONE), GRAVEL);
		SurfaceRules.ConditionSource surfacerules$conditionsource12 = SurfaceRules.isBiome(Biomes.WARM_OCEAN, Biomes.DESERT, Biomes.BEACH, Biomes.SNOWY_BEACH);
		SurfaceRules.RuleSource surfacerules$rulesource3 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.STONY_PEAKS), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.CALCITE, -0.0125D, 0.0125D), CALCITE), STONE)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.STONY_SHORE), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.GRAVEL, -0.05D, 0.05D), gravelCeiling), STONE)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_HILLS), SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE)), SurfaceRules.ifTrue(surfacerules$conditionsource12, sandCeiling), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.DRIPSTONE_CAVES), STONE));
		SurfaceRules.RuleSource surfacerules$rulesource4 = SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.45D, 0.58D), POWDER_SNOW);
		SurfaceRules.RuleSource surfacerules$rulesource5 = SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.35D, 0.6D), POWDER_SNOW);
		SurfaceRules.RuleSource surfacerules$rulesource6 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS), SurfaceRules.sequence(SurfaceRules.ifTrue(isSteep, PACKED_ICE), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PACKED_ICE, -0.5D, 0.2D), PACKED_ICE), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, -0.0625D, 0.025D), ICE), SNOW_BLOCK)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SNOWY_SLOPES), SurfaceRules.sequence(SurfaceRules.ifTrue(isSteep, STONE), surfacerules$rulesource4, SNOW_BLOCK)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.JAGGED_PEAKS), STONE), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.GROVE), SurfaceRules.sequence(surfacerules$rulesource4, DIRT)), surfacerules$rulesource3, SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_SAVANNA), SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_GRAVELLY_HILLS), SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), gravelCeiling), SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE), SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), DIRT), gravelCeiling)), DIRT);
		SurfaceRules.RuleSource surfacerules$rulesource7 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS), SurfaceRules.sequence(SurfaceRules.ifTrue(isSteep, PACKED_ICE), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PACKED_ICE, 0.0D, 0.2D), PACKED_ICE), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.ICE, 0.0D, 0.025D), ICE), SNOW_BLOCK)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SNOWY_SLOPES), SurfaceRules.sequence(SurfaceRules.ifTrue(isSteep, STONE), surfacerules$rulesource5, SNOW_BLOCK)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.JAGGED_PEAKS), SurfaceRules.sequence(SurfaceRules.ifTrue(isSteep, STONE), SNOW_BLOCK)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.GROVE), SurfaceRules.sequence(surfacerules$rulesource5, SNOW_BLOCK)), surfacerules$rulesource3, SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_SAVANNA), SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE), SurfaceRules.ifTrue(surfaceNoiseAbove(-0.5D), COARSE_DIRT))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WINDSWEPT_GRAVELLY_HILLS), SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), gravelCeiling), SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE), SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), grassOrDirt), gravelCeiling)), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA), SurfaceRules.sequence(SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), COARSE_DIRT), SurfaceRules.ifTrue(surfaceNoiseAbove(-0.95D), PODZOL))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.ICE_SPIKES), SNOW_BLOCK), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.MUSHROOM_FIELDS), MYCELIUM), grassOrDirt);
		SurfaceRules.ConditionSource surfacerules$conditionsource13 = SurfaceRules.noiseCondition(Noises.SURFACE, -0.909D, -0.5454D);
		SurfaceRules.ConditionSource surfacerules$conditionsource14 = SurfaceRules.noiseCondition(Noises.SURFACE, -0.1818D, 0.1818D);
		SurfaceRules.ConditionSource surfacerules$conditionsource15 = SurfaceRules.noiseCondition(Noises.SURFACE, 0.5454D, 0.909D);
		SurfaceRules.RuleSource allSurfaceRules = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WOODED_BADLANDS), SurfaceRules.ifTrue(surfacerules$conditionsource, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource13, COARSE_DIRT), SurfaceRules.ifTrue(surfacerules$conditionsource14, COARSE_DIRT), SurfaceRules.ifTrue(surfacerules$conditionsource15, COARSE_DIRT), grassOrDirt))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.SWAMP), SurfaceRules.ifTrue(surfacerules$conditionsource4, SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource5), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP, 0.0D), WATER)))))), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource1, ORANGE_TERRACOTTA), SurfaceRules.ifTrue(surfacerules$conditionsource3, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource13, TERRACOTTA), SurfaceRules.ifTrue(surfacerules$conditionsource14, TERRACOTTA), SurfaceRules.ifTrue(surfacerules$conditionsource15, TERRACOTTA), SurfaceRules.bandlands())), SurfaceRules.ifTrue(aboveWater, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, RED_SANDSTONE), RED_SAND)), SurfaceRules.ifTrue(SurfaceRules.not(isHole), ORANGE_TERRACOTTA), SurfaceRules.ifTrue(surfacerules$conditionsource8, WHITE_TERRACOTTA), gravelCeiling)), SurfaceRules.ifTrue(surfacerules$conditionsource2, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource5, SurfaceRules.ifTrue(SurfaceRules.not(surfacerules$conditionsource3), ORANGE_TERRACOTTA)), SurfaceRules.bandlands())), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(surfacerules$conditionsource8, WHITE_TERRACOTTA)))), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(aboveWater, SurfaceRules.sequence(SurfaceRules.ifTrue(isFrozenOcean, SurfaceRules.ifTrue(isHole, SurfaceRules.sequence(SurfaceRules.ifTrue(surfacerules$conditionsource7, AIR), SurfaceRules.ifTrue(SurfaceRules.temperature(), ICE), WATER))), surfacerules$rulesource7))), SurfaceRules.ifTrue(surfacerules$conditionsource8, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(isFrozenOcean, SurfaceRules.ifTrue(isHole, WATER))), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, surfacerules$rulesource6), SurfaceRules.ifTrue(surfacerules$conditionsource12, SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, true, true, CaveSurface.FLOOR), SANDSTONE)))), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS), STONE), SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN), sandCeiling), gravelCeiling)));
		Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
		if (bedrockRoof) {
			builder.add(SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK));
		}	
		
		if (bedrockFloor) {
			builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK));
		}
		
		SurfaceRules.RuleSource allSurfaceRulesAbovePreliminarySurface = SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), allSurfaceRules);
		
		SurfaceRules.RuleSource defaultSurface = SurfaceRules.sequence(
				SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, grassOrDirt),
				SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, DIRT)
				);
		
		SurfaceRules.RuleSource defaultSurfaceAndSnow = SurfaceRules.sequence(
				SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.45D, 0.58D), SNOW_BLOCK),
				defaultSurface
				);
		
		builder.add(SurfaceRules.sequence(
				SurfaceRules.ifTrue(SurfaceRules.isBiome(FFWTBiomes.ICY_CAVES), STONE)
				));
		builder.add(SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), SurfaceRules.sequence(
				SurfaceRules.ifTrue(SurfaceRules.isBiome(FFWTBiomes.GLACIER), PACKED_ICE),
				SurfaceRules.ifTrue(SurfaceRules.isBiome(FFWTBiomes.GEOTHERMAL_VALLEY), defaultSurfaceAndSnow),
				SurfaceRules.ifTrue(SurfaceRules.isBiome(FFWTBiomes.MOUNTAIN_SPRINGS), defaultSurface)
				)));
		
		builder.add(hasSurface ? allSurfaceRulesAbovePreliminarySurface : allSurfaceRules);
		builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)), DEEPSLATE));
		return SurfaceRules.sequence(builder.build().toArray((p_198379_) -> {
			return new SurfaceRules.RuleSource[p_198379_];
		}));
	}
	
	private static SurfaceRules.RuleSource makeStateRule(Block p_194811_) {
	      return SurfaceRules.state(p_194811_.defaultBlockState());
	}
	
	private static SurfaceRules.ConditionSource surfaceNoiseAbove(double p_194809_) {
	      return SurfaceRules.noiseCondition(Noises.SURFACE, p_194809_ / 8.25D, Double.MAX_VALUE);
	}
}
