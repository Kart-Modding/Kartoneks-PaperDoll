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

package org.ayamemc.ayamepaperdoll.hud;

import com.google.common.collect.ImmutableList;
//? if <1.21.6 {
/*import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
*///?}
//? if <26.1 {
/*import com.mojang.blaze3d.vertex.PoseStack;
*///?}
//? if <1.21.6 {
/*import com.mojang.math.Axis;
*///?}
import net.minecraft.client.Minecraft;
//? if >=26.1 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?}
//? if <26.1 {
/*import net.minecraft.client.gui.GuiGraphics;
*///?}
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
//? if >=26.3 {
import net.minecraft.client.renderer.entity.state.*;
//?}
//? if >=26.1 && <26.3 {
/*import net.minecraft.client.renderer.entity.state.BoatRenderState;
*///?}
//? if >=26.1 {
import net.minecraft.util.LightCoordsUtil;
//?}
//? if <26.1 {
/*import net.minecraft.client.renderer.LightTexture;
*///?}
//? if <1.21.6 {
/*import net.minecraft.client.renderer.MultiBufferSource;
*///?}
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
//? if >=1.21.6 {
import net.minecraft.client.renderer.entity.EntityRenderer;
//?}
//? if >=1.21.6 && <26.3 {
/*import net.minecraft.client.renderer.entity.state.EntityRenderState;
*///?}
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
//? if >=1.21.11 && <26.1 {
/*import net.minecraft.world.entity.vehicle.boat.Boat;
*///?}
//? if <1.21.11 {
/*import net.minecraft.world.entity.vehicle.Boat;
*///?}
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.ayamemc.ayamepaperdoll.config.Configs;
import org.ayamemc.ayamepaperdoll.config.Configs.RotationMode;
import org.ayamemc.ayamepaperdoll.hud.DataBackup.DataBackupEntry;
//? if <1.21.5 {
/*import org.ayamemc.ayamepaperdoll.mixininterface.BufferSourceMixinInterface;
*///?}
//? if >=26.1 {
import org.ayamemc.ayamepaperdoll.mixininterface.GuiGraphicsExtractorInterface;
import org.jetbrains.annotations.Nullable;
//?}
//? if >=1.21.6 && <26.1 {
/*import org.ayamemc.ayamepaperdoll.mixininterface.GuiGraphicsInterface;
*///?}
//? if <1.21.6 {
/*import org.joml.Matrix4fStack;
*///?}
import org.joml.Quaternionf;
import org.joml.Vector3f;

//? if <26.1 {
/*import java.awt.geom.Rectangle2D;
*///?}
import java.util.List;

import static org.ayamemc.ayamepaperdoll.AyamePaperDoll.CONFIGS;

public class PaperDollRenderer {
    private static final List<DataBackupEntry<LivingEntity, ?>> LIVINGENTITY_BACKUP_ENTRIES = ImmutableList.of(
            new DataBackupEntry<>(LivingEntity::getPose, LivingEntity::setPose),
            // required for player on client side
            new DataBackupEntry<>(Entity::isCrouching, (e, flag) -> {
                if (e instanceof LocalPlayer player) player.crouching = flag;
            }),
            //? if <26.3 {
            /*new DataBackupEntry<>(e -> e.swimAmount, (e, pitch) -> e.swimAmount = pitch),
            new DataBackupEntry<>(e -> e.swimAmountO, (e, pitch) -> e.swimAmountO = pitch),
            *///?}
            new DataBackupEntry<>(LivingEntity::isFallFlying, (e, flag) -> e.setSharedFlag(7, flag)),
            new DataBackupEntry<>(LivingEntity::getFallFlyingTicks, (e, ticks) -> e.fallFlyTicks = ticks),

            new DataBackupEntry<>(LivingEntity::getVehicle, (e, vehicle) -> e.vehicle = vehicle),

            new DataBackupEntry<>(e -> e.yBodyRotO, (e, yaw) -> e.yBodyRotO = yaw),
            new DataBackupEntry<>(e -> e.yBodyRot, (e, yaw) -> e.yBodyRot = yaw),
            new DataBackupEntry<>(e -> e.yHeadRotO, (e, yaw) -> e.yHeadRotO = yaw),
            new DataBackupEntry<>(e -> e.yHeadRot, (e, yaw) -> e.yHeadRot = yaw),
            new DataBackupEntry<>(e -> e.xRotO, (e, pitch) -> e.xRotO = pitch),
            //? if >=26.3 {
            new DataBackupEntry<>(LivingEntity::getXRot, LivingEntity::setXRot)
            //?}
            //? if <26.3 {
            /*new DataBackupEntry<>(LivingEntity::getXRot, LivingEntity::setXRot),

            new DataBackupEntry<>(e -> e.attackAnim, (e, prog) -> e.attackAnim = prog),
            new DataBackupEntry<>(e -> e.oAttackAnim, (e, prog) -> e.oAttackAnim = prog),
            new DataBackupEntry<>(e -> e.hurtTime, (e, time) -> e.hurtTime = time),
            new DataBackupEntry<>(LivingEntity::getRemainingFireTicks, LivingEntity::setRemainingFireTicks),
            new DataBackupEntry<>(e -> e.getSharedFlag(0), (e, flag) -> e.setSharedFlag(0, flag)) // on fire
            *///?}
    );
    private static final PaperDollRenderer instance = new PaperDollRenderer();
    private final Minecraft minecraft = Minecraft.getInstance();
    //? if <26.1 {
    /*private Rectangle2D.Double currentRenderBounds;
    *///?}

