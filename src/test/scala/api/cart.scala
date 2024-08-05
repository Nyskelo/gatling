package api

import config.BaseHelpers._
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object cart {
  def openCart(): ChainBuilder = {
    exec(http("Open Cart").get(cartPath)
      .check(status.is(200),
        saveCartContentFromSession(),
        saveTotalNetFromSession(),
        saveTransIdFromSession(),
        regex(product_data_regex)
          .ofType[(String, String, String, String)]
          .findAll
          .saveAs(PRODUCT_CHECKOUT)
      )
    )
  }
}
