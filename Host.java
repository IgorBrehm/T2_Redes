import java.net.*;
import java.io.*;
import java.util.Scanner;

/*
 * Programa que simula um host/maquina de uma rede.
 * 
 * @author Guilherme Piccoli, Fernando Maioli, Igor Brehm
 */
 

/*
Uma máquina poderá ter vários processos de transferência de arquivos, onde cada processo
será considerado uma máquina diferente na rede simulada. Quando um arquivo precisar ser
entregue para uma outra rede, ela precisará ser enviado para o processo que faz o papel de roteador
e este deverá enviar o pacote para a outra máquina. (TODO)

O endereço das máquinas da rede simulada deve corresponder a um par <IP,Porta>. Nesse
par <IP, Porta>, IP é o endereço da máquina onde o módulo que implementa um host está
instalado e Porta é o endereço pela qual são enviados/recebidos os dados, ou seja, a porta que o
socket real utiliza para ler e escrever da rede. (PRONTO)

Quando um arquivo precisar ser enviado, deve-se verificar o IP de destino. Caso este seja o
mesmo da máquina que vai enviar o arquivo, deve-se verificar a porta de destino, para que a entrega
seja feita para o processo correto. Caso o endereço de destino seja diferente, o arquivo deve ser
enviado para o host que faz o papel de roteador.  (PRONTO)
 
Somente um host na rede simulada deve fazer o papel de roteador. Os pacotes que circulam na rede devem 
possuir um cabeçalho com as informações necessárias na transferência. Essas informações são: endereço
IP origem, endereço IP destino, porta origem, porta destino e nome do arquivo. (PRONTO)

Todo o processo de recepção e roteamento dos pacotes deve estar impresso na tela. Dessa
forma, será possível visualizar todo o caminho por onde o pacote passou. Além disso, quando o
destino receber um arquivo, o mesmo deve ser sinalizado com uma mensagem na tela, além de ser
armazenado na própria máquina. (TODO)

Não existirá um número fixo de hosts, eles poderão ser incluídos a qualquer momento na
rede. (PRONTO?)

Visualização dos Resultados:
• demonstração deverá acontecer em mais de uma máquina (no mínimo 2);
• deve permitir a edição de um pacote a ser enviado a algum destino (IP, porta, dados);
• na chegada de um pacote ao destino, seu conteúdo deve ser mostrado;
• deve ser possível visualizar o roteamento realizado.
*/
public class Host {
    //falta implementar uma repeticao no processo de envio de arquivo ate a pessoa querer parar de enviar coisas
    //falta implementar o programa ficar esperando a todo momento por receber arquivos de outros hosts
    public static void main(String args[]) throws Exception {
        Scanner in = new Scanner(System.in);
        System.out.println("Digite a porta deste host:");
        int port = Integer.parseInt(in.nextLine());
        System.out.println("Agora digite o IP a ser usado:");
        String ip = in.nextLine();
        System.out.println("Especifique a porta do router a ser usado:");
        int router_port = Integer.parseInt(in.nextLine());
        System.out.println("Indique o caminho completo do arquivo a ser enviado:");
        String path = in.nextLine();
        String array[] = path.split("/");
        String filename = array[array.length - 1];
        System.out.println(filename);
        File file = new File(path);
        System.out.println("Qual o IP de destino?");
        String destination_ip = in.nextLine();
        System.out.println("Qual a porta de destino?");
        int destination_port = Integer.parseInt(in.nextLine());
        System.out.println("Enviando arquivo...");
        Scanner input = new Scanner (file);
        
        DatagramSocket clientSocket = new DatagramSocket();
        while (input.hasNextLine()) {
            String message = ip + "|" + destination_ip + "|" + port + "|" + destination_port + "|" + filename + "|" + input.nextLine();
            
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
        System.out.println("Arquivo enviado!");
        clientSocket.close();
        input.close();
    }
}
