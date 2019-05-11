import java.net.*;
import java.io.*;

/*
* Programa que simula um roteador.
* 
* @author Guilherme Piccoli, Fernando Maioli, Igor Brehm
*/

public class Server extends Thread {
    private ServerSocket serverSocket;
    private static int[] scores;
    private static int[] board;
    private static int board_size;

    public Server(int port,int board_size) throws IOException {
        serverSocket = new ServerSocket(port);
        this.board_size = board_size;
    }
    /*
    // Metodo que executa as rodadas de uma partida do jogo
    public void rodadas(String[] players,DataInputStream in, DataInputStream in2, DataOutputStream out, DataOutputStream out2){
        try{
            int roll = 0;
            boolean end_game = false;
            String clientSentence = "";
            while(end_game == false){
                out.writeUTF("Sua vez");
                out2.writeUTF("Aguardando adversario");
                clientSentence = in.readUTF();
                String[] data = clientSentence.split("-", 2);
                if(data[0].equals("Roll")){
                    roll = Integer.parseInt(data[1]);
                    System.out.println("Jogador "+players[0]+" tirou "+roll+" nos dados!");
                    scores[0] = scores[0] + roll;
                    out2.writeUTF("\nAdversario avancou " + roll + " casas!");
                    if(scores[0] >= board_size){
                        end_game = true;
                        System.out.println("Jogador "+players[0]+" venceu a corrida!");
                        out.writeUTF("Fim de jogo");
                        out2.writeUTF("Fim de jogo");
                        out.writeUTF("Jogador "+players[0]+" venceu a corrida!");
                        out2.writeUTF("Jogador "+players[0]+" venceu a corrida!");
                        break;
                    }
                    switchCasos(1,board[scores[0]], out, out2);

                    out.writeUTF("-------------------------------");
                    out.writeUTF("Voce esta na casa: " + scores[0] );
                    out.writeUTF("-------------------------------");
                    out.writeUTF("Seu adversario esta na casa: " + scores[1] );
                    out.writeUTF("-------------------------------");

                    out2.writeUTF("-------------------------------");
                    out2.writeUTF("Voce esta na casa: " + scores[1] );
                    out2.writeUTF("-------------------------------");
                    out2.writeUTF("Seu adversario esta na casa: " + scores[0] );
                    out2.writeUTF("-------------------------------");

                }
                else if(data[0].equals("Exit")){
                    out2.writeUTF("Desistencia");
                    System.out.println("Jogador "+data[1]+" desistiu da partida.");
                    System.out.println("Esperando proximos jogadores");
                    return;
                }
                else{
                    System.out.println(clientSentence);
                }
                out.writeUTF("Aguardando adversario");
                out2.writeUTF("Sua vez");
                clientSentence = in2.readUTF();
                data = clientSentence.split("-", 2);
                if(data[0].equals("Roll")){
                    roll = Integer.parseInt(data[1]);
                    System.out.println("Jogador "+players[1]+" tirou "+roll+" nos dados!");
                    scores[1] = scores[1] + roll;
                    out.writeUTF("\nAdversario avancou " + roll + " casas!");
                    if(scores[1] >= board_size){
                        end_game = true;
                        System.out.println("Jogador "+players[1]+" venceu a corrida!");
                        out.writeUTF("Fim de jogo");
                        out2.writeUTF("Fim de jogo");
                        out.writeUTF("Jogador "+players[1]+" venceu a corrida!");
                        out2.writeUTF("Jogador "+players[1]+" venceu a corrida!");
                        break;
                    }
                    switchCasos(2,board[scores[1]], out2, out);

                    out2.writeUTF("-------------------------------");
                    out2.writeUTF("Voce esta na casa: " + scores[1] );
                    out2.writeUTF("-------------------------------");
                    out2.writeUTF("Seu adversario esta na casa: " + scores[0] );
                    out2.writeUTF("-------------------------------");

                    out.writeUTF("-------------------------------");
                    out.writeUTF("Voce esta na casa: " + scores[0] );
                    out.writeUTF("-------------------------------");
                    out.writeUTF("Seu adversario esta na casa: " + scores[1] );
                    out.writeUTF("-------------------------------");



                }
                else if(data[0].equals("Exit")){
                    out.writeUTF("Desistencia");
                    System.out.println("Jogador "+data[1]+" desistiu da partida.");
                    System.out.println("Esperando próximos jogadores.");
                    return;
                }
                else{
                    System.out.println(clientSentence);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
    //Metodo que inicializa as coneccoes com os jogadores e organiza o inicio da partida
    public void run() {
        try {
            while(true){
                System.out.println("\nIniciando nova partida:");
                Random r = new Random();
                Socket server1 = serverSocket.accept();
                Socket server2 = serverSocket.accept();
                
                String clientSentence; // String usada para as mensagens enviadas pelos clientes
                String[] players = new String[2]; // lista de nomes dos jogadores
                players[0] = ""; // nome do jogador 1
                players[1] = ""; // nome do jogador 2
                scores = new int[]{0,0}; // indice no tabuleiro onde esta cada jogador
                board = new int[board_size];
                
                DataInputStream in = new DataInputStream(server1.getInputStream());
                DataOutputStream out = new DataOutputStream(server1.getOutputStream());
                DataInputStream in2 = new DataInputStream(server2.getInputStream());
                DataOutputStream out2 = new DataOutputStream(server2.getOutputStream());
                out.writeUTF("Adversario encontrado!");
                out2.writeUTF("Adversario encontrado!");
                
                //inicializando os valores das posicoes do tabuleiro
                //dependendo do valor na posicao do tabuleiro, o jogador sofre efeitos extras
                for(int i = 0; i < board.length; i++){
                    if(i % 3 == 0){
                        board[i] = 0; //posicao onde nada acontece
                    }
                    else{
                        board[i] = r.nextInt(6); //posicoes onde se voltam casas ou se andam casas extras
                    }
                }
                
                //adquirindo os nomes dos jogadores
                out2.writeUTF("Aguardando adversario");
                out.writeUTF("Aguardando nome");
                while(players[0].equals("")){
                    clientSentence = in.readUTF();
                    String[] data = clientSentence.split("-", 2);
                    if(data[0].equals("Name")){
                        if(data[1].equals("") || data[1].length() < 3){
                            out.writeUTF("Nome invalido");
                        }
                        else{
                            players[0] = data[1];
                        }
                    }
                    else{
                        out.writeUTF("Nome invalido");
                    }
                }
                out.writeUTF("Aguardando adversario");
                out2.writeUTF("Aguardando nome");
                while(players[1].equals("")){
                    clientSentence = in2.readUTF();
                    String[] data = clientSentence.split("-", 2);
                    if(data[0].equals("Name")){
                        if(data[1].equals("") || data[1].length() < 3 || data[1].equals(players[0])){
                            out2.writeUTF("Nome invalido");
                        }
                        else{
                            players[1] = data[1];
                        }
                    }
                    else{
                        out2.writeUTF("Nome invalido");
                    }
                }
                
                System.out.println("Jogadores: "+players[0]+" e "+players[1]+"\n");
                //executando as rodadas
                out.writeUTF("Iniciando jogo");
                out2.writeUTF("Iniciando jogo");
                rodadas(players, in, in2, out, out2);
                server1.close();
                server2.close();    
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
  
    // Metodo main que inicia o programa servidor
    public static void main(String [] args) {
        int port = Integer.parseInt(args[0]);
        int board_size = Integer.parseInt(args[1]);
        try {
            Thread t = new Server(port,board_size);
            t.start();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
