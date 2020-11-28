package moe.plushie.armourers_workshop.client.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticlePaintSplash extends Particle {

    private final EnumFacing facing;
    
    public ParticlePaintSplash(World worldIn, BlockPos pos, byte r, byte g, byte b, EnumFacing facing) {
        super(worldIn, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F);
        
        particleTextureIndexX = (int) (rand.nextFloat() * 4F);
        
        posX += 0.5F * facing.getFrontOffsetX();
        posY += 0.5F * facing.getFrontOffsetY();
        posZ += 0.5F * facing.getFrontOffsetZ();
        
        posX += (rand.nextFloat() - 0.5F) * facing.getFrontOffsetZ() + (rand.nextFloat() - 0.5F) * facing.getFrontOffsetY();
        posY += (rand.nextFloat() - 0.5F) * facing.getFrontOffsetX() + (rand.nextFloat() - 0.5F) * facing.getFrontOffsetZ();
        posZ += (rand.nextFloat() - 0.5F) * facing.getFrontOffsetX() + (rand.nextFloat() - 0.5F) * facing.getFrontOffsetY();
        
        setPosition(posX, posY, posZ);
        
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        
        motionX = 0.06 * facing.getFrontOffsetX();
        motionY = 0.06 * facing.getFrontOffsetY();
        motionZ = 0.06 * facing.getFrontOffsetZ();
        
        motionX += ((rand.nextFloat() - 0.5F) * 0.04);
        motionY += ((rand.nextFloat() - 0.5F) * 0.04);
        motionZ += ((rand.nextFloat() - 0.5F) * 0.04);
        
        particleScale = 1;
        
        particleGravity = 0.05F;
        
        this.particleRed = (float)r / 255F;
        this.particleGreen = (float)g / 255F;
        this.particleBlue = (float)b / 255F;
        
        this.facing = facing;
        this.particleMaxAge = 200;
        this.canCollide = true;
    }
    
    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        int fadeTime = 50;
        int lifeLeft = particleMaxAge - particleAge;
        if (lifeLeft <= fadeTime) {
            this.particleAlpha = ((float)lifeLeft / (float)fadeTime);
        }
        
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }
}
