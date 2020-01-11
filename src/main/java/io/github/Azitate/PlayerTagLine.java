package io.github.Azitate;

import me.clip.placeholderapi.PlaceholderAPI;
import net.iso2013.mlapi.api.tag.TagController;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PlayerTagLine implements TagController.TagLine {

    private String pattern;

    PlayerTagLine(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getText(Entity target, Player viewer) {
        if (!(target instanceof Player)) {
            return "";
        }
        return PlaceholderAPI.setPlaceholders((Player)target, pattern);
    }

    @Override
    public boolean keepSpaceWhenNull(Entity target) {
        return false;
    }
}