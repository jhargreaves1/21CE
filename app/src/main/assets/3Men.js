// =====================
var N_Sel, rowSelected, colSelected, gameOver, Size=3, Start, startPlayer, pieceType;
var MoveCount, MaxMoveCount, MoveVal, MaxMove=81;
Level = new Array(2);
//default player1 set to human
Level[0]=0;
//default player2 set to level2
Level[1]=2;
//default starting player is player1
startPlayer = 0;
//default piecetype=0 (stones)
pieceType = 0;

//create 2x3x2 array for pieces to contain (player#,piece#,row,col)
Piece = new Array(2);
for (i=0; i < 2; i++)
{ 
	Piece[i]=new Array(Size); 
}
for (i=0; i < 2; i++)
{ for (j=0; j < Size; j++)
  { 
	  Piece[i][j]=new Array(2); 
  }
}
// history array to store moves
History=new Array(MaxMove+9);
for (i=0; i<MaxMove; i++)
  History[i]=new Array(3);
Fld = new Array(Size);
for (i=0; i < Size; i++)
{ 
	Fld[i]=new Array(Size); 
}


// =========================
// =========================
function NewGame()
{ var playerNum, piece;
  Start = startPlayer;
  SetPieceType(pieceType);
  //set all pieces to -1
  for (playerNum=0; playerNum < 2; playerNum++)
  { for (piece=0; piece < Size; piece++)
    { Piece[playerNum][piece][0]=-1; 
      Piece[playerNum][piece][1]=-1;
    }
  }
  //set board to -1
  for (row=0; row < Size; row++)
  { for (col=0; col < Size; col++)
      Fld[row][col]=-1; 
  }
  N_Sel=-1;
  gameOver=0;
  MoveCount=0;
  MaxMoveCount=0;
  RefreshScreen();
}

// =========================
function SetPieceType(type){
	if(type==0)
		typeString="piece_"; //stones
		else
		typeString="cap_"; //bottle caps
		
	//create array of images for selected pieces
	Pic= new Array(5);
	for (i=0; i<5; i++)
	{ Pic[i] = new Image(); 
		Pic[i].src = "images/" + typeString + i + ".png";
	} 
}

// =========================


function SetStartPlayer(selected)
{ 
	startPlayer=selected; 
}

// =========================
function SetLevel(player, selected)
{ 
	Level[player]=selected;
}

// =========================

function SelectPieceType (optionpieceNumumber)
{
	pieceType = optionpieceNumumber;
	}
// =========================

function Timer()
{ if (gameOver) return;
  var playerNum=(MoveCount+Start)%2;
  if (Level[playerNum]==0) return; //human
  if (MoveCount<2*Size)
  { if (N_Sel<0)
    { GetBestMove(playerNum, Level[playerNum]);
      return;
    }
  }
  if (N_Sel<0)
  { GetBestMove(playerNum, Level[playerNum]);
    var row=Piece[playerNum][N_Sel][0];
    var col=Piece[playerNum][N_Sel][1];
    Fld[row][col]+=2;
    RefreshPic(row, col);
  } 
  else
    MakeMove(playerNum, N_Sel, rowSelected, colSelected);
}

// =========================
function Back()
{ if (N_Sel>=0) return;
  if (MoveCount==0) return;
  gameOver=0;
  MoveCount--;
  var playerNum=(MoveCount+Start)%2;
  var pieceOne=History[MoveCount][0];
  var pieceTwo=History[MoveCount][1];
  var pieceThree=History[MoveCount][2];
  Fld[Piece[playerNum][pieceOne][0]][Piece[playerNum][pieceOne][1]]=-1;
  RefreshPic(Piece[playerNum][pieceOne][0], Piece[playerNum][pieceOne][1]);
  Piece[playerNum][pieceOne][0]-=pieceTwo;
  Piece[playerNum][pieceOne][1]-=pieceThree;
  if (MoveCount>2*Size-1)
  { Fld[Piece[playerNum][pieceOne][0]][Piece[playerNum][pieceOne][1]]=playerNum;
    RefreshPic(Piece[playerNum][pieceOne][0], Piece[playerNum][pieceOne][1]);    
  }
  if (MoveCount<10)
    window.document.OptionsForm.Moves.value=" "+MoveCount+" ";
  else
    window.document.OptionsForm.Moves.value=MoveCount;
}
// =========================

