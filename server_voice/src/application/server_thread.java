
package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;

import javax.sound.sampled.SourceDataLine;

public class server_thread extends Thread{
    public DatagramSocket socket;
    public SourceDataLine audio_out;
    byte buffer_receive[] = new byte[11000];
    boolean messageReceived = false;
    @Override
    public void run() {
        System.out.println("Nhan am thanh tu phia client");
        while (server_form.Calling) {
            try {
                DatagramPacket data = new DatagramPacket(buffer_receive, buffer_receive.length);
                socket.receive(data);
                byte[] receivedData = data.getData();
                Singleton.getInstance().addUser(new DatagramPacket(receivedData, receivedData.length, data.getAddress(), data.getPort()));
                // Sao chép dữ liệu âm thanh từ receivedData sang audioData
                byte[] audioData = new byte[receivedData.length];
                System.arraycopy(receivedData, 0, audioData, 0, receivedData.length);
                
                audio_out.write(audioData, 0, audioData.length);

                // Gửi lại âm thanh cho client
                List<DatagramPacket> users = Singleton.getInstance().getUsers();
                for (DatagramPacket user : users) {
                    DatagramPacket audioPacket = new DatagramPacket(audioData, audioData.length, user.getAddress(), user.getPort());
                    socket.send(audioPacket);
                }
            } catch (IOException ex) {
                // Xử lý ngoại lệ
            }
        }

        audio_out.close();
        audio_out.drain();
        System.out.print("stop");
    }
}