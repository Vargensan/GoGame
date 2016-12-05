/**
 * Kinga Krata 2016-12-05.
 */
public class START {
    private static GameServer serverSocket;

    public static void main(String argv[])
    {
        serverSocket=new GameServer();
        serverSocket.listenSocket();
    }
}
