package abused_master.abusedlib.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelRenderOBJ extends ModelRenderer {

    private int displayList;
    private boolean compiled = false;
    private IBakedModel objModel;
    public float scale = 0;

    public ModelRenderOBJ(ModelBase baseModel, ResourceLocation customModel) {
        super(baseModel);

        try {
            objModel = ModelLoaderRegistry.getModel(customModel).bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(float scale) {
        if (!this.isHidden && this.showModel) {
            if (!this.compiled) {
                this.compileDisplayList(scale);
            }
            GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);

            if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
                if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
                    GlStateManager.callList(this.displayList);
                } else {
                    GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                    GlStateManager.callList(this.displayList);
                    GlStateManager.translate(-this.rotationPointX * scale, -this.rotationPointY * scale, -this.rotationPointZ * scale);
                }
            } else {
                GlStateManager.pushMatrix();
                GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                if (this.rotateAngleZ != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
                }

                if (this.rotateAngleY != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
                }

                if (this.rotateAngleX != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
                }

                GlStateManager.callList(this.displayList);
                GlStateManager.popMatrix();
            }

            GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
        }
    }

    private void compileDisplayList(float scale) {
        if (this.scale == 0) {
            this.scale = scale;
        }

        if (objModel == null) {
            compiled = true;
            return;
        }

        scale = this.scale;
        this.displayList = GLAllocation.generateDisplayLists(1);
        GlStateManager.glNewList(this.displayList, GL11.GL_COMPILE);

        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.rotate(180, -1, 0, 1);

        GlStateManager.bindTexture(Minecraft.getMinecraft().getTextureMapBlocks().getGlTextureId());
        ModelUtils.renderQuads(objModel.getQuads(null, null, 0));

        GlStateManager.popMatrix();

        GlStateManager.glEndList();
        this.compiled = true;
    }

    @Override
    public void renderWithRotation(float scale) {
        if (!this.isHidden && this.showModel) {
            if (!this.compiled) {
                this.compileDisplayList(scale);
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
            if (this.rotateAngleY != 0.0F) {
                GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if (this.rotateAngleX != 0.0F) {
                GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            if (this.rotateAngleZ != 0.0F) {
                GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            GlStateManager.callList(this.displayList);
            GlStateManager.popMatrix();
        }

    }

    @Override
    public void postRender(float scale) {
        if (!this.isHidden && this.showModel) {

            if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
                if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F) {
                    GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                }
            } else {
                GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                if (this.rotateAngleZ != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
                }

                if (this.rotateAngleY != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
                }

                if (this.rotateAngleX != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
                }
            }
        }
    }
}