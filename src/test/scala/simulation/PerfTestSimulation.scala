package simulation

import config.BaseHelpers.httpProtocol
import io.gatling.core.Predef._
import io.gatling.core.controller.inject.closed.ClosedInjectionStep
import io.gatling.core.controller.inject.open.OpenInjectionStep
import io.gatling.core.structure.ScenarioBuilder
import scenarios.ApplicationScenario.applicationScenario
import scala.concurrent.duration.DurationInt

class PerfTestSimulation extends Simulation{

  //mvn gatling:test
  //mvn clean gatling:test -DUsers_Per_Simulation=100 -DDuration_Minutes=10

  val scenario: ScenarioBuilder = applicationScenario
  val usersPerSimulation: Int = System.getProperty("Users_Per_Simulation","10").toInt
  val durationMinutes: Int = System.getProperty("Duration_Minutes","0").toInt
  val openModel: OpenInjectionStep = atOnceUsers(usersPerSimulation)
  val closedModel: ClosedInjectionStep = constantConcurrentUsers(usersPerSimulation).during(durationMinutes.minutes)

  setUp(
    scenario.inject(openModel),
//    scenario.inject(closedModel)
  ).protocols(httpProtocol)
    .assertions(global.successfulRequests.percent.gt(95))
}