    private PaperDollRenderer() {
    }

    public static PaperDollRenderer getInstance() {
        return instance;
    }

    @SuppressWarnings("resource")
    private static int getLight(Entity entity, float tickDelta) {
        if (CONFIGS.useWorldLight.getValue()) {
            Level world = entity.level();
            int blockLight = world.getBrightness(LightLayer.BLOCK, BlockPos.containing(entity.getEyePosition(tickDelta)));
            int skyLight = world.getBrightness(LightLayer.SKY, BlockPos.containing(entity.getEyePosition(tickDelta)));
            int min = CONFIGS.worldLightMin.getValue();
            blockLight = Mth.clamp(blockLight, min, 15);
            skyLight = Mth.clamp(skyLight, min, 15);
            //? if >=26.1 {
            return LightCoordsUtil.pack(blockLight, skyLight);
            //?}
            //? if <26.1 {
            /*return LightTexture.pack(blockLight, skyLight);
            *///?}
        }
        //? if >=26.1 {
        return LightCoordsUtil.pack(15, 15);
        //?}
        //? if <26.1 {
        /*return LightTexture.pack(15, 15);
        *///?}
    }

    private static float getFallFlyingLeaning(LivingEntity entity, float partialTicks) {
        float ticks = partialTicks + entity.getFallFlyingTicks();
        return Mth.clamp(ticks * ticks / 100f, 0f, 1f);
    }

    public static boolean shouldLockRotationYaw() {
        final RotationMode rotationUnlock = CONFIGS.rotationMode.getValue();
        //? if >=1.21.3 {
        return (rotationUnlock == RotationMode.LOCK); //|| (rotationUnlock == RotationMode.SMOOTH_LOCK)* /;
        //?}
        //? if <1.21.3 {
        /*return (rotationUnlock == RotationMode.LOCK);//|| (rotationUnlock == RotationMode.SMOOTH_LOCK);
        *///?}

    }

