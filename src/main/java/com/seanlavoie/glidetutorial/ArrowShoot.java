package com.seanlavoie.glidetutorial;

import javafx.util.Pair;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.HashMap;

public class ArrowShoot implements Listener {

    private HashMap<Integer, Pair<Player, Location>> activeArrows = new HashMap<>();

    @EventHandler
    public void onPlayerShootBowEvent(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            player.setGravity(false);
            player.setGameMode(GameMode.SPECTATOR);
            event.getProjectile().addPassenger(player);
            activeArrows.put(event.getProjectile().getEntityId(), new Pair<>(player, player.getLocation()));
//            player.setSpectatorTarget(event.getProjectile());
        }
    }

    @EventHandler
    public void onArrowHitEvent(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            if (activeArrows.containsKey(event.getEntity().getEntityId())) {
                Pair<Player, Location> playerPrevLocationPair = activeArrows.get(event.getEntity().getEntityId());
                Player player = playerPrevLocationPair.getKey();
                player.setGameMode(GameMode.SURVIVAL);
                player.setGravity(true);
                player.teleport(playerPrevLocationPair.getValue());
            }
        }
    }
}
