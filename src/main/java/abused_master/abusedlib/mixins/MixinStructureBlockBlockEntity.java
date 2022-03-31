package abused_master.abusedlib.mixins;

import abused_master.abusedlib.blocks.multiblock.MultiBlockBuilder;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.text.LiteralText;
//import net.minecraft.text.Style;
//import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(StructureBlockBlockEntity.class)
public abstract class MixinStructureBlockBlockEntity {

    @Shadow
    private String author;

    @Shadow
    private Vec3i size;

    @Shadow
    private Identifier structureName;

    @Inject(method = "saveStructure(Z)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/structure/Structure;setAuthor(Ljava/lang/String;)V", shift = At.Shift.AFTER), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void saveStructure(boolean save, CallbackInfoReturnable<Boolean> cir, BlockPos pos, ServerWorld serverWorld, StructureManager structureManager, Structure structure) {
        if(save && !author.isEmpty() && !((StructureBlockBlockEntity) (Object) this).getWorld().isClient()) {
            Vec3i pos2 = size.add(pos).add(-1, -1, -1);
            BlockPos minCorner = new BlockPos(Math.min(pos.getX(), pos2.getX()), Math.min(pos.getY(), pos2.getY()), Math.min(pos.getZ(), pos2.getZ()));

            ServerWorld world = (ServerWorld) ((StructureBlockBlockEntity) (Object) this).getWorld();
            ServerPlayerEntity player = world.getServer().getPlayerManager().getPlayer(author);

            if(player != null && MultiBlockBuilder.playerCommandCache.containsKey(player.getUuid())) {
                MultiBlockBuilder.createMultiBlock(serverWorld, MultiBlockBuilder.playerCommandCache.get(player.getUuid()), minCorner, structure, structureName.getPath() + ".json");
                player.sendMessage(new LiteralText("§6Successfully saved multiblock " + structureName.getPath() + ".json"), false);
                cir.setReturnValue(true);
            }
        }
    }
}