    //? if <26.3 {
    /*// 这会导致织布机渲染问题，不要使用↓
    // follow convention in LayeredDrawer#renderInternal
    // guiGraphics.pose().translate(0, 0, 200);
    *///?}
//? if >=1.21.6 && <26.3 {
/*
*///?}
    //
    //? if >=26.1 {
    // * Mimics the code in {@link InventoryScreen#extractEntityInInventoryFollowsMouse}
    //?}
    //? if >=1.21.11 && <26.1 {
    /*// * Mimics the code in {@link InventoryScreen#renderEntityInInventoryFollowsMouse}
    *///?}
    //? if <1.21.11 {
    /*// * Mimics the code in {@link InventoryScreen#renderEntityInInventory}
    *///?}
    //
    //? if >=26.1 {
    public void extractPaperdoll(GuiGraphicsExtractor graphics, float a) {
    //?}
    //? if <26.1 {
    /*public void render(float partialTicks, GuiGraphics guiGraphics) {
    *///?}
        if (minecraft.level == null || minecraft.player == null || !CONFIGS.displayPaperDoll.getValue()) return;
        LivingEntity targetEntity = minecraft.level.players().stream().filter(p -> p.getName().getString().equals(CONFIGS.playerName.getValue())).findFirst().orElse(minecraft.player);
        if (CONFIGS.spectatorAutoSwitch.getValue() && minecraft.player.isSpectator()) {
            Entity cameraEntity = minecraft.getCameraEntity();
            if (cameraEntity instanceof LivingEntity livingEntity) {
                targetEntity = livingEntity;
            } else if (cameraEntity != null) {
                return;
            }
        }

        //? if <26.1 {
        /*int scaledWidth = minecraft.getWindow().getGuiScaledWidth();
        int scaledHeight = minecraft.getWindow().getGuiScaledHeight();
        *///?}
        Configs.PoseOffsetMethod poseOffsetMethod = CONFIGS.poseOffsetMethod.getValue();

        var backup = new DataBackup<>(targetEntity, LIVINGENTITY_BACKUP_ENTRIES);
        backup.save();

        //? if >=26.3 {
        boolean forceStanding = poseOffsetMethod == Configs.PoseOffsetMethod.FORCE_STANDING;

        transformEntity(targetEntity, a, forceStanding);
        //?}
        //? if >=26.1 && <26.3 {
        /*transformEntity(targetEntity, a, poseOffsetMethod == Configs.PoseOffsetMethod.FORCE_STANDING);
        *///?}
        //? if <26.1 {
        /*transformEntity(targetEntity, partialTicks, poseOffsetMethod == Configs.PoseOffsetMethod.FORCE_STANDING);
        *///?}

        //? if >=26.1 {
        EntityRenderState vehicleRenderState = null;
        Vector3f vehicleOffset = null;
        //?}
        DataBackup<LivingEntity> vehicleBackup = null;
        if (CONFIGS.renderVehicle.getValue() && poseOffsetMethod != Configs.PoseOffsetMethod.FORCE_STANDING && targetEntity.isPassenger()) {
            var vehicle = targetEntity.getVehicle();
            assert vehicle != null;

            // get the overall yaw before transforming
            //? if >=26.1 {
            var yawLerped = vehicle.getViewYRot(a);
            //?}
            //? if <26.1 {
            /*var yawLerped = vehicle.getViewYRot(partialTicks);
            *///?}

            // FIXME: NEVERFIX - the rendered yaw of minecart is determined non-trivially in its MinecartEntityRenderer#render, so it cannot be fixed to 0 easily
            if (vehicle instanceof LivingEntity livingVehicle) {
                vehicleBackup = new DataBackup<>(livingVehicle, LIVINGENTITY_BACKUP_ENTRIES);
                vehicleBackup.save();
                //? if >=26.1 {
                transformEntity(livingVehicle, a, false);
                //?}
                //? if <26.1 {
                /*transformEntity(livingVehicle, partialTicks, false);
                *///?}
            }

            //? if >=26.3 {
            vehicleRenderState = extractRenderState(vehicle, a, false);
            //?}
            //? if >=26.1 && <26.3 {
            /*vehicleRenderState = extractRenderState(vehicle, a);
            *///?}
            //? if >=26.1 {
            vehicleOffset = vehicle.getPosition(a).subtract(targetEntity.getPosition(a))
                    .yRot((float) Math.toRadians(yawLerped+180)).toVector3f();// undo the rotation
            //?}
            //? if <26.1 {
            /*performRendering(vehicle,
                    CONFIGS.offsetX.getValue() * scaledWidth,
                    CONFIGS.offsetY.getValue() * scaledHeight,
                    CONFIGS.size.getValue() * scaledHeight,
            *///?}
                    //? if >=1.21.6 && <26.1 {
                    /*true,
                    *///?}
                    //? if <1.21.6 {
                    /*CONFIGS.mirrored.getValue(),
                    *///?}
                    //? if <26.1 {
                    /*vehicle.getPosition(partialTicks).subtract(targetEntity.getPosition(partialTicks))
                    *///?}
                            //? if >=1.21.6 && <26.1 {
                            /*.yRot((float) Math.toRadians(yawLerped+180)).toVector3f(), // undo the rotation
                            *///?}
                            //? if <1.21.6 {
                            /*.yRot((float) Math.toRadians(yawLerped)).toVector3f(), // undo the rotation
                            *///?}
                    //? if <26.1 {
                    /*CONFIGS.lightDegree.getValue(),
                    partialTicks, guiGraphics);
                    *///?}
        }

        //? if >=26.3 {
        var targetRenderState = extractRenderState(targetEntity, a, forceStanding);
        //?}
        //? if >=26.1 && <26.3 {
        /*var targetRenderState = extractRenderState(targetEntity, a);
        *///?}

        //? if >=26.1 {
        extractPaperdoll(
                graphics,
                targetRenderState,
                new Vector3f(0, (float) getPoseOffsetY(targetEntity, a, poseOffsetMethod), 0),
                vehicleRenderState,
                vehicleOffset
        );
        //?}
        //? if <26.1 {
        /*performRendering(targetEntity,
                CONFIGS.offsetX.getValue() * scaledWidth,
                CONFIGS.offsetY.getValue() * scaledHeight,
                CONFIGS.size.getValue() * scaledHeight,
        *///?}
                //? if >=1.21.6 && <26.1 {
                /*false,
                *///?}
                //? if <1.21.6 {
                /*CONFIGS.mirrored.getValue(),
                *///?}
                //? if <26.1 {
                /*new Vector3f(0, (float) getPoseOffsetY(targetEntity, partialTicks, poseOffsetMethod), 0),
                CONFIGS.lightDegree.getValue(),
                partialTicks, guiGraphics);
                *///?}

        if (vehicleBackup != null) vehicleBackup.restore();

        backup.restore();
    }

