package com.hipstersavage.hsn.mixin;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import java.util.logging.Logger;

@Mixin(AbstractSkeletonEntity.class)
public abstract class SkeletonEntityMixin {
    private static final Logger LOGGER = Logger.getLogger("hsn");

    @ModifyArg(method = "attack", at = @At(value="INVOKE", target="Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private Entity NerfSkeletonAttack(Entity projectile) {

        // 45% chance to nerf the skeleton's arrow velocity
        if (Math.random() > 0.5) {
            LOGGER.info("Skeleton arrow velocity not nerfed!");
            return projectile;
        }

        // Modify the projectile's trajectory
        Vec3d projectileVelocity = projectile.getVelocity();
        // Make random offset
        double offset = Math.random() * (0.2 - 0.07) + 0.07;
        // Randomly reverse the offset
        if (Math.random() < 0.5) {
            offset = offset * -1;
        }

        // Set the new velocity
        projectile.setVelocity(projectileVelocity.x - offset, projectileVelocity.y, projectileVelocity.z - offset);

        LOGGER.info("Skeleton arrow velocity nerfed! Velocity is now " + projectile.getVelocity().toString() + " with offset " + offset + "!");

        // Get world
        World world = projectile.getEntityWorld();
        // Say offset in chat
        world.getServer().getPlayerManager().broadcast(Text.of("Offset " + offset + "!"), false);

        // Return the modified projectile
        return projectile;
    }

    @Inject(method = "attack", at = @At("RETURN"))
    private void NerfSkeletonAttack(LivingEntity target, float pullProgress, CallbackInfo ci) throws InterruptedException {
        LOGGER.info("Skeleton attack nerfed!");

        // Get the skeleton entity
        SkeletonEntity skeleton = (SkeletonEntity) (Object) this;

        // Get the world
        World world = skeleton.getEntityWorld();

        // Check if world is on hard difficulty
        if (world.getDifficulty() != Difficulty.HARD) {
            LOGGER.info("World is not on hard difficulty!");
            return;
        }

        // Check if skeleton is holding a bow
        if (skeleton.getMainHandStack().getItem().toString().equals("bow")) {
            LOGGER.info("Skeleton is holding a bow!");

            // Adda a delay before the skeleton attacks
            long delay = (long) ((Math.random() * (1.5 - 1) + 1) * 1000);
            Thread.sleep(delay);

            LOGGER.info("Skeleton attack delayed by " + delay + " milliseconds!");

        } else {
            LOGGER.info("Skeleton is not holding a bow! " + skeleton.getMainHandStack().getItem().getName().getString());
        }
    }

}
