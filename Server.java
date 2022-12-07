import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    private static DataOutputStream dataOutputStream1 = null;
    private static DataInputStream dataInputStream1 = null;
    public static void main(String[] args) {
        String serverDirectory = "";
        try{
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("listening to port 5000");
            Socket clientSocket = serverSocket.accept();
            System.out.println(clientSocket+"Connected\n");
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            String message = "";
                message = dataInputStream.readUTF();
                System.out.println(message);
                send(message,clientSocket,serverSocket);
            dataInputStream.close();
            dataOutputStream.close();
        }catch (Exception e){
            System.out.println(e.toString());
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
