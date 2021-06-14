package moe.plushie.armourers_workshop.common.init.items.block;

import java.util.List;
import moe.plushie.armourers_workshop.client.config.ConfigHandlerClient;
import moe.plushie.armourers_workshop.common.creativetab.ISortOrder;
import moe.plushie.armourers_workshop.common.lib.LibModInfo;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItemBlock extends ItemBlock implements ISortOrder {

    public ModItemBlock(Block block) {
        super(block);
    }

    @Override
    public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
        return super.getUnlocalizedNameInefficiently(par1ItemStack);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getModdedUnlocalizedName(super.getUnlocalizedName(stack), stack);
    }
    
    protected String getModdedUnlocalizedName(String unlocalizedName, ItemStack stack) {
        String name = unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
        if (hasSubtypes) {
            return "tile." + LibModInfo.ID.toLowerCase() + ":" + name + "." + stack.getItemDamage();
        } else {
            return "tile." + LibModInfo.ID.toLowerCase() + ":" + name;
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (block instanceof ISortOrder & ConfigHandlerClient.showSortOrderToolTip) {
            tooltip.add("sortPriority" + String.valueOf(getSortPriority()));
        }
    }
    
    @Override
    public int getSortPriority() {
        if (block instanceof ISortOrder) {
            return ((ISortOrder)block).getSortPriority();
        }
        return 100;
    }
}
