package model.cards

import model.objects.cards.{AssistCard, AssistCardType}
import org.scalatest.wordspec.AnyWordSpec

class AssistCardsSpec extends AnyWordSpec {

    "An AssistCard" should {

        "have a card type" in {
            val card = AssistCard(AssistCardType.Skip)
            assert(card.cardType == AssistCardType.Skip)
        }

        "return 'Special' as color" in {
            val card = AssistCard(AssistCardType.Freeze)
            assert(card.getColor == "Special")
        }

        "return the correct string representation" in {
            val card = AssistCard(AssistCardType.MeowThis)
            assert(card.toString == "MeowThis")
        }
    }
}
