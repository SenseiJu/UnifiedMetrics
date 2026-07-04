/*
 *     This file is part of UnifiedMetrics.
 *
 *     UnifiedMetrics is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     UnifiedMetrics is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with UnifiedMetrics.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.cubxity.plugins.metrics.waterdog.metric.events

import dev.cubxity.plugins.metrics.api.metric.collector.Collector
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection
import dev.cubxity.plugins.metrics.api.metric.collector.Counter
import dev.cubxity.plugins.metrics.waterdog.bootstrap.UnifiedMetricsWaterdogBootstrap
import dev.waterdog.waterdogpe.event.defaults.InitialServerConnectedEvent
import dev.waterdog.waterdogpe.event.defaults.PlayerChatEvent
import dev.waterdog.waterdogpe.event.defaults.PlayerDisconnectedEvent
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent
import dev.waterdog.waterdogpe.event.defaults.ProxyPingEvent

/**
 * Counts core proxy events on WaterdogPE.
 *
 * Unlike Velocity's and Bungee's event managers, WaterdogPE's [dev.waterdog.waterdogpe.event.EventManager]
 * only exposes `subscribe` with no way to unsubscribe. Because the counters are backed by a thread-safe
 * `DoubleAdderStore` (several of these events are dispatched asynchronously on WaterdogPE's event executor),
 * we register the handlers once and gate them behind a [disposed] flag so that a re-registered collection
 * (e.g. after a config reload) does not double-count.
 */
class EventsCollection(private val bootstrap: UnifiedMetricsWaterdogBootstrap) : CollectorCollection {
    private val loginCounter = Counter("minecraft_events_login_total")
    private val joinCounter = Counter("minecraft_events_join_total")
    private val quitCounter = Counter("minecraft_events_quit_total")
    private val chatCounter = Counter("minecraft_events_chat_total")
    private val pingCounter = Counter("minecraft_events_ping_total")

    @Volatile
    private var disposed = false

    override val collectors: List<Collector> =
        listOf(loginCounter, joinCounter, quitCounter, chatCounter, pingCounter)

    override fun initialize() {
        val eventManager = bootstrap.proxy.eventManager
        eventManager.subscribe(PlayerLoginEvent::class.java) { if (!disposed) loginCounter.inc() }
        eventManager.subscribe(InitialServerConnectedEvent::class.java) { if (!disposed) joinCounter.inc() }
        eventManager.subscribe(PlayerDisconnectedEvent::class.java) { if (!disposed) quitCounter.inc() }
        eventManager.subscribe(PlayerChatEvent::class.java) { if (!disposed) chatCounter.inc() }
        eventManager.subscribe(ProxyPingEvent::class.java) { if (!disposed) pingCounter.inc() }
    }

    override fun dispose() {
        // WaterdogPE's EventManager has no unsubscribe; disarm the handlers instead.
        disposed = true
    }
}
