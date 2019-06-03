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
    //falta implementar o direcionamento das mensagens
    //precisamos de alguma forma saber os enderecos IP e porta dos outros routers
    public static void main(String args[]) throws Exception {
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
   		DatagramSocket serverSocket = new DatagramSocket(port);

        ArrayList<String> routers_list = new ArrayList<String>();
        byte[] receiveData = new byte[1024];
        boolean flag = true;
        while(flag == true) {
	         DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	         serverSocket.receive(receivePacket);
             
	         String sentence = new String(receivePacket.getData());
             System.out.println("Mensagem recebida: " + sentence);
             String array[] = sentence.split("|");
             String sender_ip = array[1];
             String destination_ip = array[2];
             String sender_port = array[3];
             String destination_port = array[4];
             if(array[0].equals("connect")){ //router querendo descobrir esse router
                 String info = sender_ip + "|" + sender_port;
                 routers_list.add(info);
             }
             else if(array[0].equals("file")){ //arquivo sendo recebido
                 String filename = array[5];
                 String message_data = array[6];
                 if(!destination_ip.equals(ip)){ //mensagem para outra rede
                     System.out.println("Redirecionando mensagem para: " ); //enviar para o ip e porta do router da rede alvo
                     System.out.println("Rota: "+ ip + "/" + port + " -> ");
                 }
                 else if(destination_ip.equals(ip) && (port != Integer.parseInt(destination_port))){ //mensagem para host desta rede
                     System.out.println("Redirecionando mensagem para: " + destination_ip + "|" + destination_port); //enviar para o ip e porta do host desta rede
                     System.out.println("Rota: "+ ip + "/" + port + " -> " + destination_ip + "/" + destination_port);
                 }
                 else{ //mensagem para este router
                     //salvar arquivo localmente
                 }
             }
             else{
                System.out.println("Mensagem de formato inexperado recebida");
             }
        }
        serverSocket.close();
   }
}
