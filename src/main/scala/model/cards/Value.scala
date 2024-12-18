package model.cards

object Value extends Enumeration {
    type Value1 = Value
    val One: Value = Value(1)
    val Two: Value = Value(2)
    val Three: Value = Value(3)
    val Four: Value = Value(4)
    val Five: Value = Value(5)
    val Six: Value = Value(6)
    val Seven: Value = Value(7)
    val Eight: Value = Value(8)

    // Helper method to convert a Value to its integer representation
    def toInt(value: Value): Int = value match {
        case One => 1
        case Two => 2
        case Three => 3
        case Four => 4
        case Five => 5
        case Six => 6
        case Seven => 7
        case Eight => 8
    }
}