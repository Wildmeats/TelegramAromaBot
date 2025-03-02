package org.example

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import io.github.cdimascio.dotenv.dotenv

class MyBot : TelegramLongPollingBot() {
    override fun getBotUsername(): String = "AromaRootsBot"
    override fun getBotToken(): String {
        val dotenv = dotenv()
        return dotenv["BOT_TOKEN"] ?: throw IllegalStateException("Токен бота не найден в .env")
    }

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {
            val messageText = update.message.text
            val chatId = update.message.chatId.toString()

            // Обработка команды /start
            if (messageText == "/start") {
                val welcomeMessage = """
                    Добро пожаловать! 😊
                    Чтобы получить промокод, пожалуйста, подпишитесь на наш основной канал: @aromaroots.
                    После подписки нажмите /check, чтобы проверить подписку и получить промокод.
                """.trimIndent()

                val message = SendMessage(chatId, welcomeMessage)
                try {
                    execute(message)
                    println("Приветственное сообщение отправлено пользователю: $chatId")
                } catch (e: TelegramApiException) {
                    println("Ошибка при отправке сообщения: ${e.message}")
                    e.printStackTrace()
                }
            }

            // Обработка команды /check
            if (messageText == "/check") {
                val isSubscribed = checkSubscription(chatId)
                val response = if (isSubscribed) {
                    "Спасибо за подписку! Ваш промокод: PROMO123"
                } else {
                    "Вы еще не подписались на канал @aromaroots. Пожалуйста, подпишитесь и попробуйте снова."
                }
                val message = SendMessage(chatId, response)
                try {
                    execute(message)
                    println("Проверка подписки выполнена для пользователя: $chatId")
                } catch (e: TelegramApiException) {
                    println("Ошибка при отправке сообщения: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }

    private fun checkSubscription(chatId: String): Boolean {
        val channelChatId = "@aromaroots"
        try {
            val chatMember: ChatMember = execute(
                org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember(
                    channelChatId,
                    chatId.toLong()
                )
            )
            println("Статус пользователя: ${chatMember.status}") // Логирование статуса
            return chatMember.status == "member" || chatMember.status == "administrator" || chatMember.status == "creator"
        } catch (e: TelegramApiException) {
            println("Ошибка при проверке подписки: ${e.message}") // Логирование ошибки
            e.printStackTrace()
        }
        return false
    }
}

fun main() {
    val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
    try {
        botsApi.registerBot(MyBot())
        println("Бот запущен!")
    } catch (e: TelegramApiException) {
        println("Ошибка при запуске бота: ${e.message}")
        e.printStackTrace()
    }
}