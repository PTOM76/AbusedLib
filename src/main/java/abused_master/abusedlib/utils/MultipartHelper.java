package abused_master.abusedlib.utils;

import abused_master.abusedlib.blocks.multipart.IMultipart;
import abused_master.abusedlib.mixins.MixinBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class MultipartHelper {

    public static NbtCompound serialize(IMultipart multipart, NbtCompound tag) {

        ((MixinBlockEntity)multipart.getMultipartEntity()).invoteWriteNbt(tag);
        tag.putString("multipartIdentifier", Registry.BLOCK_ENTITY_TYPE.getId(multipart.getMultipartEntity().getType()).toString());

        return tag;
    }

    public static IMultipart deserialize(NbtCompound tag, BlockPos pos, BlockState state) {
        BlockEntity multipartEntity = Registry.BLOCK_ENTITY_TYPE.get(new Identifier(tag.getString("multipartIdentifier"))).instantiate(pos, state);

        if(multipartEntity != null) {
            ((MixinBlockEntity)multipartEntity).invokeReadNbt(tag);
        }

        return (IMultipart) multipartEntity;
    }
}
