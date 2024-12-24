//package aview.Gui
//
//class Test
//
//def showCardsGUI(cards: Seq[Card]): Unit = {
//  println("Starting showCardsGUI method")
//  GuiInitializer.ensureInitialized()
//
//  Platform.runLater {
//    try {
//      println("Processing cards...")
//      val cardImages = cards.map {
//        case NumberCards(suit, value) =>
//          println(s"Processing card: $suit $value")
//          val numericValue = value match {
//            case Value.One => "1"
//            case Value.Two => "2"
//            case Value.Three => "3"
//            case Value.Four => "4"
//            case Value.Five => "5"
//            case Value.Six => "6"
//            case _ => throw new IllegalArgumentException(s"Unsupported card value: $value")
//          }
//
//          val germanSuit = suit.toString match {
//            case "Red" => "Rot"
//            case "Purple" => "Lila"
//            case "Brown" => "Braun"
//            case "Blue" => "Blau"
//            case "Green" => "Grün"
//            case _ => throw new IllegalArgumentException(s"Unsupported card suit: $suit")
//          }
//
//          println(s"Created CardImage for $numericValue of $germanSuit")
//          CardImage(numericValue, germanSuit)
//      }
//
//      println("Creating card buttons...")
//      val cardButtons = cardImages.map(cardImage => {
//        println(s"Creating button for ${cardImage.value} of ${cardImage.suit}")
//        new CardButton(cardImage, _ => {})
//      })
//
//      cardPane.children = cardButtons
//      updateDisplay()
//
//    } catch {
//      case e: Exception =>
//        println(s"Error in GUI creation: ${e.getMessage}")
//        e.printStackTrace()
//    }
//  }
//}
