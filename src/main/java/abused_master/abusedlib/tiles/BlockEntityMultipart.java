package abused_master.abusedlib.tiles;

import abused_master.abusedlib.AbusedLib;
import abused_master.abusedlib.blocks.multipart.IMultipart;
import abused_master.abusedlib.blocks.multipart.IMultipartHost;
import abused_master.abusedlib.utils.MultipartHelper;
import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Random;

/**
 * Template multipart block entity class, extend this or create your own.
 */
public class BlockEntityMultipart extends BlockEntityBase implements IMultipartHost {

    public EnumMap<Direction, IMultipart> multiparts = Maps.newEnumMap(Direction.class);

    public BlockEntityMultipart(BlockPos pos, BlockState state) {
        super(AbusedLib.MULTIPART, pos, state);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        if(tag.contains("multipartList")) {
            this.multiparts.clear();
            NbtList NbtList = tag.getList("multipartList", NbtType.COMPOUND);

            for (int i = 0; i < NbtList.size(); i++) {
                NbtCompound dataTag = NbtList.getCompound(i);
                Direction direction = Direction.byId(dataTag.getInt("direction"));
                IMultipart multipart = MultipartHelper.deserialize(dataTag, this.getPos(), this.getCachedState());
                this.multiparts.put(direction, multipart);
            }
        }
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        if(!multiparts.isEmpty()) {
            NbtList NbtList = new NbtList();

            for (Direction direction : multiparts.keySet()) {
                NbtCompound dataTag = new NbtCompound();
                dataTag.putInt("direction", direction.ordinal());
                MultipartHelper.serialize(multiparts.get(direction), dataTag);
                NbtList.add(dataTag);
            }

            tag.put("multipartList", NbtList);
        }
    }

    @Override
    public void tick() {
        super.tick();
        for (IMultipart multipart : multiparts.values()) {
            BlockEntity tile = multipart.getMultipartEntity();
            if(tile.getCachedState().hasRandomTicks()) {
                if (!tile.getWorld().isClient)
                    tile.getCachedState().randomTick((ServerWorld) tile.getWorld(), tile.getPos(), new Random());
            }
            if (world.isClient()) {
                if (tile instanceof net.minecraft.client.util.ClientPlayerTickable) {
                    ((net.minecraft.client.util.ClientPlayerTickable) tile).tick();
                }

            }
        }
    }

    @Override
    public boolean tryAddMultipart(Direction direction, IMultipart multipart) {
        if(!multiparts.containsKey(direction)) {
            this.multiparts.put(direction, multipart);
            this.updateEntity();
            return true;
        }

        return false;
    }

    @Override
    public boolean tryRemoveMultipart(Direction direction, IMultipart multipart) {
        if(multiparts.containsKey(direction)) {
            this.multiparts.remove(direction);
            this.updateEntity();
            return true;
        }

        return false;
    }

    @Override
    public boolean hasMultipart(Direction direction) {
        return multiparts.containsKey(direction);
    }

    @Nullable
    @Override
    public IMultipart getMultipart(Direction direction) {
        return multiparts.get(direction);
    }

    @Override
    public EnumMap<Direction, IMultipart> getMultiparts() {
        return multiparts;
    }
}
