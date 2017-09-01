package com;
import java.util.Scanner;
import java.io.*;
import java.util.*;
import com.srmfiip.SQL;
class sqllive{
    public static void main(String[] args)throws IOException{
        Scanner sc=new Scanner(System.in);
        System.out.println("To initiate entry. Enter file name: ");
        String filename=sc.nextLine();
        System.out.println("Mention table");
        String tbname=sc.nextLine();
        System.out.println("Mention the number of columns:");
        int col=sc.nextInt();
        System.out.println("Number of empty fields: ");
        int emptyFields=sc.nextInt();
        int position[]=new int[emptyFields];
        for(int i=0;i<emptyFields;i++){
            System.out.println("Column number "+i);
            position[i]=sc.nextInt();
        }
        FileReader fr=new FileReader(filename);
        String content="",tbname=args[2],pswd=args[3];
        List<String> l;
        int n=5,ctr=0;
        while((ctr=fr.read())!=-1)content+=(char)ctr;
        content=content.replaceAll("<t xml:space=\"preserve\">","<t>");
        for(int i=0;i<emptyFields;i++)
            position[i]=Integer.parseInt(args[i+n]);
            
        String temp=content;
        for(int i=temp.indexOf("<si><t>");i!=-1;i=temp.indexOf("<si><t>")){
            int end=temp.indexOf("</t></si>");
              String data=temp.substring(i+7,end);
              temp=temp.substring(end+9);
              System.out.println("Accept data(y/n) -> "+data);
              String s=sc.next();
              if(s.equalsIgnoreCase("y")){
                
              }
        }
    }
}
