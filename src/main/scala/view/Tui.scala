package view

class Tui {

  // Mapping our desired colors to ANSI color codes
  private val colors = Map(
    "Green" -> "\u001b[32m",
    "Brown" -> "\u001b[33m",
    "Purple" -> "\u001b[35m",
    "Blue" -> "\u001b[34m",
    "Red" -> "\u001b[31m",
    "White" -> "\u001b[37m"
  )

  // Method to print the cat in a specific color
  private def printColoredCat(color: String): Unit = {
    println(s"$color ∧,,,∧")
    println(s"$color( ̳• · •̳)")
    println(s"$color/    づ♡")
    println("\u001b[0m") // Reset color after printing
  }

  // Method to print the cat in all colors in a loop
  def printCatLoop(): Unit = {
    for (color <- colors.values) {
      printColoredCat(color)
      Thread.sleep(500) // Optional: Delay for visual effect
    }
  }

  // New method to print the cat in a specific color based on the color name
  def printCatInColor(color: String): Unit = {
    val colorCode = colors.getOrElse(color, "\u001b[0m")
    println(s"$colorCode ∧,,,∧")
    println(s"$colorCode( ̳• · •̳)")
    println(s"$colorCode/    づ♡")
    println("\u001b[0m") // Reset color after printing
  }

  // Method to print a bad choice message
  def printBadChoice(color: String): Unit = {
    val colorCode = colors.getOrElse(color, "\u001b[0m") //
    println(s"$colorCode ∧,,,∧")
    println(s"$colorCode ( ̳- · -̳)")
    println(s"$colorCode/ づbad choiceづ")
    println("\u001b[0m") // Reset color after printing
  }

  // Method to print a meh message
  def printMeh(color: String): Unit = {
    val colorCode = colors.getOrElse(color, "\u001b[0m")
    println(s"$colorCode ∧,,,∧")
    println(s"$colorCode ( ̳- · -̳)")
    println(s"$colorCode/ づmehづ")
    println("\u001b[0m") // Reset color after printing
  }
}