package de.groodian.hyperiorproxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import de.groodian.hyperiorcore.command.HArgument;
import de.groodian.hyperiorcore.command.HCommandVelocity;
import de.groodian.hyperiorproxy.main.Main;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class SlotsCommand extends HCommandVelocity<CommandSource> {

    private final Main plugin;

    public SlotsCommand(Main plugin) {
        super(CommandSource.class, "slots", "Change the slots", Main.PREFIX_COMPONENT, "slots", List.of(), List.of(new HArgument("slots")));
        this.plugin = plugin;
    }

    @Override
    protected void onCall(CommandSource source, String[] args) {
        int slots;
        try {
            slots = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sendMsg(source, Component.text("The slots has to be a number.", NamedTextColor.RED));
            return;
        }

        if (slots < 0) {
            sendMsg(source, Component.text("The slots has to be a positive.", NamedTextColor.RED));
            return;
        }

        String sourceName;
        if (source instanceof Player sourcePlayer) {
            sourceName = sourcePlayer.getUsername();
        } else {
            sourceName = "Console";
        }

        plugin.setSlots(slots);
        sendMsg(source, Component.text("Successfully changed the slots.", NamedTextColor.GREEN));
        plugin.getTeam().notify("§6" + sourceName + "§a changed the slots to §6" + slots + "§a.");
    }

}
