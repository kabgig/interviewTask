package mobi.sevenwinds.app.budget

import com.papsign.ktor.openapigen.annotations.parameters.PathParam
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.annotations.type.number.integer.max.Max
import com.papsign.ktor.openapigen.annotations.type.number.integer.min.Min
import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.path.normal.post
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import mobi.sevenwinds.app.budget.api.AuthorService
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun NormalOpenAPIRoute.budget() {
    route("/budget") {
        route("/add").post<Unit, BudgetRecord, BudgetRecord>(info("Добавить запись")) { param, body ->
            val authorId = body.authorId // Get the optional authorId from the request
            respond(BudgetService.addRecord(body, authorId))
        }

        route("/year/{year}/stats") {
            get<BudgetYearParam, BudgetYearStatsResponse>(info("Получить статистику за год")) { param ->
                val stats = BudgetService.getYearStats(param)
                val itemsWithAuthorInfo = stats.items.map { budgetRecord ->
                    val author = if (budgetRecord.authorId != null) {
                        val authorEntity = AuthorService.getAuthorById(budgetRecord.authorId)
                        AuthorInfo(authorEntity!!.fullName, LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(authorEntity.creationDate.millis),
                            ZoneId.systemDefault()
                        ))
                    } else {
                        null
                    }
                    BudgetRecordWithAuthor(
                        budgetRecord.year,
                        budgetRecord.month,
                        budgetRecord.amount,
                        budgetRecord.type,
                        author
                    )
                }
                respond(BudgetService.getYearStats(param))
            }
        }
    }
}

data class BudgetRecord(
    @Min(1900) val year: Int,
    @Min(1) @Max(12) val month: Int,
    @Min(1) val amount: Int,
    val type: BudgetType,
    val authorId: Int? = null // Optional author ID
)

data class BudgetYearParam(
    @PathParam("Год") val year: Int,
    @QueryParam("Лимит пагинации") val limit: Int,
    @QueryParam("Смещение пагинации") val offset: Int,
    @QueryParam("Автор (подстрока, игнорирующая регистр)") val authorNameSubstring: String? = null
)

class BudgetYearStatsResponse(
    val total: Int,
    val totalByType: Map<String, Int>,
    val items: List<BudgetRecord>
)

enum class BudgetType {
    Приход, Расход
}
data class BudgetRecordWithAuthor(
    val year: Int,
    val month: Int,
    val amount: Int,
    val type: BudgetType,
    val author: AuthorInfo? // AuthorInfo is a new data class
)
data class AuthorInfo(
    val fullName: String,
    val creationDate: LocalDateTime
)