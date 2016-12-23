/**
 * Kinga Krata 2016-11-21.
 */


package com.GO;


public class Board implements BoardI {

    private int size;
    private double pointsBlack;
    private double pointsWhite;
    private Play play;
    private PLACE GameTable[][];
    private PLACE DeleteGameTable[][];
    private PLACE BlackTerritoryTable[][];
    private PLACE WhiteTerritoryTable[][];
    //Who's Territory
    private boolean isWhom[][];
    //Table of death pools
    private boolean TableofDeath[][];
    //Table of Territory of Player
    private PLACE TerritoryTable[][];

    private int[] nextCoordinates;
    //------------------------------
    private int[] koSituationXY = new int[2];
    private boolean ko_detected;
    private int error_option;
    private boolean one_time_calculate = true;
    //------------------------------
    public Board(int size,Play play)
    {
        this.size=size;
        this.play=play;
        GameTable=new PLACE[size][size];
        DeleteGameTable = new PLACE[size][size];
        //BlackTerritoryTable = new PLACE[size][size];
        //WhiteTerritoryTable = new PLACE[size][size];
        //Czyje Terytorium true - Czarny false - Biały
        isWhom = new boolean[size][size];
        //Tablica Martwych Pól
        /*
        Tablica martwych pól:
        pole martwe -> pole martwe = czarne -> +1 dla białego
        pole martwe -> pole martwe = białe -> +1 dla czarnego
        + Grafika dla rysowania martwych pól -> jeżeli martwe -> narysuj jakąś kropke,
        martwe to gdy nie równa się EMPTY;
         */
        //+6.5 points to black player
        pointsBlack = 6.5;
        pointsWhite = 0.0;
        TableofDeath = new boolean[size][size];
        //Table of Territory
        TerritoryTable = new PLACE[size][size];

        initialize();
        nextCoordinates=new int[4];

        //Tests
        //TerritoryTable[2][2] = PLACE.BLACK;
        //TableofDeath[1][1] = true;
        //TableofDeath[2][2] = true;

        koSituationXY[0] = -1;
        koSituationXY[1] = -1;
    }

    /**
     * initialize board with empty places
     */
    private void initialize()
    {
        for(int i=0;i<size;++i)
        {
            for(int j=0;j<size;++j)
            {

                GameTable[i][j]=PLACE.EMPTY;
                DeleteGameTable[i][j]=PLACE.EMPTY;
                TableofDeath[i][j] = false;
                isWhom[i][j] = false;
            }
        }
    }

    /**
     * Method that calculates points for player for each dead-group
     */
    public void deleteDeadFromGameTable(){
        for(int i = 0; i < size; i++){
            for(int j=0; j< size; j++){
                if(TableofDeath[i][j]){
                    if(GameTable[i][j] == PLACE.BLACK){
                        pointsWhite+=1.0;
                    }
                    if(GameTable[i][j] == PLACE.WHITE){
                        pointsBlack+=1.0;
                    }
                    GameTable[i][j] = PLACE.EMPTY;
                    TableofDeath[i][j] = false;
                }
            }
        }
    }

    /**
     * Method that sets a selected point as dead
     * @param X takes coordinate X of point
     * @param Y takes coordinate Y of point
     */
    public void markAsDead(int X, int Y){
        if(GameTable[X][Y] != PLACE.EMPTY)
            TableofDeath[X][Y] = true;

    }

    /**
     * Method that sets selected point as not dead
     * @param X takes coordinate X of point
     * @param Y takes coordinate Y of point
     */
    public void unMarkAsDead(int X, int Y){
        TableofDeath[X][Y] = false;
    }

    /**
     * Getter for table of dead points
     * @return table of dead points
     */
    public boolean[][] getDeadTable(){
        return TableofDeath;
    }

    /**
     * Method that marks selected point as Territory of player
     * @param player takes a color of player
     * @param X takes a coordinate X of point
     * @param Y takes a coordinate Y of point
     */
    public void markAsTerritory(PLAYER player, int X, int Y){
        if(GameTable[X][Y] != PLACE.BLACK || GameTable[X][Y] != PLACE.WHITE){
            TerritoryTable[X][Y] = player.playerToPlace();
        }
    }

