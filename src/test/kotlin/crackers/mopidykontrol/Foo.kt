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

import com.typesafe.config.ConfigFactory

/**
 * TODO suffices for now, but looking for a "real" fake server to test with
 */
fun main() {
    val client = with(ConfigFactory.load()) {
        MopidyKlient(getString("mopidy.host"), getInt("mopidy.port"))
    }

    while (true) {
        println(client.volume)
        Thread.sleep(1000)
    }
}

/*

10:17:51.353 [HttpClient-12-Worker-2] INFO MopidyKlient -- {"event": "tracklist_changed"}
10:17:51.534 [main] DEBUG MopidyKlient -- Sending command: core.mixer.get_volume
10:17:52.114 [HttpClient-12-Worker-2] INFO MopidyKlient -- {"volume": 40, "event": "volume_changed"}
40
10:17:52.717 [HttpClient-12-Worker-2] INFO MopidyKlient -- {"old_state": "stopped", "new_state": "playing", "event": "playback_state_changed"}
10:17:52.764 [HttpClient-12-Worker-2] INFO MopidyKlient -- {"tl_track": {"__model__": "TlTrack", "tlid": 1, "track": {"__model__": "Track", "uri": "local:track:noise/7.Super%20Deep%20Stretched%20Brown%20Noise-1.mp3", "name": "7.Super Deep Stretched Brown Noise-1.mp3", "length": 3647772, "last_modified": 1696701077000}}, "event": "track_playback_started"}


10:19:14.196 [HttpClient-1-Worker-0] INFO MopidyKlient -- {"old_state": "playing", "new_state": "paused", "event": "playback_state_changed"}
10:19:14.196 [HttpClient-1-Worker-0] INFO MopidyKlient -- {"tl_track": {"__model__": "TlTrack", "tlid": 1, "track": {"__model__": "Track", "uri": "local:track:noise/7.Super%20Deep%20Stretched%20Brown%20Noise-1.mp3", "name": "7.Super Deep Stretched Brown Noise-1.mp3", "length": 3647772, "last_modified": 1696701077000}}, "time_position": 80844, "event": "track_playback_paused"}

10:19:16.906 [HttpClient-1-Worker-0] INFO MopidyKlient -- {"old_state": "paused", "new_state": "playing", "event": "playback_state_changed"}
10:19:16.907 [HttpClient-1-Worker-0] INFO MopidyKlient -- {"tl_track": {"__model__": "TlTrack", "tlid": 1, "track": {"__model__": "Track", "uri": "local:track:noise/7.Super%20Deep%20Stretched%20Brown%20Noise-1.mp3", "name": "7.Super Deep Stretched Brown Noise-1.mp3", "length": 3647772, "last_modified": 1696701077000}}, "time_position": 81055, "event": "track_playback_resumed"}
 */
