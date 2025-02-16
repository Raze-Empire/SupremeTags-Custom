package net.noscape.project.supremetags.listeners;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.noscape.project.supremetags.*;
import net.noscape.project.supremetags.checkers.UpdateChecker;
import net.noscape.project.supremetags.handlers.*;
import net.noscape.project.supremetags.storage.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import java.util.*;

import static net.kyori.adventure.translation.GlobalTranslator.renderer;
import static net.noscape.project.supremetags.utils.Utils.*;

public class PlayerEvents implements Listener {

    private final Map<String, Tag> tags;

    public PlayerEvents() {
        tags = SupremeTags.getInstance().getTagManager().getTags();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UserData.createPlayer(player);

        if (SupremeTags.getInstance().getConfig().getBoolean("settings.forced-tag")) {
            String activeTag = UserData.getActive(player.getUniqueId());
            if (activeTag.equalsIgnoreCase("None")) {
                String defaultTag = SupremeTags.getInstance().getConfig().getString("settings.default-tag");
                UserData.setActive(player, defaultTag);
            }
        }

        if (!UserData.getActive(player.getUniqueId()).equalsIgnoreCase("None") && !tags.containsKey(UserData.getActive(player.getUniqueId()))) {
            String defaultTag = SupremeTags.getInstance().getConfig().getString("settings.default-tag");
            UserData.setActive(player, defaultTag);
        }

        if (!UserData.getActive(player.getUniqueId()).equalsIgnoreCase("None") && tags.containsKey(UserData.getActive(player.getUniqueId()))) {
            Tag tag = SupremeTags.getInstance().getTagManager().getTag(UserData.getActive(player.getUniqueId()));
            if (!player.hasPermission(tag.getPermission())) {
                String defaultTag = SupremeTags.getInstance().getConfig().getString("settings.default-tag");
                UserData.setActive(player, defaultTag);
            }
        }

        if (SupremeTags.getInstance().getConfig().getBoolean("settings.update-check")) {
            if (player.isOp()) {
                new UpdateChecker(SupremeTags.getInstance()).getVersionAsync(version -> {
                    if (!SupremeTags.getInstance().getDescription().getVersion().equals(version)) {
                        msgPlayer(player, "&6&lSupremeTags &8&l> &7An update is available! &b" + version,
                                "&eDownload at &bhttps://www.spigotmc.org/resources/103140/");
                    }
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncChatEvent e) {
        Player player = e.getPlayer();
        ChatRenderer renderer = e.renderer();

        String tagFormat;

        if (!UserData.getActive(player.getUniqueId()).equalsIgnoreCase("None") && tags.containsKey(UserData.getActive(player.getUniqueId()))) {
            Tag tag = SupremeTags.getInstance().getTagManager().getTag(UserData.getActive(player.getUniqueId()));
            if (!player.hasPermission(tag.getPermission())) {
                String defaultTag = SupremeTags.getInstance().getConfig().getString("settings.default-tag");
                UserData.setActive(player, defaultTag);
            }
        }

        // Store the value of UserData.getActive(player.getUniqueId()) in a local variable
        String activeTag = UserData.getActive(player.getUniqueId());
        if (activeTag == null || activeTag.equalsIgnoreCase("None")) {
            tagFormat = "";
        } else {
            // Store the value of SupremeTags.getInstance().getTagManager().getTags().get(activeTag) in a local variable
            Tag tag = SupremeTags.getInstance().getTagManager().getTags().get(activeTag);
            if (tag == null) {
                tagFormat = "";
            } else {
                // Store the value of format(tag.getTag()) in a local variable
                String formattedTag = format(tag.getTag().replace("$", "$")); // Escaping $
                formattedTag = replacePlaceholders(player, formattedTag);

                tagFormat = formattedTag;
            }
        }

        e.renderer((source, displayname, message, viewer) -> {
            Component component = renderer.render(source, displayname, message, viewer);

            return component.replaceText(TextReplacementConfig.builder().match("\\{(?:TAG|tag|supremetags_tag)\\}").replacement(tagFormat).build());
        });
    }

    public Map<String, Tag> getTags() {
        return tags;
    }
}