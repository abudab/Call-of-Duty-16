var pl;
var xres;
var terrain_y;
var player_pos;
var player_life;
var msciwoj_mk1;
var powerBar=false;
var powerBarValue;
var powerBarValueD;
var alfa=0;
var alfaD=0;
var enemies;
var start;
var end;
var t35;
var ws;
var myId;
var playerEye;
var leci=false;
var trajx;
var trajy;
var trajktora;

function letsRoll(){
	pl=document.getElementById('tutaj');
	xres=parseInt(document.getElementById('terrain_xres').innerHTML);
	t=document.getElementById('terrain_y').innerHTML.split(new RegExp("\\s+"));
	terrain_y=new Array();
	for (i in t){
		terrain_y.push(parseInt(t[i]));
	}
        myId=parseInt(document.getElementById('myid').innerHTML);
	player_pos=parseInt(document.getElementById('player_pos').innerHTML);
	player_life=parseInt(document.getElementById('player_life').innerHTML);
	msciwoj_mk1=document.getElementById('msciwoj_mk1');
	var eL=document.getElementById('enemies_list').children;
	enemies=new Array();
	var i;
	for(i=0;i<eL.length;++i) {
		var enemyData=eL[i].innerHTML.split(" ");
		enemies.push({imie: enemyData[0],
				pos: parseInt(enemyData[1]),
				life: parseInt(enemyData[2])});
	}
        playerEye=player_pos;
        t35=document.getElementById('t35');
	setInterval(animuj,100);
	//setInterval(jakKlikasz,200);
	document.onkeydown=wcisniecieHandler;
	document.onkeyup=puszczenieHandler;
	document.onmousedown=wcisniecieMHandler;
	document.onmouseup=puszczenieMHandler;
        //ws = new WebSocket("ws://192.168.242.155:8080/Call16/WSServlet");
        ws = new WebSocket("ws://127.0.0.1:8080/Call16/WSServlet");
        ws.onopen = function(evt) {alert("Connection open ...");};
        ws.onclose = function(evt) {alert("Connection closed ...");};
        ws.onmessage = recv;
}

function rysujTeren(k){
	k.strokeStyle="rgb(0,0,0)";
	k.beginPath();
	/*start=player_pos-500>0?player_pos+500>3000?2000:player_pos-500:0;*/
        start=playerEye-500>0?playerEye+500>2000?1000:playerEye-500:0;
	end=start+1000;
	/*player_pos+500>xres*terrain_y.length?xres*terrain_y.length:player_pos+500*/;
	var i;
	
	var dy=(terrain_y[Math.floor(start/xres)+1]-
		terrain_y[Math.floor(start/xres)])*
		((start%xres)/xres);
	
	k.moveTo(0,800-(terrain_y[Math.floor(start/xres)]+dy));
	//k.lineTo(start%xres,800-terrain_y[Math.floor(start/xres)]);
	for (i=1;i<1000/xres+1;i++){
		k.lineTo(i*xres-start%xres,800-terrain_y[Math.floor(start/xres)+i]);
	}
	dy=(terrain_y[Math.floor(start/xres)+i]-
		terrain_y[Math.floor(start/xres)+i-1])*((end%xres)/xres);
	k.lineTo(1000,800-(terrain_y[Math.floor(start/xres)+i-1]+dy));
	k.lineTo(1000,800);
	k.lineTo(0,800);
	k.stroke();
	k.fillStyle="rgb(150,200,100)";
	k.fill();
}

function rysujGracza(k){
	/*var x=player_pos<500?player_pos:player_pos>1500?player_pos-1000:500;
	var y=terrain_y[Math.floor(player_pos/xres)];
	var dy=(terrain_y[Math.floor(player_pos/xres)==terrain_y.length-1?terrain_y.length-1:
		Math.floor(player_pos/xres)+1]-terrain_y[Math.floor(player_pos/xres)])*
		((player_pos%xres)/xres);*/
        var x=player_pos-start;
        var y=terrain_y[Math.floor(player_pos/xres)];
	var dy=(terrain_y[Math.floor(player_pos/xres)==terrain_y.length-1?terrain_y.length-1:
		Math.floor(player_pos/xres)+1]-terrain_y[Math.floor(player_pos/xres)])*
		((player_pos%xres)/xres);
	y+=dy;
	k.strokeStyle="rgb(255,0,255)";
	k.beginPath();
	k.arc(x,
		800-y-msciwoj_mk1.height/2,
		msciwoj_mk1.width,
		0,
		(Math.PI/180)*alfa,
		true);
	k.stroke();
	k.drawImage(msciwoj_mk1,x-Math.floor(msciwoj_mk1.width/2),800-y-msciwoj_mk1.height);
	if(powerBar){
		k.fillStyle="rgb(255,0,0)";
		k.fillRect(x-Math.floor(msciwoj_mk1.width/2),
			800-y-1.5*msciwoj_mk1.height,
			powerBarValue,5);
		powerBarValueD=powerBarValue==100?-5:powerBarValue==5?5:powerBarValueD;
		powerBarValue=powerBarValue+powerBarValueD;	
	}
	alfa=alfa+alfaD<-180?-180:alfa+alfaD>0?0:
		alfa+alfaD;
}

