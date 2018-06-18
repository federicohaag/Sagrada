package it.polimi.se2018.networking.socket;

import it.polimi.se2018.networking.*;
import it.polimi.se2018.utils.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Federico Haag
 */
public class SocketClientGateway extends Thread implements SenderInterface, SocketReceiverInterface {

    private ObjectOutputStream out;

    private Client client;
    private String hostName;
    private int portNumber;

    private volatile boolean running = false;

    public SocketClientGateway(String hostName, int portNumber, Client client) {
        this.client = client;
        this.hostName = hostName;
        this.portNumber = portNumber;

        this.start();
    }

    @Override
    public void sendMessage(Message message) throws NetworkingException {

        //Waits that socket is open and connection correctly established
        while(!running){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        try {
            this.out.reset();
            this.out.writeObject(message);
        } catch (IOException e) {
            throw new NetworkingException("Failed sending message from SocketClientGateway due to IOException");
        }
    }

    @Override
    public void receiveMessage(Message message, ClientProxy sender) {

        client.notify(message); //client doesn't directly answer to server's messages so it is unnecessary sender
    }

    @Override
    public void fail(String m) {
        this.client.fail(m);
    }

    @Override
    public void run() {
        try(Socket echoSocket = new Socket(this.hostName, this.portNumber)){
            this.out = new ObjectOutputStream(echoSocket.getOutputStream());
            this.out.flush();

            ObjectInputStream in = null;
            in = new ObjectInputStream(echoSocket.getInputStream());
            this.running = true;
            while(true){
                receiveMessage((Message) in.readObject(),null);
            }

        } catch(Exception e){
            e.printStackTrace();
            this.client.fail("Exception thrown opening socket or reading from stream");
        }
    }
}