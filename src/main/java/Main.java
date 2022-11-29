import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));

        try (ServerSocket serverSocket = new ServerSocket(8989);) {
            while (true) {
                try (Socket socket = serverSocket.accept(); BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); PrintWriter out = new PrintWriter(socket.getOutputStream());) {
                    final String words = in.readLine();
                    List<PageEntry> answerFromServer = new ArrayList<>();
                    String[] wordsForParsing = words.split("\\P{IsAlphabetic}+");
                    if (wordsForParsing.length == 1) {
                        answerFromServer = engine.search(wordsForParsing[0]);
                    } else {
                        answerFromServer = engine.multiSearch(wordsForParsing);
                    }
                    Gson gson = new Gson();
                    String json = gson.toJson(answerFromServer);
                    out.println(json);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}