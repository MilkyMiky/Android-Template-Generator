package ui.project

import com.intellij.ui.layout.panel
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JPanel
import javax.swing.JTextField

class ProjectPanel : JPanel(true) {


    val packageTextField = JTextField()

    init {
        layout = BorderLayout()
        val panel = panel {
            row("Project user package:") { packageTextField() }
        }
        add(panel, BorderLayout.CENTER)
    }

    override fun getPreferredSize() = Dimension(450, 110)


}