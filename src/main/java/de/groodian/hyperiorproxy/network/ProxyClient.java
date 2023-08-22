package de.groodian.hyperiorproxy.network;

import com.velocitypowered.api.proxy.server.ServerInfo;
import de.groodian.hyperiorproxy.main.Main;
import de.groodian.network.Client;
import de.groodian.network.DataPackage;
import java.net.InetSocketAddress;

public class ProxyClient extends Client {

    private final Main plugin;

    public ProxyClient(Main plugin, String hostname, int port, DataPackage loginPack) {
        super(hostname, port, loginPack);
        this.plugin = plugin;
    }

    @Override
    protected void handleDataPackage(DataPackage dataPackage) {
        String header = dataPackage.get(0).toString();
        String id = dataPackage.get(1).toString() + "-" + dataPackage.get(2);
        if (header.equalsIgnoreCase("CONNECTED")) {
            InetSocketAddress address = new InetSocketAddress(dataPackage.get(3).toString(), (int) dataPackage.get(4));
            plugin.getServer().registerServer(new ServerInfo(id, address));
        } else if (header.equalsIgnoreCase("DISCONNECTED")) {
            plugin.getServer()
                    .getServer(id)
                    .ifPresent((registeredServer) -> plugin.getServer().unregisterServer(registeredServer.getServerInfo()));
        } else {
            System.out.println("[Client] Unknown header: " + header);
        }
    }

    @Override
    protected void onSuccessfulLogin() {
    }

}
