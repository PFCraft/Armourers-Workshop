
package moe.plushie.armourers_workshop.common.inventory;

import moe.plushie.armourers_workshop.api.common.capability.IPlayerWardrobeCap;
import moe.plushie.armourers_workshop.api.common.capability.IWardrobeCap;
import moe.plushie.armourers_workshop.api.common.skin.type.ISkinType;
import moe.plushie.armourers_workshop.common.capability.entityskin.EntitySkinCapability;
import moe.plushie.armourers_workshop.common.config.ConfigHandler;
import moe.plushie.armourers_workshop.common.init.items.ItemSkin;
import moe.plushie.armourers_workshop.common.inventory.slot.SlotSkin;
import moe.plushie.armourers_workshop.common.skin.type.SkinTypeRegistry;
import moe.plushie.armourers_workshop.utils.SkinNBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class ContainerSkinWardrobe extends ModContainer {

    private final EntitySkinCapability skinCapability;
    private final IWardrobeCap wardrobeCapability;
    private int slotsUnlocked;

    private int indexSkinsStart = 0;
    private int indexSkinsEnd = 0;

    private int indexDyeStart = 0;
    private int indexDyeEnd = 0;

    private int indexOutfitStart = 0;
    private int indexOutfitEnd = 0;

    private int indexMannequinHandsStart = 0;
    private int indexMannequinHandsEnd = 0;

    public ContainerSkinWardrobe(InventoryPlayer invPlayer, EntitySkinCapability skinCapability, IWardrobeCap wardrobeCapability) {
        super(invPlayer);
        this.skinCapability = skinCapability;
        this.wardrobeCapability = wardrobeCapability;
        SkinInventoryContainer skinInv = skinCapability.getSkinInventoryContainer();
        boolean isPlayer = wardrobeCapability instanceof IPlayerWardrobeCap;
        boolean isCreative = invPlayer.player.capabilities.isCreativeMode;

        if (isPlayer) {
            if (ConfigHandler.wardrobeTabSkins | isCreative) {
                IPlayerWardrobeCap playerWardrobe = (IPlayerWardrobeCap) wardrobeCapability;

                WardrobeInventory headInv = skinInv.getSkinTypeInv(SkinTypeRegistry.skinHead);
                WardrobeInventory chestInv = skinInv.getSkinTypeInv(SkinTypeRegistry.skinChest);
                WardrobeInventory legsInv = skinInv.getSkinTypeInv(SkinTypeRegistry.skinLegs);
                WardrobeInventory feetInv = skinInv.getSkinTypeInv(SkinTypeRegistry.skinFeet);
                WardrobeInventory wingInv = skinInv.getSkinTypeInv(SkinTypeRegistry.skinWings);

                WardrobeInventory swordInv = skinInv.getSkinTypeInv(SkinTypeRegistry.skinSword);
                WardrobeInventory shieldInv = skinInv.getSkinTypeInv(SkinTypeRegistry.skinShield);
                WardrobeInventory bowInv = skinInv.getSkinTypeInv(SkinTypeRegistry.skinBow);

                WardrobeInventory pickaxeInv = skinInv.getSkinTypeInv(SkinTypeRegistry.skinPickaxe);
                WardrobeInventory axeInv = skinInv.getSkinTypeInv(SkinTypeRegistry.skinAxe);
                WardrobeInventory shovelInv = skinInv.getSkinTypeInv(SkinTypeRegistry.skinShovel);
                WardrobeInventory hoeInv = skinInv.getSkinTypeInv(SkinTypeRegistry.skinHoe);

                for (int i = 0; i < EntitySkinCapability.MAX_SLOTS_PER_SKIN_TYPE; i++) {
                    if (i < playerWardrobe.getUnlockedSlotsForSkinType(SkinTypeRegistry.skinHead)) {
                        addSlotToContainer(new SlotSkin(headInv, i, 83 + i * 19, 27, SkinTypeRegistry.skinHead));
                        indexSkinsEnd += 1;
                    }
                    if (i < playerWardrobe.getUnlockedSlotsForSkinType(SkinTypeRegistry.skinChest)) {
                        addSlotToContainer(new SlotSkin(chestInv, i, 83 + i * 19, 46, SkinTypeRegistry.skinChest));
                        indexSkinsEnd += 1;
                    }
                    if (i < playerWardrobe.getUnlockedSlotsForSkinType(SkinTypeRegistry.skinLegs)) {
                        addSlotToContainer(new SlotSkin(legsInv, i, 83 + i * 19, 65, SkinTypeRegistry.skinLegs));
                        indexSkinsEnd += 1;
                    }
                    if (i < playerWardrobe.getUnlockedSlotsForSkinType(SkinTypeRegistry.skinFeet)) {
                        addSlotToContainer(new SlotSkin(feetInv, i, 83 + i * 19, 84, SkinTypeRegistry.skinFeet));
                        indexSkinsEnd += 1;
                    }
                    if (i < playerWardrobe.getUnlockedSlotsForSkinType(SkinTypeRegistry.skinWings)) {
                        addSlotToContainer(new SlotSkin(wingInv, i, 83 + i * 19, 103, SkinTypeRegistry.skinWings));
                        indexSkinsEnd += 1;
                    }
                }

                addSlotToContainer(new SlotSkin(swordInv, 0, 83, 122, SkinTypeRegistry.skinSword));
                addSlotToContainer(new SlotSkin(shieldInv, 0, 102, 122, SkinTypeRegistry.skinShield));
                addSlotToContainer(new SlotSkin(bowInv, 0, 121, 122, SkinTypeRegistry.skinBow));

                addSlotToContainer(new SlotSkin(pickaxeInv, 0, 159, 122, SkinTypeRegistry.skinPickaxe));
                addSlotToContainer(new SlotSkin(axeInv, 0, 178, 122, SkinTypeRegistry.skinAxe));
                addSlotToContainer(new SlotSkin(shovelInv, 0, 197, 122, SkinTypeRegistry.skinShovel));
                addSlotToContainer(new SlotSkin(hoeInv, 0, 216, 122, SkinTypeRegistry.skinHoe));
                indexSkinsEnd += 7;
            }
        } else {
            ISkinType[] skinTypes = skinCapability.getValidSkinTypes();
            int yindex = 0;
            for (int i = 0; i < skinTypes.length; i++) {
                if (skinTypes[i] == SkinTypeRegistry.skinOutfit) {
                    continue;
                }

                for (int j = 0; j < skinCapability.getSlotCountForSkinType(skinTypes[i]); j++) {
                    if (skinTypes[i].getVanillaArmourSlotId() != -1 | skinTypes[i] == SkinTypeRegistry.skinWings) {
                        addSlotToContainer(new SlotSkin(skinCapability.getSkinInventoryContainer().getSkinTypeInv(skinTypes[i]), j, 83 + j * 19, 27 + (yindex) * 19, skinTypes[i]));
                    } else {
                        addSlotToContainer(new SlotSkin(skinCapability.getSkinInventoryContainer().getSkinTypeInv(skinTypes[i]), j, 83 + (yindex - 5) * 19, 122 + j * 19, skinTypes[i]));
                    }
                    indexSkinsEnd++;
                }
                yindex++;
            }
        }

        indexDyeStart = indexSkinsEnd;
        indexDyeEnd = indexSkinsEnd;
        if (!isPlayer | (isPlayer & (ConfigHandler.wardrobeTabDyes | isCreative))) {
            for (int i = 0; i < 8; i++) {
                indexDyeEnd += 1;
            }
        }

        indexOutfitStart = indexDyeEnd;
        indexOutfitEnd = indexDyeEnd;
        if (!isPlayer | (isPlayer & (ConfigHandler.wardrobeTabOutfits | isCreative))) {
            if (skinCapability.getSlotCountForSkinType(SkinTypeRegistry.skinOutfit) > 0) {
                WardrobeInventory invOutfit = skinInv.getSkinTypeInv(SkinTypeRegistry.skinOutfit);
                int outfitSlots = invOutfit.getSizeInventory();
                if (isPlayer) {
                    IPlayerWardrobeCap playerWardrobe = (IPlayerWardrobeCap) wardrobeCapability;
                    outfitSlots = playerWardrobe.getUnlockedSlotsForSkinType(SkinTypeRegistry.skinOutfit);
                }
                for (int i = 0; i < outfitSlots; i++) {
                    int y = 19 * (MathHelper.floor(i / 10F));
                    int x = 19 * i - (y * 8);
                    addSlotToContainer(new SlotSkin(invOutfit, i, 83 + x, 27 + y, SkinTypeRegistry.skinOutfit));
                    indexOutfitEnd += 1;
                }
            }
        }

        indexMannequinHandsStart = indexOutfitEnd;
        indexMannequinHandsEnd = indexMannequinHandsStart;

        addPlayerSlots(59, 168);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return !player.isDead;
    }

    public int getIndexSkinsStart() {
        return indexSkinsStart;
    }

    public int getIndexSkinsEnd() {
        return indexSkinsEnd;
    }

    public int getIndexDyeStart() {
        return indexDyeStart;
    }

    public int getIndexDyeEnd() {
        return indexDyeEnd;
    }

    public int getIndexOutfitStart() {
        return indexOutfitStart;
    }

    public int getIndexOutfitEnd() {
        return indexOutfitEnd;
    }

    public int getIndexMannequinHandsStart() {
        return indexMannequinHandsStart;
    }

    public int getIndexMannequinHandsEnd() {
        return indexMannequinHandsEnd;
    }

    @Override
    protected ItemStack transferStackFromPlayer(EntityPlayer playerIn, int index) {
        Slot slot = getSlot(index);
        if (slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            ItemStack result = stack.copy();

            boolean slotted = false;

            // Putting skin in inv
            if (stack.getItem() instanceof ItemSkin & SkinNBTHelper.stackHasSkinData(stack)) {
                for (int i = indexSkinsStart; i < indexSkinsEnd; i++) {
                    Slot targetSlot = getSlot(i);
                    if (targetSlot.isItemValid(stack)) {
                        if (this.mergeItemStack(stack, i, i + 1, false)) {
                            slotted = true;
                            break;
                        }
                    }
                }
            }

            // TODO Add check for valid outfit.
            if (stack.getItem() instanceof ItemSkin & SkinNBTHelper.stackHasSkinData(stack)) {
                for (int i = indexOutfitStart; i < indexOutfitEnd; i++) {
                    Slot targetSlot = getSlot(i);
                    if (targetSlot.isItemValid(stack)) {
                        if (this.mergeItemStack(stack, i, i + 1, false)) {
                            slotted = true;
                            break;
                        }
                    }
                }
            }

            if (stack.getItem() instanceof ItemSkin & SkinNBTHelper.stackHasSkinData(stack)) {
                for (int i = indexMannequinHandsStart; i < indexMannequinHandsEnd; i++) {
                    Slot targetSlot = getSlot(i);
                    if (targetSlot.isItemValid(stack)) {
                        if (this.mergeItemStack(stack, i, i + 1, false)) {
                            slotted = true;
                            break;
                        }
                    }
                }
            }

            if (!slotted) {
                return ItemStack.EMPTY;
            }

            if (stack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            slot.onTake(playerIn, stack);

            return result;

        }
        return ItemStack.EMPTY;
    }
}
