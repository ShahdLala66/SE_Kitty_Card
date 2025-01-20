package model.baseImp

import model.gameModelComp.baseImp.Value
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ValueSpec extends AnyWordSpec with Matchers {
    "Value1 Enumeration" should {
        "have correct integer values" in {
            Value.toInt(Value.One)
            Value.toInt(Value.Two)
            Value.toInt(Value.Three)
            Value.toInt(Value.Four)
            Value.toInt(Value.Five)
            Value.toInt(Value.Six)
        }
        "contain all defined values" in {
            Value.values should contain allOf (Value.One, Value.Two, Value.Three, Value.Four, Value.Five, Value.Six)
        }

        "convert all values to their integer representation" in {
            Value.values.foreach { value =>
                Value.toInt(value) shouldBe value.id
            }
        }
    }
}