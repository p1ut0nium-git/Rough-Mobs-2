package com.p1ut0nium.roughmobsrevamped.client.particle;

import com.p1ut0nium.roughmobsrevamped.misc.BossHelper.BossApplier;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleSmokeyAura extends Particle {

    float smokeParticleScale;
	float colorRed = Float.parseFloat(BossApplier.bossFogColor[0]);
	float colorGreen = Float.parseFloat(BossApplier.bossFogColor[1]);
	float colorBlue = Float.parseFloat(BossApplier.bossFogColor[2]);

    private ParticleSmokeyAura(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double speedX, double speedY, double speedZ) {
    	this(worldIn, xCoordIn, yCoordIn, zCoordIn, speedX, speedY, speedZ, 1.0F);
    }

    protected ParticleSmokeyAura(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double speedX, double speedY, double speedZ, float scale)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.motionX += speedX;
        this.motionY += speedY;
        this.motionZ += speedZ;
        float f = (float)(Math.random() * 0.30000001192092896D);
        this.particleRed = f * colorRed;
        this.particleGreen = f * colorGreen;
        this.particleBlue = f * colorBlue;
        this.particleScale *= 0.75F;
        this.particleScale *= scale;
        this.smokeParticleScale = this.particleScale;
        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.particleMaxAge = (int)((float)this.particleMaxAge * scale);
    }

    /**
     * Renders the particle
     */
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge * 32.0F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        this.particleScale = this.smokeParticleScale * f;
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.motionY += 0.004D;
        this.move(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY)
        {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory {
    	
    	float colorRed = Float.parseFloat(BossApplier.bossFogColor[0]);
    	float colorGreen = Float.parseFloat(BossApplier.bossFogColor[1]);
    	float colorBlue = Float.parseFloat(BossApplier.bossFogColor[2]);
    	
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleSmokeyAura(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
