package aview

import scalafx.Includes.handle
import scalafx.scene.control.Button

class GridButton(x: Int, y: Int, onClick: (Int, Int) => Unit) extends Button {
  minWidth = 100
  minHeight = 150
  style = "-fx-background-color: rgba(255, 255, 255, 0.3)"

  onAction = handle {
    onClick(x, y)
  }
}