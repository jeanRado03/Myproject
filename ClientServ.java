import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientServ extends JFrame {
    private int height = 600,width = 1000;
    public JFileChooser fileChooser = new JFileChooser("D:\\");
    JProgressBar bar;
    public static String send;

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
        //this.setFocusable(true);
        String fileChoose = "Send";
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setDialogTitle("Transfert de fichier");
        int rt = fileChooser.showDialog(null,fileChoose);
        if(rt == JFileChooser.APPROVE_OPTION){
            System.out.println(fileChooser.getCurrentDirectory());
            System.out.println(fileChooser.getSelectedFile());
            send = fileChooser.getSelectedFile().toString();
            String path = "D:\\myXender\\Main.java";
            receive(path,send);
        }
        bar = new JProgressBar(0,100);
        bar.setFocusable(false);
        bar.setBounds(250,475,200,40);
        bar.setValue(0);
        //iterate();
        bar.setStringPainted(true);
        this.add(bar);
        Contenant cont = new Contenant(this);
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

    public static void main(String[] args) throws IOException {
        ClientServ client = new ClientServ();
    }

    public void receive(String file_to_receive,String file_to_send) throws IOException {
        final int port = 5000;
        final String host = "localhost";
        final int file_size = 60120;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;

        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket socket = null;
        try{
            socket = new Socket(host,port);
            System.out.println("Connecting...");
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            if(send!=null) {
                String path = file_to_send;
                dataOutputStream.writeUTF(path);
            }
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
            if(socket != null) socket.close();
            if(dataInputStream != null) dataOutputStream.close();
        }
    }
}
