package com.seanlavoie.glidetutorial;

import net.minecraft.server.v1_13_R2.EntityBat;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

public class MountEntity extends EntityBat {

    private final List<Vector> path;
    private int pathIndex = 0;
    private final double speed;

    public MountEntity(World world, Location spawnpoint, List<Vector> path, double speed) { //Create mount entity
        super(world);
        this.setLocation(spawnpoint.getX(), spawnpoint.getY(), spawnpoint.getZ(), spawnpoint.getYaw(), spawnpoint.getPitch());
        this.setAsleep(false);
        this.setSilent(true);
        this.setNoGravity(true);
        this.path = path;
        this.speed = speed;
//        getBukkitEntity().teleport(getBukkitEntity().getLocation().setDirection(getDirToTarget(path.get(0)))); //Make entity look at new target.
    }

    @Override
    protected void mobTick() //Originally generates random movement, now handles movement.
    {
        Vector target = path.get(pathIndex); //Get target
        Vector dirToTar = getDirToTarget(target);
        if(!isNaN(dirToTar))
        {
            Vector entityLoc = getBukkitEntity().getLocation().toVector();
            if(isNearTarget(entityLoc, target)) //If this entity is at it's target location
            {
                if(pathIndex+1 == path.size()) //If this is the last point
                {
                    this.setNoAI(true); //Disable AI, also disables this method.
                    return;
                }
                pathIndex++;
                getBukkitEntity().teleport(getBukkitEntity().getLocation().setDirection(getDirToTarget(path.get(pathIndex)))); //Make entity look at new target.
                return;
            }
            Vector velocity = dirToTar.divide(new Vector(3, 1, 3)); //Default speed, a bit faster than a player
            velocity.multiply(speed); //Multiply by speed
            getBukkitEntity().setVelocity(velocity);
        }
    }

    private Vector getDirToTarget(Vector target)
    {
        return target.clone().subtract(getBukkitEntity().getLocation().toVector()).normalize();
    }

    private boolean isNaN(Vector vector)
    {
        return (Double.isNaN(vector.getX()) || Double.isNaN(vector.getY()) || Double.isNaN(vector.getZ()));
    }

    private boolean isNearTarget(Vector vector, Vector target)
    {
        double speedDiff = speed/5; //Very fast taxis (<20 speed) aren't precise and may even skip points if they're near each other, but they will not get stuck.
        return (vector.getX() < target.getX() + speedDiff) && (vector.getX() > target.getX() - speedDiff) && (vector.getY() < target.getY() + speedDiff) && (vector.getY() > target.getY() - speedDiff) && (vector.getZ() < target.getZ() + speedDiff) && (vector.getZ() > target.getZ() - speedDiff);
    }
}