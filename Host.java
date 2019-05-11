import java.net.*;
import java.io.*;
import java.util.Scanner;

/*
 * Programa que simula um host/maquina de uma rede.
 * 
 * @author Guilherme Piccoli, Fernando Maioli, Igor Brehm
 */

public class Host {
    private String serverName;
    private int port;
    
    public Host(String serverName,int port) {
        this.serverName = serverName;
        this.port = port;
        play();
    }

    //Metodo que executa o jogo em si
    private void play(){
        try {
            Random r = new Random();
            Socket client = new Socket(serverName, port);

            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
             
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
             
            Scanner input = new Scanner(System.in);
            String serverSentence = "";
            String name = "";
            serverSentence = in.readUTF();
            System.out.println(serverSentence);
            
            //Adquirindo os nomes dos jogadores
            while (true) {
                serverSentence = in.readUTF();
                if(serverSentence.equals("Iniciando jogo")){
                    break;
                }
                else if(serverSentence.equals("Aguardando nome")){
                    System.out.println("\nDigite seu nome:");
                    name = input.nextLine();
                    out.writeUTF("Name-"+name);   
                }
                else if(serverSentence.equals("Aguardando adversario")){
                    System.out.println(serverSentence);
                    while(true){
                        serverSentence = in.readUTF();
                        if(serverSentence.equals("Aguardando nome")){
                            System.out.println("\nDigite seu nome:");
                            name = input.nextLine();
                            out.writeUTF("Name-"+name); 
                        }
                        else if(serverSentence.equals("Nome invalido")){
                            System.out.println("Nome Invalido! Tente novamente.");
                            System.out.println("\nDigite seu nome:");
                            name = input.nextLine();
                            out.writeUTF("Name-"+name);
                        }
                        if(serverSentence.equals("Iniciando jogo")){
                            System.out.println("\n"+serverSentence);
                            break;
                        }
                    }
                    break;
                }
                else{
                    System.out.println("Nome Invalido! Tente novamente.");
                    System.out.println("\nDigite seu nome:");
                    name = input.nextLine();
                    out.writeUTF("Name-"+name);
                }
            }
            client.close();
        } 
        catch (IOException e) {
         e.printStackTrace();
        }   
    }
    
    //Metodo main do programa cliente com menu de interacao
    public static void main(String [] args) {
        String name = args[0];
        int number = Integer.parseInt(args[1]);
        while(true){
            Scanner in = new Scanner(System.in);
            System.out.println("-------------------------------------------------------");
            System.out.println("----------------Corrida Maluca da FACIN----------------");
            System.out.println("--1. Jogar o jogo");
            System.out.println("--2. Regras");
            System.out.println("--3. Sair");
            System.out.println("-------------------------------------------------------");
            int choice = in.nextInt();
            if(choice == 1){
                System.out.println("Encontrando adversario...");
                Client client = new Client(name,number);
            }
            else if(choice == 2){
                System.out.println("O jogo consiste de uma corrida entre voce e um adversario");
                System.out.println("A cada rodada ambos jogam um dado e avancam no tabuleiro");
                System.out.println("Dependendo da posicao onde se para, coisas extras podem acontecer!");
                System.out.println("O jogador que chegar no fim da pista primeiro vence o jogo");
            }
            else if(choice == 3){
                System.exit(0);
            }
            else{
                System.out.println("\nOpcao invalida, digite 1, 2 ou 3 de acordo com o menu!\n");
            }
        }
    }
}
