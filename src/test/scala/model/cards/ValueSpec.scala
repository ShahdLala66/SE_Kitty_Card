package model.cards

import model.cards.Value
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ValueSpec extends AnyWordSpec with Matchers {
  "Value1 Enumeration" should {
    "have correct integer values" in {
      Value.toInt(Value.One) shouldBe 1
      Value.toInt(Value.Two) shouldBe 2
      Value.toInt(Value.Three) shouldBe 3
      Value.toInt(Value.Four) shouldBe 4
      Value.toInt(Value.Five) shouldBe 5
      Value.toInt(Value.Six) shouldBe 6
      Value.toInt(Value.Seven) shouldBe 7
      Value.toInt(Value.Eight) shouldBe 8
    }
    "contain all defined values" in {
      Value.values should contain allOf (Value.One, Value.Two, Value.Three, Value.Four, Value.Five, Value.Six, Value.Seven, Value.Eight)
    }

    "convert all values to their integer representation" in {
      Value.values.foreach { value =>
        Value.toInt(value) shouldBe value.id
      }
    }
  }
}