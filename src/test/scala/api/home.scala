package api

import config.BaseHelpers.baseUrl
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef.http

object home {
  def openApplication():ChainBuilder = {
    def requestName = "Open the Application"
    def requestPath = baseUrl

    exec(http(requestName).get(requestPath))
  }
}
