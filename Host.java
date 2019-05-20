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
        Scanner in = new Scanner(System.in);
        System.out.println("Digita a tua porta ai meu bruxo:");
        int port = Integer.parseInt(in.nextLine());
        System.out.println("Agora digita o IP ai manolo:");
        String ip = in.nextLine();
        System.out.println("Qual a porta do teu router cara?");
        int router_port = Integer.parseInt(in.nextLine());
        System.out.println("Boa, agora fala o caminho do arquivo:");
        String path = in.nextLine();
        File file = new File(path);
        System.out.println("Pra que porta quer mandar?");
        int destination_port = Integer.parseInt(in.nextLine());
        System.out.println("Qual IP?");
        String destination_ip = in.nextLine();
        System.out.println("Beleza tio, vou mandar...");
        Scanner input = new Scanner (file);
        
        DatagramSocket clientSocket = new DatagramSocket();
        while (input.hasNextLine()) {
            String message = input.nextLine();
            
            byte[] sendData = new byte[1024];

            sendData = message.getBytes();
            
            if(ip.equals(destination_ip)){ //envia direto
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(destination_ip), destination_port);
                clientSocket.send(sendPacket);
            }
            else{ //envia pro router
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), router_port);
                clientSocket.send(sendPacket);
            }
            
        }
        clientSocket.close();
        input.close();
    }
}