function Replay()
{ if (N_Sel>=0) return;
  if (MoveCount>=MaxMoveCount) return;
  var playerNum=(MoveCount+Start)%2;
  var pieceOne=History[MoveCount][0];
  var pieceTwo=History[MoveCount][1];
  var pieceThree=History[MoveCount][2];
  if (MoveCount>=2*Size)
  { Fld[Piece[playerNum][pieceOne][0]][Piece[playerNum][pieceOne][1]]=-1;
    RefreshPic(Piece[playerNum][pieceOne][0], Piece[playerNum][pieceOne][1]);
  }
  Piece[playerNum][pieceOne][0]+=pieceTwo;
  Piece[playerNum][pieceOne][1]+=pieceThree;
  Fld[Piece[playerNum][pieceOne][0]][Piece[playerNum][pieceOne][1]]=playerNum;
  RefreshPic(Piece[playerNum][pieceOne][0], Piece[playerNum][pieceOne][1]);    
  gameOver=OverTest(playerNum);
  MoveCount++;
  if (MoveCount<10)
    window.document.OptionsForm.Moves.value=" "+MoveCount+" ";
  else
    window.document.OptionsForm.Moves.value=MoveCount;
  if (gameOver>0)
  { if (playerNum==0){
	  var div = document.getElementById("textDiv");  
		div.textContent = "Player 1 has won !";  
		var text = div.textContent;
  } //alert("Player 1 has won !");
    else {
		var div = document.getElementById("textDiv");  
		div.textContent = "Player 2 has won !";  
		var text = div.textContent;
	}//alert("Player 2 has won !");
    return;
  }
  if (MoveCount==MaxMove)
  { gameOver=1;
		var div = document.getElementById("textDiv");  
		div.textContent = "It's a draw";  
		var text = div.textContent;
    //alert("It's a draw !");
  }
}

// =========================
function MakeMove(playerNum, pieceOne, pieceTwo, pieceThree)
{ if (MoveCount>=2*Size)
  { Fld[Piece[playerNum][pieceOne][0]][Piece[playerNum][pieceOne][1]]=-1;
    RefreshPic(Piece[playerNum][pieceOne][0], Piece[playerNum][pieceOne][1]);
  }
  Piece[playerNum][pieceOne][0]+=pieceTwo;
  Piece[playerNum][pieceOne][1]+=pieceThree;
  Fld[Piece[playerNum][pieceOne][0]][Piece[playerNum][pieceOne][1]]=playerNum;
  RefreshPic(Piece[playerNum][pieceOne][0], Piece[playerNum][pieceOne][1]);
  if (MoveCount<9)
    window.document.OptionsForm.Moves.value=" "+eval(MoveCount+1)+" ";
  else
    window.document.OptionsForm.Moves.value=eval(MoveCount+1);
  if (History[MoveCount][0]!=pieceOne)
  { History[MoveCount][0]=pieceOne;
    MaxMoveCount=MoveCount+1;
  }
  if (History[MoveCount][1]!=pieceTwo)
  { History[MoveCount][1]=pieceTwo;
    MaxMoveCount=MoveCount+1;
  }
  if (History[MoveCount][2]!=pieceThree)
  { History[MoveCount][2]=pieceThree;
    MaxMoveCount=MoveCount+1;
  }
  gameOver=OverTest(playerNum);
  MoveCount++;
  if (MaxMoveCount<MoveCount)
    MaxMoveCount=MoveCount;
  N_Sel=-1;
  if (gameOver>0)
  { if (playerNum==0){ //alert("Player 1 has won !");
	var div = document.getElementById("textDiv");  
    div.textContent = "Player 1 has won !";  
    var text = div.textContent;  
	}
    else{ //alert("Player 2 has won !"); 
		var div = document.getElementById("textDiv");  
		div.textContent = "Player 2 has won !";  
		var text = div.textContent;
	}
    return;
  }
  if (MoveCount==MaxMove)
  { gameOver=1;
    //alert("It's a draw !");
	var div = document.getElementById("textDiv");  
		div.textContent = "It's a draw!";  
		var text = div.textContent;
  }
}

