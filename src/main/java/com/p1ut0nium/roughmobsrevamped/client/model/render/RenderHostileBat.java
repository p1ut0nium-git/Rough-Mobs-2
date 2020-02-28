package com.p1ut0nium.roughmobsrevamped.client.model.render;

import com.p1ut0nium.roughmobsrevamped.client.model.ModelHostileBat;
import com.p1ut0nium.roughmobsrevamped.entities.EntityHostileBat;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHostileBat extends RenderLiving<EntityHostileBat>
{
    private static final ResourceLocation BAT_TEXTURES = new ResourceLocation("textures/entity/bat.png");

    public RenderHostileBat(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelHostileBat(), 0.25F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityHostileBat entity)
    {
        return BAT_TEXTURES;
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    protected void preRenderCallback(EntityHostileBat entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(0.35F, 0.35F, 0.35F);
    }

    protected void applyRotations(EntityHostileBat entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
    {
        if (entityLiving.getIsBatHanging())
        {
            GlStateManager.translate(0.0F, -0.1F, 0.0F);
        }
        else
        {
            GlStateManager.translate(0.0F, MathHelper.cos(p_77043_2_ * 0.3F) * 0.1F, 0.0F);
        }

        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }
}