package com.flashfyre.ffworldtypes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.flashfyre.ffworldtypes.world.types.SnowlandsWorldType;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(FFWT.MOD_ID)
public class FFWT
{
	public static final String MOD_ID = "ffworldtypes";
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();
    
    public static SnowlandsWorldType snowlandsWorldType = new SnowlandsWorldType();

    public FFWT() {
        MinecraftForge.EVENT_BUS.register(this);
        snowlandsWorldType.setRegistryName(id("snowlands"));
        ForgeRegistries.WORLD_TYPES.register(snowlandsWorldType);
    }
    
    public static ResourceLocation id(String id) {
    	return new ResourceLocation(MOD_ID, id);
    }
}