    private double getPoseOffsetY(LivingEntity targetEntity, float partialTicks, Configs.PoseOffsetMethod poseOffsetMethod) {
        if (poseOffsetMethod == Configs.PoseOffsetMethod.AUTO) {
            final float defaultPlayerEyeHeight = Player.DEFAULT_EYE_HEIGHT;
            final float defaultPlayerSwimmingBBHeight = Player.SWIMMING_BB_HEIGHT;
            final float eyeHeightRatio = 0.85f;
            if (targetEntity.isFallFlying()) {
                return (defaultPlayerEyeHeight - targetEntity.getEyeHeight()) * getFallFlyingLeaning(targetEntity, partialTicks);
            } else if (targetEntity.isAutoSpinAttack()) {
                return defaultPlayerEyeHeight - defaultPlayerSwimmingBBHeight * eyeHeightRatio * 0.8;
            } else if (targetEntity.isVisuallySwimming()) {
                return targetEntity.getSwimAmount(partialTicks) <= 0 ? 0 : defaultPlayerEyeHeight - targetEntity.getEyeHeight();
            } else if (!targetEntity.isVisuallySwimming() && targetEntity.getSwimAmount(partialTicks) > 0) { // for swimming/crawling pose, only smooth the falling edge
                return (defaultPlayerEyeHeight - defaultPlayerSwimmingBBHeight * eyeHeightRatio * 0.85) * targetEntity.getSwimAmount(partialTicks);
            } else {
                return Player.DEFAULT_EYE_HEIGHT - targetEntity.getEyeHeight();
            }
        } else if (poseOffsetMethod == Configs.PoseOffsetMethod.MANUAL) {
            if (targetEntity.isFallFlying()) {
                return CONFIGS.elytraOffsetY.getValue() * getFallFlyingLeaning(targetEntity, partialTicks);
            } else if ((targetEntity.isVisuallySwimming()) && targetEntity.getSwimAmount(partialTicks) > 0 || targetEntity.isAutoSpinAttack()) { // require nonzero leaning to filter out glitch
                return CONFIGS.swimCrawlOffsetY.getValue();
            } else if (!targetEntity.isVisuallySwimming() && targetEntity.getSwimAmount(partialTicks) > 0) { // for swimming/crawling pose, only smooth the falling edge
                return CONFIGS.swimCrawlOffsetY.getValue() * targetEntity.getSwimAmount(partialTicks);
            } else if (targetEntity.isCrouching()) {
                return CONFIGS.sneakOffsetY.getValue();
            }
        }
        return 0;
    }

