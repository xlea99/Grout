package net.xlea99.grout.entry.spawn_frogs_eat_flies;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ninni.spawn.client.model.entity.arthropod.FlyModel;
import com.ninni.spawn.registry.SpawnEntityModelLayers;
import net.minecraft.client.model.FrogModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.frog.Frog;
import org.joml.Quaternionf;

/**
 * Draws Spawn's fly on the end of a frog's tongue while {@link FrogSnack} has it out. It's placed from the tongue itself, so that it goes back into the mouth with it,
 * and from no part of the frog's own scale, so that it isn't stretched: vanilla's tongue grows by scaling, to five times its length and back.
 */
public final class FlyOnTongueLayer extends RenderLayer<Frog, FrogModel<Frog>> {
    private static final ResourceLocation FLY_TEXTURE = ResourceLocation.fromNamespaceAndPath("spawn", "textures/gui/fly/fly.png");
    /** How far from its root the tongue's flat box goes, in model pixels, and the size Spawn draws flies at when they're particles. */
    private static final float TONGUE_LENGTH = 7.1F;
    private static final float FLY_SCALE = 0.5F;
    /** From the model: the box's middle is this far below where a fly model's parts start. */
    private static final float FLY_MIDDLE = 1.5F;
    /** It has caught it at about its full length, and it's in the mouth when the tongue's nearly short again. */
    private static final float CAUGHT_AT_TICK = 2.5F;
    private static final float IN_MOUTH_SCALE = 1.25F;
    private static final float OVER_AT_TICK = 10F;
    /** From vanilla's animation, only if another mod's model has no such part to read: the tongue's length over time, in ticks, and where it starts in pixels. */
    private static final float VANILLA_FULL_AT = 3.33F;
    private static final float VANILLA_BACK_AT = 8.33F;
    private static final float VANILLA_FULL_SCALE = 5F;
    private static final float VANILLA_TONGUE_Y = 24F - 2F - 1.01F;
    private static final float VANILLA_TONGUE_Z = 4F + 1F;

    /** How long vanilla's animation makes the tongue at its longest, and the shortest and longest this will have it reach for. */
    private static final float VANILLA_LONGEST = 5F;
    private static final float SHORTEST = 2F;
    private static final float LONGEST = 14F;

    private final FlyModel<Entity> fly;

    public FlyOnTongueLayer(RenderLayerParent<Frog, FrogModel<Frog>> parent, EntityModelSet models) {
        super(parent);
        this.fly = new FlyModel<>(models.bakeLayer(SpawnEntityModelLayers.FLY));
    }

    /**
     * Stretches a snack's tongue to reach the edge of what it's going for, leaving the animation's timing alone: it goes from 1 to its longest and back as vanilla's does. A
     * vanilla catch is left as it is. Its prey is pulled to it, and this only goes for the moments after {@link FrogSnack}'s event.
     */
    public static void reach(ModelPart tongue, Frog frog, float age) {
        float since = age - ((FrogSnack.Holder) frog).grout$flyOnTongueAt();
        if (frog.getPose() != Pose.USING_TONGUE || since < 0F || since >= OVER_AT_TICK)
            return;
        frog.getTongueTarget().ifPresent(target -> {
            float pixels = (frog.distanceTo(target) - target.getBbWidth() / 2F) * 16F + VANILLA_TONGUE_Z;
            float longest = Math.clamp(pixels / TONGUE_LENGTH, SHORTEST, LONGEST);
            tongue.zScale = 1F + (tongue.zScale - 1F) * (longest - 1F) / (VANILLA_LONGEST - 1F);
        });
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffers, int light, Frog frog, float limbSwing, float limbSwingAmount, float partialTick, float age, float netHeadYaw, float headPitch) {
        float since = age - ((FrogSnack.Holder) frog).grout$flyOnTongueAt();
        if (frog.getPose() != Pose.USING_TONGUE || since < CAUGHT_AT_TICK || since >= OVER_AT_TICK)
            return;
        pose.pushPose();
        ModelPart root = this.getParentModel().root();
        float length;
        if (root.hasChild("body") && root.getChild("body").hasChild("tongue")) {
            ModelPart body = root.getChild("body");
            ModelPart tongue = body.getChild("tongue");
            root.translateAndRotate(pose);
            body.translateAndRotate(pose);
            pose.translate(tongue.x / 16F, tongue.y / 16F, tongue.z / 16F);
            pose.mulPose(new Quaternionf().rotationZYX(tongue.zRot, tongue.yRot, tongue.xRot));
            length = tongue.zScale;
        } else {
            pose.translate(0F, VANILLA_TONGUE_Y / 16F, VANILLA_TONGUE_Z / 16F);
            length = since < VANILLA_FULL_AT ? VANILLA_FULL_SCALE : Math.max(1F, VANILLA_FULL_SCALE - (since - VANILLA_FULL_AT) / (VANILLA_BACK_AT - VANILLA_FULL_AT) * (VANILLA_FULL_SCALE - 1F));
        }
        if (length > IN_MOUTH_SCALE) {
            pose.translate(0F, -FLY_MIDDLE * FLY_SCALE / 16F, -TONGUE_LENGTH * length / 16F);
            pose.scale(FLY_SCALE, FLY_SCALE, FLY_SCALE);
            this.fly.setupAnim(null, 0F, 0F, age, 0F, 0F);
            this.fly.renderToBuffer(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(FLY_TEXTURE)), light, OverlayTexture.NO_OVERLAY, -1);
        }
        pose.popPose();
    }
}
