package com.example.hospital.presentation.patient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hospital.R
import com.example.hospital.chatBot.MessageModel
import com.example.hospital.chatBot.ChatBotViewModel
import com.example.hospital.core.theme.ColorModelMessage
import com.example.hospital.core.theme.ColorUserMessage
import com.example.hospital.core.theme.HospitalTheme
import com.example.hospital.core.theme.Purple80

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun ChatPage(
    modifier: Modifier = Modifier,
    viewModel: ChatBotViewModel
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        AppHeader()
        MessageList(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            messageList = viewModel.messageList
        )
        MessageInput(
            onMessageSend = { viewModel.sendMessage(it) },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    messageList: List<MessageModel>
) {
    if (messageList.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = R.drawable.baseline_question_answer_24),
                contentDescription = "Chat Icon",
                tint = Purple80,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ask me anything",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messageList.reversed()) { message ->
                MessageRow(messageModel = message)
            }
        }
    }
}

@Composable
fun MessageRow(messageModel: MessageModel) {
    val isModel = messageModel.role == "model"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isModel) Arrangement.Start else Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (isModel) ColorModelMessage else ColorUserMessage)
                .padding(16.dp)
        ) {
            SelectionContainer {
                Text(
                    text = messageModel.message,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun MessageInput(
    modifier: Modifier = Modifier,
    onMessageSend: (String) -> Unit
) {
    var message by remember { mutableStateOf("") }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            placeholder = { Text(text = "Type your message...", color = Color.Gray) },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = {
                if (message.isNotBlank()) {
                    onMessageSend(message)
                    message = ""
                }
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send Message",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun AppHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = "Health.AI",
            modifier = Modifier.padding(16.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Preview(showBackground = true)
@Composable
fun ChatPagePreview() {
    // Create a mock ViewModel with sample data for preview.
    val mockViewModel = ChatBotViewModel().apply {
        messageList.add(MessageModel(role = "user", message = "Hi there!"))
        messageList.add(MessageModel(role = "model", message = "Hello! How can I assist you today?"))
        messageList.add(MessageModel(role = "user", message = "I need help with my health records."))
    }
    HospitalTheme {
        ChatPage(viewModel = mockViewModel)
    }
}