// =========================
function OverTest(playerNum)
{ var row, col, pieceNum, ss;
  for (row=0; row<Size; row++)
  { ss=0;
    for (col=0; col<Size; col++)
    { if (Fld[row][col]==playerNum) ss++;
    }
    if (ss==Size) return(1);
  }
  for (col=0; col<Size; col++)
  { ss=0;
    for (row=0; row<Size; row++)
    { if (Fld[row][col]==playerNum) ss++;
    }
    if (ss==Size) return(1);
  }

  for (pieceNum=0; pieceNum<Size; pieceNum++)
  { row=Piece[1-playerNum][pieceNum][0];
    if (row<0) return(0);
    col=Piece[1-playerNum][pieceNum][1];
    if (GetFld(row-1, col)==-1) return(0);
    if (GetFld(row+1, col)==-1) return(0);
    if (GetFld(row, col-1)==-1) return(0);
    if (GetFld(row, col+1)==-1) return(0);
  }
  return(1);
}

// =========================
function GetBestMove(playerNum, ll)
{ var row, col, dd_i, dd_j, pieceNum, vv, row_best, col_best, pieceNum_best, vv_best=-10000, playerNum1=1-playerNum, ll1=ll-1;
  for (pieceNum=0; pieceNum<Size; pieceNum++)
  { row=Piece[playerNum][pieceNum][0];
    col=Piece[playerNum][pieceNum][1];
    if (row<0)
    { for (dd_i=0; dd_i<Size; dd_i++)
      { for (dd_j=0; dd_j<=Size; dd_j++)
        { if (GetFld(dd_i,dd_j)==-1)
          { Piece[playerNum][pieceNum][0]=dd_i;
            Piece[playerNum][pieceNum][1]=dd_j;
            Fld[dd_i][dd_j]=playerNum;
            vv=OverTest(playerNum);
            if (vv>0)
            { Piece[playerNum][pieceNum][0]=-1;
              Piece[playerNum][pieceNum][1]=-1;
              Fld[dd_i][dd_j]=-1;
              N_Sel=pieceNum;
              rowSelected=dd_i+1;
              colSelected=dd_j+1;
              return(1000+ll*200);
            }
            if (ll>0) vv=-GetBestMove(playerNum1, ll1); 
            vv+=Math.floor(Math.random()*19);
            if ((ll>1)&&(dd_i==1)&&(dd_j==1))
              vv+=10;
            if (vv_best<vv)
            { vv_best=vv;
              pieceNum_best=pieceNum;
              row_best=dd_i;
              col_best=dd_j;
            }
            Piece[playerNum][pieceNum][0]=-1;
            Piece[playerNum][pieceNum][1]=-1;
            Fld[dd_i][dd_j]=-1;
          }
        }
      }
      N_Sel=pieceNum_best;
      rowSelected=row_best+1;
      colSelected=col_best+1;
      return(vv_best);
    }
  }
  for (pieceNum=0; pieceNum<Size; pieceNum++)
  { row=Piece[playerNum][pieceNum][0];
    col=Piece[playerNum][pieceNum][1];
    for (dd_i=-1; dd_i<=1; dd_i++)
    { for (dd_j=-1; dd_j<=1; dd_j++)
      if ((dd_i+dd_j+2)%2==1)
      { if (GetFld(row+dd_i,col+dd_j)==-1)
        { Fld[row][col]=-1;
          Piece[playerNum][pieceNum][0]+=dd_i;
          Piece[playerNum][pieceNum][1]+=dd_j;
          Fld[row+dd_i][col+dd_j]=playerNum;
          vv=OverTest(playerNum);
          if (vv>0)
          { Fld[row][col]=playerNum;
            Piece[playerNum][pieceNum][0]-=dd_i;
            Piece[playerNum][pieceNum][1]-=dd_j;
            Fld[row+dd_i][col+dd_j]=-1;
            N_Sel=pieceNum;
            rowSelected=dd_i;
            colSelected=dd_j;
            return(1000+ll*200);
          }
          if (ll>0) vv=-GetBestMove(playerNum1, ll1); 
          vv+=Math.floor(Math.random()*19);
          if (vv_best<vv)
          { vv_best=vv;
            pieceNum_best=pieceNum;
            row_best=dd_i;
            col_best=dd_j;
          }
          Fld[row][col]=playerNum;
          Piece[playerNum][pieceNum][0]-=dd_i;
          Piece[playerNum][pieceNum][1]-=dd_j;
          Fld[row+dd_i][col+dd_j]=-1;
        }
      }
    }
  }
  N_Sel=pieceNum_best;
  rowSelected=row_best;
  colSelected=col_best;
  return(vv_best);
}     

