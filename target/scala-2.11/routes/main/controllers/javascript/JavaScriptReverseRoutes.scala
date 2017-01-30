
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/cp24/IdeaProjects/Game/conf/routes
// @DATE:Mon Jan 30 00:19:15 CET 2017

import play.api.routing.JavaScriptReverseRoute
import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package controllers.javascript {
  import ReverseRouteContext.empty

  // @LINE:12
  class ReverseAssets(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:12
    def at: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Assets.at",
      """
        function(file) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
        }
      """
    )
  
  }

  // @LINE:6
  class ReverseApplication(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:7
    def goGame: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Application.goGame",
      """
        function(username) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "room" + _qS([(username == null ? null : (""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("username", username))])})
        }
      """
    )
  
    // @LINE:9
    def goGameJs: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Application.goGameJs",
      """
        function(username) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/javascripts/gamego" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("username", username)])})
        }
      """
    )
  
    // @LINE:8
    def goSocketCreator: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Application.goSocketCreator",
      """
        function(username) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "room/goGame" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("username", username)])})
        }
      """
    )
  
    // @LINE:6
    def index: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Application.index",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + """"})
        }
      """
    )
  
  }


}