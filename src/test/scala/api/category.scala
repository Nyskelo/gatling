package api

import config.BaseHelpers.product_name_regex
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

import scala.util.Random

object category {
  def openCategory(categoryName:String):ChainBuilder = {
    def requestName = s"Open $categoryName tab"
    def requestPath = s"/$categoryName"

    exec(http(requestName).get(requestPath)
      .check(status.is(200),
        regex(product_name_regex).findAll.transform { productNames =>
            Random.shuffle(productNames).headOption.getOrElse("No Product Found")
          }.saveAs("PRODUCT_RANDOM")
      )
    )
  }
}
