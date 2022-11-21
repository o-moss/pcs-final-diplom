import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.itextpdf.kernel.pdf.PdfName.List;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        //System.out.println(engine.search("бизнес"));

        try (ServerSocket serverSocket = new ServerSocket(8989);) {
            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream());
                ) {
                    final String word = in.readLine();
                    List<PageEntry> answerFromServer = engine.search(word);
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