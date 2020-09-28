package de.groodian.network.minecraftparty;

import de.groodian.network.Server;
import de.groodian.network.ServerConnection;
import de.groodian.network.ServerStarter;

public class MinecraftPartyServerStarter extends ServerStarter {

	private static final int MAX_SERVERS = 5;
	private static final int MAX_SERVERS_IN_LOBBY_STATE = 1;

	public MinecraftPartyServerStarter(Server server, String group, int firstPort) {
		super(server, group, firstPort);
	}

	@Override
	protected void start() {
		for (int i = 0; i < MAX_SERVERS_IN_LOBBY_STATE; i++) {
			startNewServer();
		}
	}

	@Override
	protected boolean startCondition() {
		boolean start = false;
		int serversInLobbyState = 0;
		int serversInPlayingState = 0;

		for (ServerConnection serverConnection : server.getConnections()) {
			if (serverConnection instanceof MinecraftPartyServerConnection) {
				MinecraftPartyServerConnection minecraftPartyServerConnection = (MinecraftPartyServerConnection) serverConnection;
				if (minecraftPartyServerConnection.getGameState().equalsIgnoreCase("Lobby")) {
					serversInLobbyState++;
				} else {
					serversInPlayingState++;
				}
			}
		}

		if (serversInLobbyState < MAX_SERVERS_IN_LOBBY_STATE && ((serversInLobbyState + serversInPlayingState) < MAX_SERVERS)) {

			start = true;

		}

		return start;

	}

}
