package abused_master.abusedlib.utils;

import net.minecraft.block.entity.BlockEntity;
//import net.minecraft.container.Container;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public class InventoryHelper {

    public static boolean insertItemIfPossible(Inventory inventory, ItemStack stack, boolean simulate) {
        if(inventory == null) {
            return false;
        }

        for (int i = 0; i < inventory.size(); i++) {
            if(!inventory.getStack(i).isEmpty()) {
                if(canItemStacksStack(inventory.getStack(i), stack) && inventory.getStack(i).getCount() < 64) {
                    if(!simulate)
                        stack.increment(inventory.getStack(i).getCount());
                        inventory.setStack(i, stack);

                    return true;
                }
            }else {
                if(!simulate)
                    inventory.setStack(i, stack);

                return true;
            }
        }

        return false;
    }

    public static boolean canItemStacksStack(ItemStack a, ItemStack b) {
        if (a.isEmpty() || !a.isItemEqual(b) || a.hasNbt() != b.hasNbt())
            return false;

        return (!a.hasNbt() || a.getNbt().equals(b.hasNbt()));
    }

    public static Inventory getNearbyInventory(World world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos offsetPosition = new BlockPos(pos).offset(direction);
            BlockEntity entity = world.getBlockEntity(offsetPosition);
            if (entity != null && entity instanceof Inventory) {
                Inventory inventory = (Inventory) entity;
                return inventory;
            }
        }

        return null;
    }

    public static SidedInventory getNearbySidedInventory(World world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos offsetPosition = new BlockPos(pos).offset(direction);
            BlockEntity entity = world.getBlockEntity(offsetPosition);
            if (entity != null && entity instanceof SidedInventory) {
                SidedInventory inventory = (SidedInventory) entity;
                return inventory;
            }
        }

        return null;
    }

    //Credits to Diesieben07 for this method
    public static ItemStack handleShiftClick(ScreenHandler screenHandler, PlayerEntity player, int slotIndex) {
        @SuppressWarnings("unchecked")
        List<Slot> slots = screenHandler.slots;
        Slot sourceSlot = slots.get(slotIndex);
        ItemStack inputStack = sourceSlot.getStack();
        if (inputStack == ItemStack.EMPTY) {
            return ItemStack.EMPTY;
        }

        boolean sourceIsPlayer = sourceSlot.inventory == player.getInventory();

        ItemStack copy = inputStack.copy();

        if (sourceIsPlayer) {
            if (!mergeStack(player.getInventory(), false, sourceSlot, slots, false)) {
                return ItemStack.EMPTY;
            } else {
                return copy;
            }
        } else {
            boolean isMachineOutput = !sourceSlot.canTakeItems(player);
            if (!mergeStack(player.getInventory(), true, sourceSlot, slots, !isMachineOutput)) {
                return ItemStack.EMPTY;
            } else {
                return copy;
            }
        }
    }

    private static boolean mergeStack(PlayerInventory playerInv, boolean mergeIntoPlayer, Slot sourceSlot, List<Slot> slots, boolean reverse) {
        ItemStack sourceStack = sourceSlot.getStack();

        int originalSize = sourceStack.getCount();

        int len = slots.size();
        int idx;

        if (sourceStack.isStackable()) {
            idx = reverse ? len - 1 : 0;

            while (sourceStack.getCount() > 0 && (reverse ? idx >= 0 : idx < len)) {
                Slot targetSlot = slots.get(idx);
                if ((targetSlot.inventory == playerInv) == mergeIntoPlayer) {
                    ItemStack target = targetSlot.getStack();
                    if (ItemStack.areItemsEqual(sourceStack, target)) {
                        int targetMax = Math.min(targetSlot.getMaxItemCount(), target.getMaxCount());
                        int toTransfer = Math.min(sourceStack.getCount(), targetMax - target.getCount());
                        if (toTransfer > 0) {
                            target.increment(toTransfer);
                            sourceSlot.takeStack(toTransfer);
                            targetSlot.markDirty();
                        }
                    }
                }

                if (reverse) {
                    idx--;
                } else {
                    idx++;
                }
            }
            if (sourceStack.getCount() == 0) {
                sourceSlot.setStack(ItemStack.EMPTY);
                return true;
            }
        }

        idx = reverse ? len - 1 : 0;
        while (reverse ? idx >= 0 : idx < len) {
            Slot targetSlot = slots.get(idx);
            if ((targetSlot.inventory == playerInv) == mergeIntoPlayer
                    && !targetSlot.hasStack() && targetSlot.canInsert(sourceStack)) {
                targetSlot.setStack(sourceStack);
                sourceSlot.setStack(ItemStack.EMPTY);
                return true;
            }

            if (reverse) {
                idx--;
            } else {
                idx++;
            }
        }

        if (sourceStack.getCount() != originalSize) {
            sourceSlot.markDirty();
            return true;
        }
        return false;
    }

}
