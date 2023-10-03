// AuthorAPIRoutes.kt

package mobi.sevenwinds.app.budget.api

import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.post
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import java.time.LocalDateTime

data class CreateAuthorRequest(val fullName: String)

data class CreateAuthorResponse(val id: Int, val fullName: String, val creationDate: LocalDateTime)

fun NormalOpenAPIRoute.authorAPIRoutes() {
    route("/author") {
        post<Unit, CreateAuthorRequest, CreateAuthorResponse>(
            info("Create a new Author record")) { _,

          body ->
            val author = AuthorService.createAuthor(body.fullName)
            respond(CreateAuthorRequest( author.fullName))
        }
    }
}
