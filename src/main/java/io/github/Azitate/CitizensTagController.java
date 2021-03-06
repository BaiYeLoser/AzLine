package io.github.Azitate;

import me.clip.placeholderapi.PlaceholderAPI;
import net.citizensnpcs.api.CitizensAPI;
import net.iso2013.mlapi.api.tag.TagController;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CitizensTagController implements TagController, TagLineDisplayHandler {
    private final JavaPlugin parent;
    private ConfigurationSection configurationSection;

    CitizensTagController(JavaPlugin parent) {
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
            tagLines.add(new OthersTagLine(tag, this));
        }
        return tagLines;
    }

    @Override
    public String getName(Entity target, Player viewer, String previous) {
        if (!shouldShow(target)) {
            return previous;
        }

        if (previous == null) {
            previous = target.getName();
        }
        String replaced = configurationSection.getString("name").replace("<name>", previous);
        return PlaceholderAPI.setPlaceholders(viewer, replaced);
    }

    @Override
    public EntityType[] getAutoApplyFor() {
        return EntityType.values();
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

    @Override
    public boolean shouldShow(Entity target) {
        return CitizensAPI.getNPCRegistry().getByUniqueIdGlobal(target.getUniqueId()) != null;
    }
}
