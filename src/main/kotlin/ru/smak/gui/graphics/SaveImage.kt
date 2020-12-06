package ru.smak.gui.graphics

import ru.smak.gui.graphics.convertation.CartesianScreenPlane
import ru.smak.gui.graphics.convertation.Converter
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import javax.imageio.ImageIO
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.filechooser.FileNameExtensionFilter

class SaveImage(val plane: CartesianScreenPlane, val img:BufferedImage, val detal:Boolean, val colorScheme:String) {
    init {
        val fileChooser = JFileChooser()
        with(fileChooser) {
            setDialogTitle("Сохранение файла...")
            val filter1 = FileNameExtensionFilter("jpg", "jpg")
            val filter2 = FileNameExtensionFilter("png", "png")
            addChoosableFileFilter(filter1)
            addChoosableFileFilter(filter2)
        }
        SaveImage(fileChooser)
    }

    fun SaveImage(fileChooser: JFileChooser) {
        //Код, который нужно выполнить при нажатии
        // Создать буфер изображения в оперативной памяти
      //  val bufferedImage = BufferedImage(img.getWidth(), img.getHeight() + Converter.yCrt2Scr(0.001, plane)/5, BufferedImage.TYPE_INT_ARGB)
      //  val bufferedImage = BufferedImage(img.getWidth(), img.getHeight() + 50, BufferedImage.TYPE_INT_ARGB)
        val bufferedImage = BufferedImage(img.getWidth(), img.getHeight() + (img.getHeight().toDouble()*0.1).toInt(), BufferedImage.TYPE_INT_ARGB)
        val g: Graphics = bufferedImage.getGraphics()
        val xMin= BigDecimal(plane.xMin).setScale(2, RoundingMode.HALF_EVEN).toString()
        val xMax= BigDecimal(plane.xMax).setScale(2, RoundingMode.HALF_EVEN).toString()
        val yMin= BigDecimal(plane.yMin).setScale(2, RoundingMode.HALF_EVEN).toString()
        val yMax= BigDecimal(plane.yMax).setScale(2, RoundingMode.HALF_EVEN).toString()
        val fnt= Font("Cambria", Font.BOLD, 14)
        g.font=fnt
        val m=g.fontMetrics
        val hxMin=m.getStringBounds("xMin="+xMin, g).height.toInt()
        val wxMin=m.getStringBounds("xMin="+xMin, g).width.toInt()
        val hyMin=m.getStringBounds("yMin="+yMin, g).height.toInt()
        val wyMin=m.getStringBounds("yMin="+yMin, g).width.toInt()
        val wxMax=m.getStringBounds("xMax="+xMax, g).width.toInt()
        g.setColor(Color.black)
      //  g.drawRect(0, img.getHeight(), img.getWidth() - 2, Math.abs(Converter.yCrt2Scr(0.001, plane)/6))
      //  g.drawRect(0, img.getHeight(), img.getWidth() - 2, 48)
        g.drawRect(0, img.getHeight(), img.getWidth() - 1, (img.getHeight().toDouble()*0.09).toInt())
        println(Converter.yCrt2Scr(0.001, plane)/6)
        g.setColor(Color.blue)
        g.drawString("xMin="+xMin, 20, img.getHeight() + 25)
        g.drawString("xMax="+xMax, 20, img.getHeight() + 27+hxMin)
        g.drawString("yMin="+yMin, 60+wxMin, img.getHeight() + 25)
        g.drawString("yMax="+yMax, 60+wxMax, img.getHeight() + 27+hyMin)
        val detalization=if (detal) "Вкл." else "Выкл."
        g.drawString("Детализация: "+detalization, 80+wxMin+wyMin, img.getHeight()+25)
        g.drawString("Цветовая схема: "+colorScheme, 80+wxMin+wyMin, img.getHeight()+27+hyMin)
        g.drawImage(img, 0, 0, null)
        g.dispose()
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY)
        val result = fileChooser.showSaveDialog(fileChooser)
        if (result == JFileChooser.APPROVE_OPTION) {
            var str = fileChooser.getSelectedFile().absolutePath
            if (fileChooser.getSelectedFile().extension == "") {
                if (fileChooser.fileFilter.description != "All Files") str = str + "." + fileChooser.fileFilter.description else str += ".jpg"
            }
            val outputFile = File(str)
            ImageIO.write(bufferedImage, "PNG", outputFile)
            JOptionPane.showMessageDialog(fileChooser,
                    "Файл '" + str +
                            "' сохранен")
        }
    }
}