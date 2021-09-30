import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    private final static int BUFFER_SIZE = 2048;
    private final static String SERVER_HOST = "localhost";
    private final static int SERVER_PORT = 23334;

    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

        while (true) {
            try (SocketChannel socketChannel = serverChannel.accept()) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(BUFFER_SIZE);

                while (socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(inputBuffer);
                    if (bytesCount == -1) {
                        break;
                    }

                    final String message = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                    if (message.equals("end")) {
                        return;
                    }

                    inputBuffer.clear();

                    String result = message.replaceAll(" ", "");
                    socketChannel.write(ByteBuffer.wrap(("С сервера получен результат строки без пробелов: " +
                            result).getBytes(StandardCharsets.UTF_8)));
                }

            } catch (Exception exp) {
                System.out.println(exp.getMessage());
            }
        }
    }
}
