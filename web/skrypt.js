var pl; //płótno
var xres; //odległość x pomiędzy kolejnymi węzłami terenu
var terrain_y; //tablica węzłów terenu (wartości y)
var player_pos; //pozycja x gracza
var player_life; //punkty życia gracza
var msciwoj_mk1; //obrazek czołgu gracza
var powerBar=false; //czy ma być wyświetlony pasek siły strzału
var powerBarValue; //siła strzału/długość paska siły strzału
var powerBarValueD; //zmiana siły strzału na klatkę animacji
var alfa=0; //kąt strzału
var alfaD=0; //zmiana kąta strzału na klatkę animacji
var enemies; //tablica z przeciwnikami
var start; //współrzędna lewej krawędzi okna w UW terenu
var end; //współrzędna prawej krawędzi okna w UW terenu
var t35; //obrazek czołgu przeciwników
var ws; //web socket
var myId; //id gracza na serwerze
var playerEye; //pozycja środka ekranu w UW terenu - zmienia się przy przesuwaniu planszy
var leci=false; //czy trwa animacja trajektorii
var trajx; //trajektoria pocisku tablica z x-ami
var trajy; //trajektoria pocisku tablica z y-ami
var trajktora; //który punkt trajektorii w tej klatce animacji

var dlugoscplanszy; //długość planszy xres*(ile el. tablicy terrain_y)
var graid; //id areny na serwerze
var ruchupdate=0; //przesunięcie czołgu które należy wykonać
var ruchid; //id czołgu który jest przesuwany
var reload=0; //okres bezczynności po strzale/ruchu w ms.
var polaczano=false;
var laczenieInterwal=10;

//
var WEBSOCKET="ws://192.168.242.155:8080/Call16";

/*Funkcja wywołuje się przy ładowaniu dokumentu i ustawia
 *odpowiednie wartości zmiennych globalnych oraz inicjuje web sockety.
 **/
function letsRoll(){
	pl=document.getElementById('tutaj');
	xres=parseInt(document.getElementById('terrain_xres').innerHTML);
	t=document.getElementById('terrain_y').innerHTML.split(new RegExp("\\s+"));
	terrain_y=new Array();
	for (i in t){
		terrain_y.push(parseInt(t[i]));
	}
        myId=parseInt(document.getElementById('myid').innerHTML);
	//player_pos=parseInt(document.getElementById('player_pos').innerHTML);//
	//player_life=parseInt(document.getElementById('player_life').innerHTML);
	msciwoj_mk1=document.getElementById('msciwoj_mk1');
	var eL=document.getElementById('enemies_list').children;
	enemies=new Array();
	var i;
	for(i=0;i<eL.length;++i) {
		var enemyData=eL[i].innerHTML.split(" ");
                if(parseInt(enemyData[3])==myId){
                    player_pos=parseInt(enemyData[1]);
                    player_life=parseInt(enemyData[2]);
                }
                else
                    enemies.push({imie: enemyData[0],
				pos: parseInt(enemyData[1]),
				life: parseInt(enemyData[2]),
                                id: parseInt(enemyData[3])});
	}
        graid=parseInt(document.getElementById('graid').innerHTML);
        playerEye=player_pos;
        t35=document.getElementById('t35');
        dlugoscplanszy =(terrain_y.length-1)*xres;
	setInterval(animuj,100);
	//setInterval(jakKlikasz,200);
	document.onkeydown=wcisniecieHandler;
	document.onkeyup=puszczenieHandler;
	document.onmousedown=wcisniecieMHandler;
	document.onmouseup=puszczenieMHandler;
        //ws = new WebSocket("ws://192.168.242.155:8080/Call16/WSServlet");
        wsConnect();
}

/*
 *Ponawia połączenie - wywoływane w interwałach 1s po nieoczekiwanym przerwaniu transmisji
 **/
function wsConnect(){
    ws = new WebSocket(WEBSOCKET);
    ws.onopen = przywitajSie;
    ws.onclose = function(evt) {polaczano=false };
    ws.onmessage = recv;
}

/*Rysuje ehm... teren
 */
