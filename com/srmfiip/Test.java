package com.srmfiip;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Statement;
class SQL{
    private static Connection c;
    public SQL(){c=null;}
    public static String executeQuery(String str){
        Statement s=null;
        ResultSet rs=null;
        String res="";
        
        try{
            try{
                Class.forName("com.mysql.jdbc.Driver");
            }catch(ClassNotFoundException cnfe){
                System.out.println("Class not found");
                cnfe.printStackTrace();
                }
            c=DriverManager.getConnection("jdbc:mysql://localhost:3306/detainees","root","piku1996");
            System.out.println("Connection succesfully created.");
            s=c.createStatement();
            rs=s.executeQuery(str);
            while(rs.next()){
                int i=1;
                while(true){
                    try{
                        res+=rs.getString(i++)+" ";
                    }catch(SQLException e){break;}
                }
                res+="\n";
            }
            c.close();
        }catch(SQLException sqle){System.out.println("SQL server failed. Reason :"+sqle);}
        System.out.println(res);
        return res;
    }
    public static void main(String[] args){
        Statement s=null;
        ResultSet rs=null;
        try{
            try{
                Class.forName("com.mysql.jdbc.Driver");
            }catch(ClassNotFoundException cnfe){
                System.out.println("Class not found");
                cnfe.printStackTrace();
                }
            c=DriverManager.getConnection("jdbc:mysql://localhost:3306/detainees","root","piku1996");
            System.out.println("Connection succesfully created.");
            s=c.createStatement();
            rs=s.executeQuery(args[0]);
            while(rs.next()){
                System.out.println("<button>"+rs.getString(1)+" "+rs.getString(2)+"</button>");
            }
            c.close();
        }catch(SQLException sqle){System.out.println("SQL server failed. Reason :"+sqle);}
    }
}
