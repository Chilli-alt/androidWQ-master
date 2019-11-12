package com.example.androidwq;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class client extends Thread {

    public static String message;
    //定义一个Socket对象
    Socket socket = null;
    public client(String host, int port) {
        try {
            //需要服务器的IP地址和端口号，才能获得正确的Socket对象
            socket = new Socket(host, port);
            System.out.println("连接成功");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        //客户端一连接就可以写数据个服务器了
        new sendMessThread().start();//？
        new receiveMessThread().start();
        /*super.run();
        try {
            // 读取服务器回写的数据
            InputStream s = socket.getInputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = s.read(buf)) != -1) {
                message=new String(buf, 0, len);
                System.out.println(new String(buf, 0, len));//输出服务器回写的数据
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public class receiveMessThread extends Thread{
        public void run() {
            super.run();
            try {
                // 读取服务器回写的数据
                InputStream s = socket.getInputStream();
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = s.read(buf)) != -1) {
                    message=new String(buf, 0, len);
                    System.out.println(new String(buf, 0, len));//输出服务器回写的数据
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //往Socket里面写数据，需要新开一个线程
    public class sendMessThread extends Thread{

        public void run(JSONObject json ) {
            super.run();
            //写操作
            Scanner scanner=null;
            OutputStream os= null;
            try {
                scanner=new Scanner(System.in);
                os= socket.getOutputStream();
                String in="";
                do {
                    in=scanner.next();
                    os.write((""+in).getBytes());
                    os.flush();
                } while (!in.equals("bye"));

//                String imgStr = Base64Image.GetImageStr(imgPath);//是将图片的信息转化为base64编码
                //String imgStr="";
                //向服务器端发送数据
                /*JSONObject json = new JSONObject();
                json.put("type","register");
                json.put("username","ren");
                json.put("password","123456");
                json.put("phone","123");
                json.put("userImage",imgStr);*/
                //将json转化为String类型
                String jsonString = "";
                jsonString = json.toString();

                //将String转化为byte[]
                byte[] jsonByte = jsonString.getBytes();

                DataOutputStream outputStream = null;
                outputStream = new DataOutputStream(socket.getOutputStream());
                System.out.println("发的数据长度为:"+jsonByte.length);
                outputStream.write(jsonByte);
                outputStream.flush();
                System.out.println("传输数据完毕");
                socket.shutdownOutput();
            } catch (IOException e) {
                e.printStackTrace();
            }
            scanner.close();
            System.out.println("发送成功");
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

