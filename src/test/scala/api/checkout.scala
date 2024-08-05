package api

import config.BaseHelpers._
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object checkout {
  def checkoutOrder(requestName:String):ChainBuilder = {
    exec(session => transformProductDataToParamsCheckout(session))
      .exec(http(requestName).post(checkoutPath)
        .formParamSeq("#{PRODUCT_PARAMS}")
        .formParamSeq(commonFormParams)
        .check(status.is(200),
          regex(product_price_regex)
            .ofType[(String, String)]
            .findAll
            .saveAs(PRODUCT_PRICE))
      )
  }

   def submitOrder(requestName:String):ChainBuilder = {
     exec(session => transformProductDataToParamsSubmit(session))
       .exec(http(requestName).post(checkoutPath)
         .formParamSeq("#{PRODUCT_PARAMS}")
         .formParamSeq(commonFormParams)
         .formParam("ic_formbuilder_redirect",s"$baseUrl$thankyouPath")
         .formParam("cart_type","order")
         .formParam("cart_inside_header_1","<b>BILLING ADDRESS</b>")
         .formParam("cart_inside_header_2","<b>DELIVERY ADDRESS</b> (FILL ONLY IF DIFFERENT FROM THE BILLING ADDRESS)")
         .formParam("cart_name",randomString(5))
         .formParam("cart_address",randomString(20))
         .formParam("cart_city",randomString())
         .formParam("cart_country",randomString())
         .formParam("cart_phone",s"$RANDOM_NUMBER")
         .formParam("cart_postal",s"$RANDOM_NUMBER")
         .formParam("cart_email",s"$RANDOM_EMAIL")
         .formParam("cart_submit","Place Order")
         .check(status.is(200),
           css("a[aria-current=\"page\"]", "href").is(s"$baseUrl$thankyouPath")
         )
       )
  }
}
