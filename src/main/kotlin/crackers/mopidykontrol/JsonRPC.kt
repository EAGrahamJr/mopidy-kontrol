/*
 * Copyright 2022-2023 by E. A. Graham, Jr.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package crackers.mopidykontrol

import org.json.JSONObject

enum class Command(val method: String) {
    MixerGetVolume("core.mixer.get_volume"),
    MixerSetVolume("core.mixer.set_volume"),
    MixerGetMute("core.mixer.get_mute"),
    MixerSetMute("core.mixer.set_mute"),
    PlaybackGetState("core.playback.get_state"),
    PlaybackSetState("core.playback.set_state"),
    PlaybackNext("core.playback.next"),
    PlaybackPrevious("core.playback.previous"),
    PlaybackStop("core.playback.stop"),
    PlaybackPause("core.playback.pause"),
    PlaybackResume("core.playback.resume"),
    PlaybackPlay("core.playback.play")
}

open class RPCRequest(
    val jsonrpc: String = "2.0",
    val id: Int,
    command: Command,
    val params: Map<String, Any>? = null
) {
    val method = command.method

    fun toJson(): String {
        val json = JSONObject(this)
        return json.toString()
    }
}

open class RPCResponse(val jsonrpc: String = "2.0", val id: Int, val result: String? = null) {
    companion object {
        fun fromJson(string: String): RPCResponse {
            val json = JSONObject(string)
            return RPCResponse(
                json.getString("jsonrpc"),
                json.getInt("id"),
                if (json.has("result")) json.get("result").toString() else null
            )
        }
    }
}