    /**
     * Method that unmark selected point as Territory of player
     * @param X takes a coordinate X of selected point
     * @param Y takes a coordinate Y of selected point
     */
    public void unMarkAsTerritory(int X, int Y){
        if(GameTable[X][Y] != PLACE.BLACK || GameTable[X][Y] != PLACE.WHITE){
            isWhom[X][Y] = false;

        }
    }

    /**
     * Getter for Territory Table
     * @return table of whos is this territory
     */
    public PLACE[][] getTerritoryTable(){
        return TerritoryTable;
    }

    public void calculateTerritory(){
        if(one_time_calculate) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (TerritoryTable[i][j] == PLACE.BLACK) {
                        pointsBlack++;
                    } else if (TerritoryTable[i][j] == PLACE.WHITE) {
                        pointsWhite++;
                    }
                    //Nulling Table
                    //TerritoryTable[i][j] = PLACE.EMPTY;
                }
            }
            one_time_calculate = false;
        }
    }


    /**
     * Getter for ammount of points White player
     * @return number of points white player
    */

    public double getPoints(PLAYER player){
        if(player == PLAYER.BLACK){
            return pointsBlack;
        }else{
            return pointsWhite;
        }
    }
    /**
     *
     * @param color color of player=stone for which we are asking
     * @param placeX where we want to know if stone can breath
     * @param placeY where we want to know if stone can breath
     * @return returns if stone in given color can breath on given place
     */
    private boolean canBreathHere(PLACE[][] table,PLAYER color, int placeX,int placeY,int IgnoreX,int IgnoreY) {

        PLACE actuall;
        int XY[] = new int[2];
        int tempX;
        int tempY;
        boolean checkifavaliable = false;
        if(!lessandhighbool(placeX,placeY)){
            return false;
        }
        DeleteGameTable[placeX][placeY] = table[placeX][placeY];
        /*
        Edit: Let's assume we have point placeX=11 placeY=11
        Check around points should be: as bellow
                (11,10)
         (10,11)(11,11)(12,11)
                (11,12)
         */
        /*
        Dlaczego nie działa: #GODKILLMEPLX
        Imagine:
        Przy wykryciu wroga na polu X,Y, gdzie ten wróg ma oddech, ale ten zamiast sprawdzenia
        wszystkich swoich pól i pójścia do następnego swojego sprzymierzeńca, robi coś innego, a mianowicie
        sprawdza po kolei pola jeśli napotka na swojego to robi rekurencje od niego
        ale jeśli ta rekurencja zwraca brak oddechu, to również to pole przyjmuje brak oddechu
        pomimo tego że go ma, tylko nie sprawdziło wszystkich swoich pól.

        ROZWIĄZANIE PROBLEMU:?
        priorityQueque ?
        if -> moje puste pola to null to
        if -> mam sąsiada -> idę do niego
        pytanie -> co gdy mam dwoch sasiadow i wejde w tego z brakiem oddechu?
        wejdzie w niego i zwroci null
        wtedy trzeba zrobic countera od liczby sasiadow, jakas strukture ktora przechowuje

         */
        //Sprawdz wszystkich otaczających i zobacz czy to są puste pola, jeśli tak zwróc true
        /*
        Question: Problem czy bedzie trzeba zastosowac Array List ze wzgledu gdy sasiedzi beda obok siebie
        Bo wtedy sasiad moze ignorowac tylko tego ktory przekazal rekurencje do niego
        Answer: No jasne ;-;
         */
        for(int i = 0; i < 4 ; i++){
            XY = values(i);
            tempX = placeX+XY[0];
            tempY = placeY+XY[1];
            if(lessandhighbool(tempX,tempY)){
                try{
                    actuall = table[tempX][tempY];
                    if(actuall.equals(PLACE.EMPTY)){
                        return true;
                    }
                }
                catch(ArrayIndexOutOfBoundsException ex){

                }
            }
        }
        for(int i = 0; i < 4 ; i++){
            XY = values(i);
            tempX = placeX+XY[0];
            tempY = placeY+XY[1];
            if(lessandhighbool(tempX,tempY)){
                actuall = table[tempX][tempY];
                if(actuall.equals(color.playerToPlace())) {
                    if (table[tempX][tempY] != DeleteGameTable[tempX][tempY]) {
                        checkifavaliable = canBreathHere(table, color, tempX, tempY, placeX, placeY);
                    }
                    if (checkifavaliable) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void addStone(PLAYER color, int placeX, int placeY) {
        GameTable[placeX][placeY] = color.playerToPlace();
        canEnemyBreath(GameTable,color,placeX,placeY);
    }

    /**
     * Method that checks if enemy player's pawn can breath after put in
     * X,Y coordinates player's pawn
     * @param GameTable takes a GameTable
     * @param color takes a color of player
     * @param placeX takes X coordinate of pawn
     * @param placeY takes Y coordinate of pawn
     */

    private void canEnemyBreath(PLACE[][] GameTable, PLAYER color, int placeX, int placeY){
        PLAYER enemy;
        int XY[] = new int[2];
        enemy = color.getEnemyColor();
        int tempX;
        int tempY;
        boolean state = true;
        for(int i = 0; i < 4; i++){
            XY = values(i);
            tempX = placeX+XY[0];
            tempY = placeY+XY[1];
            //Dla wszystkich sąsiadów takich, że sąsiad to wróg
            if(lessandhighbool(tempX,tempY)){
                if(GameTable[tempX][tempY] == enemy.playerToPlace()){
                    //System.out.println("FOUND ENEMY AT: X: "+tempX+" Y: "+tempY);
                    //Sprawdź czy sąsiad może oddychać ignoruj przeskok, do samego siebie
                    //Can breathe here sprawdza czy moge postawić na X, Y a nie czy oddycha
                    //Wyzeruj tablice usuwan
                    deleteTableNuller();
                    //I wejdz w pole ktore trzeba sprawdzic czy oddycha
                    state = canBreathHere(GameTable,enemy,tempX,tempY,tempX,tempY);
                    //jeśli nie oddycha wywal to
                    if(state == false){
                        deleteFromTable();
                    }
                    //if(canBreathHere(GameTable,enemy,tempX,tempY,tempX,tempY) == false){
                    //    System.out.println("Gonna kick your ass: X: "+tempX+" Y: "+tempY);
                    //    DeleteGameTable[tempX][tempY] = GameTable[tempX][tempY];
                    //}
                }
            }
        }
        //wynuluj tablice po zakonczeniu pracy
        deleteTableNuller();
    }
    private void deleteFromTable(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (DeleteGameTable[i][j] == GameTable[i][j])
                    GameTable[i][j] = PLACE.EMPTY;
                DeleteGameTable[i][j] = PLACE.EMPTY;
            }
        }
    }
    private void deleteTableNuller(){
        for(int j = 0; j < size ; j++) {
            for (int z = 0; z < size; z++) {
                DeleteGameTable[j][z] = PLACE.EMPTY;
            }
        }
    }

    /**
     * Method that checks if position on X,Y is empty if yes, then it allows to
     * give there a pawn, unless some other functions detects errors in this action
     * @param table takes a Game Table
     * @param color takes a color of player
     * @param X takes a X coordinate of pawn in gameboard
     * @param Y takes a Y coordinate of pawn in gameboard
     * @return status of pawn(empty or not)
     */
    private boolean checkifempty(PLACE[][] table, PLAYER color, int X, int Y){
        PLACE actuall;
        actuall = GameTable[X][Y];
        if(actuall != PLACE.EMPTY)
            return false;
        return true;
    }

    @Override
    public boolean canAddHere(PLAYER color, int placeX, int placeY) {
        //Can only add on empty place
        if(!lessandhighbool(placeX,placeY)){
            return false;
        }
        if(!checkifempty(GameTable,color,placeX,placeY)){
            error_option = 1;
            return false;
        }
        if(!isItNotA_KO(color,placeX,placeY)){
            error_option = 2;
            return false;
        }
        if(canBreathHere(GameTable,color,placeX,placeY,placeX,placeY)){
           nullKO_situation();
          // System.out.println("\nI can breathe at here <3\n");
           return true;
        }
       //Check sicmering
        if(canBreatheAfterSicmering(color, placeX, placeY)) {
            System.out.println("\nI can breathe here - Simmeric <3\n");
            return true;
        }else{
            System.out.println("Inavild");
            GameTable[placeX][placeY] = PLACE.EMPTY;
            error_option = 3;
            return false;
        }
        //return false;
    }

    /**
     * Getter for Error Message
     * @return error option
     */
    public int getErrorMessage(){
        return error_option;
    }

    /**
     * Method that return status of checking if player pawn can breath after put it on a place, where
     * it has no right to be, but in situation, in which it will erase other player's
     * pawn/pawns it will be placed
     * @param color takes a color of player
     * @param placeX takes a X coordinate of pawn, where player demands to place it
     * @param placeY takes a Y coordiante of pawn, where player demands to place it
     * @return status of checking
     */
    public boolean canBreatheAfterSicmering(PLAYER color, int placeX, int placeY){
        PLAYER enemyplayer;
        //pentla for na tempboard[][]
        PLACE tempboard[][] = GameTable;
        int[] XY = new int[2];
        for(int i = 0; i <size ; i++) {
            for(int j =0; j < size; j++) {
                tempboard[i][j] = GameTable[i][j];
            }
        }
        tempboard[placeX][placeY] = color.playerToPlace();
        int ko_state_counter = 0;
        boolean one_time = true;
        boolean dead = false;
        boolean cantBreath;
        //if KO situation
        if(!isItNotA_KO(color,placeX,placeY)){
            //if yes
            return false;
        }
        //nuller to KO
        nullKO_situation();
        //if KO wasn't detected then do normal checkout
        //Set enemy colour
        enemyplayer = color.getEnemyColor();
        for(int i = 0; i<4 ; i++){
            XY = values(i);
            cantBreath = canBreathHere(tempboard,enemyplayer, placeX+XY[0],placeY+XY[1],placeX,placeY) == false;
            System.out.println("State: "+cantBreath);
            if(cantBreath){
                //then ++ ko_state_counter
                //Unused-Dunno-Where-To-Put
              //  System.out.println("X:"+(placeX+XY[0])+ "Y:"+(placeY+XY[1]));
                ko_state_counter++;
                if(ko_state_counter == 1 && one_time) {
                    koSituationXY[0] = placeX + XY[0];
                    koSituationXY[1] = placeY + XY[1];
                    one_time = false;
                } else if (ko_state_counter > 1){
                    nullKO_situation();
                }
                dead = true;
            }

        }
        return dead;
    }

    /**
     * Method that checks
     * @param color takes a color of player
     * @param placeX takes a coordinate X of specific point
     * @param placeY takes a coordinate Y of specific point
     * @return status of
     */
    public boolean isItNotA_KO(PLAYER color, int placeX, int placeY){
       if( placeX == koSituationXY[0] && placeY == koSituationXY[1]) {
          // System.out.println("KO Situation detected!");
           ko_detected = true;
           return false;
       }
       return true;
    }

    private void nullKO_situation(){
        ko_detected = false;
        koSituationXY[0] = -1;
        koSituationXY[1] = -1;
    }

    /**
     * Method that in dependence of value from 1 to 4 returns borderer of
     * specific pawn
     * @param i takes a number from 1 to 4
     * @return X and Y coordinate of neightboor of pawn
     */
    private int[] values(int i){
        int p[] = new int[2];
        switch(i) {
            case 1:
                p[0] = 1;
                p[1] = 0;
                break;
            case 2:
                p[0] = -1;
                p[1] = 0;
                break;
            case 3:
                p[0] = 0;
                p[1] = 1;
                break;
            default:
                p[0] = 0;
                p[1] = -1;
                break;
        }
        return p;
    }
    //TODO: Add nuller to KO_SITUATION
    /*
     Problem: after detecting KO situation it returns that PLACE.color cant be added here
     and after diffrent move of that player we need to null integer ko_situationXY
     (by saying null, assume to set it to -1) so it does not attach any field on game board
     */

    /**
     * Getter for point where KO SITUATION OCCURS
     * @return point where ko situation occurs
     */
    public int[] getKO_Points(){
        return koSituationXY;
    }
    public boolean getKO_Status(){
        return ko_detected;
    }

    @Override
    public int howMuchteritory(PLAYER color) {
        /*
        Opis implementacji:
        1.Weź dowolny pusty punkt na planszy, rekurencja, wywołanie po wszystkich sąsiadach(4) -> odnotuj punkt
        2.  a) jeżeli wywołanie rekurencyjne jest niepuste -> odnotuj do innej tabeli, nie rób dalej rekurencji
        2   b) jeżeli wywołanie rekurencyjne jest puste -> odnotuj, wywołaj rekurencje
                a .1) jeżeli wywołanie dotyczy pola które zostało już wywołane rekurencyjnie zaniechaj
        3. jeżeli liczba odnotowanych pól jest mniejsza niż połowa planszy to wyszukaj pasmo blokujące dalszy rozwój
        rekurencji i określ jego charakter (kolor)
            a) możliwa automatyczna weryfikacja i przerwanie wykoanania gdy >= polowa planszy
        4. powtórz operacje dla dowolnego punktu należącego do game board ale nie odnotowanego wśród pustych
         */
        return 0;
    }

    /*
    Algorytm:
    1.Gracz wybiera punkt, i przekazuje siebie(jako kolor)
    Sprawdzamy wszystkich sąsiadów danego punktu, jeżeli
    jest to gracz przeciwny lub pole puste to przypisujemy dane pole
    do tabeli tymczasowej i dalej wywołujemy rekurencje aż do natrafienia na
    kolor gracza, pole to dodajemy do tabeli tymczasowej o nazwie obwód, gdy cała rekurncja się skończy
    sprawdzamy czy pola z tabeli obwód tworzą go rzeczywiście tj.

    a) wybieramy punkt o najmniejszym X i Y (taki istnieje tylko jeden) i sprawdzamy czy ma sąsiadów
    sąsiadów. Dodajemy to wszystko do tabeli tymczasowej żeby ponownie nie wejść do nich.

    b) Waruenk jeżeli dowolne pole z sąsiadem różni się dokładnie o jedną wartość X/Y oraz
     ma co najmniej dwóch sąsiadów to mamy obwód: Złożoność: O(n), dokładniej 4n
        1.b) Wybieramy po kolei każde pole i sprawdzamy, z czym że sąsiad będzie oznaczało również pole po
        przekątnej

        (a,b)       (a+1,b)         (a+2,b)
        (a,b-1)     (/a+1/,/b-1/)   (a+2,b-1)
        (a,b-2)     (a+1,b-2)       (a+2,b-2)

        2.b) Druga implementacja:
            2.b) 1. Jeżeli mamy dany punkt najmniejszy, to wśród sąsiadów wybieramy najbliżej jemu pasujący
            (o najmniejszych wartościach X,Y) i idziemy do niego -> powtarzamy rekurencje
            Gdyby powyższa metoda była nie prawdziwa to nie mielibyśmy do czynienia z obwodem, a ten zakładamy.
            Czyli gdy coś pójdzie nie tak to wiemy że to nie jest obwód.
            Adnote: Osobny przypadek dla obwodów, tworzonych również z osiami X oraz Y planszy gry, wtedy trzeba
            założyć że istnieje linia która łączy dowolne dwa kamienie po tej osi.
                 2. Priorytet kładziemy na przejściu do współrzędnej która jest mniejsza w punkcie najmniejszym
                 3. Jeżeli współrzędne są sobie równe w punkcie o najmniejszych współrzędnych, dowolność wyboru
                 Złożoność: W najgorszym przypadku n/2 a dokładniej wykonamy mniej więcej
                 |x[1] - b[1]| + |x[2] - b[2]| przejść, gdzie "x" - to współrzędne punktu o najmniejszych wartościach
                 a gdzie "b" to współrzędne dowolnego punktu, wartość zmniejszona jeżeli przeskakujemy po przekątnej.

     */
    //@Override
    public void giveToMyTerritory(PLAYER player, int placeX, int placeY) {
        boolean isEmpty = GameTable[placeX][placeY] == PLACE.EMPTY;
        if(isEmpty){
            if(player == PLAYER.BLACK){
                //if(isInAreaOfControl())
                BlackTerritoryTable[placeX][placeY] = GameTable[placeX][placeY];
            }
            else {
                WhiteTerritoryTable[placeX][placeY] = GameTable[placeX][placeY];
            }
        }
    }
    public boolean showTerritory(){
        return true;
    }

    @Override
    public PLACE[][] getGameTable() {
        return GameTable;
    }

    /**
     * Method that returns color of actual player
     * @return color of actual player
     */
    public PLAYER getPLayerColor()
    {
        return play.get_player_color();
    }

    /**
     * Method responsiable for checking if putted stone is on right position
     * and doesnt not overflow
     * @param tempX get X of point
     * @param tempY get Y of point
     * @return boolean which tells is operation of insert legal
     */
    private boolean lessandhighbool(int tempX, int tempY){
        return ((tempX>=0 && tempX <=(size-1)) && (tempY >=0 && tempY <=(size-1)));
    }
}
