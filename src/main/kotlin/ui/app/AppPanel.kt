package ui.app

import com.intellij.ui.layout.panel
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class AppPanel : JPanel(true) {

    val packageTextField = JTextField()

    init {
        layout = BorderLayout()
        val panel = panel {
            row("App module package:") { packageTextField() }
        }
        add(panel, BorderLayout.CENTER)
    }

    override fun getPreferredSize() = Dimension(450, 110)

}