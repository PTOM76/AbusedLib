package abused_master.abusedlib.blocks;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockWithEntityBase extends BlockBase implements BlockEntityProvider {

    public BlockWithEntityBase(String name, Material material, float hardness, ItemGroup itemGroup) {
        super(name, material, hardness, itemGroup);
    }

    public BlockWithEntityBase(String name, ItemGroup itemGroup, Settings blockSettings) {
        super(name, itemGroup, blockSettings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState var1) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState blockState_1, World world_1, BlockPos blockPos_1, BlockState blockState_2, boolean boolean_1) {
        if (blockState_1.getBlock() != blockState_2.getBlock()) {
            super.onStateReplaced(blockState_1, world_1, blockPos_1, blockState_2, boolean_1);
            world_1.removeBlockEntity(blockPos_1);
        }
    }

    @Override
    public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos blockPos, int type, int data) {
        super.onSyncedBlockEvent(state, world, blockPos, type, data);
        BlockEntity blockEntity_1 = world.getBlockEntity(blockPos);
        return blockEntity_1 != null && blockEntity_1.onSyncedBlockEvent(type, data);
    }

    public abstract boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockHitResult hitResult);
}
