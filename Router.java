import java.net.*;
import java.io.*;
import java.util.ArrayList;

/*
* Programa que simula um roteador.
* 
* @author Guilherme Piccoli, Fernando Maioli, Igor Brehm
*/

public class Router {

    // Metodo main que inicia o programa router
    public static void main(String args[]) throws Exception {
        String ip = args[0];
        int port = 5723;
   		DatagramSocket serverSocket = new DatagramSocket(port);
        DatagramSocket clientSocket = new DatagramSocket();

        ArrayList<String> routers_list = new ArrayList<String>();
        byte[] receiveData = new byte[1024];
        boolean flag = true;
        while(flag == true) {
	         DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	         serverSocket.receive(receivePacket);
             
	         String sentence = new String(receivePacket.getData());
             String array[] = sentence.split("|");
             String sender_ip = array[0];
             String destination_ip = array[1];
             String sender_port = array[2];
             String destination_port = array[3];
             String filename = array[4];
             String message_data = array[5];
             System.out.println("Mensagem recebida: " + filename + "/" + destination_ip);
             
             byte[] sendData = new byte[1024];
             sendData = sentence.getBytes();
             
             if(!destination_ip.equals(ip)){ //mensagem para outra rede
                 System.out.println("Redirecionando mensagem para: " + destination_ip + "|" + "5723"); //enviar para o ip e porta do router da rede alvo
                 System.out.println("Rota: "+ ip + "/" + port + " -> " + destination_ip + "/" + "5723");
                 DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(destination_ip), 5723);
                 clientSocket.send(sendPacket);
             }
             else if(destination_ip.equals(ip) && (port != Integer.parseInt(destination_port))){ //mensagem para host desta rede
                 System.out.println("Redirecionando mensagem para: " + destination_ip + "|" + destination_port); //enviar para o ip e porta do host desta rede
                 System.out.println("Rota: "+ ip + "/" + port + " -> " + destination_ip + "/" + destination_port);
                 DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(destination_ip), Integer.parseInt(destination_port));
                 clientSocket.send(sendPacket);
             }
             else{ //mensagem para este router
                 //salvar arquivo em disco
             }
        }
        serverSocket.close();
   }
}
