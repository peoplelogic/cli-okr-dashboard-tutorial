import ai.peoplelogic.directory.api.Employee
import ai.peoplelogic.okrs.api.*
import ai.peoplelogic.okrs.client.okrsClient
import ai.peoplelogic.api.JwtConfig
import ai.peoplelogic.api.ServerConfig
import ai.peoplelogic.api.Role
import kotlinx.coroutines.runBlocking
import java.util.UUID

fun main() {
    val API_KEY = "<YOUR_API_KEY_HERE>"
    val ORG_ID = UUID.fromString("<YOUR_ORG_ID_HERE>")

    val client = okrsClient(
        serverConfig = ServerConfig(baseUrl = "https://api.peoplelogic.dev"),
        jwtConfig = JwtConfig(issuer = "example-app", audience = "directory-server", secret = API_KEY)
    )

    runBlocking {
        client.withAuth(Role.ORG_ADMIN, org = ORG_ID) {
            val employee: Employee = Employee.getByEmail("john.doe@example.com")
            println("Employee '${employee.name}' found (ID: ${employee.id})")

            val topObjective = ObjectiveRequest(
                name = "Increase Customer Satisfaction",
                description = "Improve customer satisfaction by 20%",
                parentId = null,
                startingValue = 70.0,
                currentValue = 70.0,
                targetValue = 90.0,
                metricType = ObjectiveMetricType.NUMBER,
                metricUnit = null,
                owner = employee.id
            ).post()
            println("Created objective: '${topObjective.name}' (ID: ${topObjective.id})")

            val kr1 = ObjectiveRequest(
                name = "Implement 5 New Features",
                description = "Launch features to enhance user experience",
                parentId = topObjective.id,
                startingValue = 0.0,
                currentValue = 0.0,
                targetValue = 5.0,
                metricType = ObjectiveMetricType.NUMBER,
                metricUnit = null,
                owner = employee.id
            ).post()
            println("Created key result: '${kr1.name}' (ID: ${kr1.id})")

            val kr2 = ObjectiveRequest(
                name = "Achieve 50 Positive Reviews",
                description = "Get 50 positive customer reviews",
                parentId = topObjective.id,
                startingValue = 0.0,
                currentValue = 0.0,
                targetValue = 50.0,
                metricType = ObjectiveMetricType.NUMBER,
                metricUnit = null,
                owner = employee.id
            ).post()
            println("Created key result: '${kr2.name}' (ID: ${kr2.id})")

            val allObjectives = Objective.listAll(ObjectiveProjections.OBJECTIVE_TREE)
            println("\nOKRs Summary:")
            fun printObjective(obj: Objective, indent: String = "") {
                val metric = obj.metric
                val progress = metric?.let {
                    if (it.targetValue != it.startingValue)
                        (((it.currentValue - it.startingValue) / (it.targetValue - it.startingValue)) * 100).toInt()
                    else 0
                } ?: 0
                println("${'$'}{indent}- ${'$'}{obj.name}: ${'$'}{progress}% complete (Owner: ${'$'}{employee.name})")
                obj.childObjectives?.forEach { printObjective(it, indent + "    ") }
            }
            allObjectives.filter { it.parentId == null }.forEach { printObjective(it) }
        }
    }
}
