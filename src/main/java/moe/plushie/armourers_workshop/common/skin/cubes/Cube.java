package moe.plushie.armourers_workshop.common.skin.cubes;

public class Cube implements ICube {
    
    protected final byte id;
    
    public Cube() {
        id = CubeRegistry.INSTANCE.getTotalCubes();
    }
    
    @Override
    public boolean isGlowing() {
        return false;
    }
    
    @Override
    public boolean needsPostRender() {
        return false;
    }
    
    @Override
    public byte getId() {
        return id;
    }
}
