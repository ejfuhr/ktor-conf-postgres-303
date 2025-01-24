package com.example


import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class Author(val name: String)

@Serializable
data class Book(val title: String)

object Authors : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", length = 50)
    override val primaryKey = PrimaryKey(id)
}

object Books : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", length = 100)
    override val primaryKey = PrimaryKey(id)
}

object AuthorBooks : Table() {
    val authorId = integer("author_id").references(Authors.id)
    val bookId = integer("book_id").references(Books.id)
    override val primaryKey = PrimaryKey(authorId, bookId)
}

class LibraryService(database: Database) {

    init {

        transaction(database) {
            SchemaUtils.create(Authors, Books, AuthorBooks)
        }
    }

    suspend fun addAuthor(author: Author): Int = dbQuery {
        Authors.insert {
            it[name] = author.name
        }[Authors.id]
    }

    suspend fun addBook(book: Book): Int = dbQuery {
        Books.insert {
            it[title] = book.title
        }[Books.id]
    }

    suspend fun addAuthorToBook(authorId: Int, bookId: Int) = dbQuery {
        AuthorBooks.insert {
            it[AuthorBooks.authorId] = authorId
            it[AuthorBooks.bookId] = bookId
        }
    }

    suspend fun getBooksByAuthor(authorId: Int): List<Book> = dbQuery {
        (Books innerJoin AuthorBooks)
            .selectAll().where { AuthorBooks.authorId eq authorId }
            .map { Book(it[Books.title]) }
    }

    suspend fun getAuthorsByBook(bookId: Int): List<Author> = dbQuery {
        (Authors innerJoin AuthorBooks)
            .selectAll().where { AuthorBooks.bookId eq bookId }
            .map { Author(it[Authors.name]) }
    }

    suspend fun readBook(bookId: Int): Book? = dbQuery {
        Books.selectAll()
            .where { Books.id eq bookId }
            .map { Book(it[Books.title]) }
            .singleOrNull()
    }

    suspend fun updateBook(bookId: Int, newTitle: String): Int = dbQuery {
        Books.update({ Books.id eq bookId }) {
            it[title] = newTitle
        }
    }

    suspend fun deleteBookById(bookId: Int): Int = dbQuery {
        Books.deleteWhere { Books.id eq bookId }
    }

    suspend fun queryAllBooks(): List<Book> = dbQuery {
        Books.selectAll().map { Book(it[Books.title]) }
    }

    suspend fun queryAllAuthors(): List<Author> = dbQuery {
        Authors.selectAll().map { Author(it[Authors.name]) }
    }

    suspend fun readAuthor(authorId: Int): Author? = dbQuery {
        Authors.selectAll()
            .where { Authors.id eq authorId }
            .map { Author(it[Authors.name]) }
            .singleOrNull()
    }

    suspend fun updateAuthor(authorId: Int, newName: String): Int = dbQuery {
        Authors.update({ Authors.id eq authorId }) {
            it[name] = newName
        }
    }

    suspend fun deleteAuthorById(authorId: Int): Int = dbQuery {
        Authors.deleteWhere { Authors.id eq authorId }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}