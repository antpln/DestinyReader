package com.plin.destinyreader

import org.junit.Test
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun unzip() {
        val filePath = "/home/antoine/Bureau/temp_database.zip"
        val fis = FileInputStream(filePath)
        val zis = ZipInputStream(BufferedInputStream(fis))

        val zipfile = ZipFile(filePath)
        val entry = zis.nextEntry
        val file = FileOutputStream("/home/antoine/Bureau/DATABASE.sqlite", false)
        var a = 0
        val buffer = ByteArray(1024)
        var count = 0
        while (zis.read(buffer).also { count = it } > 0) {
            file.write(buffer, 0, count)
        }
        print(File("/home/antoine/Bureau/DATABASE.sqlite").length().toString())
        zis.closeEntry()
        zipfile.close()
    }

}
