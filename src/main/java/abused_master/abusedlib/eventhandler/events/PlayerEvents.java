package abused_master.abusedlib.eventhandler.events;

import abused_master.abusedlib.eventhandler.CancelableEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PlayerEvents extends CancelableEvent {

    private PlayerEntity player;
    private Hand hand;
    private BlockPos pos;
    private Direction direction;

    public PlayerEvents(PlayerEntity player, Hand hand) {
        this.player = player;
        this.hand = hand;
        this.pos = pos;
        this.direction = direction;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public Hand getHand() {
        return hand;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Direction getDirection() {
        return direction;
    }

    public static class PlayerInteractEntity extends PlayerEvents {

        private Entity target;

        public PlayerInteractEntity(PlayerEntity player, Hand hand, Entity target) {
            super(player, hand);
            this.target = target;
        }

        public Entity getTarget() {
            return target;
        }

        @Override
        public boolean isCanceled() {
            return true;
        }
    }

    /**
     * Is called on client side only, send a packed with the data to server to handle stuff
     */
    public static class PlayerLeftClick extends PlayerEvents {

        public PlayerLeftClick(PlayerEntity player, Hand hand) {
            super(player, hand);
        }
    }
}
