import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    Map<String, List<PageEntry>> answer = new HashMap<>();
    File file = new File("stop-ru.txt");
    List<String> stopList = new ArrayList<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String s;
                while ((s = br.readLine()) != null) {
                    stopList.add(s);
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
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
                    for (String s : stopList) {
                        if (s.equals(word)) {
                            break;
                        }
                    }
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }
                for (Map.Entry<String, Integer> entry : freqs.entrySet()) {
                    PageEntry pageEntry = new PageEntry(pdf.getName(), i, entry.getValue());
                    if (answer.get(entry.getKey()) != null) {
                        List<PageEntry> list = answer.get(entry.getKey());
                        list.add(pageEntry);
                        Collections.sort(list);
                        answer.put(entry.getKey(), list);
                    } else {
                        List<PageEntry> list = new ArrayList<>();
                        list.add(pageEntry);
                        answer.put(entry.getKey(), list);
                    }
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        return answer.get(word.toLowerCase());
    }

    public List<PageEntry> multiSearch(String[] words) {
        List<PageEntry> list = new ArrayList<>();
        for (String s : words) {
            s = s.toLowerCase();
            if (list.isEmpty() | list == null) {
                if (answer.get(s) != null) {
                    list = answer.get(s);
                }
            } else {
                if (answer.get(s) != null) {
                    List<PageEntry> list2 = answer.get(s);
                    List<PageEntry> tempList = new ArrayList<>();
                    for (PageEntry p2 : list2) {
                        for (PageEntry p1 : list) {
                            if (p2.getPdfName().equals(p1.getPdfName())) {
                                if (p2.getPage() == p1.getPage()) {
                                    p1.setCount(p1.getCount() + p2.getCount());
                                    break;
                                }
                            }
                        }
                        tempList.add(p2);
                    }
                    list.addAll(tempList);
                }
            }
        }
        Collections.sort(list);
        return list;
    }
}
