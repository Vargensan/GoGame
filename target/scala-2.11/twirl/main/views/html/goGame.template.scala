
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object goGame_Scope0 {
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

class goGame extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template1[String,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(username: String):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.20*/("""

"""),_display_(/*3.2*/main(username)/*3.16*/ {_display_(Seq[Any](format.raw/*3.18*/("""
    
    """),format.raw/*5.5*/("""<div class="page-header">
        <h1>Game Go - Web Application</h1>
    </div>
    
    <div id="onError" class="alert-message error">
        <p>
            <strong>Oops!</strong> <span></span>
        </p>
    </div>
    
    <div id="onChat" class="row">
    	<input id="nr" placeholder="Give a position for a stone"></input>    	

    	<div class="span10" id="main">
     		<div id="messages"></div>          
   		</div>        
    </div>

    <script type='text/javascript'>
        """),_display_(/*24.10*/Html(views.js.goGame.render(username).toString())),format.raw/*24.59*/("""
    """),format.raw/*25.5*/("""</script>
""")))}))
      }
    }
  }

  def render(username:String): play.twirl.api.HtmlFormat.Appendable = apply(username)

  def f:((String) => play.twirl.api.HtmlFormat.Appendable) = (username) => apply(username)

  def ref: this.type = this

}


}

/**/
object goGame extends goGame_Scope0.goGame
              /*
                  -- GENERATED --
                  DATE: Mon Jan 23 05:11:48 CET 2017
                  SOURCE: C:/Users/cp24/IdeaProjects/Game/app/views/goGame.scala.html
                  HASH: 8bee74123d0b76140922a5e12f5716f98472e901
                  MATRIX: 747->1|860->19|888->22|910->36|949->38|985->48|1505->541|1575->590|1607->595
                  LINES: 27->1|32->1|34->3|34->3|34->3|36->5|55->24|55->24|56->25
                  -- GENERATED --
              */
          