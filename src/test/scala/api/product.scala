package api

import config.BaseHelpers.{baseUrl, productsPath, saveCartContentFromSession}
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object product {
  def openProduct(productName:String): ChainBuilder = {
    def requestName = s"Open $productName product"
    def requestPath = s"$productsPath/$productName"

      exec(http(requestName).get(requestPath)
        .check(status.is(200),
          headerRegex("Link", s"""<$baseUrl/\\?p=(\\d+)>""").saveAs("ID"),
          saveCartContentFromSession()
        )
      )
  }
}
