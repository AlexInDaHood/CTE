package com.osterph.manager;

import com.osterph.cte.CTE;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageManager {

    private String Message;
    private String[] HoverText;
    private String suggestCommand;
    private String forceCommand;
    private String title;
    private boolean sendHoverPrefix;
    private boolean sendPrefix;

    public MessageManager(String Message) {
        this.Message = Message;
        HoverText = null;
        title = null;
        suggestCommand = null;
        forceCommand = null;
        sendPrefix = true;
        sendHoverPrefix = true;
    }

    public MessageManager hidePrefix() {
        sendPrefix = false;
        return this;
    }

    public MessageManager setHoverTitle(String title) {
        this.title = title;
        return this;
    }

    public MessageManager setHover(String... text) {
        this.HoverText = text;
        return this;
    }

    public MessageManager hideHoverPrefix() {
        this.sendHoverPrefix = false;
        return this;
    }

    public MessageManager suggestCommand(String command) {
        this.suggestCommand = command;
        return this;
    }

    public MessageManager forceCommand(String command) {
        this.forceCommand = command;
        return this;
    }

    public void complete(Player p) {
        TextComponent t = new TextComponent();
        String msg = this.Message;
        if (sendPrefix) msg = CTE.prefix + msg;
        t.setText(msg);

        if (HoverText != null) {
            String hover = "";
            if (this.sendHoverPrefix) {
                if (this.title == null) {
                    hover = CTE.prefix + "§eSyntax\n";
                } else {
                    hover = CTE.prefix + "§e"+this.title+"\n";
                }
            }
            for (String ht: HoverText) {
                if (HoverText[HoverText.length-1].equals(ht)) {
                    hover += "§7"+ht;
                    continue;
                }
                hover += "§7"+ht+"\n";
            }
            t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(hover)));
        }
        if (suggestCommand != null) t.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggestCommand));
        if (forceCommand != null) t.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, forceCommand));

        p.spigot().sendMessage(t);
    }

    public void complete(CommandSender s) {
        String msg = this.Message;
        if (sendPrefix) msg = CTE.prefix +msg;

        s.sendMessage(msg);
    }
}
