package com.flashfyre.ffworldtypes.world.types;

import java.util.function.Supplier;

import com.flashfyre.ffworldtypes.FFWT;
import com.flashfyre.ffworldtypes.world.FFWTNoiseGeneratorSettings;
import com.flashfyre.ffworldtypes.world.biomebuilder.SnowlandsBiomeBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
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
		Builder<Pair<Climate.ParameterPoint, Supplier<Biome>>> builder = ImmutableList.builder();
        (new SnowlandsBiomeBuilder()).addBiomes((pair) -> {
           builder.add(pair.mapSecond((biome) -> {
              return () -> {
                 return biomeReg.getOrThrow(biome);
              };
           }));
        });
        return new Climate.ParameterList<>(builder.build());
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
