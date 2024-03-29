package chat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChannelBase implements Channel {
    private PrintWriter pw;
    private Scanner sc;

    public ChannelBase(InputStream inputStream, OutputStream outputStream) {
        sc = new Scanner(inputStream);
        pw = new PrintWriter(outputStream, true);
    }

    public static Channel of(Socket socket) throws IOException {
        return new ChannelBase(
                socket.getInputStream(),
                socket.getOutputStream()
        );
    }

    public void sendMessage(Object message) {
        if (message instanceof Serializable) {
            pw.print(message);
        } else {
            throw new IllegalArgumentException(
                    "message is not serializable");
        }
    }

    @Override
    public void sendMessage(Message message) {
        pw.println(message.toString());
    }

    @Override
    public Message getMessage() {
        String s = sc.nextLine();
        if (s == null || s.trim().isEmpty()) return null;
        if (s.equals("/exit"))
            return new Message(MessageType.EXIT_COMMAND, "");
        else if (s.startsWith("/w "))
            return new Message(MessageType.PRIVATE_MESSAGE, s.substring(3).trim());
        return new Message(MessageType.BROADCAST_CHAT, s);
    }

    @Override
    public boolean hasNextLine() {
        return sc.hasNextLine();
    }
}