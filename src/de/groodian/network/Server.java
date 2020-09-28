package de.groodian.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import de.groodian.network.minecraftparty.MinecraftPartyServerConnection;
import de.groodian.network.minecraftparty.MinecraftPartyServerStarter;

public class Server {

	private Server server;

	private int port;
	private ServerSocket serverSocket;
	private Thread listeningThread;

	private List<ServerConnection> connections;

	private MinecraftPartyServerStarter minecraftPartyServerStarter;

	public Server(int port) {
		this.server = this;
		this.port = port;
		this.connections = new ArrayList<>();
		this.minecraftPartyServerStarter = new MinecraftPartyServerStarter(server, "MinecraftParty", 41000);
	}

	private void startListening() {
		listeningThread = new Thread(new Runnable() {
			@Override
			public void run() {

				while (!serverSocket.isClosed()) {

					try {
						System.out.println("[Server] Waiting for connection...");
						Socket tempSocket = serverSocket.accept();
						System.out.println("[Server] Connected to client: " + tempSocket.getRemoteSocketAddress());

						ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(tempSocket.getInputStream()));
						Object pack = ois.readObject();
						ois.close();

						if (pack instanceof Datapackage) {
							Datapackage datapackage = (Datapackage) pack;
							String header = datapackage.get(0).toString();
							if (header.equalsIgnoreCase("LOGIN")) {
								String group = datapackage.get(1).toString();
								if (group.equalsIgnoreCase("MinecraftParty")) {
									MinecraftPartyServerConnection minecraftPartyServerConnection = new MinecraftPartyServerConnection(server, group, (int) datapackage.get(2), tempSocket);
									connections.add(minecraftPartyServerConnection);
									System.out.println("[Server] Server: " + minecraftPartyServerConnection.getId() + " logged in.");
								} else if (group.equalsIgnoreCase("Lobby")) {

								} else {
									System.err.println("[Server] Unknowen group: " + group);
									tempSocket.close();
								}
							} else {
								System.err.println("[Server] Unknowen header: " + header);
								tempSocket.close();
							}
						} else {
							System.err.println("[Server] Unknowen pack: " + pack);
							tempSocket.close();
						}

					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					}

				}

			}
		});

		listeningThread.start();
	}

	public void start() {
		System.out.println("[Server] Server starting...");
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("[Server] Could not start the Server. Is the port free?");
		}
		System.out.println("[Server] Server started.");
		startListening();
	}

	public void stop() {
		System.out.println("[Server] Server stopping...");
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		listeningThread.interrupt();
		System.out.println("[Server] Server stopped.");
	}

	public void broadcast(Datapackage pack) {
		new Thread(new Runnable() {
			@Override
			public void run() {

				for (ServerConnection serverConnection : connections) {
					sendMessage(serverConnection, pack);
				}

			}
		}).start();
	}

	public void broadcastToGroup(String group, Datapackage pack) {
		new Thread(new Runnable() {
			@Override
			public void run() {

				for (ServerConnection serverConnection : connections) {
					if (serverConnection.getGroup().equalsIgnoreCase(group)) {
						sendMessage(serverConnection, pack);
					}
				}

			}
		}).start();
	}

	public void sendTo(ServerConnection serverConnection, Datapackage pack) {
		new Thread(new Runnable() {
			@Override
			public void run() {

				sendMessage(serverConnection, pack);

			}
		}).start();
	}

	private void sendMessage(ServerConnection serverConnection, Datapackage pack) {
		try {
			serverConnection.getOos().writeObject(pack);
			serverConnection.getOos().flush();
		} catch (SocketException e) {
			System.err.println("[Server] The server " + serverConnection.getId() + " is unreachable!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<ServerConnection> getConnections() {
		return connections;
	}

	public MinecraftPartyServerStarter getMinecraftPartyServerStarter() {
		return minecraftPartyServerStarter;
	}

}