// =========================
function GetFld(pieceNum, playerNum)
{ if (pieceNum<0) return(-2);
  if (pieceNum>=Size) return(-2);
  if (playerNum<0) return(-2);
  if (playerNum>=Size) return(-2);
  return(Fld[pieceNum][playerNum]);
}
// =========================

function Clicked(row, col)
{ var pieceNum, playerNumove=(MoveCount+Start)%2;
  if (gameOver>0) return;
  if (Level[playerNumove]>0) return;
  if ((N_Sel<0)&&(MoveCount>=2*Size))
  { for (pieceNum=0; pieceNum<Size; pieceNum++)
    { if ((Piece[playerNumove][pieceNum][0]==row)&&(Piece[playerNumove][pieceNum][1]==col))
      { N_Sel=pieceNum;
        Fld[row][col]+=2;
        RefreshPic(row, col);
        return;
      }
    }
  }
  else
  { if (MoveCount<2*Size)
    { if (Fld[row][col]>=0) return;
      N_Sel=Math.floor(MoveCount/2);
      MakeMove(playerNumove, N_Sel, row-Piece[playerNumove][N_Sel][0], col-Piece[playerNumove][N_Sel][1]);
      return;
    }
    if ((Piece[playerNumove][N_Sel][0]==row)&&(Piece[playerNumove][N_Sel][1]==col))
    { N_Sel=-1;
      Fld[row][col]-=2;
      RefreshPic(row, col);
      return;
    }
    if (Fld[row][col]>=0) return;
    if (Piece[playerNumove][N_Sel][0]==row)
    { if (Math.abs(col-Piece[playerNumove][N_Sel][1])==1)
      { MakeMove(playerNumove, N_Sel, 0, col-Piece[playerNumove][N_Sel][1]);
        return;
      }
      return;
    }
    if (Piece[playerNumove][N_Sel][1]==col)
    { if (Math.abs(row-Piece[playerNumove][N_Sel][0])==1)
      { MakeMove(playerNumove, N_Sel, row-Piece[playerNumove][N_Sel][0], 0);
        return;
      }
      return;
    }
  }
} 

// =========================
function RefreshPic(row, col)
{ 
	window.document.images[(2*Size-1)*2*row+2*col].src = Pic[Fld[row][col]+1].src;
}

// =========================
  
function RefreshScreen()
{ var row, col;
  for (row=0; row < 2*Size-1; row++)
  { for (col=0; col < 2*Size-1; col++) 
      window.document.images[(2*Size-1)*row+col].src = Pic[0].src;
  }  
  if (MoveCount<10)
    window.document.OptionsForm.Moves.value=" "+MoveCount+" ";
  else
    window.document.OptionsForm.Moves.value=MoveCount;
}

// =========================

function Rules()
{
	window.open('3MenRules.htm');
}

// =========================