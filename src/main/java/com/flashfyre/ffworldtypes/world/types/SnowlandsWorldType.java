package com.flashfyre.ffworldtypes.world.types;

import com.flashfyre.ffworldtypes.FFWT;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;
import net.minecraftforge.common.world.ForgeWorldPreset;

public class SnowlandsWorldType extends ForgeWorldPreset {
	
	public static final ResourceKey<NoiseGeneratorSettings> SNOWLANDS_NOISE_SETTINGS = FFWTNoiseGeneratorSettings.register(ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation(FFWT.MOD_ID, "snowlands")), FFWTNoiseGeneratorSettings.createSnowlandsNoiseGeneratorSettings());
	
	public SnowlandsWorldType() {
		super(null);
	}
	
	public static final MultiNoiseBiomeSource.Preset SNOWLANDS = new MultiNoiseBiomeSource.Preset(FFWT.id("snowlands"), (biomeReg) -> {
        return new Climate.ParameterList<>(ImmutableList.of(
        		Pair.of(Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), () -> {
        			return biomeReg.getOrThrow(Biomes.SNOWY_PLAINS);
        		}), Pair.of(Climate.parameters(0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), () -> {
        			return biomeReg.getOrThrow(Biomes.ICE_SPIKES);
        		}), Pair.of(Climate.parameters(0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), () -> {
        	return biomeReg.getOrThrow(Biomes.GROVE);
        		}), Pair.of(Climate.parameters(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), () -> {
        			return biomeReg.getOrThrow(Biomes.SNOWY_TAIGA);
        		}), Pair.of(Climate.parameters(-0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), () -> {
        			return biomeReg.getOrThrow(Biomes.FROZEN_PEAKS);
        		})));
	});

	@Override
	public ChunkGenerator createChunkGenerator(RegistryAccess registryAccess, long seed, String generatorSettings) {
		Registry<NoiseParameters> noiseParameterReg = registryAccess.registryOrThrow(Registry.NOISE_REGISTRY);
		Registry<NoiseGeneratorSettings> noiseGenSettingsReg = registryAccess.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);
		return new NoiseBasedChunkGenerator(noiseParameterReg, SNOWLANDS.biomeSource(registryAccess.registryOrThrow(Registry.BIOME_REGISTRY)), seed, () -> noiseGenSettingsReg.getOrThrow(SNOWLANDS_NOISE_SETTINGS));
	}
	
	@Override
	public WorldGenSettings createSettings(RegistryAccess registryAccess, long seed, boolean generateStructures,
			boolean generateLoot, String generatorSettings) {
		Registry<DimensionType> dimensionTypeRegistry = registryAccess.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
		return new WorldGenSettings(seed, generateStructures, generateLoot, 
				WorldGenSettings.withOverworld(dimensionTypeRegistry,
                        DimensionType.defaultDimensions(registryAccess, seed),
                        createChunkGenerator(registryAccess, seed, generatorSettings)));
	}

}
