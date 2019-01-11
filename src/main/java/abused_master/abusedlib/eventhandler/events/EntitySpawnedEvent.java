package abused_master.abusedlib.eventhandler.events;

import abused_master.abusedlib.eventhandler.CancelableEvent;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntitySpawnedEvent extends CancelableEvent {

    private World world;
    private Entity entity;

    public EntitySpawnedEvent(World world, Entity entity) {
        this.world = world;
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public World getWorld() {
        return world;
    }
}
