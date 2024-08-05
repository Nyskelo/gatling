package api

import config.BaseHelpers.database
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object addToCart {
  def addProductToCart(productName:String):ChainBuilder = {
    def requestName = s"Add $productName to cart"

      exec(http(requestName).post(database)
        .formParam("action","ic_add_to_cart")
        .formParam("cart_widget","0")
        .formParam("cart_container","0")
        .formParam("add_cart_data","current_product=#{ID}&cart_content=#{CART_CONTENT}&current_quantity=1")
        .check(status.is(200), substring("Added!").exists)
      )

  }
}
