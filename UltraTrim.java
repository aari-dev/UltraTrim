package dev.aari.ultratrim;

import org.bukkit.plugin.java.JavaPlugin;

public final class UltraTrim extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("trim").setExecutor(new TrimCommand());
    }
}
