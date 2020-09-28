package de.groodian.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public abstract class ServerConnection {

	private ServerConnection serverConnection;

	protected Server server;

	private String group;
	private int groupNumber;
	private Socket socket;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private ServerInfo serverInfo;

	private String state;
	private int onlinePlayers;
	private int maxPlayers;

	public ServerConnection(Server server, String group, int groupNumber, Socket socket) {
		this.serverConnection = this;
		this.server = server;
		this.group = group;
		this.groupNumber = groupNumber;
		this.socket = socket;

		serverInfo = ProxyServer.getInstance().constructServerInfo(getId(), InetSocketAddress.createUnresolved("localhost", socket.getPort()), getId(), false);
		ProxyServer.getInstance().getServers().put(getId(), serverInfo);

		try {
			ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		startListening();
	}

	protected abstract void handleDatapackage(Datapackage datapackage);

	private void startListening() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {

					try {

						Object pack = ois.readObject();
						if (pack instanceof Datapackage) {
							handleDatapackage((Datapackage) pack);
						} else {
							System.err.println("[" + getId() + "] Unknowen pack: " + pack);
						}

					} catch (ClassNotFoundException | IOException e) {
						System.err.println("[" + getId() + "] The server is unreachable, logging out server...");
						ProxyServer.getInstance().getServers().remove(getId());
						server.getConnections().remove(serverConnection);
						break;
					}
				}
			}
		}).start();
	}

	public String getGroup() {
		return group;
	}

	public int getGroupNumber() {
		return groupNumber;
	}

	public Socket getSocket() {
		return socket;
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public String getId() {
		return group + "-" + groupNumber;
	}

	public String getState() {
		return state;
	}

	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

}
