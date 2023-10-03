package mobi.sevenwinds.app.budget


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction


object BudgetService {
    suspend fun addRecord(body: BudgetRecord, authorId: Int?): BudgetRecord = withContext(Dispatchers.IO) {
        transaction {
            val entity = BudgetEntity.new {
                this.year = body.year
                this.month = body.month
                this.amount = body.amount
                this.type = body.type
                this.authorId = authorId // Store the authorId if it's present
            }

            return@transaction entity.toResponse()
        }
    }

    suspend fun getYearStats(param: BudgetYearParam): BudgetYearStatsResponse = withContext(Dispatchers.IO) {
        transaction {
            val query = BudgetTable
                .select { BudgetTable.year eq param.year }
                .apply {
                    param.limit.takeIf { it > 0 }?.let { limit(it, param.offset) }
                    param.authorNameSubstring?.let { AuthorTable.fullName.lowerCase() like "%${it.toLowerCase()}%" }
                }

            val total = query.count()
            val data = BudgetEntity.wrapRows(query).map { it.toResponse() }

            val sumByType = data.groupBy { it.type.name }.mapValues { it.value.sumOf { v -> v.amount } }

            return@transaction BudgetYearStatsResponse(
                total = total,
                totalByType = sumByType,
                items = data
            )
        }
    }

}