function rysujTeren(k){
	k.strokeStyle="rgb(0,0,0)";
	k.beginPath();
	/*start=player_pos-500>0?player_pos+500>3000?2000:player_pos-500:0;*/
        start=playerEye-500>0?playerEye+500>dlugoscplanszy?dlugoscplanszy-1000:playerEye-500:0;
        start=start<0?0:start;
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

/*Rysuje czołg gracza, powerbar, łuk kątu strzału, info o reload itp.
 */
function rysujGracza(k){
        //współrzędne czołgu na ekranie
        var x=player_pos-start;
        var y=terrain_y[Math.floor(player_pos/xres)];
	var dy=(terrain_y[Math.floor(player_pos/xres)==terrain_y.length-1?terrain_y.length-1:
		Math.floor(player_pos/xres)+1]-terrain_y[Math.floor(player_pos/xres)])*
		((player_pos%xres)/xres);
	y+=dy;
        //Łuk kąta strzału
	k.strokeStyle="rgb(255,0,255)";
	k.beginPath();
	k.arc(x,
		800-y-msciwoj_mk1.height/2,
		msciwoj_mk1.width,
		0,
		(Math.PI/180)*alfa,
		true);
	k.stroke();
        //rysunek czołgu
	k.drawImage(msciwoj_mk1,x-Math.floor(msciwoj_mk1.width/2),800-y-msciwoj_mk1.height);
        //ruchoma lufa
        k.strokeStyle="rgb(0,0,0)";
        k.lineWidth=5;
        k.beginPath();
        if(alfa>=-90){
            k.moveTo(x-Math.floor(msciwoj_mk1.width/2)+25,800-y-msciwoj_mk1.height+8);
            k.lineTo(x-Math.floor(msciwoj_mk1.width/2)+25+
                Math.cos( (Math.PI/180)*Math.abs(alfa) )*10,
                800-y-msciwoj_mk1.height+8-
                    Math.sin( (Math.PI/180)*Math.abs(alfa) )*10 );
        }
        else{
            k.moveTo(x-Math.floor(msciwoj_mk1.width/2)+17,800-y-msciwoj_mk1.height+8);
            k.lineTo(x-Math.floor(msciwoj_mk1.width/2)+17+
                Math.cos( (Math.PI/180)*Math.abs(alfa) )*10,
                800-y-msciwoj_mk1.height+8-
                    Math.sin( (Math.PI/180)*Math.abs(alfa) )*10 );
        }
        k.stroke();
        k.lineWidth=1;
        //wskaźnik siły strzału
	if(powerBar){
		k.fillStyle="rgb(255,0,0)";
		k.fillRect(x-Math.floor(msciwoj_mk1.width/2),
			800-y-1.5*msciwoj_mk1.height,
			powerBarValue,5);
		powerBarValueD=powerBarValue==100?-5:powerBarValue==5?5:powerBarValueD;
		powerBarValue=powerBarValue+powerBarValueD;	
	}
        //info że trwa ładowanie (nie można strzelać)
        if(reload>0){
            k.fillStyle="rgb(0,0,0)";
            k.fillText("Recharging: "+Math.floor(reload/10),460,390);
            --reload;
        }
        //nowe wartości zmiennych
	alfa=alfa+alfaD<-180?-180:alfa+alfaD>0?0:
		alfa+alfaD;
        if(alfa<-90)
            msciwoj_mk1=document.getElementById("msciwoj_mirrored");
        else
            msciwoj_mk1=document.getElementById("msciwoj_mk1");
}

/*Rysuje wskaźniki poziomu życia gracza i przeciwników w prawym górnym rogu
*/
function rysujLifeBar(k){
        if(player_pos>0){
	if(player_life>66)
		k.fillStyle="rgb(0,255,0)";
	else if(player_life>33)
		k.fillStyle="rgb(255,255,0)";
	else
		k.fillStyle="rgb(255,0,0)"
	k.fillRect(5,5,player_life,10);
	k.fillStyle="rgb(0,0,0)";
	k.fillText("Gracz",5,15);
        }
        else{
            k.fillStyle="rgb(0,0,0)";
            k.fillText("Tylko obserwujesz",5,15);
        }
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

/*Rysuje wrogów oraz odpowiada za animację eksplozji
 */
function rysujWrogow(k){
	for (i in enemies){//rysowanie wrogów
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
        for(j in trupy){//eksplozje
            if(trupy[j].t>0){
                y=terrain_y[Math.floor(trupy[j].gdzie/xres)];
		dy=(terrain_y[Math.floor(trupy[j].gdzie/xres)==terrain_y.length-1?terrain_y.length-1:
				Math.floor(trupy[j].gdzie/xres)+1]-terrain_y[Math.floor(trupy[j].gdzie/xres)])*
				((trupy[j].gdzie%xres)/xres);
		y+=dy;
                switch(trupy[j].t){
                    case 3:
                        k.fillStyle="rgb(255,0,0)"
                        k.beginPath();
                        k.arc(trupy[j].gdzie-start,800-y-t35.height/2,20,0,Math.PI*2,true);
                        k.fill();
                        break;
                    case 2:
                        k.fillStyle="rgb(255,255,0)"
                        k.beginPath();
                        k.arc(trupy[j].gdzie-start,800-y-t35.height/2,30,0,Math.PI*2,true);
                        k.fill();
                        k.fillStyle="rgb(255,0,0)"
                        k.beginPath();
                        k.arc(trupy[j].gdzie-start,800-y-t35.height/2,10,0,Math.PI*2,true);
                        k.fill();
                        break;
                    case 1:
                        k.fillStyle="rgb(255,255,0)"
                        k.beginPath();
                        k.arc(trupy[j].gdzie-start,800-y-t35.height/2,40,0,Math.PI*2,true);
                        k.fill();
                        break;
                }
                trupy[j].t-=1;
            }
        }
}

var czyn=0; //wartość przesunięcia "oka" w jednej klatce animacji

/*Rysuje lecący pocisk*/
function rysujTrajektorie(k){
    if(leci){
            if(trajktora>trajx.length+2)
                leci=false;
            if(trajktora>trajx.length-1){
                k.strokeStyle="rgb(255,0,0)";
                k.beginPath();
                k.arc(trajx[trajx.length-1]-start,
                    800-trajy[trajy.length-1],
                    30,
                    0,
                    2*Math.PI,
                    true);
                k.stroke();
                trajktora+=1;
            }
            else{
                if(trajx[trajktora]-2>start && trajx[trajktora]+2<end && 
                    trajy[trajktora]>2 && trajy[trajktora]<1998)
                    k.fillRect(trajx[trajktora]-2-start,800-trajy[trajktora]-2,4,4);
                trajktora+=1;
            }
        }
}

/*Odpowiada za animowaną zmianę położenia czołgu*/
function naprawdeRuszKogos(){
    if(ruchupdate==0 || leci)
        return;
    var deix=ruchupdate;
    if(Math.abs(ruchupdate)<3)
        deix=ruchupdate;
    else
        deix=ruchupdate>0?3:-3;
    if(ruchid==myId){
        player_pos=Math.round(player_pos+deix);
    }
    else{
        var i;
        for(i=0;i<enemies.length;++i){
            if(enemies[i].id==ruchid){
                enemies[i].pos=Math.round(enemies[i].pos+deix);
                break;
            }
        }
    }
    ruchupdate=Math.round(ruchupdate-deix);
}

function rysujStanPolaczenia(k){
    if(polaczano){
        k.fillStyle="rgb(50,100,50)";
        k.fillText("Connected :)",5,35);
        return;
    }
    else{
        k.fillStyle="rgb(255,0,0)";
        k.fillText("Disconnected :((( !!!",5,35);
        if(laczenieInterwal>0){
            laczenieInterwal-=1;
            return
        }
        wsConnect();
        laczenieInterwal=10;
    }
}

/*rysuje kolejne warstwy animacji i uaktualnia zmienne na następną klatkę*/
function animuj(){
	var k=pl.getContext('2d');
	k.fillStyle="rgb(150,200,250)";
	k.fillRect(0,0,1000,800);//niebo
	rysujTeren(k);
        if(player_pos>0)
            rysujGracza(k);
	rysujWrogow(k);
	rysujLifeBar(k);
        rysujTrajektorie(k);
        rysujStanPolaczenia(k);
        graczeUpdate();
	ruszGracza();
        naprawdeRuszKogos();
        
	
}

/*Nie rusza gracza tylko "oko" - pozwala oglądać planszę większą niż okno.
 */
function ruszGracza(){

    playerEye=playerEye+czyn>dlugoscplanszy-500?dlugoscplanszy-500:playerEye+czyn<500?500:
		playerEye+czyn;
}


/*
 * Obsługa naciśnięcia klawisza na klawiaturze
 */
function wcisniecieHandler(e){
	if(e.keyCode==37)//strzałka w lewo - przesunięcie "oka" w lewo
		czyn=-20;
	else if(e.keyCode==39) //strzałka w prawo - przesunięcie "oka" tamże
		czyn=20;
	else if(e.keyCode==38) //strzałka w górę - ruch lufy do góry
		alfaD=-5;
	else if(e.keyCode==40) //strzałka w dół - ruch lufy w dół
		alfaD=5;
}

/*
 * Obsługa puszczenia klawisza na klawiaturze
 */
function puszczenieHandler(e){
	if(e.keyCode==37 || e.keyCode==39)//strzałka w lewo/prawo - stop przesunięcia "oka"
		czyn=0;
	else if(e.keyCode==38 || e.keyCode==40)//strzałka w górę/dół - stop ruchu lufy
		alfaD=0;
}

/*
 * Wysyła wiadomość o wykonaniu strzału
 */
function strzal(){
    if(leci || player_pos==0 || ruchupdate!=0 || reload!=0)
        return;
    var pbv=powerBarValue*2;
    var wysylka=player_pos+" "+alfa+" "+pbv;
    //alert("wysylam: "+wysylka);
    ws.send(wysylka);
    reload=pbv<30?30:pbv>80?80:pbv;
    //ws.close();
}

/*
 * Obsługa wciśnięcia klawisza myszy
 */
function wcisniecieMHandler(e){
    if(e.which==1){//lewy klawisz - zwiększanie mocy strzału
	powerBar=true;
	powerBarValue=5;
    }
    else if(e.which>=2){//inny klawisz - ruch do przodu/do tyłu
        if(!leci && ruchupdate==0 && player_pos>0 && reload==0){
            if(alfa>-90 && player_pos<dlugoscplanszy-50){
                ws.send("Ruch "+myId+" 1");//do przodu
                reload=40;
            }
            else if(player_pos>50){
                ws.send("Ruch "+myId+" -1");//do tyłu
                reload=40;
            }
        }
            
    }
}

/*
 * Obsługa puszczenia klawisza myszy
 */
function puszczenieMHandler(e){
    if(e.which==1){//lewy km. strzał z aktualną siłą
	powerBar=false;
        strzal();
    }
}

var nowiGracze; //tablica w której czekają uaktualnione dane graczy otrzymane z serwera
var gracze_update=false; //czy należy uaktualnić dane gracza i przeciwników teblicą nowiGracze
var trupy=new Array();//tablica czołgów które należy usunąć z listy i wyświetlić ich eksplozje

/*
 * Zamienia dane gracza i przeciwników na nowe gdy (są jakieś nowe oraz )animacja lotu pocisku zakończona
 */
function graczeUpdate(){
    if(leci && trajktora<trajx.length-1)
        return;
    if(gracze_update){
        var i;
        enemies=new Array();
        for(i=1;i<nowiGracze.length;i+=4) {
            if(parseInt(nowiGracze[i+3])!=myId)
                enemies.push({imie: nowiGracze[i],
			pos: parseInt(nowiGracze[i+1]),
			life: parseInt(nowiGracze[i+2]),
                        id: nowiGracze[i+3]});
            else{
                player_pos=parseInt(nowiGracze[i+1]);
                player_life=parseInt(nowiGracze[i+2]);
            }
        }
        gracze_update=false;
    }
    if(trupy.length>0){
        var j;
        var czysc=true;
        for(j in trupy){
            if(trupy[j].t!=0){
                czysc=false;
                if(trupy[j].gdzie==player_pos){
                    fieryDeath();
                }
            }
            if(trupy[j].t==-1)
                trupy[j].t=3;
        }
        if(czysc)
            trupy=new Array();
    }
}

/*
 * Konsekwencje otrzymania wiadomości o śmierci gracza
 */
function fieryDeath(){
    player_pos=0;
}

/*
 * Obsługa otrzymania wiadomości przez web socket
 */
function recv(e){
    //alert(e.data);
    var data=e.data.split(" ");
    if(data[0]=="Komunikat")
        alert(e.data);
    else if(data[0]=="Gracze"){
        nowiGracze=data;
        gracze_update=true;
    }
    else if(data[0]=="Trajektoria"){
        
        leci=true;
        trajx=new Array();
        trajy=new Array();
        trajktora=0;
        var i;
        for(i=1;i<data.length;i+=2){
            if(parseInt(data[i])>-100 && parseInt(data[i])<10000 )
                trajx.push(parseInt(data[i]));
            if(parseInt(data[i+1])>-100 && parseInt(data[i+1])<10000 )
                trajy.push(parseInt(data[i+1]));
        }
    }
    else if(data[0]=="Padl"){
        var z={gdzie: parseInt(data[1]),t: -1};
        trupy.push(z);
    }
    else if(data[0]=="Ruch"){
        ruchid=Number(data[1]);
        ruchupdate=Number(data[2]);
    }
}

/*
 * Wykonuje się po uzyskaniu połączenia przez web socket
 */
function przywitajSie(evt){
    //alert("Connection open...");
    polaczano=true;
    ws.send("Uklony "+graid+" "+myId);
}


function uciekaj(){
    if(polaczano)
        ws.send("Imout");
}