import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

/*
 * Programa que simula um host/maquina de uma rede.
 * 
 * @author Guilherme Piccoli, Fernando Maioli, Igor Brehm
 */
 

/*

Todo o processo de recepção e roteamento dos pacotes deve estar impresso na tela. Dessa
forma, será possível visualizar todo o caminho por onde o pacote passou. Além disso, quando o
destino receber um arquivo, o mesmo deve ser sinalizado com uma mensagem na tela, além de ser
armazenado na própria máquina. (TODO)

• deve ser possível visualizar o roteamento realizado. (PARTIALLY DONE)
*/
public class Host{
    
    public static int findIndex(ArrayList<ArrayList<String>> matrix, String filename){
        int index = -1;
        for(int i = 0; i < matrix.size(); i++){
            if(matrix.get(i).get(0).equals(filename)){
                index = i;
            }
        }
        return index;
    }
    
    public static void waitForMessages(int port) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(port);
            
            ArrayList<ArrayList<String>> matrix = new ArrayList<ArrayList<String>>();

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
                 System.out.println("Mensagem recebida: " + sentence);
                 
                 int index = findIndex(matrix, filename);
                 if(findIndex(matrix,filename) == -1){
                    ArrayList<String> row = new ArrayList<String>();
                    row.add(filename);
                    row.add(message_data);
                    matrix.add(row);
                 }
                 else if(message_data.equals("ENDOFFILE")){
                     PrintStream stream = new PrintStream(new File(filename));
                     for(int i = 1; i < matrix.get(index).size(); i++){
                         stream.println(matrix.get(index).get(i)); 
                     }
                     stream.close();
                     matrix.remove(index);
                 }
                 else{
                     matrix.get(index).add(message_data);
                 }
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {
        
        Scanner in = new Scanner(System.in);
        System.out.println("Digite a porta deste host:");
        int port = Integer.parseInt(in.nextLine());
        System.out.println("Agora digite o IP a ser usado:");
        String ip = in.nextLine();
        
        File dir = new File(Integer.toString(port)); //pasta de destino das mensagens recebidas
        dir.mkdir();
        
        try {
            Thread t = new Thread() {
                public void run() {
                    waitForMessages(port);
                }
            };
            t.start();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        
        DatagramSocket clientSocket = new DatagramSocket();
        
        while(true){
            System.out.println("---------------------------------------------------------");
            System.out.println("--- 1. Enviar Arquivo ---");
            System.out.println("--- 2. Sair           ---");
            System.out.println("---------------------------------------------------------");
            int choice = in.nextInt();
            if(choice == 1){
                System.out.println("Indique o caminho completo do arquivo a ser enviado:");
                String path = in.nextLine();
                String array[] = path.split("/");
                String filename = array[array.length - 1];
                File file = new File(path);
                Scanner input = new Scanner (file);
                
                System.out.println("Qual o IP de destino?");
                String destination_ip = in.nextLine();
                System.out.println("Qual a porta de destino?");
                int destination_port = Integer.parseInt(in.nextLine());
                System.out.println("Enviando arquivo...");
                
                byte[] sendData = new byte[1024];
                while (input.hasNextLine()) {
                    String message = ip + "|" + destination_ip + "|" + port + "|" + destination_port + "|" + filename + "|" + input.nextLine();

                    sendData = message.getBytes();
                    
                    if(ip.equals(destination_ip)){ //envia direto para o host
                        System.out.println("Rota: "+ ip + "/" + port + " -> " + destination_ip + "/" + destination_port);
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(destination_ip), destination_port);
                        clientSocket.send(sendPacket);
                    }
                    else{ //envia pro router
                        System.out.println("Rota: "+ ip + "/" + port + " -> " + ip + "/" + "5723");
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), 5723);
                        clientSocket.send(sendPacket);
                    }
                }
                
                String message = ip + "|" + destination_ip + "|" + port + "|" + destination_port + "|" + filename + "|" + "ENDOFFILE";

                sendData = message.getBytes();
                
                if(ip.equals(destination_ip)){ //envia direto para o host
                    System.out.println("Rota: "+ ip + "/" + port + " -> " + destination_ip + "/" + destination_port);
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(destination_ip), destination_port);
                    clientSocket.send(sendPacket);
                }
                else{ //envia pro router
                    System.out.println("Rota: "+ ip + "/" + port + " -> " + ip + "/" + "5723");
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), 5723);
                    clientSocket.send(sendPacket);
                }
                System.out.println("Arquivo enviado!");
            }
            else{
                break;
            }
        }
        clientSocket.close();
    }
}