    private void transformEntity(LivingEntity targetEntity, float partialTicks, boolean forceStanding) {
        // synchronize values to remove glitch
        if (!targetEntity.isSwimming() && !targetEntity.isFallFlying() && !targetEntity.isVisuallyCrawling()) {
            targetEntity.setPose(targetEntity.isCrouching() ? Pose.CROUCHING : Pose.STANDING);
        }

        if (forceStanding) {
            if (targetEntity instanceof LocalPlayer player) {
                player.crouching = false;
            }
            targetEntity.vehicle = null;

            //? if <26.3 {
            /*targetEntity.swimAmount = 0;
            targetEntity.swimAmountO = 0;

            *///?}
            targetEntity.setSharedFlag(7, false);
            targetEntity.fallFlyTicks = 0;
        }

        // FIXME: NEVERFIX - glitch when the mouse moves too fast, caused by lerping a warped value, it is possibly wrapped in LivingEntity#tick or LivingEntity#turnHead
        final float headLerp = Mth.lerp(partialTicks, targetEntity.yHeadRotO, targetEntity.yHeadRot);
        final double headYaw = CONFIGS.headYaw.getValue(), headYawRange = CONFIGS.headYawRange.getValue();
        final double bodyYaw = CONFIGS.bodyYaw.getValue(), bodyYawRange = CONFIGS.bodyYawRange.getValue();
        final double pitch = CONFIGS.pitch.getValue(), pitchRange = CONFIGS.pitchRange.getValue();
        //? if >=26.3 {
        final float headClamp = (float) Mth.clamp(Mth.wrapDegrees(headLerp), headYaw - headYawRange, headYaw + headYawRange);
        //?}
        //? if <26.3 {
        /*final float headClamp = (float) Mth.clamp(headLerp, headYaw - headYawRange, headYaw + headYawRange);
        *///?}
        final float bodyLerp = Mth.lerp(partialTicks, targetEntity.yBodyRotO, targetEntity.yBodyRot);
        final float diff = headLerp - bodyLerp;
        final float bodyClamp = (float) Mth.clamp(Mth.wrapDegrees(headClamp - diff), bodyYaw - bodyYawRange, bodyYaw + bodyYawRange);
        final float pitchClamp = (float) (Mth.clamp(Mth.lerp(partialTicks, targetEntity.xRotO, targetEntity.getXRot()), -pitchRange, pitchRange) + pitch);
        final RotationMode rotationMode = CONFIGS.rotationMode.getValue();

        // 头部锁定
        //? if >=1.21.3 {
        if (rotationMode == RotationMode.LOCK) {
        //?}
        //? if <1.21.3 {
        /*if (rotationMode == RotationMode.LOCK) {//|| (targetEntity.ayame_paperdoll$isSitting() && (rotationMode == RotationMode.SMOOTH_LOCK))) {
        *///?}
            targetEntity.yHeadRot = targetEntity.yHeadRotO = 180 - headClamp;
        }
        // 身体锁定
        //? if >=1.21.3 {
        if (rotationMode == RotationMode.LOCK) {
        //?}
        //? if <1.21.3 {
        /*if (rotationMode == RotationMode.LOCK) {//|| (targetEntity.ayame_paperdoll$isSitting() && (rotationMode == RotationMode.SMOOTH_LOCK))) {
        *///?}
            targetEntity.yBodyRot = targetEntity.yBodyRotO = 180 - bodyClamp;
        }

        // 头部俯视角度
        targetEntity.setXRot(targetEntity.xRotO = pitchClamp);
        //? if <26.3 {
/*

        if (!CONFIGS.swingHands.getValue()) {
            targetEntity.attackAnim = 0;
            targetEntity.oAttackAnim = 0;
        }

        if (!CONFIGS.hurtFlash.getValue()) {
            targetEntity.hurtTime = 0;
        }

        targetEntity.setRemainingFireTicks(0);

        targetEntity.setSharedFlag(0, false);
        *///?}
    }

