package mobi.sevenwinds.app.budget.api

import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.time.LocalDateTime

object AuthorService {
    suspend fun createAuthor(fullName: String): AuthorEntity = transaction {
        val currentDateTime = LocalDateTime.now()
        val author = AuthorEntity.new {
            this.fullName = fullName
            this.creationDate = DateTime(
                currentDateTime.year,
                currentDateTime.monthValue,
                currentDateTime.dayOfMonth,
                currentDateTime.hour,
                currentDateTime.minute,
                currentDateTime.second
            )
        }
        return@transaction author
    }

    // Add this function to retrieve an author by ID
    suspend fun getAuthorById(authorId: Int): AuthorEntity? = transaction {
        return@transaction AuthorEntity.findById(authorId)
    }
}
