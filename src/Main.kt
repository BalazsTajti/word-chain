fun main() {
    val wordLine = readLine()!!
    val words = wordLine.split(" ")
    val graph = buildGraph(words)
    val path = findHamiltonianPath(graph)
    if (path.isEmpty()) {
        print("No word chain was found!")
    } else {
        print("Word chain found: ")
        path.forEach {
            print("${it.word} ")
        }
    }
}

fun buildGraph(words: List<String>): Graph {
    val wordNodes = words.map { Node(word = it) }
    wordNodes.forEach {
        for ((index, word) in wordNodes.withIndex()) {
            if (it.word == word.word) continue
            if (isOneLetterAway(it.word!!, word.word!!)) {
                it.neighbors.add(word)
                word.neighbors.add(it)
            }
        }
    }
    return Graph(wordNodes)
}

fun isOneLetterAway(wordA: String, wordB: String): Boolean {
    var difference = 0
    val letterCountA = countLetters(wordA)
    val letterCountB = countLetters(wordB)
    if (wordA.length > wordB.length) {
        if (wordA.length - wordB.length > 1) {
            return false
        } else if (wordA.length - wordB.length == 1) {
            for ((character, countA) in letterCountA) {
                val countB = letterCountB[character]
                difference += when (countB) {
                    null -> countA
                    else -> kotlin.math.abs(countA - countB)
                }
            }
            return difference == 1
        }
    }
    if (wordB.length > wordA.length) {
        if (wordB.length - wordA.length > 1) {
            return false
        } else if (wordB.length - wordA.length == 1) {
            for ((character, countB) in letterCountB) {
                val countA = letterCountA[character]
                difference += when (countA) {
                    null -> countB
                    else -> kotlin.math.abs(countA - countB)
                }
            }
            return difference == 1
        }
    }

    for (i in wordA.indices) {
        if (wordA[i] != wordB[i]) {
            difference++
        }
    }

    return difference <= 1
}

fun findHamiltonianPath(graph: Graph): Set<Node> {
    val numberOfNodes = graph.nodes.size
    val nodesTried = mutableMapOf<Node, MutableSet<Node>>()

    graph.nodes.forEach { node ->
        graph.nodes.forEach {
            nodesTried[it] = mutableSetOf()
        }
        val path = mutableSetOf(node)
        iterateNeighborsRecursively(node, path, numberOfNodes, nodesTried)
        if (path.isNotEmpty()) return path.toSet()
    }

    return setOf()
}

fun iterateNeighborsRecursively(node: Node, path: MutableSet<Node>, totalNumberOfNodes: Int, nodesTried: MutableMap<Node, MutableSet<Node>>) {
    if (path.size == totalNumberOfNodes) {
        return
    }
    var nextNode = node.neighbors.find { !path.contains(it) && !nodesTried[node]?.contains(it)!! }
    if (nextNode == null) {
        path.remove(node)
        if (path.isEmpty()) {
            return
        }
        nextNode = path.last()
        nodesTried[nextNode]?.add(node)
    } else {
        path.add(nextNode)
    }
    iterateNeighborsRecursively(nextNode, path, totalNumberOfNodes, nodesTried)
}

fun countLetters(word: String): Map<Char, Int> {
    val letterCountMap: MutableMap<Char, Int> = mutableMapOf()
    word.forEach {
        val letterCount = letterCountMap[it]
        if (letterCount == null) {
            letterCountMap[it] = 1
        } else {
            letterCountMap[it] = letterCount + 1
        }
    }
    return letterCountMap.toMutableMap()
}