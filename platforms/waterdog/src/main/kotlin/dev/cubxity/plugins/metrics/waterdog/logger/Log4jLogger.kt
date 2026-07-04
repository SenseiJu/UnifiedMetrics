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

package dev.cubxity.plugins.metrics.waterdog.logger

import dev.cubxity.plugins.metrics.api.logging.Logger

/**
 * WaterdogPE exposes a log4j2 logger through [dev.waterdog.waterdogpe.plugin.Plugin.getLogger],
 * so unlike the Velocity (SLF4J) and Bungee (java.util.logging) platforms we wrap that here.
 */
class Log4jLogger(private val logger: org.apache.logging.log4j.Logger) : Logger {
    override fun info(message: String) {
        logger.info(message)
    }

    override fun warn(message: String) {
        logger.warn(message)
    }

    override fun warn(message: String, error: Throwable) {
        logger.warn(message, error)
    }

    override fun severe(message: String) {
        logger.error(message)
    }

    override fun severe(message: String, error: Throwable) {
        logger.error(message, error)
    }
}
