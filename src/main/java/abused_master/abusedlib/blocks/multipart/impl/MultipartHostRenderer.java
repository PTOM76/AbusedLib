package abused_master.abusedlib.blocks.multipart.impl;

import abused_master.abusedlib.tiles.BlockEntityMultipart;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;

public class MultipartHostRenderer<T extends BlockEntity> implements BlockEntityRenderer<BlockEntityMultipart> {

    @Override
    public void render(BlockEntityMultipart tile, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        if(!tile.multiparts.isEmpty()) {
            for (Direction direction : tile.multiparts.keySet()) {
                BlockEntity blockEntity = tile.multiparts.get(direction).getMultipartEntity();

                if(blockEntity != null) {
                    BlockEntityRenderer renderer = MinecraftClient.getInstance().getBlockEntityRenderDispatcher().get(blockEntity);

                    if(renderer instanceof MultipartRenderer) {
                        ((MultipartRenderer) renderer).renderMultipart(blockEntity, direction, tickDelta, matrices, vertexConsumers, light, overlay);
                    }
                }
            }
        }
    }
}
