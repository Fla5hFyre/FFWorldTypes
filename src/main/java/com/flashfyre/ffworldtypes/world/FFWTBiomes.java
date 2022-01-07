package com.flashfyre.ffworldtypes.world;

import com.flashfyre.ffworldtypes.FFWT;

import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FFWTBiomes {
	
	public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, FFWT.MOD_ID);
	
	public static final ResourceKey<Biome> SNOWY_GIANT_SPRUCE_TAIGA = createKeyAndRegister("snowy_old_growth_spruce_taiga");	
	public static final ResourceKey<Biome> SNOWY_GIANT_PINE_TAIGA = createKeyAndRegister("snowy_old_growth_pine_taiga");
	public static final ResourceKey<Biome> GLACIER = createKeyAndRegister("glacier");
	public static final ResourceKey<Biome> MOUNTAIN_SPRINGS = createKeyAndRegister("mountain_springs");
	public static final ResourceKey<Biome> GEOTHERMAL_VALLEY = createKeyAndRegister("geothermal_valley");
	public static final ResourceKey<Biome> COLD_RIVER = createKeyAndRegister("cold_river");
	public static final ResourceKey<Biome> ICY_CAVES = createKeyAndRegister("icy_caves");
	
	private static ResourceKey<Biome> createKeyAndRegister(String name) {
		ResourceKey<Biome> biomeKey = ResourceKey.create(Registry.BIOME_REGISTRY, FFWT.id(name));
		BIOMES.register(name, () -> OverworldBiomes.theVoid());
		return biomeKey;
	}

	/**
	 * Adds biome dictionary types to ffwt biomes. 
	 * Order types by:
	 * 1. Temperature - hot/cold
	 * 2. Humidity - wet/dry
	 * 3. Vegetation - sparse/dense
	 * 4. Nature - spooky/dead/lush/magical
	 * 5. Terrain type - sandy/snowy
	 * 6. Other - coniferous/rare/modified
	 * 7. Biome type - savanna/jungle/mesa/forest/plains/mountain/swamp/beach/wasteland
	 * 8. Terrain shape - hills/plateau/void
	 * 9. Dimension - overworld/nether/end
	 */
	public static void addTypes() {
		BiomeDictionary.addTypes(COLD_RIVER, Type.COLD, Type.RIVER, Type.OVERWORLD);
		BiomeDictionary.addTypes(SNOWY_GIANT_SPRUCE_TAIGA, Type.COLD, Type.SNOWY, Type.CONIFEROUS, Type.FOREST, Type.OVERWORLD);
		BiomeDictionary.addTypes(SNOWY_GIANT_PINE_TAIGA, Type.COLD, Type.SNOWY, Type.CONIFEROUS, Type.FOREST, Type.OVERWORLD);
		BiomeDictionary.addTypes(GLACIER, Type.COLD, Type.SNOWY, Type.OVERWORLD);
		BiomeDictionary.addTypes(MOUNTAIN_SPRINGS, Type.COLD, Type.WET,  Type.SPARSE, Type.CONIFEROUS, Type.FOREST, Type.MOUNTAIN, Type.OVERWORLD);
		BiomeDictionary.addTypes(GEOTHERMAL_VALLEY, Type.COLD, Type.WET, Type.SPARSE, Type.CONIFEROUS, Type.OVERWORLD);
		BiomeDictionary.addTypes(ICY_CAVES, Type.SNOWY, Type.OVERWORLD);
	}
}
