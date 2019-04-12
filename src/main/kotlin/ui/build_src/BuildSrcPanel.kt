package ui.build_src

import com.intellij.ui.layout.panel
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class BuildSrcPanel : JPanel(true) {

    private val label = JLabel("Create build src?")

    init {
        layout = BorderLayout()
        val panel = panel { label }
        add(panel.add(label), BorderLayout.CENTER)
    }

    override fun getPreferredSize() = Dimension(450, 110)

}