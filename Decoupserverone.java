import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Decoupserverone {
    static ServerSocket server;
    static Socket socket;

    public static void main(String[] args) throws IOException {
        server = new ServerSocket(9021);
        socket = server.accept();
        System.out.println("Server principal detectee");
        DataInputStream din = new DataInputStream(socket.getInputStream());
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        int length = din.readInt();
        if(length > 0){
            byte[] message = new byte[length];
            fos = new FileOutputStream("D:\\Alarms\\test1.txt");
            bos = new BufferedOutputStream(fos);
            din.readFully(message,0, message.length);
            bos.write(message,0, length);
            bos.flush();
            System.out.println("save");
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            dout.writeInt(message.length);
            dout.write(message);
            System.out.println("Done");
        }
    }
}
