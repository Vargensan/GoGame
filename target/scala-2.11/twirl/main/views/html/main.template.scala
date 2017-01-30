
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object main_Scope0 {
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import java.lang._
import java.util._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import play.core.j.PlayMagicForJava._
import play.mvc._
import play.data._
import play.api.data.Field
import play.mvc.Http.Context.Implicit._

class main extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template2[String,Html,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(connected: String)(content: Html):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.36*/("""

"""),format.raw/*3.1*/("""<!DOCTYPE html>

<html>
    <head>
        <title>Game Go</title>
        <link rel="stylesheet" media="screen" href=""""),_display_(/*8.54*/routes/*8.60*/.Assets.at("stylesheets/bootstrap.css")),format.raw/*8.99*/("""">
        <link rel="stylesheet" media="screen" href=""""),_display_(/*9.54*/routes/*9.60*/.Assets.at("stylesheets/main.css")),format.raw/*9.94*/("""">
        <link rel="shortcut icon" type="image/png" href=""""),_display_(/*10.59*/routes/*10.65*/.Assets.at("images/go_icon.png")),format.raw/*10.97*/("""">
        <script src=""""),_display_(/*11.23*/routes/*11.29*/.Assets.at("javascripts/jquery-1.7.1.min.js")),format.raw/*11.74*/("""" type="text/javascript"></script>
    </head>
    <body>

        <div class="topbar">
            <div class="fill">
                <div class="container">

                """),_display_(/*19.18*/if(connected != null)/*19.39*/ {_display_(Seq[Any](format.raw/*19.41*/("""
                    """),format.raw/*20.21*/("""// JESLI JESTESMY POLACZENI Z ROZGRYWKA
                    <p class="pull-right">
                        Logged in as """),_display_(/*22.39*/connected),format.raw/*22.48*/(""" """),format.raw/*22.49*/("""—
                        <a href=""""),_display_(/*23.35*/routes/*23.41*/.Application.index()),format.raw/*23.61*/("""">Disconnect</a>
                    </p>
                """)))}/*25.19*/else/*25.24*/{_display_(Seq[Any](format.raw/*25.25*/("""
                    """),format.raw/*26.21*/("""// JEZELI NIE JESTESMY POLACZENI Z ROZGRYWKA
                    // tutaj mamy przycisk sign in w poczatkowym menu
                    // mozna go zgapic do potwierdzenia rozmiaru planszy/rozpoczecia gry
                    // ma on zdefiniowana akcje na klikniecie
                    <form action=""""),_display_(/*30.36*/routes/*30.42*/.Application.goGame()),format.raw/*30.63*/("""" class="pull-right">
                        <input id="username" name="username" class="input-small" type="text" placeholder="Username">
                        <button class="btn" type="submit">Sign in</button>
                    </form>
                """)))}),format.raw/*34.18*/("""

                """),format.raw/*36.17*/("""</div>
            </div>
        </div>

        <div class="container">

            <div class="content">
            """),_display_(/*43.14*/content),format.raw/*43.21*/("""
            """),format.raw/*44.13*/("""</div>

        </div>

    </body>
</html>
"""))
      }
    }
  }

  def render(connected:String,content:Html): play.twirl.api.HtmlFormat.Appendable = apply(connected)(content)

  def f:((String) => (Html) => play.twirl.api.HtmlFormat.Appendable) = (connected) => (content) => apply(connected)(content)

  def ref: this.type = this

}


}

/**/
object main extends main_Scope0.main
              /*
                  -- GENERATED --
                  DATE: Mon Jan 30 00:17:11 CET 2017
                  SOURCE: C:/Users/cp24/IdeaProjects/Game/app/views/main.scala.html
                  HASH: 8d84cbaf8fa7d0ac929cd03cb5847b3125b8ac6a
                  MATRIX: 748->1|877->35|905->37|1050->156|1064->162|1123->201|1205->257|1219->263|1273->297|1361->358|1376->364|1429->396|1481->421|1496->427|1562->472|1766->649|1796->670|1836->672|1885->693|2033->814|2063->823|2092->824|2155->860|2170->866|2211->886|2289->946|2302->951|2341->952|2390->973|2718->1274|2733->1280|2775->1301|3065->1560|3111->1578|3260->1700|3288->1707|3329->1720
                  LINES: 27->1|32->1|34->3|39->8|39->8|39->8|40->9|40->9|40->9|41->10|41->10|41->10|42->11|42->11|42->11|50->19|50->19|50->19|51->20|53->22|53->22|53->22|54->23|54->23|54->23|56->25|56->25|56->25|57->26|61->30|61->30|61->30|65->34|67->36|74->43|74->43|75->44
                  -- GENERATED --
              */
          