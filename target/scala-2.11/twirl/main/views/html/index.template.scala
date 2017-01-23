
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object index_Scope0 {
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

class index extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply():play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](_display_(/*1.2*/main(null)/*1.12*/ {_display_(Seq[Any](format.raw/*1.14*/("""

    """),_display_(/*3.6*/if(flash.containsKey("error"))/*3.36*/ {_display_(Seq[Any](format.raw/*3.38*/("""

        """),format.raw/*5.9*/("""<div class="alert-message error">
            <p>
                <strong>Oops!</strong> """),_display_(/*7.41*/flash/*7.46*/.get("error")),format.raw/*7.59*/("""
            """),format.raw/*8.13*/("""</p>
        </div>

    """)))}),format.raw/*11.6*/("""

    """),format.raw/*13.5*/("""<div class="alert-message block-message info">
        <p>
            <strong>Welcome In Go Game!</strong>
            To start, specify Size of Game Board and click Start Button.
        </p>
    </div>

""")))}))
      }
    }
  }

  def render(): play.twirl.api.HtmlFormat.Appendable = apply()

  def f:(() => play.twirl.api.HtmlFormat.Appendable) = () => apply()

  def ref: this.type = this

}


}

/**/
object index extends index_Scope0.index
              /*
                  -- GENERATED --
                  DATE: Sun Jan 22 04:08:44 CET 2017
                  SOURCE: C:/Users/cp24/IdeaProjects/Game/app/views/index.scala.html
                  HASH: 4de4a7a26a6474376571b5bff0d03b19802594ec
                  MATRIX: 827->1|845->11|884->13|916->20|954->50|993->52|1029->62|1145->152|1158->157|1191->170|1231->183|1287->209|1320->215
                  LINES: 32->1|32->1|32->1|34->3|34->3|34->3|36->5|38->7|38->7|38->7|39->8|42->11|44->13
                  -- GENERATED --
              */
          