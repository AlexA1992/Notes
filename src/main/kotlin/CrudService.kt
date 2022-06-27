interface CrudService<E> {
    fun add(entity: E, list: List<E>): List<E>
    fun delete(id: Int, list: List<E>): List<E>
    fun edit(entity: E, list: List<E>): List<E>
    fun getById(id: Int, list: List<E>): E?
    fun restore(id: Int, list: List<E>): List<E>
}