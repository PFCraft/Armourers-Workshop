package moe.plushie.armourers_workshop.common.init.blocks;

import moe.plushie.armourers_workshop.client.config.ConfigHandlerClient;
import moe.plushie.armourers_workshop.common.init.items.block.ModItemBlockWithMetadata;
import moe.plushie.armourers_workshop.common.lib.EnumGuiId;
import moe.plushie.armourers_workshop.common.lib.LibBlockNames;
import moe.plushie.armourers_workshop.common.lib.LibModInfo;
import moe.plushie.armourers_workshop.common.tileentities.TileEntitySkinLibrary;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class BlockSkinLibrary extends AbstractModBlockContainer {

    public static final PropertyDirection STATE_FACING = BlockHorizontal.FACING;

    public BlockSkinLibrary() {
        super(LibBlockNames.SKIN_LIBRARY);
        this.setDefaultState(this.blockState.getBaseState().withProperty(STATE_FACING, EnumFacing.NORTH));
        setSortPriority(198);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { STATE_FACING });
    }

    public IBlockState getStateFromMeta(int meta) {
        boolean northSouthBit = getBitBool(meta, 1);
        boolean posNegBit = getBitBool(meta, 2);
        EnumFacing facing = EnumFacing.EAST;
        if (northSouthBit) {
            if (posNegBit) {
                facing = EnumFacing.SOUTH;
            } else {
                facing = EnumFacing.NORTH;
            }
        } else {
            if (posNegBit) {
                facing = EnumFacing.EAST;
            } else {
                facing = EnumFacing.WEST;
            }
        }
        return this.getDefaultState().withProperty(STATE_FACING, facing);
    }

    public int getMetaFromState(IBlockState state) {
        EnumFacing facing = state.getValue(STATE_FACING);
        int meta = setBit(0, 0, true);
        if (facing == EnumFacing.NORTH | facing == EnumFacing.SOUTH) {
            meta = setBit(meta, 1, true);
        }
        if (facing == EnumFacing.EAST | facing == EnumFacing.SOUTH) {
            meta = setBit(meta, 2, true);
        }
        return meta;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumFacing enumfacing = placer.getHorizontalFacing().getOpposite();
        return getDefaultState().withProperty(STATE_FACING, enumfacing);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        if (!ConfigHandlerClient.useClassicBlockModels) {
            return BlockRenderLayer.CUTOUT_MIPPED;
        } else {
            return BlockRenderLayer.SOLID;
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public void registerItemBlock(IForgeRegistry<Item> registry) {
        registry.register(new ModItemBlockWithMetadata(this).setRegistryName(getRegistryName()).setHasSubtypes(true));
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(new ResourceLocation(LibModInfo.ID, getUnlocalizedName() + "-classic"), "creative"));
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().withSuffix("-classic").ignore(STATE_FACING).build());
    }

    /*
     *
     * @Override public int damageDropped(int meta) { return meta; }
     *
     * @Override public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
     * BlockUtils.dropInventoryBlocks(world, x, y,z);
     * super.breakBlock(world, x, y, z, block, meta);
     * }
     *
     */

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        openGui(playerIn, EnumGuiId.ARMOUR_LIBRARY, worldIn, pos, state, facing);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileEntitySkinLibrary();
    }
}
