package application;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Singleton {
    private static Singleton instance;
    private List<DatagramPacket> users;

    private Singleton() {
        users = Collections.synchronizedList(new ArrayList<>());
    }

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public List<DatagramPacket> getUsers() {
        return users;
    }

    public void addUser(DatagramPacket packet) {
        users.add(packet);
    }

    public void removeUser(DatagramPacket packet) {
        users.remove(packet);
    }
}