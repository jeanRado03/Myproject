import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientServ1 extends JFrame {
    private int height = 600,width = 1000;
    public static JFileChooser fileChooser1 = new JFileChooser("C:\\");
    JProgressBar bar;
    public static String sendfile1;
    static final String host1 = "127.0.0.1";
    static final int port = 5000;
    public static String name;
    static Socket socket;
    static String taille;

    static {
        try {
            socket = new Socket(host1,port);
            Content.connecter1 = "Connection accepted: server";
        } catch (ConnectException ce){
            System.out.println(ce);
            Content.connecter1 = ce.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public ClientServ1() throws IOException {
        this.setSize(this.getWidth(), this.getHeight());
        this.setTitle("Trasfer-File");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocation(100, 20);
        this.setVisible(true);
        this.setFocusable(true);
        Content cont = new Content(this, host1);
        this.add(cont);
    }

    public static void choice1() throws IOException {
        fileChooser1.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser1.setDialogTitle("File to send");
        String fileChoose = "Send";
        int rt = fileChooser1.showDialog(null,fileChoose);
        System.out.println("tafiditra");
        if(rt == JFileChooser.APPROVE_OPTION){
            System.out.println(fileChooser1.getCurrentDirectory());
            System.out.println(fileChooser1.getSelectedFile());
            System.out.println("tafiditra");
            sendfile1 = fileChooser1.getSelectedFile().toString();
            System.out.println(fileChooser1.getSelectedFile());
            send1(sendfile1);
        }
    }

    public static void main(String[] args) throws IOException {
        ClientServ1 client = new ClientServ1();
    }

    public static void waiting1(String nom){
        String path = "C:\\myXender\\"+nom;
        try {
            ClientServ1.receive1(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void send1(String file_to_send) throws IOException{
        DataOutputStream dataOutputStream = null;
        InputStream inputStream = null;
        System.out.println("miditra");
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            if (sendfile1 != null) {
                String path = file_to_send;
                sendFile(path);
            }
        }finally {
            dataOutputStream.close();
        }
    }

    public static void sendFile(String path) throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
            try {
                File myFile = new File(path);
                byte[] mybyte = new byte[(int) myFile.length()];
                fis = new FileInputStream(myFile);
                bis = new BufferedInputStream(fis);
                bis.read(mybyte, 0, mybyte.length);
                os = socket.getOutputStream();
                System.out.println(" Sending " + path + " (" + mybyte.length + " bytes)");
                taille = String.valueOf(mybyte.length);
                os.write(mybyte, 0, mybyte.length);
                os.flush();
                System.out.println("Done.");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bis != null) bis.close();
                if (os != null) os.close();
            }
    }



    public static void receive1(String file_to_receive) throws IOException {
        final int file_size = 60120;

        DataInputStream dataInputStream = null;

        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try{
            System.out.println("Connecting...");
            byte [] mybyte = new byte[file_size];
            InputStream is = socket.getInputStream();
            fos = new FileOutputStream(file_to_receive);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybyte,0, mybyte.length);
            current = bytesRead;
            do{
                bytesRead = is.read(mybyte,current, (mybyte.length-current));
                if(bytesRead >= 0) current += bytesRead;
            }while (bytesRead > -1);
            bos.write(mybyte,0, current);
            bos.flush();
            System.out.println("File "+file_to_receive+" downloaded ("+current+" bytes read)");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null) fos.close();
            if(bos != null) bos.close();
            socket.close();
        }
    }
}
