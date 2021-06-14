package moe.plushie.armourers_workshop.common.skin.cubes;

import java.util.ArrayList;
import moe.plushie.armourers_workshop.utils.ModLogger;


public final class CubeRegistry {
    
    public static CubeRegistry INSTANCE;
    
    private ArrayList<ICube> cubeList;
    
    public static void init() {
        INSTANCE = new CubeRegistry();
        INSTANCE.registerCubes();
    }
    
    public CubeRegistry() {
        cubeList = new ArrayList<ICube>();
    }
    
    public ICube getCubeFormId(byte id) {
        if (id >= 0 && id < cubeList.size()) {
            return cubeList.get(id);
        }
        return null;
    }
    
    public byte getTotalCubes() {
        return (byte) cubeList.size();
    }
    
    private void registerCube(ICube cube) {
        cubeList.add(cube);
        ModLogger.log("Registering equipment cube - id:" + cube.getId() + " name:" + cube.getClass().getSimpleName());
    }
    
    public void registerCubes() {
        registerCube(new Cube());
        registerCube(new CubeGlowing());
        registerCube(new CubeGlass());
        registerCube(new CubeGlassGlowing());
    }
}
