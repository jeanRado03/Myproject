import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    static JFrame frame = new JFrame("Transfert en cours");
    static JProgressBar bar;

    public static void main(String[] args) throws IOException {
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
        }catch (IOException e){
            System.out.println(e.getMessage()+"0");
        }
        for (int i = 0; i < sockets.size(); i++) {
            sockets.get(i).close();
        }
        serverSocket.close();
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
                receiveFl(clientSocket, clientSocket1, serverSocket);
            } catch (IOException e) {
                System.out.println(e.getMessage()+"wesh");
            }finally {
                try {
                    dataInputStream.close();
                    dataOutputStream.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage()+"1");
                }
            }
        }
    }

    public static void send(byte[] byte_send, Socket socket, ServerSocket server) throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        try{
                try {
                    byte[] mybyte = byte_send;
                    os = socket.getOutputStream();
                    System.out.println(" Sending a file (" + mybyte.length + " bytes)");
                    os.write(mybyte, 0, mybyte.length);
                    bar = new JProgressBar(0,100);
                    bar.setFocusable(true);
                    bar.setBounds(50,25,356,30);
                    bar.setForeground(new Color(210,105,030));
                    bar.setStringPainted(true);
                    frame.setSize(500,150);
                    frame.setLocation(570,225);
                    frame.setLayout(null);
                    frame.add(bar);
                    frame.setVisible(true);
                    int i = 0;
                    while (i<=100){
                        bar.setValue(i);
                        i=i+2;
                        try{
                            if(mybyte.length<10000) {
                                Thread.sleep(1000);
                            }else{
                                Thread.sleep(1500);
                            }
                            if(i==100){
                                frame.dispose();
                            }
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                    }
                    os.flush();
                    System.out.println("Done.");
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage()+"2");
                } catch (IOException e) {
                    System.out.println(e.getMessage()+"3");
                } finally{
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                }
        }catch(Exception e){
            e.getMessage();
        }
    }
    public static void receiveFl(Socket socket, Socket socket1, ServerSocket server) throws IOException {
        final int file_size = 60120;

        DataInputStream dataInputStream = null;

        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            byte[] mybyte = new byte[file_size];
            InputStream is = socket.getInputStream();
            fos = new FileOutputStream("D:\\Alarms\\test.txt");
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybyte, 0, mybyte.length);
            current = bytesRead;
            do {
                bytesRead = is.read(mybyte, current, (mybyte.length - current));
                if (bytesRead >= 0) {
                    current += bytesRead;
                }
            } while (bytesRead > -1);
            Socket server1 = new Socket("localhost", 9021);
            Socket server2 = new Socket("localhost", 9022);
            ArrayList<Socket> sockets = new ArrayList<>();
            sockets.add(server1);
            sockets.add(server2);
            ByteArrayOutputStream byte_array = new ByteArrayOutputStream();
            int decomp = 0;
            for (int i = 0; i < sockets.size(); i++) {
                if(i==0) {
                    decoupage(mybyte, decomp, current / sockets.size(), sockets.get(i));
                }else{
                    decoupage(mybyte, decomp, (current / sockets.size())+(current % sockets.size()), sockets.get(i));
                }
                decomp = current / sockets.size();
                DataInputStream din = new DataInputStream(sockets.get(i).getInputStream());
                int length = din.readInt();
                byte[] bytes = new byte[length];
                din.readFully(bytes,0, bytes.length);
                byte_array.write(bytes);
            }
            byte[] concatener = byte_array.toByteArray();
            send(concatener,socket1,server);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    public static void decoupage(byte[] bytes,int off,int len,Socket socks) throws IOException {
        DataOutputStream dout = new DataOutputStream(socks.getOutputStream());
        dout.writeInt(len);
        dout.write(bytes,off,len);
    }
}
