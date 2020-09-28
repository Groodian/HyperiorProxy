package de.groodian.hyperiorcloud.commands;

import java.util.concurrent.TimeUnit;

import de.groodian.hyperiorcloud.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

public class ShopCommand extends Command {

	private Main plugin;
	private long delay = 900;

	public ShopCommand(Main plugin) {
		super("shop");
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage("�7[�bHyperior.de�7] �6Informationen zum �cGr�nder-Rang�6:");
		ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
			@Override
			public void run() {
				sender.sendMessage("�8� �7Der �cGr�nder-Rang �7ist �6Lifetime �7nur w�hrend der �cBeta-Phase �7erh�ltlich.");
				ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
					@Override
					public void run() {
						sender.sendMessage("�8� �7Du kannst �avolle Server Joinen �7und damit bevorzugt spielen.");
						ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
							@Override
							public void run() {
								sender.sendMessage("�8� �7Dein Name hat auf dem Server eine �cRote Farbe�7.");
								ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
									@Override
									public void run() {
										sender.sendMessage("�8� �7Du hast auf dem Server einen �cexklusiven �Gr�nder� Pr�fix�7 vor deinem Namen.");
										ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
											@Override
											public void run() {
												sender.sendMessage("�8� �7Du kannst dir bei der �T�glichen Belohnung� die �edreifache Anzahl an �6Coins �7abhohlen.");
												ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
													@Override
													public void run() {
														sender.sendMessage("�8� �7Du erh�lst am Ende einer Runde �edoppelte �6Coins�7.");
														ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
															@Override
															public void run() {
																sender.sendMessage("�8� �7Du kannst eine Runde mit /start �adirekt Starten�7.");
																ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
																	@Override
																	public void run() {
																		sender.sendMessage("�8� �7Du kannst dir �aalle Gegenst�nde kaufen�7 so brauchst du nicht das Gl�ck diese ein einer Kiste zu bekommen.");
																		ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
																			@Override
																			public void run() {
																				sender.sendMessage("�8� �aMit dem Kauf unterst�tzt du den Server, und tr�gst bei das neue Updates kommen und der Server weiterhin Online bleibt.");
																				ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
																					@Override
																					public void run() {
																						sender.sendMessage("�8� �7F�r weitere Informationen kannst du dich an das Server-Team wenden.");
																					}
																				}, delay, TimeUnit.MILLISECONDS);
																			}
																		}, delay, TimeUnit.MILLISECONDS);
																	}
																}, delay, TimeUnit.MILLISECONDS);
															}
														}, delay, TimeUnit.MILLISECONDS);
													}
												}, delay, TimeUnit.MILLISECONDS);
											}
										}, delay, TimeUnit.MILLISECONDS);
									}
								}, delay, TimeUnit.MILLISECONDS);
							}
						}, delay, TimeUnit.MILLISECONDS);
					}
				}, delay, TimeUnit.MILLISECONDS);
			}
		}, delay, TimeUnit.MILLISECONDS);
	}
}
