public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private int count;

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

    public String getPdfName() {
        return pdfName;
    }

    public int getPage() {
        return page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
