package abused_master.abusedlib.mixins;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockEntity.class)
public interface MixinBlockEntity {
    @Invoker("readNbt")
    public void invokeReadNbt(NbtCompound nbt);

    @Invoker("writeNbt")
    public void invoteWriteNbt(NbtCompound nbt);
}
