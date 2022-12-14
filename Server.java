import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Vector;

public class Server {
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        ArrayList<Socket> sockets = new ArrayList<>();
        try{
            serverSocket = new ServerSocket(5000);
            serverSocket.setReuseAddress(true);
            System.out.println("Waiting for clients");
            while (true){
                if(sockets.size() > 1){
                    for (int i = 0; i < sockets.size(); i++) {
                        ClientHandler clientSocket1 = new ClientHandler(sockets.get(i),serverSocket,sockets);
                        new Thread(clientSocket1).start();
                    }
                }
                Socket client = serverSocket.accept();
                System.out.println("New client connected "+client.getInetAddress().getHostAddress());
                System.out.println(client+"Connected\n");
                sockets.add(client);
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    private static class ClientHandler implements Runnable {

        private final Socket clientSocket;
        private final ServerSocket serverSocket;
        private ArrayList<Socket> lists;
        public ClientHandler (Socket socket,ServerSocket server,ArrayList<Socket> list){
            this.clientSocket = socket;
            this.serverSocket = server;
            this.lists = list;
        }

        @Override
        public void run() {
            PrintWriter out = null;
            BufferedReader in = null;
            Socket clientSocket1 = null;
            try{
                for(Socket st : lists){
                    if(clientSocket != st){
                        clientSocket1 = st;
                    }
                }
                dataInputStream = new DataInputStream(clientSocket.getInputStream());
                dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                String message = "";
                    message = dataInputStream.readUTF();
                    System.out.println(message);
                    send(message, clientSocket1, serverSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(out!=null)out.close();
                    if(in!=null)in.close();
                    dataInputStream.close();
                    dataOutputStream.close();
                    //clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void send(String file_to_send, Socket socket, ServerSocket server) throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        try{

                System.out.println("Waiting for a client");
                try {
                    File myFile = new File(file_to_send);
                    byte[] mybyte = new byte[(int) myFile.length()];
                    fis = new FileInputStream(myFile);
                    bis = new BufferedInputStream(fis);
                    bis.read(mybyte, 0, mybyte.length);
                    os = socket.getOutputStream();
                    System.out.println(" Sending " + file_to_send + " (" + mybyte.length + " bytes)");
                    os.write(mybyte, 0, mybyte.length);
                    os.flush();
                    System.out.println("Done.");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally{
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (socket != null) socket.close();
                }
        }
        finally {
            if(server != null) server.close();
        }
    }
}
