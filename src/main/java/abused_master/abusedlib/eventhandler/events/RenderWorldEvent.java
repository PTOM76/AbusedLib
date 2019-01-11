package abused_master.abusedlib.eventhandler.events;

import abused_master.abusedlib.eventhandler.Event;
import net.minecraft.client.render.WorldRenderer;

public class RenderWorldEvent implements Event {

    private WorldRenderer worldRenderer;
    private float partialTicks;

    public RenderWorldEvent(WorldRenderer worldRenderer, float partialTicks) {
        this.worldRenderer = worldRenderer;
        this.partialTicks = partialTicks;
    }

    public WorldRenderer getWorldRenderer() {
        return worldRenderer;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