    //? if <1.21.3 {
    /*@SuppressWarnings("deprecation")
    *///?}
    //? if >=26.1 {
    private void extractPaperdoll(GuiGraphicsExtractor graphics, EntityRenderState target, Vector3f offset,
                                  @Nullable EntityRenderState vehicle, @Nullable Vector3f offset2) {
        var scaledWidth = minecraft.getWindow().getGuiScaledWidth();
        var scaledHeight = minecraft.getWindow().getGuiScaledHeight();
    //?}
    //? if >=1.21.6 && <26.1 {
    /*private void performRendering(Entity targetEntity, double posX, double posY, double size, boolean boat,
    *///?}
    //? if <1.21.6 {
    /*private void performRendering(Entity targetEntity, double posX, double posY, double size, boolean mirror,
    *///?}
                                  //? if <26.1 {
                                  /*Vector3f offset, double lightDegree, float partialTicks, GuiGraphics guiGraphics) {
                                  *///?}
        //? if >=1.21.6 && <26.1 {
        /*// 其余渲染逻辑保持不变...
        *///?}
        //? if <26.1 {
        /*EntityRenderDispatcher entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
        *///?}

        //? if >=26.1 {
        var posX = CONFIGS.offsetX.getValue() * scaledWidth;
        var posY = CONFIGS.offsetY.getValue() * scaledHeight;
        var size = CONFIGS.size.getValue() * scaledHeight;
        var lightDegree = CONFIGS.lightDegree.getValue();
        //?}
        //? if >=1.21.6 && <26.1 {
        /*EntityRenderer<? super Entity, ?> entityRenderer = entityRenderDispatcher.getRenderer(targetEntity);
        *///?}
        //? if <1.21.6 {
        /*Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.scale(mirror ? -1 : 1, 1, -1);
        // IDK what shit Mojang made but let's add 180 deg to restore the old behavior
        modelViewStack.rotateY((float) Math.toRadians(lightDegree + 180));
        *///?}
//? if <26.1 {
/*
*///?}
        //? if >=1.21.6 && <26.1 {
        /*EntityRenderState state = entityRenderer.createRenderState(targetEntity, partialTicks);
        *///?}
        //? if >=1.21.9 && <26.1 {
        /*state.lightCoords =getLight(targetEntity,partialTicks);
        *///?}
        //? if >=1.21.6 && <1.21.11 {
        /*state.hitboxesRenderState = null;
        *///?}
        //? if >=1.21.3 && <1.21.6 {
        /*PoseStack poseStack = new PaperDollPoseStack();
        *///?}
        //? if <1.21.3 {
        /*RenderSystem.applyModelViewMatrix();
        PoseStack poseStack = new PoseStack();
        *///?}
        //? if <1.21.6 {
        /*poseStack.mulPose(Axis.YP.rotationDegrees(-(float) lightDegree - 180));
        poseStack.translate((mirror ? -1 : 1) * posX, posY, 0);
        poseStack.scale((float) size, (float) size, (float) size);
        Quaternionf zRot = new Quaternionf().rotateZ((float) Math.PI);
        *///?}
        //? if >=1.21.9 && <26.1 {
        /*state.shadowPieces.clear();
        state.outlineColor = 0;
        *///?}
//? if <26.1 {
/*
*///?}
        //? if >=1.21.6 && <26.1 {
        /*// 启用Tweakeroo FreeCam功能时候不显示自己的 nameTag
        if (state.nameTag != null && minecraft.player != null) {
            if ( minecraft.player.getName().getString().equals(state.nameTag.getString())) {
                state.nameTag = null;
            }
        }
        *///?}
        //? if <1.21.6 {
        /*final RotationMode rotationMode = CONFIGS.rotationMode.getValue();
        *///?}

        //? if >=1.21.6 {
        Quaternionf pose = new Quaternionf().rotateZ((float) Math.PI).rotateY((float) Math.PI);
        Quaternionf configRot = new Quaternionf().rotateXYZ(
                (float) Math.toRadians(CONFIGS.rotationX.getValue()),
                (float) Math.toRadians(CONFIGS.rotationY.getValue()),
                (float) Math.toRadians(CONFIGS.rotationZ.getValue()));
        //?}
        //? if >=1.21.3 && <1.21.6 {
        /*Quaternionf xyzRot = // (rotationMode == RotationMode.SMOOTH_LOCK) ?
        // new Quaternionf().rotateXYZ(
        *///?}
        //? if <1.21.3 {
        /*Quaternionf xyzRot;
        // if (rotationMode == RotationMode.SMOOTH_LOCK && !(targetEntity instanceof Boat)) {
        // xyzRot = new Quaternionf().rotateXYZ(
        *///?}
        //? if <1.21.6 {
        /*// (float) Math.toRadians(CONFIGS.rotationX.getValue()),
        // (float) ((targetEntity.getYRot() + CONFIGS.rotationY.getValue() - 180) * ((float) Math.PI / 180F)),
        *///?}
        //? if >=1.21.3 && <1.21.6 {
        /*// (float) Math.toRadians(CONFIGS.rotationZ.getValue()))
        // :
                new Quaternionf().rotateXYZ(
                        (float) Math.toRadians(CONFIGS.rotationX.getValue()),
                        (float) Math.toRadians(CONFIGS.rotationY.getValue()),
                        (float) Math.toRadians(CONFIGS.rotationZ.getValue()));
        *///?}
        //? if <1.21.3 {
        /*// (float) Math.toRadians(CONFIGS.rotationZ.getValue()));
        // } else {
        // 8}
        xyzRot = new Quaternionf().rotateXYZ(
                (float) Math.toRadians(CONFIGS.rotationX.getValue()),
                (float) Math.toRadians(CONFIGS.rotationY.getValue()),
                (float) Math.toRadians(CONFIGS.rotationZ.getValue()));

        *///?}

        //? if >=26.3 {
        pose.mul(configRot).rotateY((float) Math.PI);
        //?}
        //? if >=1.21.6 && <26.3 {
        /*pose.mul(configRot).rotateY((float) Math.toRadians(lightDegree + 180));
        *///?}
        //? if <1.21.6 {
        /*zRot.mul(xyzRot);
        poseStack.mulPose(zRot);
        *///?}

        //? if >=26.1 {
        var pose1 = new Quaternionf(pose);
        if(target instanceof BoatRenderState) {
            pose1.rotateY((float) Math.toRadians(180));
        }
        if(vehicle instanceof BoatRenderState boat){
        //?}
        //? if <26.1 {
        /*if (targetEntity instanceof Boat) {
        *///?}
            //? if >=1.21.6 {
            pose.rotateY((float) Math.toRadians(180));
            //?}
            //? if <1.21.6 {
            /*poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(180)));
            *///?}
            //? if >=26.1 {
            if(PaperDollRenderer.shouldLockRotationYaw()){
                boat.yRot = 0;
            }
            //?}
        }
        //? if >=26.1 {

        // different renderState, offset, pose; same rotation, pos, size
        //?}
        //? if >=26.2 {
        ((GuiGraphicsExtractorInterface)graphics).ayame_paperDoll$addPicturesInPictureState(
        //?}
        //? if >=26.1 && <26.2 {
        /*((GuiGraphicsExtractorInterface)graphics).addPicturesInPictureState(
        *///?}
                //? if >=26.1 {
                new ModRenderState(
                        target,
                        offset,
                        pose1,
                        vehicle,
                        offset2,
                        pose,
                        new Quaternionf(configRot).conjugate(),
                //?}
                        //? if >=26.3 {
                        (int) posX,
                        (int) posY,
                        scaledWidth,
                        scaledHeight,
                        (float) size,
                        (float) Math.toRadians(lightDegree),
                        CONFIGS.mirrored.getValue()
                        //?}
                        //? if >=26.1 && <26.3 {
                        /*(int) posX, (int) posY,(float) size, null
                        *///?}
                //? if >=26.1 {
                )
                //?}
        //? if >=1.21.6 && <26.1 {
        /*((GuiGraphicsInterface)guiGraphics).submitModeRenderState(
                state,
                offset,
                pose,
                new Quaternionf(configRot).conjugate(),
                (int) posX,
                (int) posY,
                (float) size,
        *///?}
                //? if >=1.21.9 && <26.1 {
                /*boat
                *///?}
                //? if >=1.21.6 && <1.21.9 {
                /*boat,
                getLight(targetEntity, partialTicks)
                *///?}
        //? if >=1.21.6 {
        );
    }
        //?}

