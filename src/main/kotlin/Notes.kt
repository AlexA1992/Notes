import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Notes(
    val id: Int,
    val userId: Int,
    val date: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm:ss a")).toString(),
    var text: String,
    var deleted: Boolean = false,
) : CrudService<Notes> {

    override fun add(entity: Notes, list: List<Notes>): List<Notes> {
        val newList = list.toMutableList().plus(entity).toList()
        NotesList.allNotes = newList
        return NotesList.allNotes
    }

    override fun delete(id: Int, list: List<Notes>): List<Notes> {
        for (notes in list) {
            if (notes.id == id) {
                if (!notes.deleted) {
                    notes.deleted = true
                }
                break
            }
        }
        return list
    }

    override fun edit(entity: Notes, list: List<Notes>): List<Notes> {
        for (notes in list) {
            if (notes.id == entity.id) {
                notes.text = entity.text
                break
            }
        }
        return list
    }

    override fun getById(id: Int, list: List<Notes>): Notes? {
        var thisNote: Notes? = null
        for (notes in list) {
            if (notes.id == id) {
                thisNote = notes
            }
        }
        return thisNote
    }

    override fun restore(id: Int, list: List<Notes>): List<Notes> {
        for (notes in list) {
            if (notes.id == id) {
                if (notes.deleted) {
                    notes.deleted = false
                }
                break
            }
        }
        return list
    }
}

object NotesList {
    var allNotes: List<Notes> = emptyList()
}

fun main() {
    //создаем записи
    val note1 = Notes(1, 2, text = "First note")
    val note2 = Notes(2, 5, text = "Second note")
    val note3 = Notes(3, 7, text = "Third note")

    //создаем комменты
    val comment1 = Comment(1, 1, text = "First comment for 1 note")
    val comment2 = Comment(2, 2, text = "First comment for 2 note")
    val comment3 = Comment(3, 2, text = "Second comment for 2 note")
    val comment4 = Comment(4, 3, text = "First comment for 3 note")

    //добавляем их
    note1.add(note1, NotesList.allNotes)
    note1.add(note2, NotesList.allNotes)
    note1.add(note3, NotesList.allNotes)

    println("После добавления заметок")
    println(NotesList.allNotes)


    //добавляем их
    comment1.add(comment1, CommentList.allComments)
    comment1.add(comment2, CommentList.allComments)
    comment1.add(comment3, CommentList.allComments)
    comment1.add(comment4, CommentList.allComments)

    println("После добавления комментов")
    println(CommentList.allComments)

    //edit note
    note1.edit(Notes(1, 2, text = "New first note"), NotesList.allNotes)
    println("После редактирования")
    println(NotesList.allNotes)

    //delete
    note1.delete(1, NotesList.allNotes)
    println("После удаления заметки распечатываются только те заметки, которые не удалены")
    for (note in NotesList.allNotes) {
        if (!note.deleted) {
            println(note)
        }
    }

    //getById
    println("Получаем Note по ID")
    println(note1.getById(2, NotesList.allNotes))

    //restore
    println("Восстанавливаем удаленную ID=1")
    note1.restore(1, NotesList.allNotes)
    println("После восстановления распечатываются те, которые не удалены")
    for (note in NotesList.allNotes) {
        if (!note.deleted) {
            println(note)
        }
    }

    //edit
    comment1.edit(Comment(1, 2, text = "New first comment"), CommentList.allComments)
    println("После редактирования")
    println(CommentList.allComments)

    //delete
    comment1.delete(1, CommentList.allComments)
    println("После удаления распечатываются только те, которые не удалены")
    for (comment in CommentList.allComments) {
        if (!comment.deleted) {
            println(comment)
        }
    }
    //getById
    println("Получаем Note по ID")
    println(comment1.getById(2, CommentList.allComments))

    //restore
    println("Восстанавливаем удаленную ID=1")
    comment1.restore(1, CommentList.allComments)
    println("После восстановления распечатываются только те, которые не удалены")
    for (comment in NotesList.allNotes) {
        if (!comment.deleted) {
            println(comment)
        }
   }

    println("Снова удаляем заметку с ID = 1")
    note1.delete(1, NotesList.allNotes)
    println(NotesList.allNotes)

    //поскольку заметка c Id = 1 удалена, то удаляем комменты с noteId = 1
    comment1.deleteCommentsIfNotesDeleted()

    println("Распечатываем комменты после удаления тех комментов, у которых удалены заметки:" +
            "заметка note1 - была удалена, соответственно comment1 будет удалена," +
            "у нее войство deleted станет false")
    for (comment in CommentList.allComments){
        if(!comment.deleted){
            println(comment)
        }
    }

    //коммант 1 уже удален (помечен как удаленный) но юзер пытается его отредактировать. Выкидываем исключение
    try {
        comment1.edit(comment1, CommentList.allComments)
    } catch (e: CommentNotFoundException) {
        println(e.message)
    }

    //коммент с ID = 2  не удален, но юзер пытается его восстановить. Выкидываем исключение
    try {
        comment1.restore(2, CommentList.allComments)
    } catch (e: CommentNotDeletedException) {
        println(e.message)
    }
}