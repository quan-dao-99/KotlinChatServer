import com.example.chatclient.Model.Time
import kotlinx.serialization.json.Json

class TopChatter : IObserver {
    private var messageCountMap = mutableMapOf<String, Int>()

    init {
        ChatHistory.registerObserver(this)
    }

    override fun newMessage(chatMessage: ChatMessage) {
        if (!messageCountMap.containsKey(chatMessage.username)) {
            messageCountMap[chatMessage.username] = 1
        } else {
            var count = messageCountMap[chatMessage.username]!!
            messageCountMap[chatMessage.username] = ++count
        }

        printTopChatter()
    }

    private fun printTopChatter() {
        sortTopChatterMap()

        println("Top Chatter List:")
        println(messageCountMap)
    }

    fun getTopChatter(username: String): String {
        sortTopChatterMap()

        val topChatter: ChatMessage
        var topChatterMessage = ""
        messageCountMap.forEach {
            val message = ChatMessage(it.key, Commands.Top, it.value.toString(), Time.getTime())
            topChatterMessage += if (messageCountMap.entries.indexOf(it) != messageCountMap.size - 1) {
                "${Json.stringify(ChatMessage.serializer(), message)}|"
            } else {
                Json.stringify(ChatMessage.serializer(), message)
            }
        }
        topChatter = ChatMessage(username, Commands.Top, topChatterMessage, Time.getTime())
        return Json.stringify(ChatMessage.serializer(), topChatter)
    }

    private fun sortTopChatterMap() {
        messageCountMap = messageCountMap.toList().sortedWith(compareByDescending { it.second }).toMap().toMutableMap()
    }

    //Not currently in used!
    fun removeUser(username: String) {
        messageCountMap.remove(username)
    }
}