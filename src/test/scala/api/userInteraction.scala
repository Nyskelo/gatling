package api

import io.gatling.core.Predef._
import io.gatling.core.structure._

object userInteraction {
  def openApplication():ChainBuilder = {
    exec(home.openApplication())
  }
  def addProductToCart(categoryName:String):ChainBuilder = {
      exec(category.openCategory(categoryName))
        .exec(product.openProduct("#{PRODUCT_RANDOM}"))
        .exec(addToCart.addProductToCart("#{PRODUCT_RANDOM}"))
  }
  def submitOrder():ChainBuilder = {
    exec(cart.openCart())
      .exec(checkout.checkoutOrder("Click 'Place an order'"))
      .exec(checkout.submitOrder("Fill in all required fields, click 'Place an order'"))

  }
}
