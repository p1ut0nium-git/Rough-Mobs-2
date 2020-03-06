package com.p1ut0nium.roughmobsrevamped.client.renderer.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.p1ut0nium.roughmobsrevamped.client.renderer.entity.model.HostileBatModel;
import com.p1ut0nium.roughmobsrevamped.entity.monster.HostileBatEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HostileBatRenderer extends MobRenderer<HostileBatEntity, HostileBatModel> {
   private static final ResourceLocation BAT_TEXTURES = new ResourceLocation("textures/entity/bat.png");

   public HostileBatRenderer(EntityRendererManager renderManagerIn) {
      super(renderManagerIn, new HostileBatModel(), 0.25F);
   }

   protected ResourceLocation getEntityTexture(HostileBatEntity entity) {
      return BAT_TEXTURES;
   }

   protected void preRenderCallback(HostileBatEntity entitylivingbaseIn, float partialTickTime) {
      GlStateManager.scalef(0.35F, 0.35F, 0.35F);
   }

   protected void applyRotations(HostileBatEntity entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
      if (entityLiving.getIsBatHanging()) {
         GlStateManager.translatef(0.0F, -0.1F, 0.0F);
      } else {
         GlStateManager.translatef(0.0F, MathHelper.cos(ageInTicks * 0.3F) * 0.1F, 0.0F);
      }

      super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
   }
}