package com.flashfyre.ffworldtypes.client;

import com.flashfyre.ffworldtypes.world.FFWTBiomes;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;

public class FogHandler {
	
	public static void renderFog(RenderFogEvent event) {
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
        LocalPlayer player = minecraft.player;

        if (level == null || player == null)
            return;
        
        boolean inMountainSprings = level.getBiome(player.blockPosition()).getRegistryName().equals(FFWTBiomes.MOUNTAIN_SPRINGS.location());
        if(inMountainSprings) {
        	RenderSystem.setShaderFogStart(-16.0F);
        	RenderSystem.setShaderFogEnd(Math.min(100F, event.getFarPlaneDistance()*0.5F));
        }
	}
}
