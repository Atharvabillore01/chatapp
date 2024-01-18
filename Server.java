import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;

import java.io.*;

class Server extends JFrame {
    // GUI
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    // constructor
    //Socket Programmin - for communacting with two system
    //
    Server() {
        try {
        server = new ServerSocket(7770);
        System.out.println("Server is ready to accept connection");
        System.out.println("waiting...");
        socket = server.accept();

        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());// also i can write true tomake autoflash : true
        

        createGUI();
        handleEvents();

        startreading();
        //startwriting();

        } catch (Exception e) {
        e.getStackTrace();
        }

    }

    // Creating GUI
    public void createGUI() {
        // Creating desktop
        this.setTitle("Server Messager[END]");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Coding for all components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        // adding mirch masala to all the components { Doing Styling }

        // heading.setIcon(new ImageIcon("image.png"));
        ImageIcon orignalIcon = new ImageIcon("image.png");
        Image orignaImage = orignalIcon.getImage();
        Image scaled = orignaImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        heading.setIcon(new ImageIcon(scaled));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        // Adding all components to the desktop frame
        this.setLayout(new BorderLayout());

        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);

    }

    //Now handling all the events
    public void handleEvents(){
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
                // TODO Auto-generated method stub
                if(e.getKeyCode()==10){
                    String content=messageInput.getText();
                    messageArea.append("Me : "+content +"\n");
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        messageInput.setEnabled(false);
                    }
                    
                    messageInput.setText("");
                    messageInput.requestFocus();

                }
               
            }

        });
    }

    // jab tak read kare dega jab tak client message dega
    public void startreading() {
        System.out.println("reader started...");
        Runnable r1 = () -> {
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client terminated this chat ");
                        JOptionPane.showMessageDialog(this, "Client Terminated this chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //System.out.println("Client : " + msg);
                    messageArea.append("Client : "+msg+"\n");

                }
            } catch (Exception e) {
                // e.getStackTrace();
                System.out.println("Connections is Closed..");
            }

        };
        new Thread(r1).start();

    }

    // user user se input leke client ko dega
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
        System.out.println("Server is going to start...");
        new Server();
    }
}