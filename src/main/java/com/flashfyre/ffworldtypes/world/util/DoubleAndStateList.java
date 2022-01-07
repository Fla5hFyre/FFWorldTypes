package com.flashfyre.ffworldtypes.world.util;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.block.state.BlockState;

public record DoubleAndStateList(double value, List<BlockState> states) {
	public static Codec<DoubleAndStateList> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.DOUBLE.fieldOf("value").forGetter(DoubleAndStateList::value),
			Codec.list(BlockState.CODEC).fieldOf("states").forGetter(DoubleAndStateList::states)
			).apply(instance, DoubleAndStateList::new));
}
