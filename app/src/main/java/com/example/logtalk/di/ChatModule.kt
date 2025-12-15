package com.example.logtalk.di

import android.content.Context
import com.example.logtalk.config.EnvManager
import com.example.logtalk.core.utils.model.OpenAILLMChatService
import com.example.logtalk.data.AppDatabase
import com.example.logtalk.data.local.MessageDao
import com.example.logtalk.domain.chat.ChatRepository
import com.example.logtalk.domain.chat.ChatRepositoryImpl
import com.example.logtalk.domain.chat.ChatUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {

    @Provides
    @Singleton
    fun provideMessageDao(@ApplicationContext context: Context): MessageDao {
        return AppDatabase.getDatabase(context).messageDao()
    }

    @Provides
    @Singleton
    fun provideOpenAILLMChatService(): OpenAILLMChatService {
        val apiKey = EnvManager.getOpenaiApiKey()
        val systemPrompt = "당신은 사용자의 고민을 경청하고 공감하며 따뜻하게 조언하는 상담사입니다. 사용자의 감정을 이해하고 적절한 조언을 제공해주세요."
        return OpenAILLMChatService(apiKey = apiKey, systemPrompt = systemPrompt)
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        messageDao: MessageDao,
        llmService: OpenAILLMChatService
    ): ChatRepository {
        return ChatRepositoryImpl(chatDao = messageDao, llmService = llmService)
    }

    @Provides
    @Singleton
    fun provideChatUseCase(
        repository: ChatRepository
    ): ChatUseCase {
        return ChatUseCase(repository = repository)
    }
}

