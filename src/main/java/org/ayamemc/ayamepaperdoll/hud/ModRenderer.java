/*
 *     Highly configurable PaperDoll mod. Forked from Extra Player Renderer.
 *     Copyright (C) 2024-2025  LucunJi(Original author), HappyRespawnanchor
 *
 *     This file is part of Ayame PaperDoll.
 *
 *     Ayame PaperDoll is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Ayame PaperDoll is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Ayame PaperDoll.  If not, see <https://www.gnu.org/licenses/>.
 */

//? if >=1.21.6 {
package org.ayamemc.ayamepaperdoll.hud;

//?}
//? if >=1.21.6 && <26.3 {
/*import com.mojang.blaze3d.ProjectionType;
*///?}
//? if >=26.1 && <26.2 {
/*import com.mojang.blaze3d.opengl.GlStateManager;
*///?}
//? if >=1.21.6 {
import com.mojang.blaze3d.platform.Lighting;
//?}
//? if >=26.1 && <26.2 {
/*import com.mojang.blaze3d.systems.GpuDevice;
*///?}
//? if >=1.21.6 {
import com.mojang.blaze3d.systems.RenderSystem;
//?}
//? if >=26.1 && <26.2 {
/*import com.mojang.blaze3d.textures.AddressMode;
*///?}
//? if >=26.1 && <26.3 {
/*import com.mojang.blaze3d.textures.FilterMode;
*///?}
//? if >=26.1 && <26.2 {
/*import com.mojang.blaze3d.textures.TextureFormat;
*///?}
//? if >=1.21.6 {
import com.mojang.blaze3d.vertex.PoseStack;
//?}
//? if >=26.3 {
import com.mojang.math.Axis;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.textures.FilterMode;
//?}
//? if >=1.21.6 {
import net.minecraft.client.Minecraft;
//?}
//? if >=26.1 {
import net.minecraft.client.gui.render.TextureSetup;
//?}
//? if >=1.21.6 {
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
//?}
//? if >=26.3 {
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderPipelines;
//?}
//? if >=26.2 {
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeStorage;
//?}
//? if >=26.1 && <26.2 {
/*import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.BlitRenderState;
*///?}
//? if >=26.3 {
import net.minecraft.client.renderer.state.gui.BlitRenderState;
//?}
//? if >=26.1 {
import net.minecraft.client.renderer.state.gui.GuiRenderState;
//?}
//? if >=26.1 && <26.3 {
/*import net.minecraft.client.renderer.ProjectionMatrixBuffer;
import net.minecraft.client.renderer.Projection;
*///?}
//? if >=1.21.6 && <26.1 {
/*import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.renderer.CachedOrthoProjectionMatrixBuffer;
*///?}
//? if >=1.21.6 && <26.2 {
/*import net.minecraft.client.renderer.MultiBufferSource;
*///?}
//? if >=1.21.6 {
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
//?}
//? if >=1.21.9 {
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
//?}
//? if >=26.1 {
import net.minecraft.client.renderer.state.level.CameraRenderState;
//?}
//? if >=1.21.9 && <26.1 {
/*import net.minecraft.client.renderer.state.CameraRenderState;
*///?}
//? if >=1.21.6 && <26.3 {
/*import org.ayamemc.ayamepaperdoll.AyamePaperDoll;
*///?}
//? if >=1.21.6 {
import org.jetbrains.annotations.NotNull;
//?}
//? if >=26.3 {
import org.joml.Matrix4fStack;
//?}
//? if >=1.21.6 {
import org.joml.Quaternionf;
import org.joml.Vector3f;

//?}
//? if >=26.3 {
import java.util.Optional;
import java.util.OptionalDouble;
//?}
//? if >=1.21.6 && <26.3 {
/*import static org.ayamemc.ayamepaperdoll.AyamePaperDoll.CONFIGS;
*///?}
//? if >=1.21.6 {

