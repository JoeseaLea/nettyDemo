package com.joesea.tcp.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * <p>@author : Joesea Lea</p>
 * <p>@date : 2019/9/24</p>
 * <p>@description : </p>
 */
public class TcpServer {
    public static void main(String[] args) {
        try {
            int i = 0;
            ServerSocket server = new ServerSocket(3804);
            while (true) {
                Socket socket = server.accept();
                socket.setSoTimeout(10000);
                InputStream in = socket.getInputStream();
                String result = "";
                byte[] buffer = new byte[100];
                in.read(buffer);
                result = new String(buffer, "UTF-8").trim();
//
                System.out.println(result + "------");

//                System.out.println(socket.getPort());
//                socket.close();
//                System.out.println(++i);
            }
//            InputStream in = socket.getInputStream();
//            System.out.println("------------------------");
//            while(true) {
//                String result = "";
//                byte[] buffer = new byte[100];
//                in.read(buffer);
//                result = new String(buffer, "UTF-8").trim();
//
//                System.out.println(result + "------");
//
//                TimeUnit.MILLISECONDS.sleep(1);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
