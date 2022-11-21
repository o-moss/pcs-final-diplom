import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    Map<String, List <PageEntry>> answer = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы
        for (File pdf : pdfsDir.listFiles()) {
            var doc = new PdfDocument(new PdfReader(pdf));
            for (int i = 1; i < doc.getNumberOfPages(); i++) {
                var page = doc.getPage(i);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> freqs = new HashMap<>();
                for (String word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }
                for (Map.Entry<String, Integer> entry : freqs.entrySet()) {
                    PageEntry pageEntry = new PageEntry(pdf.getName(), i, entry.getValue());
                    if (answer.get(entry.getKey()) != null) {
                    List<PageEntry> list = answer.get(entry.getKey());
                    list.add(pageEntry);
                    //list = (List<PageEntry>) Arrays.stream(list.toArray()).sorted();
                    Collections.sort(list);
                    answer.put(entry.getKey(), list);
                    } else {
                        List <PageEntry> list = new ArrayList<>();
                        list.add(pageEntry);
                        answer.put(entry.getKey(), list);
                    }
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову
        return answer.get(word);
    }
}
