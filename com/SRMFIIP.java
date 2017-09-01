package com;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import com.srmfiip.SQL;
class SRMFIIP{
private static int port=80;
public static void main(String[] args){
    ServerSocket ss;
    try{
        SQL.main(args);
    }catch(Exception e){System.out.println("Bad attempt for SQL login");return;}
    SQL.executeUpdate("update students set notallowed=(select code from subjects where prerequisites=students.subject)");
    try{
        ss=new ServerSocket(port);
        System.out.println("\u000CServer Started. Listening at port 80.");
        while(true){
            Thread t=new Thread(new Client(ss.accept()));
            t.start();
        }
    }catch(Exception e){System.out.println("System start failure.\nReason : "+e);}
}
}
