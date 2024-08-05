package scenarios


import api._
import config.BaseHelpers.{CONSTANT_DELAY_MAX, CONSTANT_DELAY_MIN, RANDOM_DELAY_MAX, RANDOM_DELAY_MIN, thinkTime}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure._

object ApplicationScenario {

  def applicationScenario: ScenarioBuilder = {
  scenario("Backend performance testing task")
    .exec(flushHttpCache)
    .exec(flushCookieJar)
    .exitBlockOnFail(
      group("Open Application"){
        exec(userInteraction.openApplication())
          .exec(thinkTime())
      }
        .group("Add table to cart"){
          exec(userInteraction.addProductToCart("tables"))
          .exec(thinkTime(CONSTANT_DELAY_MIN,RANDOM_DELAY_MIN))

      }
        .randomSwitch(
          50.00 -> group("Add chair to cart"){
            exec(userInteraction.addProductToCart("chairs"))
              .exec(thinkTime(CONSTANT_DELAY_MIN,RANDOM_DELAY_MIN))
          }
        )
        .randomSwitch(
          30.00 -> group("Submit Order"){
            exec(userInteraction.submitOrder())
              .exec(thinkTime(CONSTANT_DELAY_MAX,RANDOM_DELAY_MAX))
          }
        )
    )
  }
}
