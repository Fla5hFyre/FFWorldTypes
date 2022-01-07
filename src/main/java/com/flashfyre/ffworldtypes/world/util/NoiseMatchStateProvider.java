package com.flashfyre.ffworldtypes.world.util;

import java.util.List;
import java.util.Random;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseBasedStateProvider;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;

public class NoiseMatchStateProvider extends NoiseBasedStateProvider {
	
	public static final Codec<NoiseMatchStateProvider> CODEC = RecordCodecBuilder.create((instance) -> {
		return noiseCodec(instance).and(DoubleAndStateList.CODEC.listOf().fieldOf("state_map").forGetter(provider -> provider.stateMap)
				).apply(instance, NoiseMatchStateProvider::new);
	});
	
	private final List<DoubleAndStateList> stateMap;

	public NoiseMatchStateProvider(long seed, NoiseParameters noise, float scale, List<DoubleAndStateList> stateMap) {
		super(seed, noise, scale);
		this.stateMap = stateMap;
	}
	
	public NoiseMatchStateProvider(NoiseParameters noise, List<DoubleAndStateList> stateMap) {
		this(0, noise, 1.0F, stateMap);
	}

	@Override
	protected BlockStateProviderType<?> type() {
		return FFWTBlockStateProviderTypes.NOISE_MATCH_STATE_PROVIDER.get();
	}

	@Override
	public BlockState getState(Random rand, BlockPos pos) {
		double noiseVal = this.getNoiseValue(pos, (double)this.scale);
		List<BlockState> states = getClosestStateList(noiseVal, this.stateMap);
		return Util.getRandom(states, rand);
	}

	private List<BlockState> getClosestStateList(double query, List<DoubleAndStateList> stateMap) {
		
		double min = Double.MAX_VALUE;
		List<BlockState> states = List.of();
		
		for (DoubleAndStateList floatState : stateMap) {
			final double diff = Math.abs(floatState.value() - query);			
			if(diff < min) {
				min = diff;
				states = floatState.states();
			}
		}
		return states;
	}

}
