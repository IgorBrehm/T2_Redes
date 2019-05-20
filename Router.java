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
   		DatagramSocket serverSocket = new DatagramSocket(Integer.parseInt(args[0]));

        byte[] receiveData = new byte[1024];
        boolean flag = true;
        while(flag == true) {
	         DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	
	         serverSocket.receive(receivePacket);
	
	         String sentence = new String(receivePacket.getData());
	
	         System.out.println("Mensagem recebida: " + sentence);
        }
        serverSocket.close();
   }
}
