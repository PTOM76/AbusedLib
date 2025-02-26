package abused_master.abusedlib.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluidStack {

    private Fluid fluid;
    private int amount;

    public FluidStack(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
    }

    public void writeNbt(NbtCompound nbt) {
        toTag(nbt);
    }

    public NbtCompound toTag(NbtCompound tag) {
        tag.putString("fluid", Registry.FLUID.getId(fluid).toString());
        tag.putInt("amount", this.amount);
        return tag;
    }

    public static FluidStack fluidFromTag(NbtCompound tag) {
        if(tag == null || !tag.contains("fluid")) {
            return null;
        }

        Fluid fluid = Registry.FLUID.get(new Identifier(tag.getString("fluid")));
        return fluid == null ? null : new FluidStack(fluid, tag.getInt("amount"));
    }

    public Fluid getFluid() {
        return fluid;
    }

    public void setFluid(Fluid fluid) {
        this.fluid = fluid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void addAmount(int add) {
        this.amount += add;
    }

    public void subtractAmount(int subtract) {
        this.amount -= subtract;
    }

    public boolean areFluidsEqual(FluidStack other) {
        return other != null && other.getFluid() == this.getFluid();
    }
}
