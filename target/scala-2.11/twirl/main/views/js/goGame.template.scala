
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
    var chatSocket = new WS(""""),_display_(/*5.31*/routes/*5.37*/.Application.goSocketCreator(username).webSocketURL(request)),format.raw/*5.97*/("""")

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
                  DATE: Sun Jan 29 23:57:37 CET 2017
                  SOURCE: C:/Users/cp24/IdeaProjects/Game/app/views/goGame.scala.js
                  HASH: b4f8929802f9f40c1c412bd42c35a33ba737b5b0
                  MATRIX: 767->1|886->19|914->21|954->34|982->35|1013->40|1128->129|1142->135|1222->195|1286->232|1314->233|1349->242|1408->274|1436->275|1481->293|1509->294|1537->295|1597->328|1625->329|1658->335|1721->370|1750->371|1786->380|1897->463|1926->464|1967->477|2121->604|2150->605|2187->615|2220->620|2249->621|2290->634|2350->666|2379->667|2428->688|2682->915|2710->916|2743->922|2805->956|2834->957|2870->966|2938->1006|2967->1007|3008->1020|3088->1073|3117->1074|3149->1079|3177->1080|3210->1086|3315->1164|3343->1165
                  LINES: 27->1|32->1|34->3|34->3|34->3|35->4|36->5|36->5|36->5|38->7|38->7|39->8|39->8|39->8|39->8|39->8|39->8|42->11|42->11|44->13|44->13|44->13|45->14|48->17|48->17|49->18|53->22|53->22|54->23|54->23|54->23|55->24|56->25|56->25|57->26|62->31|62->31|64->33|64->33|64->33|65->34|65->34|65->34|66->35|68->37|68->37|69->38|69->38|71->40|76->45|76->45
                  -- GENERATED --
              */
          