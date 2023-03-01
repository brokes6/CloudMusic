package com.brookes6.cloudmusic.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Author: fuxinbo

 * Date: 2023/3/1

 * Description:
 */
object TimeUtils {

    /**
     * 将秒数转换为展示的时间
     *
     * ps：若转换出来的时间为个位数，则将自动补充0
     *
     * @param time 秒
     * @return
     */
    fun timeInSecond(time: Long?): String {
        if (time == null) return "未知时长"
        val tmp = time / 1000
        val min = (tmp % 3600) / 60
        val second = (tmp % 3600) % 60
        val sp = StringBuilder()
        sp.append(if (min < 10) "0${min}" else min)
        sp.append(":")
        sp.append(if (second < 10) "0${second}" else second)
        return sp.toString()
    }

    /**
     * 将日期格式转换为秒数
     *
     * @param date 时间格式
     * @return
     */
    fun dateTransformTime(date: String): Long {
        var time: Long
        date.split(":").let {
            if (it.size != 2) return 0L
            time = (it[0].toLong() * 60) + it[1].subSequence(0, 2).toString().toLong()
        }
        return time
    }
}