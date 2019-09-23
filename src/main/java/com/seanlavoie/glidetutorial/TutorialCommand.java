package com.seanlavoie.glidetutorial;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class TutorialCommand implements CommandExecutor {

    final Plugin plugin;

    TutorialCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location curLocation = player.getLocation();
            Location desiredLocation = player.getLocation().add(10, 15, 10);

            ArrayList<Vector> path = new ArrayList<Vector>() {{
                add(desiredLocation.toVector());
                add(desiredLocation.toVector().add(new Vector(30, 2, -40)));
                add(desiredLocation.toVector().add(new Vector(-20, 2, 30)));
            }};

            player.setGameMode(GameMode.SPECTATOR);
            new BukkitRunnable() {
                @Override
                public void run() {
                    int pathIndex = 0;
                    double speed = 1.0;
                    System.out.println("Doing Thing...");
                    Vector target = path.get(pathIndex); //Get target
                    Vector dirToTar = getDirToTarget(player.getLocation().toVector(), target);
                    if(!isNaN(dirToTar))
                    {
                        Vector entityLoc = player.getLocation().toVector();
                        if(isNearTarget(entityLoc, target)) //If this entity is at it's target location
                        {
                            if(pathIndex+1 == path.size()) //If this is the last point
                            {
                                player.setGameMode(GameMode.SURVIVAL);
                                this.cancel();
                            }
                            pathIndex++;
//                            player.teleport(player.getLocation().setDirection(getDirToTarget(path.get(pathIndex)))); //Make entity look at new target.
                        }
                        Vector velocity = dirToTar.divide(new Vector(3, 1, 3)); //Default speed, a bit faster than a player
                        velocity.multiply(speed); //Multiply by speed
                        player.setVelocity(velocity);
                    }
                }

                private Vector getDirToTarget(Vector cur, Vector target)
                {
                    return target.clone().subtract(cur).normalize();
                }

                private boolean isNaN(Vector vector)
                {
                    return (Double.isNaN(vector.getX()) || Double.isNaN(vector.getY()) || Double.isNaN(vector.getZ()));
                }

                private boolean isNearTarget(Vector vector, Vector target)
                {
                    double speedDiff = 3;  // Very fast taxis (<20 speed) aren't precise and may even skip points if they're near each other, but they will not get stuck.
                    return (vector.getX() < target.getX() + speedDiff) && (vector.getX() > target.getX() - speedDiff) && (vector.getY() < target.getY() + speedDiff) && (vector.getY() > target.getY() - speedDiff) && (vector.getZ() < target.getZ() + speedDiff) && (vector.getZ() > target.getZ() - speedDiff);
                }
            }.runTaskTimer(plugin, 0L, 1L);

//            MountEntity mountEntity = new MountEntity(
//                    ((CraftWorld) curLocation.getWorld()).getHandle().getMinecraftWorld(),
//                    curLocation,
//                    new ArrayList<Vector>() {{
//                        add(desiredLocation.toVector());
//                    }},
//                    0.5D
//            );

//            Bat spectatedBat = (Bat) curLocation.getWorld().spawnEntity(curLocation, EntityType.BAT);
//            Bat targetBat = ((Bat) curLocation.getWorld().spawnEntity(desiredLocation, EntityType.BAT));
//
//            spectatedBat.setTarget(targetBat);
//
//            player.setGameMode(GameMode.SPECTATOR);
//            player.setSpectatorTarget(spectatedBat);
            sender.sendMessage("Yo hobo");


//            CraftBat spectatedBatEntity = ((CraftBat) curLocation.getWorld().spawnEntity(curLocation, EntityType.BAT));
//            CraftBat targetBatEntity = ((CraftBat) curLocation.getWorld().spawnEntity(desiredLocation, EntityType.BAT));

//            CraftLivingEntity targetBatLivingEntity = (CraftLivingEntity) targetBatEntity;
//            targetBatLivingEntity.set
//            spectatedBatEntity.setGoalTarget(targetBatLivingEntity);

//            PathEntity path = bat.getNavigation().a(curLocation.add(10, 0, 0).getX(), curLocation.add(0, 10, 0).getY(), curLocation.add(0, 0, 10).getZ());
//            bat.getNavigation().a(path, 1);
//            player.setVelocity(new Vector(curLocation.getX() + 1, curLocation.getY() + 1, curLocation.getZ()));
        } else {
            return false;
        }
        return true;
    }
}
