
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/cp24/IdeaProjects/Game/conf/routes
// @DATE:Mon Jan 30 00:19:15 CET 2017


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
