package ui.mvi

import com.intellij.ui.layout.panel
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JPanel
import javax.swing.JTextField

class MVIPanel : JPanel(true) {


    val nameTextField = JTextField()
    val packageTextField = JTextField()

    init {
        layout = BorderLayout()
        val panel = panel {
            row("Package:") { packageTextField() }
            row("Name:") { nameTextField() }
        }
        add(panel, BorderLayout.CENTER)
    }

    override fun getPreferredSize() = Dimension(450, 110)

}