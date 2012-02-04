/* 
 *Ten skrypt obsługuje rozwijane elementy storny - na razie jest jeden,
 *ma być jeszcze highscores - oraz rysuje miniaturki aren na płótnach
 */

/*zwijacz-rozwijacz*/
function arenyKlik(){
    var bylo=document.getElementById("areny").style.display;
    if(bylo=="none"){
        document.getElementById("areny").style.display="block";
    }
    else{
        document.getElementById("areny").style.display="none";
    }
        
}

/*rysowanie miniatur + dopisywanie id aren do właściwych ukrytych inputów*/
function siu(){
    var kanwasy=document.getElementsByTagName("canvas");
    var k=0;
    for(k=0;k<kanwasy.length;++k){
        var tagname="forma"+k+":dokad"+k;
        document.getElementById(tagname).value=k;
        kx=kanwasy[k].getContext('2d');
        var ly=kanwasy[k].innerHTML.split(" ");
        var y=new Array();
        var j;
        for(j=0;j<ly.length;++j){
            if(parseInt(ly[j])>0 && parseInt(ly[j])<800)
                y.push(Math.floor(parseInt(ly[j])*240/800));
        }
        kx.fillStyle="rgb(150,200,250)";
	kx.fillRect(0,0,kanwasy[k].width,kanwasy[k].height);
        var i=0;
        kx.moveTo(0,240);
        kx.beginPath();
        for(i=0;i<y.length;++i){
            kx.lineTo(Math.floor(i*(320/y.length)),240-y[i]);
        }
        kx.lineTo(320,240-y[i-1]);
        kx.lineTo(320,240);
        kx.lineTo(0,240);
        kx.stroke();
        kx.fillStyle="rgb(150,200,100)";
	kx.fill();
        
    }
}
