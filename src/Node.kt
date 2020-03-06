class Node(
    var word: String? = null,
    var neighbors: MutableSet<Node> = mutableSetOf()
)