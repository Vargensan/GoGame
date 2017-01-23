
package views.js

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object goGame_Scope0 {
import models._
import controllers._
import play.api.i18n._
import views.js._
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

class goGame extends BaseScalaTemplate[play.twirl.api.JavaScriptFormat.Appendable,Format[play.twirl.api.JavaScriptFormat.Appendable]](play.twirl.api.JavaScriptFormat) with play.twirl.api.Template1[String,play.twirl.api.JavaScriptFormat.Appendable] {

  /**/
  def apply/*1.2*/(username: String):play.twirl.api.JavaScriptFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.20*/("""

"""),format.raw/*3.1*/("""$(function() """),format.raw/*3.14*/("""{"""),format.raw/*3.15*/("""
    """),format.raw/*4.5*/("""var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
 var chatSocket = new WS(""""),_display_(/*5.28*/routes/*5.34*/.Application.goSocketCreator(username).webSocketURL(request)),format.raw/*5.94*/("""")

    var sendMessage = function() """),format.raw/*7.34*/("""{"""),format.raw/*7.35*/("""
        """),format.raw/*8.9*/("""chatSocket.send(JSON.stringify( """),format.raw/*8.41*/("""{"""),format.raw/*8.42*/("""nr: $("#nr").val()"""),format.raw/*8.60*/("""}"""),format.raw/*8.61*/(""" """),format.raw/*8.62*/("""))
        $("#nr").val('')

    """),format.raw/*11.5*/("""}"""),format.raw/*11.6*/("""

    """),format.raw/*13.5*/("""var receiveEvent = function(event) """),format.raw/*13.40*/("""{"""),format.raw/*13.41*/("""
        """),format.raw/*14.9*/("""var data = JSON.parse(event.data)

        // Handle errors
        if(data.error) """),format.raw/*17.24*/("""{"""),format.raw/*17.25*/("""
            """),format.raw/*18.13*/("""chatSocket.close()
            $("#onError span").text(data.error)
            $("#onError").show()
            return
        """),format.raw/*22.9*/("""}"""),format.raw/*22.10*/(""" 
        """),format.raw/*23.9*/("""else """),format.raw/*23.14*/("""{"""),format.raw/*23.15*/("""
            """),format.raw/*24.13*/("""$("#onChat").show()
            """),format.raw/*25.13*/("""}"""),format.raw/*25.14*/("""        
            """),format.raw/*26.13*/("""// Create the message element       
	        var el = $('<div class="message"><p style="font-size:16px"></p></div>')
	        $("p", el).text(data.message)
	        $(el).addClass('me')
	        $('#messages').append(el) 
    """),format.raw/*31.5*/("""}"""),format.raw/*31.6*/("""

    """),format.raw/*33.5*/("""var handleReturnKey = function(e) """),format.raw/*33.39*/("""{"""),format.raw/*33.40*/("""
        """),format.raw/*34.9*/("""if(e.charCode == 13 || e.keyCode == 13) """),format.raw/*34.49*/("""{"""),format.raw/*34.50*/("""
            """),format.raw/*35.13*/("""e.preventDefault()
            sendMessage()
        """),format.raw/*37.9*/("""}"""),format.raw/*37.10*/("""
    """),format.raw/*38.5*/("""}"""),format.raw/*38.6*/("""

    """),format.raw/*40.5*/("""$("#nr").keypress(handleReturnKey)


    chatSocket.onmessage = receiveEvent

"""),format.raw/*45.1*/("""}"""),format.raw/*45.2*/(""")
"""))
      }
    }
  }

  def render(username:String): play.twirl.api.JavaScriptFormat.Appendable = apply(username)

  def f:((String) => play.twirl.api.JavaScriptFormat.Appendable) = (username) => apply(username)

  def ref: this.type = this

}


}

/**/
object goGame extends goGame_Scope0.goGame
              /*
                  -- GENERATED --
                  DATE: Mon Jan 23 05:24:11 CET 2017
                  SOURCE: C:/Users/cp24/IdeaProjects/Game/app/views/goGame.scala.js
                  HASH: a33c59528ac280ea01940291a211d0bb80418bef
                  MATRIX: 767->1|886->19|914->21|954->34|982->35|1013->40|1125->126|1139->132|1219->192|1283->229|1311->230|1346->239|1405->271|1433->272|1478->290|1506->291|1534->292|1594->325|1622->326|1655->332|1718->367|1747->368|1783->377|1894->460|1923->461|1964->474|2118->601|2147->602|2184->612|2217->617|2246->618|2287->631|2347->663|2376->664|2425->685|2679->912|2707->913|2740->919|2802->953|2831->954|2867->963|2935->1003|2964->1004|3005->1017|3085->1070|3114->1071|3146->1076|3174->1077|3207->1083|3312->1161|3340->1162
                  LINES: 27->1|32->1|34->3|34->3|34->3|35->4|36->5|36->5|36->5|38->7|38->7|39->8|39->8|39->8|39->8|39->8|39->8|42->11|42->11|44->13|44->13|44->13|45->14|48->17|48->17|49->18|53->22|53->22|54->23|54->23|54->23|55->24|56->25|56->25|57->26|62->31|62->31|64->33|64->33|64->33|65->34|65->34|65->34|66->35|68->37|68->37|69->38|69->38|71->40|76->45|76->45
                  -- GENERATED --
              */
          