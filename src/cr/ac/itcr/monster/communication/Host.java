package cr.ac.itcr.monster.communication;

import cr.ac.itcr.monster.App;
import cr.ac.itcr.monster.game.cards.Card;
import cr.ac.itcr.monster.gui.game.GameController;
import cr.ac.itcr.monster.gui.host.HostWindow;
import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Clase encargada de la logística de un Host y su manejo
 */
public class Host implements Runnable, Messenger{

    private static volatile Host instance;
    private String client;
    private ServerSocket ss;
    private DataOutputStream dos;
    private DataInputStream dis;
    private boolean flag =true;

    /**
     * Builder de Host
     */
    private Host(){
        //singleton!! :D
        Thread t = new Thread(this); //thread so that it's permanently checking for sockets
        t.start();
        try {
            Thread.sleep(100); //esto es para darle chance al server socket de crearse antes de que lo usen para cualquier cosa
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Da la instancia de Host
     * @return Host
     */
    public static synchronized Host getHost() {
        if (instance!=  null){
            return instance;
        }
        instance = new Host();
        return instance;
    }

    /**
     * Sucede si el Host se desconecta o cuando termina el juego
     */
    public void terminate(){
        this.flag = false;
        try {
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        instance = null;
    }

    /**
     * Módulo que envía mensajes para la comunicación entre el cliente y el host
     * @param msg
     */
    public void sendMsg(String msg) {
        try {
            dos.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Modulo que recibe e interpreta el mensaje obtenido del cliente
     * @param incomingMsg
     * @throws IOException
     */
    private void handleMsg(String incomingMsg) throws IOException {
        String[] parts = incomingMsg.split("-", 3);

        String type = parts[0];
        String info = parts[1];

        switch (type) {
            case "ESTABLISH CONNECTION":
                this.client = info;
                dos.writeUTF("CONNECTION SUCCESFUL-name host");
                System.out.println(client);

                Platform.runLater(() -> { //platform.runlater necesario para accesar javafx thread de manera segura
                    HostWindow.closeHostWindow();
                    App.startGame("host");
                });
                break;
            case "CLOSING CONNECTION":
                this.terminate();
                break;
            case "ACTION":
                switch (info){
                    case "switch turn":
                        Platform.runLater(() -> {
                            GameController.getInstance().endTurnButton.setDisable(false);
                            GameController.getInstance().endTurnButton.fire();});
                        break;
                    case "draw":
                        Platform.runLater(() -> GameController.getInstance().enemyDraw(parts[2]));
                        break;
                }
                break;
            case "ESBIRRO":
                Platform.runLater(() -> GameController.getInstance().addMinion(Card.getCardByName(parts[1]), "enemy"));
                break;
            case "HECHIZO":
                switch (info){
                    case"freeze":
                        Platform.runLater(()->GameController.getInstance().Freeze(true));
                        break;
                    case "Curar":
                        Platform.runLater(()->GameController.getInstance().heal(Integer.parseInt(parts[2]),"enemy"));
                        break;
                    case "Bola de Fuego":
                        if (parts[2]=="enemigo"){
                            Platform.runLater(()->GameController.getInstance().takeDamage(200,"player"));
                        }else{
                            Platform.runLater(()->GameController.getInstance().damageMinion(200,Integer.parseInt(parts[2]),"player"));
                        }
                        break;
                    case "Asesinar":
                        Platform.runLater(()->GameController.getInstance().killMinion(Integer.parseInt(parts[2]),"player"));
                        break;

                }
                break;
            case "ATAQUE":
                switch (info) {
                    case "enemigo":
                        Platform.runLater(()-> {
                            int damage = Card.getCardByName(parts[2]).getAtaque();
                            GameController.getInstance().takeDamage(damage,"player");
                            GameController.getInstance().getGameHandler().getHistory().addEvent(incomingMsg);
                        });
                        break;
                    default:
                        Platform.runLater(()-> {
                            int damage = Card.getCardByName(parts[2]).getAtaque();
                            GameController.getInstance().damageMinion(damage, Integer.parseInt(info),"player");
                            GameController.getInstance().getGameHandler().getHistory().addEvent(incomingMsg);
                        });
                        break;
                }
                break;

        }
    }


    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(0)) {  //this try automatically closes the server socket when done
            this.ss = server;
            Socket s = ss.accept(); //waits for client socket to connect
            this.dis = new DataInputStream(s.getInputStream()); // input stream
            this.dos = new DataOutputStream(s.getOutputStream()); // output stream to reply to socket
            while (flag) {

                String incomingMsg = dis.readUTF();

                handleMsg(incomingMsg);
            }
            ss.close(); //closes the socket
        } catch (SocketException e) {
            if (!flag) {
                System.out.println("Interrupted server socket accept");
            } else {
                e.printStackTrace();
            }
        } catch (EOFException e) {
            System.out.println("Data Stream closed prematurely");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return ss.getLocalPort();
    }
}
