package abused_master.abusedlib.tiles;

//import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
//import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.client.util.ClientPlayerTickable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
//import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public abstract class BlockEntityBase extends BlockEntity implements ClientPlayerTickable {

    public BlockEntityBase(BlockEntityType<?> blockEntityType_1, BlockPos pos, BlockState state) {
        super(blockEntityType_1, pos, state);
    }

    @Override
    public void tick() {
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public void updateEntity() {
        this.markDirty();
        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }
}