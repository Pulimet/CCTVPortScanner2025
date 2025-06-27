package net.alexandroid.network.cctvportscanner.utils

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
                        if (from < to && to < 65535 && from > 0) {
                            for (a in from..to) {
                                list.add(a)
                            }
                        }
                    }
                }
            } else {
                if (line.isNotEmpty() && line.length < 6) {
                    val check_port = line.toInt()
                    if ((check_port > 0) and (check_port < 65536)) list.add(check_port)
                }
            }
        }
        return list
    }

    fun convertIntegerListToString(list: java.util.ArrayList<Int>): String {
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
}