function rysujLifeBar(k){
	if(player_life>66)
		k.fillStyle="rgb(0,255,0)";
	else if(player_life>33)
		k.fillStyle="rgb(255,255,0)";
	else
		k.fillStyle="rgb(255,0,0)"
	k.fillRect(5,5,player_life,10);
	k.fillStyle="rgb(0,0,0)";
	k.fillText("Gracz",5,15);
	var i;
	for(i=0;i<enemies.length;++i){
		if(enemies[i].life>66)
			k.fillStyle="rgb(0,255,0)";
		else if(enemies[i].life>33)
			k.fillStyle="rgb(255,255,0)";
		else
			k.fillStyle="rgb(255,0,0)"
		k.fillRect(110*(i+1),5,enemies[i].life,10);
		k.fillStyle="rgb(0,0,0)";
		k.fillText(enemies[i].imie,110*(i+1),15);
	}
	
}

function rysujWrogow(k){
	for (i in enemies){
		if(enemies[i].pos>start && enemies[i].pos<end){
			var x=enemies[i].pos-start;
			var y=terrain_y[Math.floor(enemies[i].pos/xres)];
			var dy=(terrain_y[Math.floor(enemies[i].pos/xres)==terrain_y.length-1?terrain_y.length-1:
				Math.floor(enemies[i].pos/xres)+1]-terrain_y[Math.floor(enemies[i].pos/xres)])*
				((enemies[i].pos%xres)/xres);
			y+=dy;
			k.drawImage(t35,x-Math.floor(t35.width/2),800-y-t35.height);
			k.fillStyle="rgb(0,0,0)"
			k.fillText(enemies[i].imie,x,
				800-y-2*t35.height);
		}
	}
}

var czyn=0;

function rysujTrajektorie(k){
    if(leci){
            if(trajktora>trajx.length)
                leci=false;
            else{
                if(trajx[trajktora]-2>start && trajx[trajktora]+2<end && 
                    trajy[trajktora]>2 && trajy[trajktora]<1998)
                    k.fillRect(trajx[trajktora]-2-start,800-trajy[trajktora]-2,4,4);
                trajktora+=2;
            }
        }
}

function animuj(){
	var k=pl.getContext('2d');
	k.fillStyle="rgb(150,200,250)";
	k.fillRect(0,0,1000,800);
	rysujTeren(k);
	rysujGracza(k);
	rysujWrogow(k);
	rysujLifeBar(k);
        rysujTrajektorie(k);
	ruszGracza();
        
	/*if(player_pos==3000)
		czyn=-10;
	if(player_pos==0)
		czyn=10;
	player_pos+=czyn;*/
}

function ruszGracza(){
	/*player_pos=player_pos+czyn>1950?1950:player_pos+czyn<50?50:
		player_pos+czyn;*/
    playerEye=playerEye+czyn>1500?1500:playerEye+czyn<500?500:
		playerEye+czyn;
}



function wcisniecieHandler(e){
	if(e.keyCode==37)
		czyn=-20;
	else if(e.keyCode==39)
		czyn=20;
	else if(e.keyCode==38)
		alfaD=-5;
	else if(e.keyCode==40)
		alfaD=5;
}

function puszczenieHandler(e){
	if(e.keyCode==37 || e.keyCode==39)
		czyn=0;
	else if(e.keyCode==38 || e.keyCode==40)
		alfaD=0;
}

function strzal(){
    var pbv=powerBarValue*2;
    var wysylka=player_pos+" "+alfa+" "+pbv;
    //alert("wysylam: "+wysylka);
    ws.send(wysylka);
    //ws.close();
}

function wcisniecieMHandler(e){
	powerBar=true;
	powerBarValue=5;
}

function puszczenieMHandler(e){
	powerBar=false;
        strzal();
}

function recv(e){
   // alert(e.data);
    var data=e.data.split(" ");
    if(data[0]=="Komunikat")
        alert(e.data);
    else if(data[0]=="Gracze"){
        enemies=new Array();
	var i;
	for(i=1;i<data.length;i+=4) {
                if(parseInt(data[i+3])!=myId)
                    enemies.push({imie: data[i],
				pos: parseInt(data[i+1]),
				life: parseInt(data[i+2])});
	}
    }
    else if(data[0]=="Trajektoria"){
        
        leci=true;
        trajx=new Array();
        trajy=new Array();
        trajktora=0;
        var i;
        for(i=1;i<data.length;i+=2){
            trajx.push(parseInt(data[i]));
            trajy.push(parseInt(data[i+1]));
        }
    }
}