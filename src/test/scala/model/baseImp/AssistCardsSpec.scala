package model.baseImp

import model.gameModelComp.baseImp.{AssistCardInterface, AssistCardType}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class AssistCardsSpec extends AnyWordSpec with Matchers {

    "An AssistCard" should {
        "have a card type" in {
            val card = AssistCardInterface(AssistCardType.Skip)
            card.cardType should be (AssistCardType.Skip)
        }

        "return the correct color" in {
            val card = AssistCardInterface(AssistCardType.Freeze)
            card.getColor should be ("Special")
        }

        "return the correct string representation" in {
            val card = AssistCardInterface(AssistCardType.MeowThis)
            card.toString should be ("MeowThis")
        }
    }
}