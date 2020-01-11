package io.github.Azitate;

import io.lumine.xikage.mythicmobs.MythicMobs;
import net.iso2013.mlapi.api.MultiLineAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class AzLine extends JavaPlugin {
    private MultiLineAPI multiLineAPI;
    private MythicMobs mythicMobs;

    private boolean mmEnable = false;
    private boolean npcEnable = false;

    private PlayerTagController playerTagController;
    private MythicMobsTagController mythicMobsTagController;
    private CitizensTagController citizensTagController;

    private int playerUpdate;
    private BukkitTask playerTask;

    private int mmUpdate;
    private BukkitTask mmTask;

    private int npcUpdate;
    private BukkitTask npcTask;

    @Override
    public void onEnable() {
        // Plugin startup logic
        multiLineAPI = (MultiLineAPI) Bukkit.getPluginManager().getPlugin("MultiLineAPI");
        if (multiLineAPI == null) {
            getLogger().info("MultiLineAPI未载入!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("PlaceholderAPI未载入！");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            getLogger().info("MythicMobs已兼容！");
            mmEnable = true;
            mythicMobs = (MythicMobs) Bukkit.getPluginManager().getPlugin("MythicMobs");
        }

        // Not support yet
        if(false)
        if (Bukkit.getPluginManager().isPluginEnabled("Citizens")) {
            getLogger().info("Citizens已兼容！");
            npcEnable = true;
        }

        loadconfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            loadconfig();
            sender.sendMessage("重载完成！");
        }
        return true;
    }

    private void loadconfig() {
        saveDefaultConfig();
        reloadConfig();
        FileConfiguration config = getConfig();

        if (playerTagController == null) {
            playerTagController = new PlayerTagController(this);
            multiLineAPI.addDefaultTagController(playerTagController);
        }

        ConfigurationSection playerSection = config.getConfigurationSection("player");
        playerUpdate = playerSection.getInt("update", 1000);

        playerTagController.setConfigurationSection(playerSection);
        multiLineAPI.update(playerTagController);

        if(playerTask != null)
            playerTask.cancel();
        playerTask = Bukkit.getServer().getScheduler().runTaskTimer(this, () ->
                multiLineAPI.update(playerTagController), playerUpdate / 50L, playerUpdate / 50L);

        if (mmEnable) {
            if (mythicMobsTagController == null) {
                mythicMobsTagController = new MythicMobsTagController(this, mythicMobs.getAPIHelper());
                multiLineAPI.addDefaultTagController(mythicMobsTagController);
            }

            ConfigurationSection mmSection = config.getConfigurationSection("mythic-mobs");
            mmUpdate = mmSection.getInt("update", 1000);

            mythicMobsTagController.setConfigurationSection(mmSection);
            multiLineAPI.update(mythicMobsTagController);

            if(mmTask != null)
                mmTask.cancel();
            mmTask = Bukkit.getServer().getScheduler().runTaskTimer(this, () ->
                    multiLineAPI.update(mythicMobsTagController), mmUpdate / 50L, mmUpdate / 50L);
        }

        if (npcEnable) {
            if (citizensTagController == null) {
                citizensTagController = new CitizensTagController(this);
                multiLineAPI.addDefaultTagController(citizensTagController);
            }

            ConfigurationSection npcSection = config.getConfigurationSection("citizens");
            npcUpdate = npcSection.getInt("update", 1000);

            citizensTagController.setConfigurationSection(npcSection);
            multiLineAPI.update(citizensTagController);

            if(npcTask != null)
                npcTask.cancel();
            npcTask = Bukkit.getServer().getScheduler().runTaskTimer(this, () ->
                    multiLineAPI.update(citizensTagController), npcUpdate / 50L, npcUpdate / 50L);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
