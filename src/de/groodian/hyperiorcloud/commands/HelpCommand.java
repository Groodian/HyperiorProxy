package de.groodian.hyperiorcloud.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class HelpCommand extends Command {

	public HelpCommand() {
		super("help", null, "hilfe");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage("�7[�bHyperior.de�7] �6Informationen zum Hyperior-Netzwerk:\n�e/lobby �7Kehre zur Hauptlobby zur�ck\n�e/party �7Spiele mit Freunden in einer Party\n�e/friend �7Verwalte deine Freunde\n�e/report �7Melde einen Spieler\n�e/shop �7Informationen �ber R�nge und mehr\nF�r weitere Informationen kannst du dich an das Server-Team wenden.");
	}
}
