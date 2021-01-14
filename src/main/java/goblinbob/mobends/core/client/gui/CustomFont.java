package goblinbob.mobends.core.client.gui;

import java.util.HashMap;
import moe.plushie.armourers_workshop.common.lib.LibModInfo;
import net.minecraft.util.ResourceLocation;

public class CustomFont
{

    public static final CustomFont BOLD = new CustomFont(
            "bold", 256, 32,
            new HashMap<Character, Symbol>()
            {{
                put('a', new Symbol(0, 0, 9, 11));
                put('b', new Symbol(10, 0, 9, 11));
                put('c', new Symbol(20, 0, 9, 11));
                put('d', new Symbol(30, 0, 8, 11));
                put('e', new Symbol(39, 0, 9, 11));
                put('f', new Symbol(49, 0, 9, 11));
                put('g', new Symbol(59, 0, 9, 11));
                put('h', new Symbol(69, 0, 9, 11));
                put('i', new Symbol(79, 0, 3, 11));
                put('j', new Symbol(83, 0, 8, 11));
                put('k', new Symbol(92, 0, 10, 11));
                put('l', new Symbol(103, 0, 9, 11));
                put('m', new Symbol(113, 0, 10, 11));
                put('n', new Symbol(124, 0, 9, 11));
                put('o', new Symbol(134, 0, 9, 11));
                put('p', new Symbol(144, 0, 9, 11));
                put('q', new Symbol(154, 0, 10, 11));
                put('r', new Symbol(165, 0, 9, 11));
                put('s', new Symbol(175, 0, 9, 11));
                put('t', new Symbol(185, 0, 9, 11));
                put('u', new Symbol(195, 0, 9, 11));
                put('v', new Symbol(205, 0, 10, 11));
                put('w', new Symbol(216, 0, 10, 11));
                put('x', new Symbol(227, 0, 11, 11));
                put('y', new Symbol(239, 0, 11, 11));
                put('z', new Symbol(0, 12, 9, 11));
                put('-', new Symbol(10, 12, 9, 11));
            }});

    public final int atlasWidth;
    public final int atlasHeight;
    protected final ResourceLocation resourceLocation;
    protected final HashMap<Character, Symbol> symbolMap;

    public CustomFont(String textureName, int atlasWidth, int atlasHeight, HashMap<Character, Symbol> symbolMap)
    {
        this.atlasWidth = atlasWidth;
        this.atlasHeight = atlasHeight;
        this.resourceLocation = new ResourceLocation(LibModInfo.ID,
                "textures/gui/fonts/" + textureName + ".png");
        this.symbolMap = symbolMap;
    }

    public Symbol getSymbol(char charAt)
    {
        if (this.symbolMap.containsKey(charAt))
            return this.symbolMap.get(charAt);
        return null;
    }

    public static class Symbol
    {

        public int u;
        public int v;
        public int width;
        public int height;
        public int offsetX;
        public int offsetY;

        public Symbol(int u, int v, int width, int height, int offsetX, int offsetY)
        {
            this.u = u;
            this.v = v;
            this.width = width;
            this.height = height;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        public Symbol(int u, int v, int width, int height)
        {
            this(u, v, width, height, 0, 0);
        }

    }

}
