import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientServ extends JFrame {
    private int height = 600,width = 1000;
    public static JFileChooser fileChooser = new JFileChooser("D:\\");
    JProgressBar bar;
    public static String sendFile;
    static final String host = "localhost";
    static final int port = 5000;
    static Socket socketClient;

    static {
        try {
            socketClient = new Socket(host,port);
            Contenant.connecter = "Connection accepted: server";
        } catch (ConnectException ce){
            System.out.println(ce);
            Contenant.connecter = ce.getMessage();
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

    public ClientServ() throws IOException {
        this.setSize(this.getWidth(),this.getHeight());
        this.setTitle("Trasfer-File");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocation(100,20);
        this.setVisible(true);
        this.setFocusable(true);
        bar = new JProgressBar(0,100);
        bar.setFocusable(true);
        bar.setBounds(250,475,497,40);
        bar.setForeground(new Color(210,105,030));
        bar.setValue(0);
        //iterate();
        bar.setStringPainted(true);
        //this.add(bar);
        Contenant cont = new Contenant(this,host);
        this.add(cont);
    }

    void iterate(){
        int i = 0;
        while (i<=100){
            bar.setValue(i);
            i=i+5;
            try{
                Thread.sleep(200);
            }catch (Exception e){

            }
        }
    }

    public static void choice() throws IOException {
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setDialogTitle("File to send");
        String fileChoose = "Send";
        int rt = fileChooser.showDialog(null,fileChoose);
        if(rt == JFileChooser.APPROVE_OPTION){
            System.out.println(fileChooser.getCurrentDirectory());
            System.out.println(fileChooser.getSelectedFile());
            sendFile = fileChooser.getSelectedFile().toString();
            String name = fileChooser.getSelectedFile().getName();
            send(sendFile);
        }
    }

    public static void main(String[] args) throws IOException {
        ClientServ client = new ClientServ();
        //client.iterate();
    }

    public static void waiting(){
        String path = "D:\\myXender\\tp24h.sql";
        try {
            ClientServ.receive(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void send(String file_to_send) throws IOException{
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(socketClient.getOutputStream());
            if (sendFile != null) {
                String path = file_to_send;
                dataOutputStream.writeUTF(path);
            }
        }finally {
            if(dataOutputStream != null) dataOutputStream.close();
        }
    }

    public static void receive(String file_to_receive) throws IOException {
        final int file_size = 60120;

        DataInputStream dataInputStream = null;

        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try{
            System.out.println("Connecting...");
            byte [] mybyte = new byte[file_size];
            InputStream is = socketClient.getInputStream();
            fos = new FileOutputStream(file_to_receive);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybyte,0, mybyte.length);
            current = bytesRead;
            do{
                bytesRead = is.read(mybyte,current, (mybyte.length-current));
                if(bytesRead >= 0) current += bytesRead;
            }while (bytesRead > -1);
            bos.write(mybyte,0, mybyte.length);
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
            if(socketClient != null) socketClient.close();
        }
    }
}
