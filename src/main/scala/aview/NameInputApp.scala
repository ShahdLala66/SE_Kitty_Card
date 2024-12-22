package aview

import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.VBox
import scalafx.stage.Stage

class NameInputApp(onSubmit: (String, String) => Unit) {

  // Create the stage for the GUI
  private val stage: Stage = new Stage {
    title = "Welcome to the Kitty Zayne Cards haha"
    scene = new Scene {
      val firstNameField: TextField = new TextField {
        promptText = "Enter Player 1"
      }
      val lastNameField: TextField = new TextField {
        promptText = "Enter Player 2"
      }
      val submitButton: Button = new Button("Submit") {
        onAction = _ => {
          val firstName = firstNameField.text.value
          val lastName = lastNameField.text.value
          onSubmit(firstName, lastName) // Pass the data back via the callback
          stage.close() // Close the GUI
        }
      }
      content = new VBox(10,
        new Label("First Name:"), firstNameField,
        new Label("Last Name:"), lastNameField,
        submitButton
      )
    }
  }

  // Method to show the GUI
  def show(): Unit = {
    Platform.runLater(stage.show())
  }
}