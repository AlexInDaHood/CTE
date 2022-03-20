package com.osterph.dev;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class StaffManager {

    private Player p;
    private String UUID;

    public StaffManager(Player p) {
        this.p = p;
        this.UUID = p.getUniqueId().toString();
    }

    public boolean isDev() {
        return UUID.equals("c6f05a98-7d79-4125-91d6-27de7847de01") || UUID.equals("55dba0f2-7e78-4d9c-b967-2b3779d1ffed");
    }

    public boolean isHelper() {
        if (isDev()) return true;
        				//KartoffelPanzxr | Map Builder                            Koshix | Map Builder										//Jerry_2002 | Bugfinder, Ideen						//Meister_225 | Thomas
        return UUID.equals("470f8310-3eed-4c6b-a730-52e0fe3579f3") || UUID.equals("08bbb829-5667-4bee-9e79-8fbdaef9d68c") || UUID.equals("6e7f94c1-1f49-4b69-835f-56ff23160d43")|| UUID.equals("f9583df4-5a10-456b-89a5-136315700971");
    }

    public boolean hasRoles() {
        return isDev() || isHelper() || p.isOp();
    }

    public String activeString() {
        if (isDev()) return devString();
        if (isHelper()) return helperString();
        if (p.isOp()) return staffString();
        return "";
    }

    public String devString() {
        return "§c§l✫";
    }

    public String helperString() {
        return "§d§l✫";
    }

    public String staffString() {
        return "§4§l✫";
    }

    public TextComponent activeTag() {
        if (isDev()) return devTag();
        if (isHelper()) return helperTag();
        if (p.isOp()) return staffTag();
        return new TextComponent("");
    }

    public TextComponent devTag() {
        TextComponent t = new TextComponent("§8["+devString()+"§8] ");
        HoverEvent e = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§cEvent Management"));
        t.setHoverEvent(e);

        return t;
    }

    public TextComponent helperTag() {
        TextComponent t = new TextComponent("§8["+helperString()+"§8] ");
        HoverEvent e = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§dEvent Helfer"));
        t.setHoverEvent(e);

        return t;
    }

    public TextComponent staffTag() {
        TextComponent t = new TextComponent("§8["+staffString()+"§8] ");
        HoverEvent e = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§4Server Leitung"));
        t.setHoverEvent(e);

        return t;
    }
}
