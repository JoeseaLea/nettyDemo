package com.joesea.tcp.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * <p>@author : Joesea Lea</p>
 * <p>@date : 2019/9/24</p>
 * <p>@description : </p>
 */
public class TcpClient {
    public static void main(String[] args) {
        try {
            int i = 0;
            while (i<1) {
                System.out.println(Calendar.getInstance().getTime());
//                Socket socket = new Socket("10.141.102", 3804);
                Socket socket = new Socket("127.0.0.1", 3804);
                System.out.println(Calendar.getInstance().getTime() + " end");
//                socket.close();
//                OutputStream out = socket.getOutputStream();
//                out.write("123".getBytes("UTF-8"));
//                out.flush();

                TimeUnit.MILLISECONDS.sleep(1000);
//                socket.close();
                i++;
            }
            while (true) {

            }
//            OutputStream out = socket.getOutputStream();
//
//            while (true) {
//                out.write(Calendar.getInstance().getTime().toString().getBytes("UTF-8"));
//                out.flush();
//                TimeUnit.MILLISECONDS.sleep(1);
//                System.out.println("111");
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        while(true) {
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
