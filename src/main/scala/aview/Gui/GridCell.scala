package aview.Gui

import scalafx.scene.control.Button
import scalafx.scene.layout.GridPane
import scalafx.scene.text.Text

class GridCell(x: Int, y: Int, onClick: (Int, Int) => Unit) extends Button {
  text = s"($x, $y)"
  onAction = _ => onClick(x, y)
}