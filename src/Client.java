import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private final static int BUFFER_SIZE = 2048;
    private final static int TIME_SLEEP = 2000;
    private final static String CLIENT_ID = "127.0.0.1";
    private final static int CLIENT_PORT = 23334;

    public static void main(String[] args) throws IOException {
        InetSocketAddress socketAddress = new InetSocketAddress(CLIENT_ID, CLIENT_PORT);
        final SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(socketAddress);

        try (Scanner scanner = new Scanner(System.in)) {
            final ByteBuffer inputBuffer = ByteBuffer.allocate(BUFFER_SIZE);

            String message;
            while (true) {
                System.out.print("Введите любую строку с пробелами или end, и отправьте серверу: ");
                message = scanner.nextLine();

                socketChannel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));

                if (message.equals("end")) {
                    break;
                }

                Thread.sleep(TIME_SLEEP);

                int bytesCount = socketChannel.read(inputBuffer);

                System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                inputBuffer.clear();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
