package com.flashfyre.ffworldtypes.world.util;

import com.flashfyre.ffworldtypes.FFWT;
import com.mojang.serialization.Codec;

import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FFWTBlockStateProviderTypes {
	
	public static final DeferredRegister<BlockStateProviderType<?>> BLOCK_STATE_PROVIDER_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES, FFWT.MOD_ID);
	
	public static final RegistryObject<BlockStateProviderType<NoiseMatchStateProvider>> NOISE_MATCH_STATE_PROVIDER = register("noise_match_state_provider", NoiseMatchStateProvider.CODEC);
	
	private static <P extends BlockStateProvider> RegistryObject<BlockStateProviderType<P>> register(String id, Codec<P> codec) {
		RegistryObject<BlockStateProviderType<P>> regObject = BLOCK_STATE_PROVIDER_TYPES.register(id, () -> new BlockStateProviderType<>(codec));
		return regObject;		
	}

}
