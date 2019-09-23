package com.seanlavoie.glidetutorial;

import org.bukkit.plugin.java.JavaPlugin;

public class GlideTutorial extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("glidetutorial").setExecutor(new TutorialCommand(this));
        getServer().getPluginManager().registerEvents(new ArrowShoot(), this);
    }
}
