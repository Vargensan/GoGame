
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/cp24/IdeaProjects/Game/conf/routes
// @DATE:Mon Jan 30 00:19:15 CET 2017

import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package controllers {

  // @LINE:12
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:12
    def at(file:String): Call = {
      implicit val _rrc = new ReverseRouteContext(Map(("path", "/public")))
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[String]].unbind("file", file))
    }
  
  }

  // @LINE:6
  class ReverseApplication(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:7
    def goGame(username:String = null): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "room" + queryString(List(if(username == null) None else Some(implicitly[QueryStringBindable[String]].unbind("username", username)))))
    }
  
    // @LINE:9
    def goGameJs(username:String): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "assets/javascripts/gamego" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("username", username)))))
    }
  
    // @LINE:8
    def goSocketCreator(username:String): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix + { _defaultPrefix } + "room/goGame" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("username", username)))))
    }
  
    // @LINE:6
    def index(): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix)
    }
  
  }


}