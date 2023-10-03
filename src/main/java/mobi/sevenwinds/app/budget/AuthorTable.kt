package mobi.sevenwinds.app.budget

import org.jetbrains.exposed.dao.IntIdTable

object AuthorTable : IntIdTable("author") {
    val fullName = varchar("full_name", 255) // Adjust the maximum length as needed
    val creationDate = datetime("creation_date")
}
