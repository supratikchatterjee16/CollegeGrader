package com;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.io.*;
import java.util.Scanner;
import java.util.Date;
import com.srmfiip.SQL;
public class Client implements Runnable,Serializable{
    private Socket s;
    private static String ipid[][][]=new String[255][255][255],chat[][]=new String[2][100];
    private final String encoding="UTF-8";//use this with URLEncoder and URLDecoder
    private int address[];
    public Client(Socket a){
        s=a;
        address=new int[4];
        int ctr=0;
        String add=s.getRemoteSocketAddress().toString();
        add=add.substring(1);
        int i=0;
        while(ctr<4){
            if(Character.isDigit(add.charAt(i)))address[ctr]=(address[ctr]*10)+Integer.parseInt(add.substring(i,i+1));
            else ctr++;
            i++;
        }
        //SQL.executeQuery("update students set notallowed=(select code from subjects where prerequisites=students.subject)");
    }
    private String[] extract(String str,int n){
        Scanner sc=new Scanner(str.substring(str.indexOf("data")));
        sc.useDelimiter("&");
        String arr[]=new String[n];
        for(int i=0;i<n;i++){
            String temp=sc.next();
            temp=temp.substring(6);
            try{arr[i]=URLDecoder.decode(temp,encoding);}catch(UnsupportedEncodingException e){System.out.println("Check Decoding format");}
            //System.out.println(arr[i]);
        }
        return arr;
    }
    private String mainLogic(String str){
        int sw=Integer.parseInt(str.substring(0,1));
        str=str.substring(1);
        String res="false";
        try{str=URLDecoder.decode(str,encoding);}catch(UnsupportedEncodingException uee){System.out.println("Check decoding format.");}
        System.out.println(str);
        switch(sw){
            case 0:
                res="0";
                res+=SQL.executeQuery("select code,title from subjects order by title asc");
            break;
            case 1:
                if(str.indexOf("code=")!=-1){
                    String code="";
                    try{
                        code=str.substring(str.indexOf("code=")+5,str.indexOf(" "));
                        }catch(Exception e){code=str.substring(str.indexOf("code=")+5);}
                    res="1"+SQL.executeQuery("select * from subjects where code=\""+code+"\"");
                    res+=SQL.executeQuery("select * from students where subject=\""+code+"\"");
                }
                else if(str.indexOf("title=")!=-1){
                    String code=SQL.executeQuery("select code from subjects where title=\""+str.substring(str.indexOf("title=")+6)+"\"");
                    res=mainLogic("1code="+code.substring(0,code.indexOf("|")));
                }
                else if(str.indexOf("name=")!=-1){
                    String code=SQL.executeQuery("select id from students where name=\""+str.substring(str.indexOf("name=")+5)+"\"");
                    code=code.substring(0,code.indexOf("|"));
                    res=mainLogic("1id="+code);
                }
                else if(str.indexOf("id=")!=-1){
                    String code=str.substring(str.indexOf("id=")+3);
                    res="2";
                    res+=SQL.executeQuery("select * from students where id=\""+code+"\"");
                }
                else if(str.indexOf("section=")!=-1){
                    String code=str.substring(str.indexOf("section=")+8);
                    res="2";
                    res+=SQL.executeQuery("select * from students where section=\""+code+"\"");
                }
                else if(str.indexOf("year=")!=-1){
                    String code=str.substring(str.indexOf("year=")+5);
                    res="2";
                    res+=SQL.executeQuery("select * from students where year=\""+code+"\"");
                }
                else if(str.indexOf("search=")!=-1){
                    String id=str.substring(str.indexOf("search=")+7,str.indexOf("&")),code=str.substring(str.indexOf("&")+1);
                    res="41"+SQL.executeQuery("select id,name,subject,year,section from students where ID=\""+id+"\" and subject=\""+code+"\"");
                    res+=SQL.executeQuery("select status from compensatory where ID=\""+id+"\" and code=\""+code+"\"");
                    if(res.length()<3)res="40";
                    System.out.println(res);
                }
            break;
            case 2:{
                String password=str.substring(str.lastIndexOf(" ")+1),account=str.substring(0,str.lastIndexOf(" "));
                String check=SQL.executeQuery("select password from accounts where username=\""+account+"\"");
                try{check=check.substring(0,check.length()-2);
                if(check.equals(password)){
                    Date d=new Date();
                    SQL.executeUpdate("delete from signin where account=\""+account+"\"");
                    String s="insert into signin values(\""+account+"\",\""+Integer.toString(address[0])+"."+Integer.toString(address[1])+"."+Integer.toString(address[2])+"."+Integer.toString(address[3])+"\",\""+d.toString()+"\")";
                    //System.out.println(s);
                    SQL.executeUpdate(s);
                    res="31<adv id=\"adv\"><note id=\"adv-note\"><center>This section of the webpage is to update existing and/or to insert new data into the database.<br>Please remember, any changes you make will be reflected over the website. So do recheck the data that you update or load.<br><br><br>Using this section is pretty self explanatory. There are two actions you can perform via this section. Updation and insertion.You must select the method you wish to implement. Then fill in the required details.<br>There are a few things added in, to help you along the way. However, it needs a couple of important information to help you out. Updation requires ID number of the student and subect code. If and only if the data exists, will it be updated. Insertion requires no prior information, so one can simply enter whatever they wish to.<br><br>After reading this, please click on the continue to hide this message, and start.<br><br><br><br><button onclick=\"cont()\">Continue</button></center></note><advapp id=\"adv-app\"><center><table><tr><td>Student Code:</td><td><input id=\"adv-code\" type=\"text\" autocomplete=\"off\" title=\"important for searching\" placeholder=\"Registration ID\"/></td></tr><tr><td>Student Name:</td><td><input id=\"adv-name\" type=\"text\" autocomplete=\"off\" placeholder=\"Name\"/></td></tr><tr><td>Subject Code:</td><td><input id=\"adv-subcode\" type=\"text\" autocomplete=\"off\" title=\"important for searching\" placeholder=\"Subject code\"/></td></tr><tr><td>Current Year:</td><td><input id=\"adv-year\" type=\"number\" autocomplete=\"off\" min=\"1\" max=\"8\" placeholder=\"Current Year\"/></td></tr><tr><td>Section:</td><td><input id=\"adv-sec\" type=\"text\" autocomplete=\"off\" placeholder=\"Section\"/></td></tr><tr><td>Compensatory:</td><td><button id=\"adv0\" onclick=\"compensate(this)\">Applied</button><button id=\"adv1\" onclick=\"compensate(this)\">Ongoing</button><button id=\"adv2\" onclick=\"compensate(this)\">Completed</button><button id=\"adv3\" onclick=\"compensate(this)\">Invalid</button></td></tr></table><modeselect><button onclick=\"insertReq()\">Insert</button><button onclick=\"updateReq()\">Update</button><button onclick=\"find()\">Search</button></modeselect></center></advapp></adv>";
                }
                else res="30";
                }catch(Exception e){res="30";}
            }
            break;
            case 3:{
                String meth=str.substring(0,6);
                str=str.substring(7);
                String iden=str.substring(0,str.indexOf("&"));str=str.substring(str.indexOf("&")+1);
                String code=str.substring(0,str.indexOf("&"));str=str.substring(str.indexOf("&")+1);
                String name=str.substring(0,str.indexOf("&"));str=str.substring(str.indexOf("&")+1);
                String year=str.substring(0,str.indexOf("&"));str=str.substring(str.indexOf("&")+1);
                String sect=str.substring(0,str.indexOf("&"));str=str.substring(str.indexOf("&")+1);
                String comp=(str.indexOf("&")>0)?str.substring(0,str.indexOf("&")):"";
                String arr[]={meth,iden,code,name,year,sect,comp};
                if(meth.equalsIgnoreCase("update")){
                    /**
                    *The model for updation
                    *Required: Method, which may be insert,update and delete.
                    *The request can be made to fetch data for deletion.
                    *This is by far the most compilcated part of the entire program.
                    *After login process. A particular set of data has to be accessed based on the client request.
                    *Comma Seperated values: This data can be parsed into sorting the data from the table.
                    * The order of the recieved data is: ID, Subject Code, Name, Current Year, Section, compensatory course
                    */
                    res="50";
                    SQL.executeUpdate("update students set name=\""+name+"\" where id=\""+iden+"\"");
                    SQL.executeUpdate("update compensatory set status=\""+comp+"\" where id=\""+iden+"\" and code=\""+code+"\"");
                    res="51";
                }
                else if(meth.equalsIgnoreCase("insert")){
                    res="52";
                    String arr2[]={iden,name,code,year,sect};
                    SQL.executeInsert("students",arr);
                    SQL.executeUpdate("update students set notallowed=(select code from subjects where prerequisites=students.subject)");
                    SQL.executeUpdate("update compensatory set status=\""+comp+"\" where id=\""+iden+"\" and code=\""+code+"\"");
                    res="53";
                }
                changelog(arr);
            };break;
            default:res="9There is an error. Please do not proceed further.";
        }
        return res;
    }
    private synchronized void changelog(String arr[]){
        Date d=new Date();
        String account=SQL.executeQuery("select account from signin where ip=\""+Integer.toString(address[0])+"."+Integer.toString(address[1])+"."+Integer.toString(address[2])+"."+Integer.toString(address[3])+"\"");
        String query="";
        for(int i=0;i<arr.length;i++)query+=arr[i]+" ";
        arr={account,d.toString(),query};
        SQL.executeInsert("changelog",arr);
    }
    private synchronized void log(String str)throws IOException{
        Date d=new Date();
        String date=d.toString();
        File f=new File("logs/"+date.substring(0,date.indexOf(":")-2)+date.substring(date.length()-4)+".txt");
        if(!f.exists()){
            File fn=new File("logs");
            if(!fn.isDirectory())fn.mkdir();
            f.createNewFile();
        }
        FileWriter fw=new FileWriter(f,true);
        fw.write("["+d.toString()+"]\t"+s.getRemoteSocketAddress().toString()+"\t"+str+"\r\n");
        fw.flush();
        fw.close();
    }
    private synchronized String read(){//Note. Do not under any circumstances modify this part.
        String str="",postdata="";
        boolean flag=true;
        int n=0,ctr=0,arr[]=new int[4];
        try{
            BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
            while((n=br.read())!=-1){
                //System.out.print(n+" ");
                if(ctr==0){
                    ctr++;
                    if((char)n=='G'){
                        str='G'+br.readLine();
                        break;
                    }
                    else str='P'+br.readLine();
                }
                for(int i=0;i<3;i++)arr[i]=arr[i+1];
                arr[3]=n;
                if(arr[0] == arr[2] && arr[2] == 13 && arr[1] == arr[3] && arr[3] == 10)ctr++;
                if(ctr==2)postdata+=(char)n;//includes the \r\n\r\n part as well. That is 8 characters
                if(ctr==3)break;
            }
            if(postdata.length()>2)str=str.concat(postdata.substring(0,postdata.length()-2));
         }catch(IOException e){System.out.println(e);}
         //System.out.println(str);
        return str;
    }
    private synchronized String[] evaluated(String in){
        Scanner sc=new Scanner(in);
        String data[]=new String[5];
        int ctr=0;
        while(sc.hasNext())data[ctr++]=sc.nextLine();
        sc=new Scanner(data[0]);
        String method=sc.next(),path=sc.next(),mimetype="text/plain",dat="";
        //GET handler section:
        if(method.equalsIgnoreCase("GET")){
            if(path.endsWith("css")){path="styles"+path;mimetype="text/css";}
            else if(path.endsWith("html")||path.endsWith("htm")){path="html"+path;mimetype="text/html";}
            else if(path.endsWith("js")){path="scripts"+path;mimetype="text/javascript";}
            else if(path.endsWith("jpg")||path.endsWith("jpeg")){path="res"+path;mimetype="image/jpeg";}
            else if(path.endsWith("ico")||path.endsWith("png")){path="res"+path;mimetype="image/png";}
            else if(path.endsWith("svg")){path="res"+path;mimetype="image/svg+xml";}
            else if(path.endsWith("ttf")){path="fonts"+path;mimetype="font/ttf";}
            try{
               log(data[0]);
               //System.out.println(data[0]);
               if(path.length()<3){
                    path="html/main.html";mimetype="text/html";
                    if(ipid[address[1]][address[2]][address[3]]!=null)path="html/main.html";
               }
            }catch(IOException e){System.out.println("Error in reading required file : "+e);}
        }//POST data section comes next:
        else if(method.equalsIgnoreCase("POST")){
            path="";
            mimetype="text/plain";
            //System.out.println("\nMain logic.\nData recieved: "+data[1]);
            dat=mainLogic(data[1]);
            try{
                dat=URLEncoder.encode(dat,encoding);
            }catch(UnsupportedEncodingException e){System.out.println("The encoding was changed hence, it malfunctioned.");}
        }
        String arr[]={path,mimetype,dat};
        return arr;
    }
    private synchronized void transmit(String[] data){
        //data 0 should contain filename, if there is a file to be sent otherwise, it should have nothing. lenght = 0
        //data 1 should contain the mimetype to be sent....
        //data 2 should contain the response.
        //System.out.println(data[0]+", "+data[1]+", "+data[2]);
        int n=0;
        try{
            PrintStream pw = new PrintStream(new BufferedOutputStream(s.getOutputStream()));
            pw.print("HTTP/1.1 200 OK\r\n");
            pw.print("Content-type: "+data[1]+"\r\n\r\n");
            //pw.print(data[2]);
            File f=new File(data[0]);
            if(data[0].length()!=0){
                if(f.exists()){
                    InputStream fr=new FileInputStream(data[0]);
                    byte b[]=new byte[4096]; 
                    while((n=fr.read(b))!=-1){
                        pw.write(b,0,n);
                    }
                }
                else{
                    System.out.println("\nFile not found : "+data[0]+".\nCreate one or place right files in the right locations.");
                    pw.print("Nothing to be found...\r\n");
                }
            }//Get data transmit section
            else{
               //System.out.println(data[2]);
               pw.print(data[2]);
            }
            pw.close();  
        }catch(IOException e){System.out.println("Transmit failure: "+e);}
    }
    private void copyBlob(String path1,String path2)throws IOException{
        InputStream is=null;
        OutputStream os=null;
        try{
            is=new FileInputStream(path1);
            os=new FileOutputStream(path2);
            byte b[]=new byte[1024];int n=0;
            while((n=is.read(b))>0)os.write(b,0,n);
           }finally{
            is.close();
            os.close();
            }
    }
    public void run(){transmit(evaluated(read()));}
}
