package de.groodian.hyperiorproxy.network;

import de.groodian.network.Client;
import de.groodian.network.DataPackage;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;

public class ProxyClient extends Client {

    public ProxyClient(String hostname, int port, DataPackage loginPack) {
        super(hostname, port, loginPack);
    }

    @Override
    protected void handleDataPackage(DataPackage dataPackage) {
        String header = dataPackage.get(0).toString();
        if (header.equalsIgnoreCase("CONNECTED")) {
            String id = dataPackage.get(1).toString() + "-" + dataPackage.get(2);
            InetSocketAddress address = new InetSocketAddress(dataPackage.get(3).toString(), (int) dataPackage.get(4));
            ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(id, address, "", false);
            ProxyServer.getInstance().getServers().put(id, serverInfo);
        } else if (header.equalsIgnoreCase("DISCONNECTED")) {
            String id = dataPackage.get(1).toString() + (int) dataPackage.get(2);
            if (ProxyServer.getInstance().getServers().containsKey(id)) {
                ProxyServer.getInstance().getServers().remove(id);
            }
        } else {
            System.out.println("[Client] Unknown header: " + header);
        }
    }

}
