// AuthorEntity.kt

package mobi.sevenwinds.app.budget.api

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import java.time.LocalDateTime

object AuthorTable : IntIdTable("author") {
    val fullName = varchar("full_name", 255)
    val creationDate = datetime("creation_date")
}

class AuthorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AuthorEntity>(AuthorTable)

    var fullName by AuthorTable.fullName
    var creationDate by AuthorTable.creationDate
}
