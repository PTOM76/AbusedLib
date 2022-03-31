package abused_master.abusedlib.blocks.multipart.impl;

import abused_master.abusedlib.blocks.multipart.IMultipart;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;

public abstract class MultipartRenderer<T extends BlockEntity & IMultipart> implements BlockEntityRenderer<T> {

    public abstract void renderMultipart(T tile, Direction direction, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay);
}
