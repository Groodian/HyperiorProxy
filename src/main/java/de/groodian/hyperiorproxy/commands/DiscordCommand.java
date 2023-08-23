package de.groodian.hyperiorproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import de.groodian.hyperiorcore.command.HCommandVelocity;
import de.groodian.hyperiorproxy.main.Main;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class DiscordCommand extends HCommandVelocity<CommandSource> {

    public DiscordCommand() {
        super(CommandSource.class, "discord", "Link for the discord server", Main.PREFIX_COMPONENT, null, List.of(), List.of());
    }

    @Override
    protected void onCall(CommandSource source, String[] args) {
        TextComponent.Builder builder = LegacyComponentSerializer.legacySection()
                .deserialize("§7[§bHyperior.de§7] §6Link zum Discord Server: §a§lKLICK")
                .toBuilder();
        builder.hoverEvent(HoverEvent.showText(Component.text("Link zum Discord Server", NamedTextColor.GOLD)));
        builder.clickEvent(ClickEvent.openUrl("https://discord.gg/5sGAp8wXQt"));
        source.sendMessage(builder.build());
    }

}
