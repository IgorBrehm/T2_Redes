import java.net.*;
import java.io.*;
import java.util.Scanner;

/*
 * Programa que simula um host/maquina de uma rede.
 * 
 * @author Guilherme Piccoli, Fernando Maioli, Igor Brehm
 */

public class Host {
    
    public static void main(String args[]) throws Exception {
        int port = Integer.parseInt(args[0]);
        InetAddress IPAddress = InetAddress.getByName("localhost");

        Scanner in = new Scanner (System.in);
        File file = new File(args[1]);
        Scanner input = new Scanner (file);

        while (input.hasNextLine()) {
            String message = input.nextLine();
            DatagramSocket clientSocket = new DatagramSocket();

            byte[] sendData = new byte[1024];

            sendData = message.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

            clientSocket.send(sendPacket);
            
        }
        clientSocket.close();
        input.close();
    }
}
