package com.srmfiip;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Statement;
public class SQL{
    private static Connection c;
    private static String dbname,user,pswd;
    public SQL(){c=null;}
    public static String executeQuery(String str){
        Statement s=null;
        ResultSet rs=null;
        String res="";
        //System.out.println(str);
        try{
            try{
                Class.forName("com.mysql.jdbc.Driver");
            }catch(ClassNotFoundException cnfe){
                System.out.println("Class not found");
                cnfe.printStackTrace();
                }
            c=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbname,user,pswd);
            //System.out.println("Connection succesfully created.");
            s=c.createStatement();
            rs=s.executeQuery(str);
            while(rs.next()){
                int i=1;
                while(true){
                    try{
                        res+=rs.getString(i++)+"|";
                    }catch(SQLException e){break;}
                }
                res+="\n";
            }
            c.close();
        }catch(SQLException sqle){System.out.println("SQL server failed. Reason :"+sqle);}
        //System.out.println(res);
        return res;
    }
    public static int executeUpdate(String str){
        Statement s=null;
        int rs=0;
        String res="";
        //System.out.println(str);
        try{
            try{
                Class.forName("com.mysql.jdbc.Driver");
            }catch(ClassNotFoundException cnfe){
                System.out.println("Class not found");
                cnfe.printStackTrace();
                }
            c=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbname,user,pswd);
            //System.out.println("Connection succesfully created.");
            s=c.createStatement();
            rs=s.executeUpdate(str);
            c.close();
        }catch(SQLException sqle){System.out.println("SQL server failed. Reason :"+sqle);}
        //System.out.println(res);
        return rs;
    }
    public static int executeInsert(String tname,String[] args){
        String str="\"";
        for(int i=0;i<args.length;i++){
            if(args[i].indexOf("/*")>-1||args[i].indexOf(";")>-1)throw SQLException;
            str+=args[i]+"\",\"";
        }
        str=str.substring(0,str.length()-2);
        System.out.println(str);
        return 0;
    }
    public static void main(String[] args){
        if(args[0].indexOf(" ")==-1)
            {dbname=args[0];user=args[1];pswd=args[2];}
        else
            for(int i=0;i<args.length;i++){
                System.out.println(executeQuery(args[0]));
            }
    }
}
