/*
 * Copyright 2017 By_syk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.by_syk.onetapcdutnet.util

import android.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by By_syk on 2017-06-09.
 */

object CdutNet {
    /**
     * 检测校园网连接状态
     *
     * @return null - 未连接到校园网
     *         false - 已连接校园网但未登录
     *         true - 已登录校园网
     */
    fun check(): Boolean? {
        Log.d(C.LOG_TAG, "CdutNet - check")
        var bufferedReader: BufferedReader? = null
        try {
            val huc = URL(C.CAMPUS_NET_URL).openConnection() as HttpURLConnection
            huc.requestMethod = "GET"
            huc.connectTimeout = 3000
            huc.readTimeout = 3000
            val inputStream = huc.inputStream
            bufferedReader = BufferedReader(InputStreamReader(inputStream, "GBK"))
            val sbContent = StringBuilder()
            var buffer = bufferedReader.readLine()
            while (buffer != null) {
                sbContent.append(buffer)
                buffer = bufferedReader.readLine()
            }
            return sbContent.contains("已使用时间")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close()
            }
        }
        return null
    }

    /**
     * 登录校园网
     *
     * @param username 登录帐号
     * @param pwd 登录密码
     * @return true - 登录成功
     *         false - 登录失败
     */
    fun login(username: String, pwd: String): Boolean {
        Log.d(C.LOG_TAG, "CdutNet - login $username/$pwd")
        try {
            val huc = URL(C.CAMPUS_NET_URL).openConnection() as HttpURLConnection
            huc.requestMethod = "POST"
            huc.connectTimeout = 4000
            huc.readTimeout = 4000
            huc.doOutput = true

            val PID = "1"
            val CALG = "12345678"
            var enPwd = MD5.md5("$PID$pwd$CALG")
            enPwd = "$enPwd$CALG$PID"
            val paras = "DDDDD=$username&upass=$enPwd&R1=0&R2=1&para=00&0MKKey=123456"
            val dos = DataOutputStream(huc.outputStream)
            dos.write(paras.toByteArray())
            dos.flush()
            dos.close()

            val inputStream = huc.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream, "GBK"))
            val sbContent = StringBuilder()
            var buffer = bufferedReader.readLine()
            while (buffer != null) {
                sbContent.append(buffer)
                buffer = bufferedReader.readLine()
            }
            bufferedReader.close()

            return sbContent.contains("登录成功窗")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}
