import org.junit.Test

import org.junit.Assert.*

class NotesTest {

    @Test
    fun add() {
        //arrange
        val start = NotesList.allNotes.size
        //act
        //создаем записи
        val note1 = Notes(1, 2, text = "First note")
        note1.add(note1, NotesList.allNotes)
        val finish = NotesList.allNotes.size
        //assert
        assertEquals(start < finish, true)
    }

    @Test
    fun delete() {
        //arrange
        val note1 = Notes(1, 2, text = "First note")
        note1.add(note1, NotesList.allNotes)

        //act
        note1.delete(1, NotesList.allNotes)

        //assert
        assertEquals(note1.deleted == true, true)
    }
}