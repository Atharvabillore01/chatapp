import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Client extends JFrame {
    // GUI
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // constructor
    public Client() {
        try {
        System.out.println("Sending request to Server");
        socket = new Socket("127.0.0.1", 7770);
        System.out.println("connection successfull!");

        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());

       createGUI();
        handleEvents();
        startreading();
        //startwriting();

        } catch (Exception e) {
        e.getStackTrace();
        }
    }

    // Creating GUI
    private void createGUI() {
        // Coding for Desktop
        this.setTitle("Client Messager[END]");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Coding for all components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        // Load the original image
        ImageIcon originalIcon = new ImageIcon("image.png");
        Image originalImage = originalIcon.getImage();

        // Scale the image to the desired size
        int desiredWidth = 40;
        int desiredHeight = 40;
        Image scaledImage = originalImage.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);

        heading.setIcon(new ImageIcon(scaledImage));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        messageArea.setEditable(false);//make no change in message area
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        // Frame ka Layout set karenge
        this.setLayout(new BorderLayout());

        // adding the components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);//ading scroll slider
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);

    }

    //Handling Events in GUI
    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener(){
           

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //TODO Auto-generated method stub
                //System.out.println("Key relased"+e.getKeyCode());
                if(e.getKeyCode()==10){
                    String contentToSend=messageInput.getText();//get input text from text field to send on server
                    messageArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);// sending to server
                    out.flush();
                    if(contentToSend.equals("exit")){
                        messageInput.setEnabled(false);
                    }
                   
                    messageInput.setText("");//set the text to blank
                    messageInput.requestFocus();

                }
                
            }
        });
    }


    public void startreading() {
        System.out.println("reading started...");
        Runnable r1 = () -> {
            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated this chat ");
                        JOptionPane.showMessageDialog(this, "Server terminate this chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //System.out.println("Server : " + msg);
                    messageArea.append("Server : "+msg+"\n");
                }

            }

            catch (Exception e) {
                System.out.println("Connection is Closed");
            }
        };
        new Thread(r1).start();
    }

    public void startwriting() {
        System.out.println("writer started...");

        Runnable r2 = () -> {

            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if (content.equals("exit")) {
                        socket.close();
                    }

                }
            } catch (Exception e) {
                e.getStackTrace();

            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("client is started...");
        new Client();
    }

}
