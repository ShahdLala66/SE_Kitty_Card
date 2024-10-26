package model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class Value1Spec extends AnyWordSpec with Matchers {
  "Value1 Enumeration" should {
    "have correct integer values" in {
      Value1.toInt(Value1.One) shouldBe 1
      Value1.toInt(Value1.Two) shouldBe 2
      Value1.toInt(Value1.Three) shouldBe 3
      Value1.toInt(Value1.Four) shouldBe 4
      Value1.toInt(Value1.Five) shouldBe 5
      Value1.toInt(Value1.Six) shouldBe 6
      Value1.toInt(Value1.Seven) shouldBe 7
      Value1.toInt(Value1.Eight) shouldBe 8
    }

    "contain all defined values" in {
      Value1.values should contain allOf (Value1.One, Value1.Two, Value1.Three, Value1.Four, Value1.Five, Value1.Six, Value1.Seven, Value1.Eight)
    }

    "convert all values to their integer representation" in {
      Value1.values.foreach { value =>
        Value1.toInt(value) shouldBe value.id
      }
    }
  }
}