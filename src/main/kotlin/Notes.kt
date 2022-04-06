import kotlin.concurrent.thread

class Notes(
    val id: Int, val userId: Int, val date: String, var text: String
)

//заметки
val noteList = mutableListOf<Notes>()
fun add(note: Notes) {
    noteList.add(note)
}

// распечатать все заметки
fun get(noteList: MutableList<Notes>) {
    for (notes in noteList) {
        println(notes.text)
    }
}

// распечатать заметку по ее ID
fun getById(noteList: MutableList<Notes>, noteId: Int) {
    var replyGetById: Boolean = false
    for (note in noteList) {
        if (note.id == noteId) {
            println(note.text + " got by Id")
            println("----------------------")
            replyGetById = true
            break
        }
    }
    if (replyGetById == false) {
        println("no by Id")
        throw Exception("нет такой заметки")
    }
}

//вставляем комменты
val commentsList = mutableListOf<Comment>()
fun createComment(comment: Comment, noteList: MutableList<Notes>, commentsList: MutableList<Comment>) {
    for (thisNote in noteList) {
        if (thisNote.id == comment.noteId) {
            commentsList.add(comment)
            break
        }
    }
}

// распечатать комменты
fun printComments(commentsList: MutableList<Comment>) {
    for (comment in commentsList) {
        if (comment.deleted == false) {
            println(comment.text)
        }

    }
}

//удалить заметку
fun deleteNote(note: Notes, noteList: MutableList<Notes>, commentsList: MutableList<Comment>) {
    for (thisNote in noteList) {
        if (thisNote.id == note.id && thisNote.userId == note.userId) {
            noteList.remove(thisNote)
            deleteCommentsOfDeletedNote(thisNote.id, commentsList)
            break
        }
    }
}

//удаляем и комменты если заметка удалена
fun deleteCommentsOfDeletedNote(noteId: Int, commentsList: MutableList<Comment>) {
    val numbersIterator = commentsList.iterator()
    while (numbersIterator.hasNext()) {
        if (numbersIterator.next().noteId == noteId) {
            numbersIterator.remove()
        }
    }
}

//удалить комменты к заметке без удаления заметки (то есть не показывать эти комменты просто)
fun deleteCommentsByNote(theNoteId: Int, noteList: MutableList<Notes>, commentsList: MutableList<Comment>) {
    //val commentsIterator = commentsList.iterator()
    var replydeleteCommentsByNote = false
    commentsList.forEach(){
        if(it.noteId == theNoteId){
            println(it.noteId)
            it.deleted = true
            replydeleteCommentsByNote = true
        }
    }
    if (replydeleteCommentsByNote == false) {
        throw Exception("нет комментов к этой заметке")
    }
}

//удалить коммент (вернее перестать показывать)
fun deleteComment(comment: Comment, commentsList: MutableList<Comment>) {
    for (thiscomment in commentsList) {
        if (thiscomment.id == comment.id) {
            thiscomment.deleted = true
            break
        }
    }
}

//редактим заметку 3
fun edit(note: Notes, editedText: String, noteList: MutableList<Notes>): Boolean {
    var reply: Boolean = false
    for (thisNote in noteList) {
        if (thisNote.id == note.id) {
            thisNote.text = editedText
            reply = true
        }
    }
    if (reply == false) {
        throw Exception("нет такой заметки уже")
    }
    return reply
}

//редактить коммент
fun editComment(comment: Comment, newComment: String, commentsList: MutableList<Comment>) {
    var replyEditComment: Boolean = false
    for (theComment in commentsList) {
        if (comment.id == theComment.id) {
            theComment.text = newComment
            replyEditComment = true
        }
        if (replyEditComment == false) {
            throw Exception("нет такой заметки совсем")
        }
    }
}

//печатаем удаленные комменты
fun deletedComments(commentsList: MutableList<Comment>) {
    for (comment in commentsList) {
        if (comment.deleted == true) {
            println(comment.text)
        }
    }
}

fun main() {
    //делаем заметки
    val note1 = Notes(1, 1, "01/02/2022", "do homework")
    val note2 = Notes(5, 1, "02/02/2022", "do quickly")
    val note3 = Notes(6, 1, "03/02/2022", "do very quickly")
    val note4 = Notes(7, 2, "03/02/2022", "do ultra quickly")
    val note5 = Notes(8, 1, "03/02/2022", "do immediately")

    // добаляем их
    add(note1)
    add(note2)
    add(note3)
    add(note4)
    add(note5)
    get(noteList)
    println("----------------------")
    //делаем комменты к заметкам
    val comment1 = Comment(1, 1, 1, "02/03/2002", "good comment", false)
    val comment2 = Comment(2, 1, 3, "04/03/2002", "very good comment", false)
    val comment3 = Comment(3, 5, 3, "05/03/2002", "bad comment", false)
    val comment4 = Comment(4, 6, 3, "06/03/2002", "very bad comment", false)
    val comment5 = Comment(5, 7, 3, "07/03/2002", "1 comment to noteId=7", false)
    val comment6 = Comment(6, 7, 3, "08/03/2002", "2 comment to noteId=7", false)

    //добавляем их
    createComment(comment1, noteList, commentsList)
    createComment(comment2, noteList, commentsList)
    createComment(comment3, noteList, commentsList)
    createComment(comment4, noteList, commentsList)
    createComment(comment5, noteList, commentsList)
    createComment(comment6, noteList, commentsList)
    printComments(commentsList)
    println("----------------------")


    // удаляем заметку 1 и комменты к ней
    try {
        deleteNote(note1, noteList, commentsList)
    } catch (e: Exception) {
        println(e.message)
    }
    println("----------------------")
    get(noteList)
    println("----------------------")
    printComments(commentsList)
    println("----------------------")

    // удаляем коммент 3
    try {
        deleteComment(comment3, commentsList)
    } catch (e: Exception) {
        println(e.message)
        println("----------------------")
    }
    printComments(commentsList)
    println("----------------------")

    // редактим заметку 3
    try {
        val newText: String = "edited note"
        edit(note3, newText, noteList)
    } catch (e: Exception) {
        println(e.message)
        println("----------------------")
    }
    get(noteList)
    println("----------------------")

    //удаляем коммент
    try {
        val newComment: String = "edited comment"
        editComment(comment4, newComment, commentsList)
    } catch (e: Exception) {
        println(e.message)
        println("----------------------")
    }
    printComments(commentsList)
    println("----------------------")

    //распечатать по ID
    try {
        val theId: Int = 7
        getById(noteList, theId)
    } catch (e: Exception) {
        println(e.message)
        println("----------------------")
    }
    get(noteList)
    println("----------------------")

    // удаляем комменты к заметке 7  - их 2
    try {
        val theNoteId: Int = 7
        deleteCommentsByNote(theNoteId, noteList, commentsList)
    } catch (e: Exception) {
        println(e.message)
        println("----------------------")
    }
    printComments(commentsList)
    println("----------------------")

    println("++++++++++++++++++++")
    deletedComments(commentsList)
}