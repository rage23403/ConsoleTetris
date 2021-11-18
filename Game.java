import java.util.Random;
/**
 * Write a description of class Game here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Game
{
    static String[] arg;
    static TetrisField playArea;
    static TetrisPiece next;
    static Random ran = new Random();
    static boolean game = true;
    static long cycleTime;
    static long start;
    static boolean right = false;
    static boolean down = false;
    static boolean left = false;
    static boolean ror = false;
    static boolean rol = false;
    static boolean held = false;
    static Thread gameThread = new Thread(){
            public void run(){
                GameLoop();
            }
        };

    public static void Main(String[] args){
        System.gc();
    }

    public static void GameLoop(){
        playArea = new TetrisField(12, 10, new Insets(3), NEXT);
        TetrisPiece current = GeneratePiece(T);
        current.x = playArea.columns/2;
        current.y = 0;
        start = System.currentTimeMillis();
        GenerateNext();
        while(game){
            if(cycleTime > 50000){
                start = System.currentTimeMillis();
                cycleTime = 0;

                if(!MoveDown(current)){current = null;}
                if(!playArea.IsValidMove(next,0,0)){
                    game = false;
                }
            }
            if(current != null){
                if(right && !held){
                    MoveRight(current);
                    held = true;
                }else if(left && !held){
                    MoveLeft(current);
                    held = true;
                } else if(down && !held){
                    if(!MoveDown(current)){current = null;}
                    if(!playArea.IsValidMove(next,0,0)){
                        game = false;
                    }
                    held = true;
                } else if(ror && !held){
                    ROR(current);
                    held = true;
                } else if(rol && !held){
                    ROL(current);
                    held = true;
                }
            }
            else if(current == null){
                int i = playArea.checkLines();
                if(i > 0){
                    playArea.BreakLines(i);
                }
                current = next;
                GenerateNext();
            }
            playArea.PrintField(current);
            cycleTime += System.currentTimeMillis()-start;
        }
        System.exit(1);
    }

    public static boolean MoveDown(TetrisPiece t){
        if(t == null){return true;}
        if(!playArea.IsValidMove(t,0,1)){
            playArea.ConnectPiece(t);
            return false;
        }
        else{
            t.y+=1;
            return true;
        }
    }

    public static boolean MoveLeft(TetrisPiece t){
        if(!playArea.IsValidMove(t,-1,0)){
            return false;
        }
        else{
            t.x-=1;
            return true;
        }
    }

    public static boolean MoveRight(TetrisPiece t){
        if(!playArea.IsValidMove(t,1,0)){
            return false;
        }
        else{
            t.x+=1;
            return true;
        }
    }

    public static boolean ROL(TetrisPiece t){
        t.ROL();
        if(!playArea.IsValidMove(t,0,0)){
            t.ROR();
            return false;
        }
        return true;
    }

    public static boolean ROR(TetrisPiece t){
        t.ROR();
        if(!playArea.IsValidMove(t,0,0)){
            t.ROL();
            return false;
        }
        return true;
    }

    public static TetrisPiece GeneratePiece(String[] s){
        TetrisPiece current = new TetrisPiece(s);
        current.x = playArea.columns/2;
        current.y = 0;
        return current;
    }

    public static void GenerateNext(){
        int nextPiece = ran.nextInt(7);
        switch(nextPiece){
            case 0:next = GeneratePiece(O);break;
            case 1:next = GeneratePiece(T);break;
            case 2:next = GeneratePiece(I);break;
            case 3:next = GeneratePiece(J);break;
            case 4:next = GeneratePiece(L);break;
            case 5:next = GeneratePiece(S);break;
            case 6:next = GeneratePiece(Z);break;
            default:next = GeneratePiece(O);break;
        }
        playArea.addNext(NEXT,next);        
    }
    static String[] NEXT = {
            "XXXXXXXX",
            "X      X",
            "X      X",
            "X      X",
            "X      X",
            "X      X",
            "X      X",
            "XXXXXXXX"
        };
    static String[] T = {
            "    ",
            " X  ",
            "XXX ",
            "    "};
    static String[] J = {
            "    ",
            " X  ",
            " XXX", 
            "    "};
    static String[] L = {
            "    ",
            "  X ",
            "XXX ",
            "    "};
    static String[] Z = {
            "    ",
            "XX  ",
            " XX ",
            "    "};
    static String[] S = {
            "    ",
            " XX ",
            "XX  ",
            "    "};
    static String[] I = {
            " X  ",
            " X  ",
            " X  ",
            " X  "};
    static String[] O = {
            "    ",
            " XX ",
            " XX ",
            "    "};
}