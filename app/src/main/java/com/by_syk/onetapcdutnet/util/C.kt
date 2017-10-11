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

import android.os.Build

/**
 * Created by By_syk on 2017-06-09.
 */

object C {
    val SDK = Build.VERSION.SDK_INT

    val LOG_TAG = "ONE_TAP_CDUT_NET"

    // 校园网登录地址
    val CAMPUS_NET_URL = "http://172.20.255.252"
    // 多组备用登录帐号、密码
    val ACCOUNTS = arrayOf(
            "", "",
            "", "",
            "", "",
            "", ""
    )
}