package ingegneria_dei_dati.index;

public interface IndexHandlerInterface {
    public void generateIndex(String folderPath);
    public void addColumn2ToIndex();
    public void printIndexStats();
    public void closeIndexDirectory();
}
