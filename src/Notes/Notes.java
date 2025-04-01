package Notes;

import java.util.HashMap;

public class Notes {
    HashMap<Integer, Note> notes;
    
    /**
     * Constructor for the Notes class.
     * Initializes an empty list of notes.
     */
    public Notes() {
        this.notes = new HashMap<Integer, Note>();
    }

    /**
     * Add Note ==> overwrite exsisting note
     * @param noteText
     * @param day
     * @param stockID
     */
    public void addNote(String noteText, int day, int stockID) {
        Note note = new Note(noteText, day);
        notes.put(stockID, note);
    }

    /**
     * Returns the note for the given stock ID.
     * @param StockID
     * @return noteText or null if not found
     */
    public String getNote(int StockID) {
        if (notes.containsKey(StockID)) {
            Note note = notes.get(StockID);
            return note.noteText;
        }
        return null;
    }
}
