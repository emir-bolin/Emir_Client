import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Client {

    public static void main(String args[]) throws Exception {
        // Default port number we are going to use
        int portnumber = 8002;
        if (args.length >= 1) {
            portnumber = Integer.parseInt(args[0]);
        }

        // Create a MulticastSocket for sending messages
        MulticastSocket chatSendSocket = new MulticastSocket();

        // Create a MulticastSocket for receiving responses
        MulticastSocket chatReceiveSocket = new MulticastSocket(portnumber);

        // Determine the IP address of a host, given the host name
        InetAddress group = InetAddress.getByName("225.4.5.6");

        // Joins a multicast group for receiving responses
        chatReceiveSocket.joinGroup(group);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String msg = "";

        // Loop for sending messages
        while (true) {
            System.out.println("Type a message for the server (or type 'exit' to quit):");
            msg = br.readLine();

            if (msg.equalsIgnoreCase("exit")) {
                break;
            }

            // Send the message to Multicast address
            DatagramPacket data = new DatagramPacket(msg.getBytes(StandardCharsets.UTF_8), msg.length(), group, portnumber);
            chatSendSocket.send(data);

            // Prepare to receive the result from the server
            byte[] buf = new byte[1024];
            DatagramPacket resultPacket = new DatagramPacket(buf, buf.length);
            chatReceiveSocket.receive(resultPacket);
            String result = new String(resultPacket.getData(), StandardCharsets.UTF_8).trim();
            System.out.println(result);
        }

        // Close the sockets
        chatSendSocket.close();
        chatReceiveSocket.close();
    }
}
