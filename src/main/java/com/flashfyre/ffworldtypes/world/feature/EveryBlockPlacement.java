package com.flashfyre.ffworldtypes.world.feature;

import java.util.Random;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class EveryBlockPlacement extends PlacementModifier {
	
	private static final EveryBlockPlacement INSTANCE = new EveryBlockPlacement();
	public static Codec<EveryBlockPlacement> CODEC = Codec.unit(() -> {
		return INSTANCE;
	});	   
	   
	private EveryBlockPlacement() {
	}
	
	@Override
	public PlacementModifierType<?> type() {
		return FFWTFeatures.Placements.EVERY_BLOCK;
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext context, Random p_191846_, BlockPos pos) {
		return BlockPos.betweenClosedStream(pos, pos.offset(15, 0, 15));
	}
	
	public static EveryBlockPlacement of() {
	      return INSTANCE;
	}
}
