public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    @Override
    public int compareTo(PageEntry o) {
        int result = 0;
        if (this.count > o.count) {
            result = -1;
        }
        if (this.count == o.count) {
            result = 0;
        }
        if (this.count < o.count) {
            result = 1;
        }
        return result;
    }

    @Override
    public String toString() {
        return "PageEntry{" +
                "\npdfName='" + pdfName + '\'' +
                ", \npage=" + page +
                ", \ncount=" + count +
                '}';
    }
}
