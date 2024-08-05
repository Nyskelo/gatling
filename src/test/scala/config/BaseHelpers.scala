package config

import io.gatling.core.Predef._
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.http.check.HttpCheck
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.util.Random

object BaseHelpers {
  val gatlingConf: GatlingConfiguration = GatlingConfiguration.loadForTest()

  def cartPath = "/cart"
  def checkoutPath = "/checkout"
  def productsPath = "/products"
  def thankyouPath = "/thank-you"
  def database = "/wp-admin/admin-ajax.php"
  def baseUrl = "http://localhost"

  def CART_CONTENT = "CART_CONTENT"
  def TOTAL_NET = "TOTAL_NET"
  def TRANS_ID = "TRANS_ID"
  def PRODUCT_CHECKOUT = "PRODUCT_CHECKOUT"
  def PRODUCT_PARAMS = "PRODUCT_PARAMS"
  def PRODUCT_PRICE = "PRODUCT_PRICE"
  def PRODUCT_NAMES = "PRODUCT_NAMES"
  def PRODUCT_RANDOM = "PRODUCT_RANDOM"

  def product_name_regex = s"""$baseUrl/products/([a-zA-Z0-9-]+)"""
  def product_data_regex = """name="([^"]*)" class="product_id" value="([^"]*)"><span class="delete_product" p_id="(?:\d+)"></span> <input class="edit-product-quantity" data-p_id="(?:\d+)" data-price="(?:.+?)" type="number" min="0" max="" step="1" name="([^"]*)" value="([^"]*)""""
  def product_price_regex = """name="(product_price_(?:\d+?)__)" value="([^"]*)""""

  val httpProtocol: HttpProtocolBuilder= http(gatlingConf)
    .baseUrl(baseUrl)
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/png,image/svg+xml,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
    .upgradeInsecureRequestsHeader("1")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:128.0) Gecko/20100101 Firefox/128.0")

  val commonFormParams: Seq[(String, String)] = Seq(
    "shipping" -> "order",
    TOTAL_NET.toLowerCase() -> "$#{TOTAL_NET}",
    CART_CONTENT.toLowerCase() -> "$#{CART_CONTENT}",
    TRANS_ID.toLowerCase() -> "$#{TRANS_ID}"
  )

  var randomNumber: Number = Random.nextInt(900000000)
  var RANDOM_EMAIL: String = s"email$randomNumber@gmail.com"
  var RANDOM_NUMBER: String =  s"100000000$randomNumber"
  def randomString(length: Int = 10): String = Random.alphanumeric.take(length).mkString

  def CONSTANT_DELAY_DEFAULT = 2
  def CONSTANT_DELAY_MIN = 5
  def CONSTANT_DELAY_MAX = 30
  def RANDOM_DELAY_DEFAULT = 5
  def RANDOM_DELAY_MIN = 20
  def RANDOM_DELAY_MAX = 100
  def thinkTime(Min: Int = CONSTANT_DELAY_DEFAULT, Max:Int = RANDOM_DELAY_DEFAULT): ChainBuilder = {
    pause(Min, Max)
  }

  def saveCartContentFromSession(): HttpCheck = {
    css("input[name='cart_content']", "value").saveAs(CART_CONTENT)
  }
  def saveTotalNetFromSession(): HttpCheck = {
    css("input[name='total_net']", "value").saveAs(TOTAL_NET)
  }
  def saveTransIdFromSession(): HttpCheck = {
    css("input[name='trans_id']", "value").saveAs(TRANS_ID)
  }

  def transformProductDataToParamsCheckout(session: Session): Session = {
    val productData = session(PRODUCT_CHECKOUT).as[List[(String, String,String, String)]].flatMap {
      case (key1, id, key2, quantity) => Seq((key1, id), (key2, quantity))
    }
    session.set("PRODUCT_PARAMS", productData)
  }
  def transformProductDataToParamsSubmit(session: Session): Session = {
    val productData = session(PRODUCT_PRICE).as[List[(String, String)]].flatMap {
     case (key1, id) => Seq((key1, id))
    }
    session.set("PRODUCT_PARAMS", productData)
  }

}
