/**
 * Kinga Krata 2016-12-05.
 */

public class START {
    private static GameServer serverSocket;

    public static void main(String argv[])
    {
        //-----------STARTS SERVER ON PORT 4444-----------//
        serverSocket=new GameServer(4444);
    }
}
