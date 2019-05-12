import java.net.*;
import java.io.*;

/*
* Programa que simula um roteador.
* 
* @author Guilherme Piccoli, Fernando Maioli, Igor Brehm
*/

public class Router {

    // Metodo main que inicia o programa router
    public static void main(String args[]) throws Exception {
   		DatagramSocket serverSocket = new DatagramSocket(9876);

        byte[] receiveData = new byte[1024];
        boolean flag = true;
        while(flag == true) {
	         DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	
	         serverSocket.receive(receivePacket);
	
	         String sentence = new String(receivePacket.getData());
	         int port = receivePacket.getPort();
	
	         System.out.println("Mensagem recebida: " + sentence);
	         if(port == 99999){
	         	flag = false;
	         }
        }
        serverSocket.close();
   }
}