        //? if <1.21.6 {
        /*Lighting.setupForEntityInInventory();
        xyzRot.conjugate();

        entityRenderDispatcher.overrideCameraOrientation(xyzRot);
        boolean renderHitbox = entityRenderDispatcher.shouldRenderHitBoxes();
        entityRenderDispatcher.setRenderHitBoxes(false);
        entityRenderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();

        *///?}
        //? if <1.21.3 {
/*
        final float rotationYaw = ((rotationMode == RotationMode.UNLOCK) // || (rotationMode == RotationMode.SMOOTH_LOCK)
        ) ? targetEntity.getYRot() : 0.0F;

        *///?}
        //? if <1.21.6 {
        /*// TODO: 修复矿车锁定旋转时不被锁定的问题
        *///?}
        //? if >=1.21.5 && <1.21.6 {
        /*guiGraphics.drawSpecial(multiBufferSource -> entityRenderDispatcher.render(targetEntity, offset.x, offset.y, offset.z, partialTicks, poseStack, bufferSource, getLight(targetEntity, partialTicks)));
        *///?}
        //? if >=1.21.3 && <1.21.5 {
        /*guiGraphics.drawSpecial(multiBufferSource ->
                entityRenderDispatcher.render(targetEntity, offset.x, offset.y, offset.z, partialTicks, poseStack, bufferSource, getLight(targetEntity, partialTicks)));
        *///?}
        //? if <1.21.3 {
        /*RenderSystem.runAsFancy(
                () -> entityRenderDispatcher.render(targetEntity, offset.x, offset.y, offset.z, rotationYaw, partialTicks, poseStack, bufferSource, getLight(targetEntity, partialTicks))
        );
        *///?}
        //? if <1.21.6 {
/*
        // 事实证明1.21.3+只需一直禁用剔除，镜像也不会导致什么问题
        *///?}
        //? if <1.21.5 {
        /*((BufferSourceMixinInterface) bufferSource).ayame_PaperDoll$setForceDisableCulling(true);
        *///?}
        //? if <1.21.6 {
        /*bufferSource.endBatch();

        // do not need to restore this value in fact
        entityRenderDispatcher.setRenderShadow(true);
        entityRenderDispatcher.setRenderHitBoxes(renderHitbox);

        modelViewStack.popMatrix();
        *///?}
        //? if <1.21.3 {
        /*RenderSystem.applyModelViewMatrix();
        *///?}
        //? if <1.21.6 {
        /*Lighting.setupFor3DItems();
    }
        *///?}
    //? if >=26.1 {
    //also check =InventoryScreen#extractRenderState
    //?}
    //? if >=26.3 {
    private EntityRenderState extractRenderState(Entity targetEntity, float a, boolean forceStanding){
    //?}
    //? if >=26.1 && <26.3 {
    /*private EntityRenderState extractRenderState(Entity targetEntity, float a){
    *///?}
        //? if >=26.1 {
        EntityRenderDispatcher entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
        EntityRenderer<? super Entity, ?> entityRenderer = entityRenderDispatcher.getRenderer(targetEntity);
        EntityRenderState state = entityRenderer.createRenderState(targetEntity, a);
        state.lightCoords = getLight(targetEntity, a);
        state.shadowPieces.clear();
        state.outlineColor = 0;
        //?}
        //? if >=26.3 {

        // disable fire effect and text rendered on entity
        state.scoreText = null;
        state.nameTag = null;
        state.displayFireAnimation = false;

        // With improved transparency (OIT) on, the boat's water patch uses the depth-only OIT_WATER_MASK
        // pipeline, which has no color target and crashes our single-color-attachment render pass.
        // The patch only hides water inside the hull, so it's useless on the HUD anyway.
        if (state instanceof BoatRenderState boatState) {
            boatState.isUnderWater = true;
        }

        if (state instanceof LivingEntityRenderState livingState) {
            if (!CONFIGS.hurtFlash.getValue()) livingState.hasRedOverlay = false;
        }

        if (state instanceof ArmedEntityRenderState armedState) {
            if (!CONFIGS.swingHands.getValue()) {
                armedState.swingAnimation = 0;
                armedState.currentSwing = null;
            }
        }

        if (state instanceof HumanoidRenderState humanoidState) {
            if (forceStanding) {
                humanoidState.swimAmount = 0;
            }
        }

        //?}
        //? if >=26.1 {
        return state;
        //?}
    //? if >=1.21.3 && <26.1 {
/*
    public Rectangle2D.Double getRenderBounds() {
        return this.currentRenderBounds;
    }

    public interface LockedPaperDoll {
    }

    *///?}
    //? if >=1.21.6 && <26.1 {
    /*public static class PaperDollPoseStack extends PoseStack implements LockedPaperDoll {
    *///?}
    //? if >=1.21.3 && <1.21.6 {
    /*public static class PaperDollPoseStack extends PoseStack implements LockedPaperDoll{
    *///?}
    //? if >=1.21.3 {
    }
    //?}
}
