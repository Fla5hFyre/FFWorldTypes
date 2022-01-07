package com.flashfyre.ffworldtypes.world.feature;

import java.util.List;

import com.flashfyre.ffworldtypes.FFWT;
import com.flashfyre.ffworldtypes.world.util.DoubleAndStateList;
import com.flashfyre.ffworldtypes.world.util.NoiseMatchStateProvider;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;

import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseThresholdProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FFWTFeatures {
	
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, FFWT.MOD_ID);
	
	public static final RegistryObject<Feature<SimpleBlockConfiguration>> BLOCK_REPLACE = register("block_replace", new BlockReplaceFeature(SimpleBlockConfiguration.CODEC));
	
	private static <FC extends FeatureConfiguration, F extends Feature<FC>> RegistryObject<F> register(String id, F feature) {
		RegistryObject<F> regObject = FEATURES.register(id, () -> feature);
		return regObject;		
	}
	
	public static final class ConfiguredFeatures {
		
		public static ConfiguredFeature<?, ?> pool;
		public static ConfiguredFeature<?, ?> stone_pool_patches;
		public static ConfiguredFeature<?, ?> valley_spring;
		public static ConfiguredFeature<?, ?> ore_ice;
		public static ConfiguredFeature<?, ?> air;
		
		public static final NoiseParameters POOL_NOISE = new NoiseParameters(-4, 1.0);
		
		private static final BlockState AIR = Blocks.AIR.defaultBlockState();
		private static final BlockState WATER = Blocks.WATER.defaultBlockState();
		private static final BlockState STONE = Blocks.STONE.defaultBlockState();
		private static final BlockState COBBLESTONE = Blocks.COBBLESTONE.defaultBlockState();
		private static final BlockState ANDESITE = Blocks.ANDESITE.defaultBlockState();
		private static final BlockState SNOW_BLOCK = Blocks.SNOW_BLOCK.defaultBlockState();
		private static final BlockState PACKED_ICE = Blocks.PACKED_ICE.defaultBlockState();
		
		public static void registerConfiguredFeatures() {
			air = register("air", Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.AIR))));
			//stone = register("stone", BLOCK_REPLACE.get().configured(new SimpleBlockConfiguration(noiseStateProvider(POOL_NOISE, -0.1F, STONE, 0.4F, AIR))));
			pool  = register("pool", BLOCK_REPLACE.get().configured(new SimpleBlockConfiguration(noiseStateProvider(POOL_NOISE, 0.0F, WATER))));
			stone_pool_patches  = register("stone_pool_patches", BLOCK_REPLACE.get().configured(new SimpleBlockConfiguration(new NoiseMatchStateProvider(POOL_NOISE, List.of(
					new DoubleAndStateList(-0.2D, List.of(AIR)),
					new DoubleAndStateList(-0.01D, List.of(STONE, COBBLESTONE, ANDESITE, AIR)),
					new DoubleAndStateList(0.0D, List.of(STONE, COBBLESTONE, ANDESITE))
					)))));
			valley_spring = register("valley_spring", Feature.SPRING.configured(new SpringConfiguration(Fluids.WATER.defaultFluidState(), true, 5, 1, ImmutableSet.of(Blocks.STONE, Blocks.COBBLESTONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DEEPSLATE, Blocks.TUFF, Blocks.CALCITE, Blocks.DIRT, Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW, Blocks.PACKED_ICE))));
			ore_ice = register("ore_ice", Feature.ORE.configured(new OreConfiguration(OreFeatures.NATURAL_STONE, PACKED_ICE, 33)));
			
		}
		
		private static ConfiguredFeature<?, ?> register(String id, ConfiguredFeature<?, ?> placedFeature) {
			Registry<ConfiguredFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_FEATURE;
			Registry.register(registry, FFWT.id(id), placedFeature);
			return placedFeature;
		}
		
		/**
		 * 
		 * @param noise The NoiseParameters instance to use.
		 * @param threshold The minimum noise value for the state to be picked.
		 * @param state The blockstate to place should the noise be above the threshold.
		 * @return
		 */
		private static NoiseThresholdProvider noiseStateProvider(NoiseParameters noise, float threshold, BlockState state) {
			return new NoiseThresholdProvider(0, noise, 1.0F, threshold, 0.0F, state, List.of(AIR), List.of(AIR));
		}
		
		/**
		 * 
		 * @param noise The NoiseParameters instance to use.
		 * @param threshold The minimum noise value for the state to be picked.
		 * @param state The blockstate to place should the noise be above the threshold.
		 * @return
		 */
		private static NoiseThresholdProvider noiseStateProvider(NoiseParameters noise, float threshold, BlockState state, float chanceOfOtherState, BlockState otherState) {
			return new NoiseThresholdProvider(0, noise, 1.0F, threshold, chanceOfOtherState, state, List.of(AIR), List.of(otherState));
		}
	}
	
	public static final class PlacedFeatures {
		public static PlacedFeature pool;
		public static PlacedFeature stone_pool_patches;
		public static PlacedFeature cascade_maker;
		public static PlacedFeature mountain_springs_trees;
		public static PlacedFeature ore_ice;
		public static PlacedFeature valley_springs;
		
		private static final List<Block> poolBlocks = List.of(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.STONE, Blocks.ANDESITE, Blocks.COBBLESTONE);
		
		public static void registerPlacedFeatures() {
			stone_pool_patches = register("stone_pool_patches", ConfiguredFeatures.stone_pool_patches.placed(List.of(
					EveryBlockPlacement.of(),
					PlacementUtils.HEIGHTMAP_TOP_SOLID,
					RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
					BiomeFilter.biome()
					)));
			cascade_maker = register("cascade_maker", ConfiguredFeatures.air.placed(List.of(
					CountPlacement.of(30),
					InSquarePlacement.spread(),
					PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
					RandomOffsetPlacement.vertical(ConstantInt.of(1)),
					Placements.POOL_EDGE,
					BiomeFilter.biome()
					)));
			valley_springs = register("valley_springs", ConfiguredFeatures.valley_spring.placed(
					CountPlacement.of(60),
					InSquarePlacement.spread(),
					HeightRangePlacement.uniform(VerticalAnchor.absolute(63), VerticalAnchor.absolute(192)),
					BiomeFilter.biome()
					));
			pool = register("pool", ConfiguredFeatures.pool.placed(List.of(
					EveryBlockPlacement.of(), // run on every block in the chunk
					PlacementUtils.HEIGHTMAP_WORLD_SURFACE, // snap each y coord to heightmap
					BiomeFilter.biome(), // check biome
					CountPlacement.of(10), // run 10 times
					//RandomOffsetPlacement.vertical(ConstantInt.of(1)), // offset up by one
					EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.allOf(
							BlockPredicate.not(BlockPredicate.anyOf(  // if block isn't next to air
									BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(-1, 0, 0)),
									BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(1, 0, 0)),
									BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(0, 0, -1)),
									BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(0, 0, 1)),
									BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(-1, 0, -1)),
									BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(1, 0, 1)),
									BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(-1, 0, 1)),
									BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(1, 0, -1))
									)),
							BlockPredicate.matchesBlocks(poolBlocks),//, // if block is 
							BlockPredicate.allOf(
									BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(-1, 1, 0)),
									BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(1, 1, 0)),
									BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(0, 1, -1)),
									BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(0, 1, 1)),
									BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(-1, 1, -1)),
									BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(1, 1, 1)),
									BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(-1, 1, 1)),
									BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(1, 1, -1))
									)
							/*BlockPredicate.not(BlockPredicate.anyOf( // if all blocks above and diagonal are not air
									BlockPredicate.matchesBlocks(poolBlocks, new Vec3i(-1, 1,0)),
									BlockPredicate.matchesBlocks(poolBlocks, new Vec3i(1, 1, 0)),
									BlockPredicate.matchesBlocks(poolBlocks, new Vec3i(0, 1, -1)),
									BlockPredicate.matchesBlocks(poolBlocks, new Vec3i(0, 1, 1)),
									BlockPredicate.matchesBlocks(poolBlocks, new Vec3i(-1, 1, -1)),
									BlockPredicate.matchesBlocks(poolBlocks, new Vec3i(-1, 1, 1)),
									BlockPredicate.matchesBlocks(poolBlocks, new Vec3i(1, 1, -1)),
									BlockPredicate.matchesBlocks(poolBlocks, new Vec3i(1, 1, 1))))*/
									), 
							6)
					)));
			
			mountain_springs_trees = register("mountain_springs_trees", TreeFeatures.SPRUCE.placed(VegetationPlacements.treePlacement(CountPlacement.of(2), Blocks.SPRUCE_SAPLING)));
			
			ore_ice = register("ore_ice", ConfiguredFeatures.ore_ice.placed(List.of(CountPlacement.of(75), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(160)), BiomeFilter.biome())));
		}
		
		private static PlacedFeature register(String id, PlacedFeature placedFeature) {
			Registry<PlacedFeature> registry = BuiltinRegistries.PLACED_FEATURE;
			Registry.register(registry, FFWT.id(id), placedFeature);
			return placedFeature;
		}
	}
	
	public static final class Placements {
		public static PlacementModifierType<?> EVERY_BLOCK;
		
		public static void registerPlacements() {
			EVERY_BLOCK = register("every_block", EveryBlockPlacement.CODEC);
		}
		
		private static <P extends PlacementModifier> PlacementModifierType<P> register(String id, Codec<P> codec) {
	        return Registry.register(Registry.PLACEMENT_MODIFIERS, FFWT.id(id), () -> codec);
	    }
		
		public static final EnvironmentScanPlacement POOL_EDGE = EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.anyOf(
				BlockPredicate.allOf(
						BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(-1, 0, 0)),
						BlockPredicate.matchesBlock(Blocks.WATER, new Vec3i(1, 0, 0))),
				BlockPredicate.allOf(
						BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(1, 0, 0)),
						BlockPredicate.matchesBlock(Blocks.WATER, new Vec3i(-1, 0, 0))),
				BlockPredicate.allOf(
						BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(0, 0, -1)),
						BlockPredicate.matchesBlock(Blocks.WATER, new Vec3i(0, 0, 1))),
				BlockPredicate.allOf(
						BlockPredicate.matchesBlock(Blocks.AIR, new Vec3i(0, 0, 1)),
						BlockPredicate.matchesBlock(Blocks.WATER, new Vec3i(0, 0, -1)))), 
				2);
	}

}
