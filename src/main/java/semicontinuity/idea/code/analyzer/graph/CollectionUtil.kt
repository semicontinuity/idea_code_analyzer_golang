package semicontinuity.idea.code.analyzer.graph

fun <K, V> invertToSets(map: Map<K, V>): Map<V, Set<K>> =
    map.entries
        .groupBy({ it.value }, { it.key })
        .mapValues { it.value.toSet() }
