/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package crackers.mopidykontrol

import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.WebSocket
import java.time.Duration
import java.util.concurrent.CompletionStage
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class MopidyKlient(server: String, port: Int = 6680, timeout: Duration = Duration.ofSeconds(5)) : AutoCloseable {
    private val logger = LoggerFactory.getLogger(MopidyKlient::class.java.simpleName)

    enum class PlayerState {
        PLAYING, PAUSED, STOPPED
    }

    private val idCounter = AtomicInteger(0)
    private val nextId: Int
        get() = idCounter.incrementAndGet()

    private val errorResponse = RPCResponse(id = -1)
    private val responseMapper = ConcurrentHashMap<Int, SynchronousQueue<RPCResponse>>()
    private val socketListener = object : WebSocket.Listener {
        override fun onText(webSocket: WebSocket?, data: CharSequence?, last: Boolean): CompletionStage<*>? {
            val response = try {
                data?.let {
                    logger.debug("Received: $it")
                    if (it.contains("\"event\":")) {
                        logger.info("Event: $it")
                        return super.onText(webSocket, data, last)
                    }
                    RPCResponse.fromJson(it.toString())
                } ?: errorResponse
            } catch (e: Exception) {
                logger.error("Cannot parse response: $e\n$data")
                errorResponse
            }
            responseMapper[response.id]?.put(response)
            return super.onText(webSocket, data, last)
        }
    }

    private val lock = ReentrantLock()

    private val wsClient: WebSocket = HttpClient.newHttpClient().newWebSocketBuilder()
        .connectTimeout(timeout)
        .buildAsync(URI("ws://$server:$port/mopidy/ws"), socketListener).get()

    override fun close() {
        wsClient.sendClose(WebSocket.NORMAL_CLOSURE, "Close").thenRun {
            logger.info("Closed")
        }.exceptionally {
            if (it !is IOException) logger.error("Close: $it")
            null
        }
    }

    fun play(args: String? = null) = lock.withLock {
        sendRequest(RPCRequest(id = nextId, command = Command.PlaybackPlay))
    }

    fun resume() = lock.withLock {
        sendRequest(RPCRequest(id = nextId, command = Command.PlaybackResume))
    }

    fun pause() = lock.withLock {
        sendRequest(RPCRequest(id = nextId, command = Command.PlaybackPause))
    }

    fun stop() = lock.withLock {
        sendRequest(RPCRequest(id = nextId, command = Command.PlaybackStop))
    }

    fun next() = lock.withLock {
        sendRequest(RPCRequest(id = nextId, command = Command.PlaybackNext))
    }

    fun previous() = lock.withLock {
        sendRequest(RPCRequest(id = nextId, command = Command.PlaybackPrevious))
    }

    var volume: Int
        get() = lock.withLock {
            logger.debug("Getting volume")
            return 0
        }
        set(value) = lock.withLock {
            logger.debug("Setting volume to $value")
        }
    var mute: Boolean
        get() = lock.withLock {
            logger.debug("Getting mute")
            return false
        }
        set(value) = lock.withLock {
            logger.debug("Setting mute to $value")
        }
    var state: PlayerState
        get() = lock.withLock {
            logger.debug("Getting state")
            return PlayerState.STOPPED
        }
        set(value) = lock.withLock {
            logger.debug("Setting state to $value")
        }

    private fun sendRequest(request: RPCRequest) {
        logger.info("Sending command: ${request.method}")
        wsClient.sendText(request.toJson(), true).exceptionally {
            logger.error("Error on send", it)
            null
        }
    }
}
