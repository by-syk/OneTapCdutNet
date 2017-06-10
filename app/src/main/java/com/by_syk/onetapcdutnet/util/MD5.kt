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

import java.security.MessageDigest

/**
 * Created by By_syk on 2017-06-09.
 */

object MD5 {
    /**
     * MD5 加密并返回十六进制结果
     */
    fun md5(text: String): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.update(text.toByteArray())
        val bytes = messageDigest.digest()
        return parseHex(bytes)
    }

    private fun parse(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(b)
        }
        return sb.toString()
    }

    private fun parseHex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            val tmp = (b.toInt() and 0xff).toString(16)
            if (tmp.length == 1) {
                sb.append("0")
            }
            sb.append(tmp)
        }
        return sb.toString()
    }
}