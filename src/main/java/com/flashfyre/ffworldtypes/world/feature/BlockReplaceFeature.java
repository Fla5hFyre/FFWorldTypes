package com.flashfyre.ffworldtypes.world.feature;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.material.Material;

public class BlockReplaceFeature extends Feature<SimpleBlockConfiguration> {

	public BlockReplaceFeature(Codec<SimpleBlockConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {
		SimpleBlockConfiguration config = context.config();
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		BlockState stateToPlace = config.toPlace().getState(context.random(), pos);
		if (stateToPlace.getMaterial() == Material.AIR) return false;
		if (stateToPlace.canSurvive(level, pos)) {
			if (stateToPlace.getBlock() instanceof DoublePlantBlock) {
				if (!level.isEmptyBlock(pos.above())) {
					return false;
				}
				DoublePlantBlock.placeAt(level, stateToPlace, pos, 2);
			} else {
				level.setBlock(pos, stateToPlace, 2);
				if(!stateToPlace.getFluidState().isEmpty()) { // if block is fluid, schedule tick
					level.scheduleTick(pos, stateToPlace.getFluidState().getType(), 0);
				} else { // if block isn't fluid, place multiple times down
					for(int i = 0; i < 4; ++i) {
						if(level.getBlockState(pos.offset(0,-i,0)).isCollisionShapeFullBlock(level, pos.offset(0, -i, 0))) { // places blocks down
							level.setBlock(pos.offset(0, -i, 0), stateToPlace, 2);
						}						
					}
				}
				
				for(int y = pos.getY(); y < level.getMaxBuildHeight(); y++) { // places air above
					pos = pos.offset(0, 1, 0);
					Material material = level.getBlockState(pos).getMaterial();
					if(material == Material.AIR || material == Material.WATER) break;
					level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
				}				
			}
			
			return true;
		} else {
			return false;
		}
	}

}
