package de.groodian.network.minecraftparty;

import java.net.Socket;

import de.groodian.network.Server;
import de.groodian.network.ServerConnection;

public class MinecraftPartyServerConnection extends ServerConnection {

	private String gameState;
	private int onlinePlayers;
	private int maxPlayers;

	public MinecraftPartyServerConnection(Server server, String group, int groupNumber, Socket socket) {
		super(server, group, groupNumber, socket);
	}

	@Override
	protected void handleDatapackage(Datapackage datapackage) {
		String header = datapackage.get(0).toString();
		if (header.equalsIgnoreCase("SERVER_INFO")) {
			gameState = datapackage.get(1).toString();
			onlinePlayers = (int) datapackage.get(2);
			maxPlayers = (int) datapackage.get(3);

			server.broadcastToGroup("Lobby", new Datapackage("MINECRAFTPARTY_SERVER_INFO", gameState, onlinePlayers, maxPlayers));
			server.getMinecraftPartyServerStarter().check();
		} else {
			System.err.println("[" + getId() + "] Unknowen header: " + header);
		}
	}

	public String getGameState() {
		return gameState;
	}

	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

}
