package de.groodian.network;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

public abstract class ServerStarter {

	protected Server server;
	private String group;
	private int firstPort;
	private OS os;

	private List<Integer> starting;
	private List<Integer> deleting;

	public ServerStarter(Server server, String group, int firstPort) {
		this.server = server;
		this.group = group;
		this.firstPort = firstPort;
		this.os = getOS();

		starting = new ArrayList<>();
		deleting = new ArrayList<>();

		File temp = new File("../temp");
		if (!temp.exists()) {
			temp.mkdir();
		}

		File logs = new File("../logs/" + group);
		if (!logs.exists()) {
			logs.mkdir();
		}

		start();

	}

	protected abstract void start();

	protected abstract boolean startCondition();

	public void check() {

		if (startCondition()) {
			startNewServer();
		}

	}

	public void startNewServer() {

		BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("§a[ServerStarter] Starting new  " + group + " server..."));
		
		List<Integer> serverNumbers = new ArrayList<>();

		for (ServerConnection serverConnection : server.getConnections()) {
			if (serverConnection.getGroup().equalsIgnoreCase(group)) {
				serverNumbers.add(serverConnection.getGroupNumber());
			}
		}

		for (Integer integer : starting) {
			serverNumbers.add(integer);
		}

		for (Integer integer : deleting) {
			serverNumbers.add(integer);
		}

		for (int count = 1; count <= serverNumbers.size() + 1; count++) {
			if (!serverNumbers.contains(count)) {
				starting.add(count);
				createServer(count);
				break;
			}
		}

		serverNumbers.clear();

	}
	
	// Probleme: wenn server gestatet wird muss geschaut werden wann und wie er aus der starte oliste entfert wird
	//           wenn server verbindung veriehrt dann lobby melden

	private void createServer(int number) {
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {

					String id = group + "-" + number;
					int port = firstPort + number;
					long startTime = System.currentTimeMillis();

					BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("§a[ServerStarter] [" + id + "] Copying files... "));

					copyFolder(new File("../" + group), new File("../temp/" + id));

					BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("§a[ServerStarter] [" + id + "] Setting port and server number... "));

					File server_number_file = new File("../temp/" + id + "/plugins/" + group + "_by_Groodian/servernumber.yml");
					String server_number_yml = new String(Files.readAllBytes(server_number_file.toPath()));
					String server_number_flag = "server-number: ";
					int server_number_pos = server_number_yml.indexOf(server_number_flag) + server_number_flag.length();
					server_number_yml = server_number_yml.substring(0, server_number_pos) + number + server_number_yml.substring(server_number_pos + 1);
					PrintWriter server_number_writer = new PrintWriter(server_number_file);
					server_number_writer.write(server_number_yml);
					server_number_writer.close();

					File server_properties_file = new File("../temp/" + id + "/server.properties");
					String server_properties = new String(Files.readAllBytes(server_properties_file.toPath()));
					String server_properties_flag = "server-port=";
					int server_properties_pos = server_properties.indexOf(server_properties_flag) + server_properties_flag.length();
					server_properties = server_properties.substring(0, server_properties_pos) + port + server_properties.substring(server_properties_pos + 5);
					PrintWriter server_properties_writer = new PrintWriter(server_properties_file);
					server_properties_writer.write(server_properties);
					server_properties_writer.close();

					BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("§a[ServerStarter] [" + id + "] Finished (" + (System.currentTimeMillis() - startTime) + "ms)"));

					if (os == OS.WINDOWS) {
						BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("§a[ServerStarter] [" + id + "] Starting... (using start.bat)"));
						Runtime.getRuntime().exec("cmd /c start start.bat", null, new File("../temp/" + id));
					} else if (os == OS.LINUX) {
						BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("§a[ServerStarter] [" + id + "] Starting... (using start.sh)"));
						Runtime.getRuntime().exec("sh -c ./start.sh", null, new File("../temp/" + id));
					} else {
						BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("§a[ServerStarter] [" + id + "] Could not start the server because the OS is unknown!"));
					}

					ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
					exec.schedule(new Runnable() {
						@Override
						public void run() {
							
							removeServerFromStarting(number);
							
						}

					}, 60, TimeUnit.SECONDS);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}
	
	public void removeServerFromStarting(int number) {
		if(starting.contains(number)) {
			// Object cast to force right function
			starting.remove((Object) number);
		}
	}

	public void deleteServer(int number) {
		deleting.add(number);

		String id = group + "-" + number;

		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

		exec.schedule(new Runnable() {
			@Override
			public void run() {

				try {
					BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("§c[ServerStarter] Delete " + id + "..."));
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd---HH-mm-ss");
					LocalDateTime now = LocalDateTime.now();
					Files.copy(new File("../temp/" + id + "/logs/latest.log").toPath(), new File("../logs/" + group + "/" + dateTimeFormatter.format(now) + "---" + id + ".log").toPath(),
							StandardCopyOption.REPLACE_EXISTING);
					deleteDirectory(new File("../temp/" + id));
					ProxyServer.getInstance().getServers().remove(id);
				} catch (IOException e) {
					e.printStackTrace();
				}

				deleting.remove((Object) number);

			}

		}, 5, TimeUnit.SECONDS);

	}

	private void copyFolder(File sourceFolder, File destinationFolder) throws IOException {
		// Check if sourceFolder is a directory or file
		// If sourceFolder is file; then copy the file directly to new location
		if (sourceFolder.isDirectory()) {
			// Verify if destinationFolder is already present; If not then create it
			if (!destinationFolder.exists()) {
				destinationFolder.mkdir();
				// System.out.println("Directory created :: " + destinationFolder);
			}

			// Get all files from source directory
			String files[] = sourceFolder.list();

			// Iterate over all files and copy them to destinationFolder one by one
			for (String file : files) {
				File srcFile = new File(sourceFolder, file);
				File destFile = new File(destinationFolder, file);

				// Recursive function call
				copyFolder(srcFile, destFile);
			}
		} else {
			// Copy the file content from one place to another
			Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
			// System.out.println("File copied :: " + destinationFolder);
		}
	}

	private void deleteDirectory(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				deleteDirectory(file);
			}
		}
		directoryToBeDeleted.delete();
	}

	private OS getOS() {
		BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("§a[ServerStarter] Scanning OS..."));
		String osName = System.getProperty("os.name").toLowerCase();
		BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("§a[ServerStarter] OS Name: " + System.getProperty("os.name")));
		if (osName.contains("windows")) {
			BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("§a[ServerStarter] OS: Windows"));
			return OS.WINDOWS;
		} else if (osName.contains("linux")) {
			BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("§a[ServerStarter] OS: Linux"));
			return OS.LINUX;
		} else {
			BungeeCord.getInstance().getConsole().sendMessage(new TextComponent("§a[ServerStarter] OS: Unknown"));
			return OS.UNKNOWN;
		}
	}

}
