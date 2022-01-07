package com.flashfyre.ffworldtypes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.flashfyre.ffworldtypes.client.ClientEvents;
import com.flashfyre.ffworldtypes.client.FogHandler;
import com.flashfyre.ffworldtypes.world.FFWTBiomes;
import com.flashfyre.ffworldtypes.world.feature.FFWTFeatures;
import com.flashfyre.ffworldtypes.world.types.SnowlandsWorldType;
import com.flashfyre.ffworldtypes.world.util.FFWTBlockStateProviderTypes;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(FFWT.MOD_ID)
public class FFWT
{
	public static final String MOD_ID = "ffworldtypes";
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();
    
    public static SnowlandsWorldType snowlandsWorldType = new SnowlandsWorldType();

    public FFWT() {
    	IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    	IEventBus forgeBus = MinecraftForge.EVENT_BUS;
    	modBus.addListener(this::commonSetup);
    	if(FMLEnvironment.dist == Dist.CLIENT) {
    		ClientEvents.subscribeClientEvents(forgeBus);
    	}
    	forgeBus.register(this);
        snowlandsWorldType.setRegistryName(id("snowlands"));
        ForgeRegistries.WORLD_TYPES.register(snowlandsWorldType);
        FFWTBiomes.BIOMES.register(modBus);
        FFWTFeatures.FEATURES.register(modBus);
        FFWTBlockStateProviderTypes.BLOCK_STATE_PROVIDER_TYPES.register(modBus);
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
    	FFWTFeatures.ConfiguredFeatures.registerConfiguredFeatures();
    	FFWTFeatures.PlacedFeatures.registerPlacedFeatures();
    	FFWTFeatures.Placements.registerPlacements();
    	FFWTBiomes.addTypes();
    }
    
    public static ResourceLocation id(String id) {
    	return new ResourceLocation(MOD_ID, id);
    }
}