public class ModRenderer extends PictureInPictureRenderer<ModRenderState> {
//?}
    //? if >=26.1 && <26.3 {
    /*private final ProjectionMatrixBuffer projectionMatrixBuffer = new ProjectionMatrixBuffer("PIP - " + this.getClass().getSimpleName());
    *///?}
    //? if >=1.21.6 && <26.1 {
    /*private final CachedOrthoProjectionMatrixBuffer projectionMatrixBuffer = new CachedOrthoProjectionMatrixBuffer(
            "PIP - " + this.getClass().getSimpleName(), -1000.0F, 1000.0F, true
    );
    *///?}
    //? if >=1.21.6 {
    private final EntityRenderDispatcher entityRenderDispatcher;
    //?}
    //? if >=26.1 && <26.3 {
    /*private final Projection projection = new Projection();
    *///?}
    //? if >=26.2 {
    private final SubmitNodeStorage submitNodeStorage = new SubmitNodeStorage();
    //?}
    //? if >=26.3 {
    private int width, height, guiScale;
    //?}
    //? if >=26.1 && <26.3 {
    /*private int width, height;
    *///?}
    //? if >=26.2 {
    public ModRenderer(EntityRenderDispatcher entityRenderDispatcher) {
    //?}
    //? if >=1.21.6 && <26.2 {
    /*public ModRenderer(MultiBufferSource.BufferSource bufferSource, EntityRenderDispatcher entityRenderDispatcher) {
        super(bufferSource);
    *///?}
        //? if >=1.21.6 {
        this.entityRenderDispatcher = entityRenderDispatcher;
    }

    @Override
    public @NotNull Class<ModRenderState> getRenderStateClass() {
        return ModRenderState.class;
    }

    @Override
        //?}
    //? if >=26.2 {
    protected void renderToTexture(ModRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        Minecraft.getInstance().gameRenderer.lighting().setupFor(Lighting.Entry.ENTITY_IN_UI);
    //?}
    //? if >=1.21.6 && <26.2 {
    /*protected void renderToTexture(ModRenderState renderState, PoseStack poseStack) {
        Minecraft.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ENTITY_IN_UI);
    *///?}
        //? if >=1.21.6 && <26.1 {
        /*Vector3f vector3f = renderState.translation();
        poseStack.mulPose(renderState.rotation());
        *///?}
        //? if >=1.21.6 {
        Quaternionf quaternionf = renderState.overrideCameraAngle();
        //?}
        //? if >=1.21.9 && <26.2 {
        /*FeatureRenderDispatcher featurerenderdispatcher = Minecraft.getInstance().gameRenderer.getFeatureRenderDispatcher();
        *///?}
        //? if >=1.21.9 {
        CameraRenderState camerarenderstate = new CameraRenderState();

        //?}
        //? if >=1.21.6 {
        if (quaternionf != null) {
        //?}
            //? if >=1.21.9 {
            camerarenderstate.orientation = quaternionf.conjugate(new Quaternionf()).rotateY((float) Math.PI);
            //?}
            //? if >=1.21.6 && <1.21.9 {
            /*this.entityRenderDispatcher.overrideCameraOrientation(quaternionf.conjugate(new Quaternionf()).rotateY((float) Math.PI));
            *///?}
        //? if >=1.21.6 {
        }
        //?}
        //? if >=26.1 {
        if(renderState.vehicleRenderState() != null){
            poseStack.pushPose();
            Vector3f vector3f = renderState.translation2();
            assert vector3f != null;
        //?}
            //? if >=26.3 {
            poseStack.rotate(renderState.rotation2());
            //?}
            //? if >=26.1 && <26.3 {
            /*poseStack.mulPose(renderState.rotation2());
            *///?}
            //? if >=26.2 {
            this.entityRenderDispatcher.submit(renderState.vehicleRenderState(), camerarenderstate, vector3f.x, vector3f.y, vector3f.z, poseStack, submitNodeCollector);
            //?}
            //? if >=26.1 && <26.2 {
            /*this.entityRenderDispatcher.submit(renderState.vehicleRenderState(), camerarenderstate, vector3f.x, vector3f.y, vector3f.z, poseStack, featurerenderdispatcher.getSubmitNodeStorage());
            *///?}
            //? if >=26.1 {
            poseStack.popPose();
        }
        Vector3f vector3f = renderState.translation();
            //?}
        //? if >=26.3 {
        poseStack.rotate(renderState.rotation());
        //?}
        //? if >=26.1 && <26.3 {
        /*poseStack.mulPose(renderState.rotation());
        *///?}
        //? if >=26.2 {
        this.entityRenderDispatcher.submit(renderState.renderState(), camerarenderstate, vector3f.x, vector3f.y, vector3f.z, poseStack, submitNodeCollector);
        //?}
        //? if >=1.21.9 && <26.2 {
        /*this.entityRenderDispatcher.submit(renderState.renderState(), camerarenderstate, vector3f.x, vector3f.y, vector3f.z, poseStack, featurerenderdispatcher.getSubmitNodeStorage());
        featurerenderdispatcher.renderAllFeatures();
        *///?}
        //? if >=1.21.6 && <1.21.9 {
        /*this.entityRenderDispatcher.setRenderShadow(false);
        this.entityRenderDispatcher.render(renderState.renderState(), vector3f.x, vector3f.y, vector3f.z, poseStack, this.bufferSource, renderState.light());
        this.entityRenderDispatcher.setRenderShadow(true);
        *///?}
    //? if >=1.21.6 {
    }

