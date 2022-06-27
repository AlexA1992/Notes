import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//классы искючений
class CommentNotFoundException(massage: String) : Exception() {
    override val message: String?
        get() = "No such comment (("
}

class CommentNotDeletedException(massage: String) : Exception() {
    override val message: String?
        get() = "The Comment was not deleted (("
}

data class Comment(
    val id: Int,
    var noteId: Int,
    val userId: Int = 2,
    val date: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm:ss a")).toString(),
    var text: String,
    var deleted: Boolean = false
) : CrudService<Comment> {

    override fun add(entity: Comment, list: List<Comment>): List<Comment> {
        val newList = list.toMutableList().plus(entity).toList()
        CommentList.allComments = newList
        return CommentList.allComments
    }

    override fun delete(id: Int, list: List<Comment>): List<Comment> {
        for (comment in list) {
            if (comment.noteId == id) {
                if (!comment.deleted) {
                    comment.deleted = true
                }
                break
            }
        }
        return list
    }

    override fun edit(entity: Comment, list: List<Comment>): List<Comment> {
        for (comment in list) {
            if (comment.id == entity.id) {
                if (!comment.deleted) {
                    comment.text = entity.text
                    println("Комментарий c ID - ${entity.id} успешно изменен")
                    break
                } else {
                    throw CommentNotFoundException("0")
                }
            }
        }
        return list
    }

    override fun getById(id: Int, list: List<Comment>): Comment? {
        var thisNote: Comment? = null
        for (comment in list) {
            if (comment.id == id) {
                thisNote = comment
            }
        }
        return thisNote
    }

    override fun restore(id: Int, list: List<Comment>): List<Comment> {
        for (comment in list) {
            if (comment.id == id) {
                if (comment.deleted) {
                    comment.deleted = false
                    println("Комментарий с ID = $id успешно восстановлен")
                    break
                } else {
                    throw CommentNotDeletedException("0")
                }
            }
        }
        return list
    }

    fun deleteCommentsIfNotesDeleted(){
        for(note in NotesList.allNotes){
            if(note.deleted) {
                for (comment in CommentList.allComments){
                    if(comment.noteId == note.id){
                        delete(comment.id, CommentList.allComments)
                    }
                }
            }
        }
    }
}

object CommentList {
    var allComments: List<Comment> = emptyList()
}