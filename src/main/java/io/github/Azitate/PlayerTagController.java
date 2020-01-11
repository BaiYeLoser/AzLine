package io.github.Azitate;

import me.clip.placeholderapi.PlaceholderAPI;
import net.iso2013.mlapi.api.tag.TagController;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PlayerTagController implements TagController {
    private final JavaPlugin parent;
    private ConfigurationSection configurationSection;

    PlayerTagController(JavaPlugin parent) {
        this.parent = parent;
    }

    public void setConfigurationSection(ConfigurationSection section) {
        this.configurationSection = section;
    }

    @Override
    public List<TagLine> getFor(Entity target) {
        List<String> tags = configurationSection.getStringList("tags");
        List<TagLine> tagLines = new ArrayList<>();

        for (String tag : tags) {
            tagLines.add(new PlayerTagLine(tag));
        }
        return tagLines;
    }

    @Override
    public String getName(Entity target, Player viewer, String previous) {
        if (!(target instanceof Player)) {
            return previous;
        }
        String replaced = configurationSection.getString("name").replace("<name>", previous);
        return PlaceholderAPI.setPlaceholders((Player)target, replaced);
    }

    @Override
    public EntityType[] getAutoApplyFor() {
        return new EntityType[]{
                EntityType.PLAYER
        };
    }

    @Override
    public JavaPlugin getPlugin() {
        return parent;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int getNamePriority() {
        return 0;
    }
}