    @Override
    protected @NotNull String getTextureLabel() {
        return "ayame-paperdoll";
    }
    //?}
    //? if >=26.2 {
    public void prepare(ModRenderState renderState, GuiRenderState guiRenderState, FeatureRenderDispatcher featureRenderDispatcher, int guiScale) {
    //?}
        //? if >=26.3 {
        int width = renderState.x1() - renderState.x0();
		int height = renderState.y1() - renderState.y0();
        boolean needsAResize = this.width != width || this.height != height || this.guiScale != guiScale;
        //?}
        //? if >=26.2 && <26.3 {
        /*int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int raw_width = Minecraft.getInstance().getWindow().getWidth();
        int raw_height = Minecraft.getInstance().getWindow().getHeight();
        *///?}
    //? if >=1.21.6 && <26.2 {
    /*@Override
    public void prepare(ModRenderState renderState, GuiRenderState guiRenderState, int guiScale) {
    *///?}
        //? if >=26.1 && <26.2 {
        /*var width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        var height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        var raw_width = Minecraft.getInstance().getWindow().getWidth();
        var raw_height = Minecraft.getInstance().getWindow().getHeight();
        *///?}
        //? if >=26.1 && <26.3 {
        /*boolean needsAResize = this.width != width || this.height != height;
        *///?}
        //? if >=26.1 {
        if (needsAResize) {
            this.width = width;
            this.height = height;
        //?}
            //? if >=26.3 {
            this.guiScale = guiScale;
            //?}
        //? if >=26.1 {
        }
        //?}
        //? if >=26.3 {
        this.prepareTexturesAndProjection(needsAResize, width * guiScale, height * guiScale);
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.rotateY(renderState.lightDegree());
        //?}
        //? if >=26.1 && <26.3 {
        /*this.prepareTexturesAndProjection(true, raw_width, raw_height);
        this.projection.setupOrtho(-1000.0F, 1000.0F, width, height, true);
        RenderSystem.setProjectionMatrix(this.projectionMatrixBuffer.getBuffer(this.projection), ProjectionType.ORTHOGRAPHIC);
        RenderSystem.outputColorTextureOverride = this.textureView;
        RenderSystem.outputDepthTextureOverride = this.depthTextureView;
        *///?}
        //? if >=26.1 {
        PoseStack posestack = new PoseStack();
        //?}
        //? if >=26.3 {
        posestack.rotate(Axis.YP.rotation(-renderState.lightDegree()));
        posestack.scale(guiScale, guiScale, guiScale);
        float offsetX = renderState.mirror() ? width - renderState.offsetX() : renderState.offsetX();
        posestack.translate(offsetX, renderState.offsetY(), 0.0F);
        float f = renderState.scale();
        //?}
        //? if >=26.1 && <26.3 {
        /*float x0 =CONFIGS.mirrored.getValue()? width - renderState.x0() : renderState.x0();
        posestack.translate(x0, renderState.y0(), 0.0F);
        float f =  renderState.scale();
        *///?}
        //? if >=26.1 {
        posestack.scale(f, f, -f);
        //?}
        //? if >=1.21.11 && <26.1 {
        /*AyamePaperDoll.identifier = CONFIGS.mirrored.getValue();
        *///?}
        //? if >=1.21.6 && <1.21.11 {
        /*AyamePaperDoll.identifier=CONFIGS.mirrored.getValue();
        *///?}
        //? if >=1.21.6 && <26.1 {
        /*RenderSystem.setProjectionMatrix(
                this.projectionMatrixBuffer.getBuffer(Minecraft.getInstance().getWindow().getGuiScaledWidth(),
                        Minecraft.getInstance().getWindow().getGuiScaledHeight()),
                ProjectionType.ORTHOGRAPHIC);

        *///?}
        //? if >=1.21.11 && <26.1 {
        /*PoseStack posestack = renderState.boat() ? new PaperDollRenderer.PaperDollPoseStack() : new PoseStack();
        *///?}
        //? if >=1.21.6 && <1.21.11 {
        /*PoseStack posestack = renderState.boat()?new PaperDollRenderer.PaperDollPoseStack():new PoseStack();
        *///?}
        //? if >=1.21.6 && <26.1 {
        /*posestack.translate(renderState.x0(), renderState.y0(), 0.0F);
        *///?}
        //? if >=1.21.11 && <26.1 {
        /*posestack.scale(CONFIGS.mirrored.getValue() ? -1.0f : 1.0f, 1.0f, -1.0f);
        float f = renderState.scale();
        *///?}
        //? if >=1.21.6 && <1.21.11 {
        /*posestack.scale(CONFIGS.mirrored.getValue()?-1.0f:1.0f, 1.0f, -1.0f);
        float f =  renderState.scale();
        *///?}
        //? if >=1.21.6 && <26.1 {
        /*posestack.scale(f, f, f);
        *///?}
        //? if >=26.2 {
        this.renderToTexture(renderState, posestack, this.submitNodeStorage);
        //?}
        //? if >=26.3 {

        GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
        boolean temp = gameRenderer.useUiLightmap;
        gameRenderer.useUiLightmap = false;

        try (
            FeatureRenderDispatcher.PreparedFrame frame = featureRenderDispatcher.prepareFrame(this.submitNodeStorage);
            RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Picture in picture", this.textureView, Optional.empty(), this.depthTextureView, OptionalDouble.empty());
        ) {
            RenderSystem.bindDefaultUniforms(renderPass);
            FeatureRenderDispatcher.renderAllFeatures(renderPass, frame);
        }

        gameRenderer.useUiLightmap = temp;

        modelViewStack.popMatrix();
        //?}
        //? if >=26.2 && <26.3 {
        /*featureRenderDispatcher.renderAllFeatures(this.submitNodeStorage);
        *///?}
        //? if >=1.21.6 && <26.2 {
        /*this.renderToTexture(renderState, posestack);
        this.bufferSource.endBatch();
        *///?}
        //? if >=26.1 && <26.3 {
        /*RenderSystem.outputColorTextureOverride = null;
        RenderSystem.outputDepthTextureOverride = null;
        *///?}
        //? if >=26.1 {
        blitTexture(renderState, guiRenderState);
    }

