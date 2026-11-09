package com.example.tp_9___composant

import org.junit.Test
import org.apache.pdfbox.Loader
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.nio.charset.StandardCharsets

class ExampleUnitTest {
    @Test
    fun extractPdfWithPdfBox() {
        val pdfFile = File("C:/Users/user/OneDrive/Documents/TP Composant/TP_9___Composant.pdf")
        if (!pdfFile.exists()) {
            println("PDF file not found at ${pdfFile.absolutePath}")
            return
        }
        val document = Loader.loadPDF(pdfFile)
        val stripper = PDFTextStripper()
        val text = stripper.getText(document)
        document.close()

        val outFile = File("C:/Users/user/AndroidStudioProjects/TP_9___Composant/.artifacts/6c0d6694-0ecb-4648-958f-86f6efce6e14/scratch/pdf_full_text.txt")
        outFile.parentFile?.mkdirs()
        outFile.writeText(text, StandardCharsets.UTF_8)
        println("Successfully extracted PDF text (${text.length} chars) to ${outFile.absolutePath}")
    }
}
