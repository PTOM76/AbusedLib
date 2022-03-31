package abused_master.abusedlib.fluid;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;

public interface IFluidContainer {

    boolean extractFluid(int amount);

    boolean fillFluid(FluidStack stack);

    int getFluidAmount();

    int getFluidCapacity();

    FluidStack getFluidStack();

    void setCapacity(int capacity);

    void setFluidStack(FluidStack stack);

    BlockEntity getBlockEntity();

    void setBlockEntity(BlockEntity blockEntity);

    FluidContainer readFromNBT(NbtCompound nbt);

    NbtCompound writeToNBT(NbtCompound nbt);
}
