/** Copyright (c) 2011-2015, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt */
package buildcraft.builders.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import buildcraft.builders.BuildCraftBuilders;
import buildcraft.builders.item.ItemConstructionMarker;
import buildcraft.builders.tile.TileBuilder;
import buildcraft.core.GuiIds;
import buildcraft.core.lib.block.BlockBuildCraft;
import buildcraft.core.lib.fluids.TankUtils;

public class BlockBuilder extends BlockBuildCraft {

    TextureAtlasSprite blockTextureTop;
    TextureAtlasSprite blockTextureSide;
    TextureAtlasSprite blockTextureFront;

    public BlockBuilder() {
        super(Material.iron, FACING_PROP, LED_DONE, LED_POWER);
        setHardness(5F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileBuilder();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityplayer, EnumFacing side, float hitX, float hitY,
            float hitZ) {
        if (super.onBlockActivated(world, pos, state, entityplayer, side, hitX, hitY, hitZ)) {
            return true;
        }

        if (entityplayer.isSneaking()) {
            return false;
        }

        TileEntity tile = world.getTileEntity(pos);
        TileBuilder builder = tile instanceof TileBuilder ? (TileBuilder) tile : null;

        Item equipped = entityplayer.getCurrentEquippedItem() != null ? entityplayer.getCurrentEquippedItem().getItem() : null;
        if (equipped instanceof ItemConstructionMarker) {
            if (ItemConstructionMarker.linkStarted(entityplayer.getCurrentEquippedItem())) {
                ItemConstructionMarker.link(entityplayer.getCurrentEquippedItem(), world, pos);
            }

            return true;
        } else if (builder != null && TankUtils.handleRightClick(builder, null, entityplayer, true, false)) {
            return true;
        } else {
            if (!world.isRemote) {
                entityplayer.openGui(BuildCraftBuilders.instance, GuiIds.BUILDER, world, pos.getX(), pos.getY(), pos.getZ());
            }

            return true;
        }
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos) {
        return 1;
    }
}
