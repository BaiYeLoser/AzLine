package io.github.Azitate;

import me.clip.placeholderapi.PlaceholderAPI;
import net.iso2013.mlapi.api.tag.TagController;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class OthersTagLine implements TagController.TagLine {

    private String pattern;
    TagLineDisplayHandler handler;

    OthersTagLine(String pattern, TagLineDisplayHandler handler) {
        this.pattern = pattern;
        this.handler = handler;
    }

    @Override
    public String getText(Entity target, Player viewer) {
        if(!handler.shouldShow(target))
            return null;
        return PlaceholderAPI.setPlaceholders(viewer, pattern);
    }

    @Override
    public boolean keepSpaceWhenNull(Entity target) {
        return false;
    }
}