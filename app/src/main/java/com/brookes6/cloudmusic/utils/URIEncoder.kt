package com.brookes6.cloudmusic.utils

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*

/**
 * @Author fuxinbo
 * @Date 2023/1/16 12:21
 * @Description TODO
 */
object URIEncoder {

    const val ALLOWED_CHARS =
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()"

    /**
     * Description:
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     * @see
     */
    @Throws(UnsupportedEncodingException::class)
    fun encodeURI(str: String): String {
        val isoStr = String(str.toByteArray(charset("UTF8")), Charset.forName("ISO-8859-1"))
        val chars = isoStr.toCharArray()
        val sb = StringBuffer()
        for (i in chars.indices) {
            if (chars[i] in 'a'..'z' || chars[i] in 'A'..'Z' || chars[i] == '-' || chars[i] == '_' || chars[i] == '.' || chars[i] == '!' || chars[i] == '~' || chars[i] == '*' || chars[i] == '\'' || chars[i] == '(' || chars[i] == ')' || chars[i] == ';' || chars[i] == '/' || chars[i] == '?' || chars[i] == ':' || chars[i] == '@' || chars[i] == '&' || chars[i] == '=' || chars[i] == '+' || chars[i] == '$' || chars[i] == ',' || chars[i] == '#' || chars[i] <= '9' && chars[i] >= '0') {
                sb.append(chars[i])
            } else {
                sb.append("%")
                sb.append(Integer.toHexString(chars[i].code))
            }
        }
        return sb.toString()
    }

    /**
     * Description:
     *
     * @param input
     * @return
     * @see
     */
    fun encodeURIComponent(input: String?): String? {
        if (null == input || "" == input.trim { it <= ' ' }) {
            return input
        }
        val l = input.length
        val o = StringBuilder(l * 3)
        try {
            for (i in 0 until l) {
                val e = input.substring(i, i + 1)
                if (ALLOWED_CHARS.indexOf(e) == -1) {
                    val b = e.toByteArray(charset("utf-8"))
                    o.append(getHex(b))
                    continue
                }
                o.append(e)
            }
            return o.toString()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return input
    }

    private fun getHex(buf: ByteArray): String {
        val o = StringBuilder(buf.size * 3)
        for (i in buf.indices) {
            val n = buf[i].toInt() and 0xff
            o.append("%")
            if (n < 0x10) {
                o.append("0")
            }
            o.append(n.toLong().toString(16).uppercase(Locale.getDefault()))
        }
        return o.toString()
    }
}