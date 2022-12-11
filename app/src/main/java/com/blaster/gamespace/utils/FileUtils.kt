package com.blaster.gamespace.utils

import android.util.Log
import java.io.*


class FileUtils {

    companion object {
        private const val TAG = "FileUtils"

        /**
         * Writes the given value into the given file
         *
         * @return true on success, false on failure
         */
        fun writeLine(fileName: String, value: String?): Boolean {
            var writer: BufferedWriter? = null
            try {
                writer = BufferedWriter(FileWriter(fileName))
                writer.write(value)
            } catch (e: FileNotFoundException) {
                Log.w(Companion.TAG, "No such file $fileName for writing", e)
                return false
            } catch (e: IOException) {
                Log.e(Companion.TAG, "Could not write to file $fileName", e)
                return false
            } finally {
                try {
                    writer?.close()
                } catch (e: IOException) {
                    // Ignored, not much we can do anyway
                }
            }
            return true
        }

        /**
         * Checks whether the given file exists
         *
         * @return true if exists, false if not
         */
        fun fileExists(fileName: String?): Boolean {
            val file = File(fileName)
            return file.exists()
        }

        /**
         * Checks whether the given file is readable
         *
         * @return true if readable, false if not
         */
        fun isFileReadable(fileName: String?): Boolean {
            val file = File(fileName)
            return file.exists() && file.canRead()
        }

        /**
         * Checks whether the given file is writable
         *
         * @return true if writable, false if not
         */
        fun isFileWritable(fileName: String?): Boolean {
            val file = File(fileName)
            return file.exists() && file.canWrite()
        }

        /**
         * Deletes an existing file
         *
         * @return true if the delete was successful, false if not
         */
        fun delete(fileName: String): Boolean {
            val file = File(fileName)
            var ok = false
            try {
                ok = file.delete()
            } catch (e: SecurityException) {
                Log.w(Companion.TAG, "SecurityException trying to delete $fileName", e)
            }
            return ok
        }

        /**
         * Renames an existing file
         *
         * @return true if the rename was successful, false if not
         */
        fun rename(srcPath: String, dstPath: String): Boolean {
            val srcFile = File(srcPath)
            val dstFile = File(dstPath)
            var ok = false
            try {
                ok = srcFile.renameTo(dstFile)
            } catch (e: SecurityException) {
                Log.w(
                    Companion.TAG,
                    "SecurityException trying to rename $srcPath to $dstPath",
                    e
                )
            } catch (e: NullPointerException) {
                Log.e(
                    Companion.TAG,
                    "NullPointerException trying to rename " + srcPath + " to " +
                            dstPath,
                    e
                )
            }
            return ok
        }

        /**
         * Reads the first line of text from the given file.
         * Reference [BufferedReader.readLine] for clarification on what a
         * line is
         *
         * @return the read line contents, or null on failure
         */
        fun readOneLine(fileName: String): String? {
            var line: String? = null
            var reader: BufferedReader? = null
            try {
                reader = BufferedReader(FileReader(fileName), 512)
                line = reader.readLine()
            } catch (e: FileNotFoundException) {
                Log.w(Companion.TAG, "No such file $fileName for reading", e)
            } catch (e: IOException) {
                Log.e(Companion.TAG, "Could not read from file $fileName", e)
            } finally {
                try {
                    reader?.close()
                } catch (e: IOException) {
                    // Ignored, not much we can do anyway
                }
            }
            return line
        }
    }
}
