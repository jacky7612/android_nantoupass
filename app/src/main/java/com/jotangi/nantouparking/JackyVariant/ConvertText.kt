package com.jotangi.nantouparking.JackyVariant

import android.graphics.Color
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import java.lang.Exception
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.random.Random

object ConvertText {
    fun resizeByteArray(originalArray: ByteArray, newSize: Int): ByteArray {
        val newArray = ByteArray(newSize)
        val length = minOf(originalArray.size, newSize)
        System.arraycopy(originalArray, 0, newArray, 0, length)
        return newArray
    }
    fun asciiToHex(ascii: String): String {
        val result = StringBuilder()
        var i=0
        for (char in ascii) {
            val hex = Integer.toHexString(char.toInt())
            if (i > 0)
                result.append(",$hex")
            else
                result.append("$hex")
            i++
        }
        return result.toString()
    }
    fun getRandomChar(length: Int): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
    fun generateRandomId(): String {
        val uuid = UUID.randomUUID()
        return uuid.toString()
    }
    fun convertByteToHex(byte: Byte): String
    {
        var hex=""

        // Iterating through each byte in the array
        hex += String.format(" %02X", byte)
        return hex;
    }
    fun convertByteToHexadecimal(byteArray: ByteArray): String
    {
        var hex=""

        // Iterating through each byte in the array
        for (i in byteArray) {
            hex += String.format(" %02X", i)
        }
        return hex;
    }
    fun convertByteToHexadecimal4View(byteArray: ByteArray): String
    {
        var hex=""

        // Iterating through each byte in the array
        var idx=0
        for (i in byteArray) {
            if (idx == 0) {
                hex += String.format(" %02X", i)
            } else {
                hex += String.format(",%02X", i)
            }
            idx++
            if (idx % 40 == 0) hex+="\n"
            else if (idx % 10 == 0) hex+="     "
        }
        return hex;
    }
    // Function to check if the input string is a valid hexadecimal number
    fun isValidHexadecimal(input: String): Boolean {
        try {
            Integer.parseInt(input, 16)
        } catch (e: NumberFormatException) {
            return false
        }
        return true
    }
    fun isValidHexadecimal2(input: String): Boolean {
        val regex = Regex("[0-9A-Fa-f,]*")
        return input.matches(regex)
    }
    fun convertStringToByteArray(input: String, LRC: Int=0): ByteArray {
        var outArray = input.split(",")
        var outCmd=ByteArray(outArray.size + LRC)
        for (i in 0 until outArray.size) {
            var intValue = outArray[i].toInt(16)
            var byteValue = intValue.toByte()
            outCmd[i] = byteValue
        }
        return outCmd
    }
    fun convertAmountStringToByteArray(input: String): ByteArray {
        var outCmd=ByteArray(input.length)
        for (i in 0 until input.length) {
            val curValue = "3" + input.substring(i, i + 1)
            var intValue = curValue.toInt(16)
            var byteValue = intValue.toByte()
            outCmd[i] = byteValue
        }
        return outCmd
    }
    fun fmtPaymentAmount(input: Int, pattern: String="%010d"): String {
        val amount = input.toString()
        var formatAmount = pattern.format(amount.toInt())
        formatAmount += "00"
        println("jacky amount :$formatAmount")
        val byteArray= convertAmountStringToByteArray(formatAmount)
        val hex= convertByteToHexadecimal(byteArray)
        println("jacky amount Hex:$hex")
        return formatAmount
    }
    fun convertDateStringToByteArray(input: String): ByteArray {
        var outCmd=ByteArray(input.length)
        for (i in 0 until input.length) {
            val curValue = "3" + input.substring(i, i + 1)
            var intValue = curValue.toInt(16)
            var byteValue = intValue.toByte()
            outCmd[i] = byteValue
        }
        return outCmd
    }
    fun getFormattedDate(input_date: String, pattern: String="yyyy-MM-dd"): String
    {
        var date_str: String=""
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                var date = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern(pattern)
                date_str = date.format(formatter)
//                println("david date_str:$date_str")
                if (input_date.isNotEmpty()) {
                    date = LocalDateTime.parse(input_date, formatter)
                    val formatter = DateTimeFormatter.ofPattern(pattern)
                    date_str = date.format(formatter)
                }
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        } catch (e: Exception) {}
        return date_str
    }
    fun getHours(start: String, end: String): Long {
        var ret: Long=0
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val dateTimeStart = LocalDateTime.parse(start, formatter)
                val dateTimeEnd = LocalDateTime.parse(end, formatter)
                val duration = Duration.between(dateTimeStart, dateTimeEnd)

                ret = duration.toHours()
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        } catch (e: Exception) {}
        return ret
    }
    fun getMinutes(start: String, end: String): Long {
        var ret: Long=0
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val dateTimeStart = LocalDateTime.parse(start, formatter)
                val dateTimeEnd = LocalDateTime.parse(end, formatter)
                val duration = Duration.between(dateTimeStart, dateTimeEnd)

                val hours = duration.toHours()
                ret = duration.toMinutes()
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        } catch (e: Exception) {}
        return ret
    }

    fun isNumeric(input: String): Boolean {
        val regex = """^-?(\d+(\.\d+)?|\.\d+)$""".toRegex()
        return regex.matches(input)
    }
    fun clearByteArray(originalArray: ByteArray): ByteArray {
        val newSize = originalArray.size
        return ByteArray(newSize) { 0 }
    }
    fun maskWords(input: String, start: Int, end: Int): String {
        // Ensure the start and end indices are within bounds
        if (start < 0 || end > input.length || start >= end) {
            throw IllegalArgumentException("Invalid start or end indices")
        }

        // Create a StringBuilder to construct the masked string
        val masked = StringBuilder(input)

        // Replace characters from start to end with '*'
        for (i in start until end) {
            masked[i] = '*'
        }

        return masked.toString()
    }

    fun maskRandomWords(text: String, numberOfWordsToMask: Int): SpannableStringBuilder {
        val words = text.split("")
        val spannableStringBuilder = SpannableStringBuilder(text)
        var iTimeOut = 15

        // 随机选择单词索引
        val indicesToMask = mutableSetOf<Int>()
        while (indicesToMask.size < numberOfWordsToMask) {
            val randomIndex = Random.nextInt(words.size)
            indicesToMask.add(randomIndex)
            iTimeOut--
            if (--iTimeOut == 0) break
        }

        // 替换随机选择的单词为星号
        for (index in indicesToMask) {
            val start = text.indexOf(words[index])
            val end = start + words[index].length

            // 使用星号替换
            spannableStringBuilder.replace(start, end, "*".repeat(words[index].length))

            // 可选：给星号部分设置颜色
            spannableStringBuilder.setSpan(
                ForegroundColorSpan(Color.RED),
                start,
                start + words[index].length,
                0
            )
        }

        return spannableStringBuilder
    }
}