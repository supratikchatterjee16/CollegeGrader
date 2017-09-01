var sublist=[];
var compensatestate="";
String.prototype.replaceAt=function(index,ch){
    var str1=this.substring(0,index), str2="";
    if(index<this.length-1)str2=this.substring(index+1);
    return (str1+ch+str2);
   }
function send(str){
    var res="";
    str=encodeURIComponent(str);
    str+="\r\n\r\n";
    var xhr=new XMLHttpRequest();
    xhr.open("POST","",true);
    xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xhr.send(str);
    xhr.onreadystatechange=function(){
    if(this.readyState==4&&this.status==200){
        res+=xhr.responseText;
        //res=res.replace(new RegExp("+","g")," ");
        if(res!="false"||res!="")resolveRes(decodeURIComponent(res));
    }
    }
}
    function fetch(x){
        var con=x.innerHTML;
        con=con.replace(/\r/g,"");
        con=con.replace(/\n/g,"");
        send("1"+x.id+"="+con);
    }
    function init(){send("0");}
    function resolveRes(res){
        var opt=res.substring(0,1);
        res=res.substring(1);
        switch(parseInt(opt)){
            case 0:{
                var ch=0;
                var str="";
                for(var i=0;i<res.length;i++){
                    var c=res.charAt(i);
                    if(c=="|"){
                        ch++;
                        if(ch%2==0){
                            sublist.push(str);
                            str="";
                            ch=0;
                        }
                        else str+=" ";
                    }
                    else if(c=="+")str+=" ";
                    else str+=c;
                }
                document.getElementById("left").innerHTML="";
                for(var i=0;i<sublist.length;i++){
                    document.getElementById("left").innerHTML+="<button id=\"code\" onclick=\"fetch(this)\">"+sublist[i]+"</button>";}
             };break;
             case 1:{
                var ch=0,res2="",i=0;
                for(i=0;i<res.length;i++){
                    if(res.charAt(i)=="|"&&ch<5)ch++;
                    else if(ch==5) break;
                 }
                var sx=formatSubject(res.substring(0,i));
                res2=res.substring(i,res.length);
                formatStudent(res2);
             };break;
             case 2:{
                document.getElementById("subject").innerHTML="";
                formatStudent(res);
             };break;
             case 3:{
                switch(parseInt(res.charAt(0))){
                    case 0:alert("Wrong account or password");break;
                    case 1:showadvanced(res.substring(1));break;
                }
             };break;
             case 4:{
                switch(parseInt(res.charAt(0))){
                    case 0:alert("No data found");break;
                    case 1:{
                        res=res.substring(1);
                        formatUpdate(res);
                    }
                    ;break;
                }
             };break;
             case 5:{
                opt=res.charAt(0);
                res=res.substring(1);
                switch(parseInt(opt)){
                    case 0:alert("Update failed");break;
                    case 1:alert("Update succesful");send("0");break;
                    case 2:alert("Insertion of new data failed");break;
                    case 3:alert("New data inserted");send("0");break;
                }
             };break;
        }
    }
    function formatSubject(res){
        var ch=0;
        res="<table><tr><td>Subject code:</td><td id='code' onclick='fetch(this)'>"+res;
        for(var i=0;i<res.length;i++){
            if(res.charAt(i)=="|"){
            switch(ch){
                case 0:res=res.replaceAt(i,"</td></tr><tr><td>Subject Title:</td><td id='title' onclick='fetch(this)'>");ch++;break;
                case 1:res=res.replaceAt(i,"</td></tr><tr><td>Semester:</td><td id='semester' onclick='fetch(this)'>");ch++;break;
                case 2:res=res.replaceAt(i,"</td></tr><tr><td>Credits:</td><td id='credit' onclick='fetch(this)'>");ch++;break;
                case 3:res=res.replaceAt(i,"</td></tr><tr><td>Dependent:</td><td id='code' onclick='fetch(this)'>");ch++;break;
                case 4:
                    res2=res.substring(i);
                    res=res.substring(0,i+1)
                    res=res.replaceAt(i,"</td></tr></table>");
                    break;
                }
            }
            else if(res.charAt(i)=="+")res=res.replaceAt(i," ");
        }
        document.getElementById("subject").innerHTML=res;
        return res;
    }
    function formatStudent(res){
        var ch=0;
        var temp="<table><tr><td id='id' onclick='fetch(this)'>";
        for(var i=0;i<res.length-2;i++){
            if(res.charAt(i)=="|"){
                ch++;
                if(ch==6){temp+="</td></tr><tr><td id='id' onclick='fetch(this)'>";ch=0;}
                else if(ch==1)temp+="</td><td id='name' onclick='fetch(this)'>";
                else if(ch==2)temp+="</td><td id='code' onclick='fetch(this)'>";
                else if(ch==3)temp+="</td><td id='year' onclick='fetch(this)'>";
                else if(ch==4)temp+="</td><td id='section' onclick='fetch(this)'>";
                else if(ch==5)temp+="</td><td id='code' onclick='fetch(this)'>";
            }
            else if(res.charAt(i)=="+")temp+=" ";
            else temp+=res.charAt(i);
        }
        temp+="</td></tr></table>";
        document.getElementById("student").innerHTML=temp;
        return res;
    }
    function formatUpdate(str){
        var pos=str.indexOf("|"),ctr=0;
        while(pos<str.length-1||pos==-1){
            switch(ctr){
                case 0:document.getElementById("adv-code").value=str.substring(0,pos);break;
                case 1:document.getElementById("adv-name").value=str.substring(0,pos).replace("+"," ");break;
                case 2:document.getElementById("adv-subcode").value=str.substring(0,pos);break;
                case 3:document.getElementById("adv-year").value=str.substring(0,pos);break;
                case 4:document.getElementById("adv-sec").value=str.substring(0,pos);break;
                case 5:
                    var com=str.substring(0,pos);
                    if(com.length<2)com="Invalid";
                    for(var i=0;i<4;i++){
                        if(com.indexOf(document.getElementById("adv"+i.toString()).innerHTML)!=-1){
                            compensate(document.getElementById("adv"+i.toString()));
                         }
                     }
                    break;
                default:return;
            }
            ctr++;
            str=str.substring(pos+1);
            pos=str.indexOf("|");
        }
    }
    var advctr=0;
    function pull(){
        if(advctr==0){
        document.getElementById("advanced").style.left="0";
        document.getElementById("pull").style.transform="rotate(360deg)";
        document.getElementById("pull").style.left="10pt";
        document.getElementById("pull").title="close";
        advctr++;
        }
        else{
            document.getElementById("advanced").style.left="100vw";
            document.getElementById("pull").style.transform="rotate(-405deg)";
            document.getElementById("pull").style.left="-30pt";
            document.getElementById("pull").title="advanced";
            advctr=0;
        }
        }
    function pushh(){
        document.getElementById("advanced").style.left="100vw";
    }
    function signin(){
        var str1=document.getElementById("acc").value, str2=document.getElementById("pswd").value;
        send("2"+str1+" "+str2);
        }
    function subsearch(){
        var key=document.getElementById("subsearch").value;
        document.getElementById("left").innerHTML="";
        if(key.length==0){
            for(var i=0;i<sublist.length;i++)
            document.getElementById("left").innerHTML+="<button id=\"code\" onclick=\"fetch(this)\">"+sublist[i]+"</button>";
        }
        else{ 
            for(var i=0;i<sublist.length;i++)
            if(sublist[i].indexOf(key)!=-1)
            document.getElementById("left").innerHTML+="<button id=\"code\" onclick=\"fetch(this)\">"+sublist[i]+"</button>";
        }
    }0
    function showadvanced(str){
        document.getElementById("login").style.display="none";
        str=str.split("+").join(" ");
        document.getElementById("advanced").innerHTML=str+"<pull title=\"Advanced\" id=\"pull\" onclick=\"pull()\"></pull>";
        document.getElementById("pull").style.left="10pt";
        document.getElementById("pull").style.transform="rotate(0deg)";
        document.getElementById("adv").style.display="block";
    }
    function cont(){document.getElementById("adv-note").style.display="none";document.getElementById("adv-app").style.display="block";}
    function find(){
        var code=document.getElementById("adv-code").value,sub=document.getElementById("adv-subcode").value;
        send("1search="+code+"&"+sub);
    }
    function compensate(x){
        compensatestate=x.innerHTML;
        var ctr=-1;
        if(compensatestate=="Applied")ctr=0;
        else if(compensatestate=="Ongoing")ctr=1;
        else if(compensatestate=="Completed")ctr=2;
        else if(compensatestate=="Invalid"){ctr=3;compensatestate="";}
        for(var i=0;i<4;i++){
            if(ctr==i){
                document.getElementById("adv"+i.toString()).style.background="rgba(40,50,120,0.8)";
             }
            else document.getElementById("adv"+i.toString()).style.background="rgba(0,0,0,0.7)";
        }
    }
    function updateReq(){
        var arr=[document.getElementById("adv-code").value,document.getElementById("adv-subcode").value,document.getElementById("adv-name").value,document.getElementById("adv-year").value,document.getElementById("adv-sec").value,compensatestate];
        var res="3update=";
        for(var i=0;i<arr.length;i++){
            res+=arr[i]+"&";
        }
        send(res);
    }
    function insertReq(){
        var arr=[document.getElementById("adv-code").value,document.getElementById("adv-subcode").value,document.getElementById("adv-name").value,document.getElementById("adv-year").value,document.getElementById("adv-sec").value,compensatestate];
        var res="3insert=";
        for(var i=0;i<arr.length;i++){
            res+=arr[i]+"&";
        }
        send(res);
    }
    function init(){send("0");pushh();}