    @Override
    protected void blitTexture(final ModRenderState renderState, final GuiRenderState guiRenderState) {
        float u0,u1;
        //?}
        //? if >=26.3 {
        if (renderState.mirror()){
        //?}
        //? if >=26.1 && <26.3 {
        /*if(CONFIGS.mirrored.getValue()){
        *///?}
            //? if >=26.1 {
            u0 = 1.0f;
            u1 = 0.0f;
        } else {
            u0 = 0.0f;
            u1 = 1.0f;
        }
            //?}
        //? if >=26.3 {
        guiRenderState.addBlitToCurrentLayer(
			new BlitRenderState(
				RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA,
				TextureSetup.singleTexture(this.textureView, RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST)),
				renderState.pose(),
				renderState.x0(),
				renderState.y0(),
				renderState.x1(),
				renderState.y1(),
				u0,
				u1,
				1.0F,
				0.0F,
				-1,
				renderState.scissorArea(),
				null
			)
		);
        //?}
        //? if >=26.1 && <26.3 {
        /*guiRenderState.addGlyphToCurrentLayer(
                new ExBlitRenderState(
                        AyamePaperDoll.MOD_PIPELINE,
                        new TextureSetup(
                                this.textureView, null, Minecraft.getInstance().gameRenderer.levelLightmap(), RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST), null, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)
                        ),
                        renderState.pose(),
                        0,0, width, height,
                        u0,u1,
                        1.0F,
                        0.0F,
                        renderState.renderState().lightCoords,
                        renderState.scissorArea(),
                        null
                )
        );
        *///?}
        //? if >=1.21.11 && <26.1 {
        /*AyamePaperDoll.identifier = false;
        *///?}
        //? if >=1.21.6 && <1.21.11 {
        /*AyamePaperDoll.identifier=false;
        *///?}
    //? if >=1.21.6 {
    }
}
    //?}
