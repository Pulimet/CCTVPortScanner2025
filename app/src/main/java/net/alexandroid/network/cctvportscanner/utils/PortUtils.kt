package net.alexandroid.network.cctvportscanner.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import net.alexandroid.network.cctvportscanner.repo.PortScanStatus

object PortUtils {
    fun convertStringToIntegerList(ports: String): ArrayList<Int> {
        val list = ArrayList<Int>()
        val regex = "[^0-9,\\-]"
        val result = ports.replace(regex.toRegex(), "")

        val lines = result.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (line in lines) {
            if (line.contains("-")) {
                val p = line.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (p.size > 1) {
                    if (p[0].isNotEmpty() && p[1].isNotEmpty() && p[0].length < 6 && p[1].length < 6) {
                        val from = p[0].toInt()
                        val to = p[1].toInt()
                        if (to in (from + 1)..65534 && from > 0) {
                            for (a in from..to) {
                                list.add(a)
                            }
                        }
                    }
                }
            } else {
                if (line.isNotEmpty() && line.length < 6) {
                    val checkPort = line.toInt()
                    if ((checkPort > 0) and (checkPort < 65536)) list.add(checkPort)
                }
            }
        }
        return list
    }

    fun convertIntegerListToString(list: ArrayList<Int>): String {
        val stringBuilder = StringBuilder()
        var firstRangeNum = -1
        for (i in list.indices) {
            if (i + 1 < list.size && list[i + 1] - 1 == list[i]) {
                if (firstRangeNum < 0) {
                    firstRangeNum = list[i]
                }
            } else {
                if (firstRangeNum > -1) {
                    stringBuilder.append(firstRangeNum)
                    stringBuilder.append("-")
                    firstRangeNum = -1
                }
                stringBuilder.append(list[i])
                if (i + 1 < list.size) {
                    stringBuilder.append(",")
                }
            }
        }

        return stringBuilder.toString()
    }

    fun convertResultToMapWithColors(results: Map<Int, PortScanStatus>): Map<String, Color> {
        val map = mutableMapOf<String, Color>()
        var firstRangeNum = 0

        val ports: List<Int> = results.keys.toList().sorted()

        for (i in ports.indices) {
            //Log.d("PortUtils", "Port: ${ports[i]} is ${results[ports[i]]}")
            val port: Int = ports[i]
            var nextPort = 0
            var isNextPortOpen = false
            if (i + 1 < ports.size) {
                nextPort = ports[i + 1]
                isNextPortOpen = results[nextPort] === PortScanStatus.OPEN
            }
            val state: PortScanStatus = results[port] ?: PortScanStatus.CLOSED

            if (state == PortScanStatus.OPEN) {
                if (isNextPortOpen && nextPort - port == 1) {
                    if (firstRangeNum == 0) {
                        firstRangeNum = port
                    }
                } else {
                    if (firstRangeNum == 0) {
                        map["$port "] = Green
                    } else {
                        map["$firstRangeNum - $port "] = Green
                        firstRangeNum = 0
                    }
                }
            } else {
                if (!isNextPortOpen && nextPort - port == 1) {
                    if (firstRangeNum == 0) {
                        firstRangeNum = port
                    }
                } else {
                    if (firstRangeNum == 0) {
                        map["$port "] = Color.Red
                    } else {
                        map["$firstRangeNum - $port "] = Color.Red
                        firstRangeNum = 0
                    }
                }
            }
        }
        return map
    }

    fun getRecommendedMaxConcurrentScans(): Int {
        val cores = Runtime.getRuntime().availableProcessors()
        val multiplier = 50 // Allow up to 50 scans per core

        var recommendedScans = cores * multiplier

        val absoluteMin = 30   // Don't go too low, even on single/dual core
        val absoluteMax = 400  // Don't go too high, regardless of cores

        if (recommendedScans < absoluteMin) {
            recommendedScans = absoluteMin
        }
        if (recommendedScans > absoluteMax) {
            recommendedScans = absoluteMax
        }
        return recommendedScans
    }

}