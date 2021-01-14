package moe.plushie.armourers_workshop.client.settings;

import org.lwjgl.input.Keyboard;

import moe.plushie.armourers_workshop.common.lib.LibKeyBindingNames;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

public class Keybindings {
    
    public static KeyBinding OPEN_WARDROBE = new KeyBinding(LibKeyBindingNames.WARDROBE, KeyConflictContext.IN_GAME, Keyboard.KEY_P, LibKeyBindingNames.CATEGORY);
}
