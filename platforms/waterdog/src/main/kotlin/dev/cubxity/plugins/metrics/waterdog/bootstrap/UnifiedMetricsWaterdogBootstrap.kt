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

package dev.cubxity.plugins.metrics.waterdog.bootstrap

import dev.cubxity.plugins.metrics.api.platform.PlatformType
import dev.cubxity.plugins.metrics.common.UnifiedMetricsBootstrap
import dev.cubxity.plugins.metrics.common.plugin.dispatcher.CurrentThreadDispatcher
import dev.cubxity.plugins.metrics.waterdog.UnifiedMetricsWaterdogPlugin
import dev.cubxity.plugins.metrics.waterdog.logger.Log4jLogger
import dev.waterdog.waterdogpe.plugin.Plugin
import kotlinx.coroutines.CoroutineDispatcher
import java.nio.file.Path

class UnifiedMetricsWaterdogBootstrap : Plugin(), UnifiedMetricsBootstrap {
    private val plugin = UnifiedMetricsWaterdogPlugin(this)

    override val type: PlatformType
        get() = PlatformType.WaterdogPE

    override val version: String
        get() = description.version

    override val serverBrand: String
        get() = "WaterdogPE"

    override val dataDirectory: Path
        get() = dataFolder.toPath()

    override val configDirectory: Path
        get() = dataFolder.toPath()

    // WaterdogPE only populates the logger/description/dataFolder in Plugin.init(), which runs
    // after the constructor, so this must be resolved lazily (first accessed during onEnable).
    override val logger by lazy { Log4jLogger(getLogger()) }

    override val dispatcher: CoroutineDispatcher = CurrentThreadDispatcher

    override fun onEnable() {
        plugin.enable()
    }

    override fun onDisable() {
        plugin.disable()
    }
}
