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

package com.by_syk.onetapcdutnet.service

import android.os.AsyncTask
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.by_syk.onetapcdutnet.R
import com.by_syk.onetapcdutnet.util.C
import com.by_syk.onetapcdutnet.util.CdutNet
import com.by_syk.onetapcdutnet.util.Net

/**
 * Created by By_syk on 2017-06-09.
 */

class QSTileService : TileService() {
    /**
     * 图块可见回调
     */
    override fun onStartListening() {
        super.onStartListening()

        if (Net.isWiFiConnected(this)) {
            updateStatus(R.string.tile_label)
        } else { // 未连接 WiFi 则将图块置为不可用状态
            updateStatus(R.string.tile_status_no_net_conn, false)
        }
    }

    /**
     * 图块点击回调
     */
    override fun onClick() {
        super.onClick()

        // 仅在图块未被置为不可用状态（已连接 WiFi）才响应点击动作
        if (qsTile.state != Tile.STATE_INACTIVE) {
            Task().execute()
        }
    }

    /**
     * 更新图块状态
     *
     * @param labelId 文字ID
     * @param enable 状态
     */
    fun updateStatus(labelId: Int?, enable: Boolean = true) {
        if (labelId != null) {
            qsTile.label = getString(labelId)
        }
        if (enable) {
            qsTile.state = Tile.STATE_ACTIVE
        } else {
            qsTile.state = Tile.STATE_INACTIVE
        }
        qsTile.updateTile()
    }

    /**
     * 核心异步任务，检查、登录校园网
     */
    inner class Task : AsyncTask<String, Int, Boolean>() {
        override fun doInBackground(vararg params: String?): Boolean {
            publishProgress(R.string.tile_status_check)
            val res = CdutNet.check() // 检查校园网连接
            if (res == null) { // 未连接校园网，结束
                publishProgress(R.string.tile_status_not)
                return false
            } else if (!res) { // 未登录校园网，进行登录操作
                publishProgress(R.string.tile_status_loggin)
                if (!CdutNet.login(C.USER_NAME, C.PWD)) { // 登录失败，结束
                    publishProgress(R.string.tile_status_failed)
                    return false
                }
            }
            // 至此，表示已成功登录
            publishProgress(R.string.tile_status_ok)
            return true
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)

            updateStatus(values[0])
        }
    }
}
