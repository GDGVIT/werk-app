package com.dscvit.werk.ui.session

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.dscvit.werk.databinding.FragmentChatBinding
import com.dscvit.werk.models.chat.ChatMessage
import com.dscvit.werk.models.chat.InitializeRequest
import com.dscvit.werk.models.chat.JoinSessionRequest
import com.dscvit.werk.models.chat.Message
import com.dscvit.werk.ui.utils.buildLoader
import com.dscvit.werk.ui.utils.runOnUiThread
import com.dscvit.werk.ui.utils.showErrorSnackBar
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class ChatFragment : Fragment() {
    private val TAG: String = this.javaClass.simpleName

    private val viewModel: ChatViewModel by activityViewModels()

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    val gson = Gson()
    val socket: Socket = IO.socket("https://infinite-eyrie-56387.herokuapp.com")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sendButton.setOnClickListener {
            if (binding.chatInput.editText!!.text.isNotEmpty()) {
                // Send message
                val messageRequest =
                    ChatMessage(binding.chatInput.editText!!.text.toString().trim(), "")
                val messageObj = JSONObject(gson.toJson(messageRequest))
                Log.d(TAG, messageObj.toString())
                socket.emit("message", messageObj)

                binding.chatInput.editText!!.setText("")
            }
        }

        val onMessage = Emitter.Listener {
            val jsonString = it[0].toString()
            val message = gson.fromJson(jsonString, Message::class.java)
            Log.d(TAG, "Message: $message")
        }

        val loader = requireContext().buildLoader()

        lifecycleScope.launchWhenResumed {
            viewModel.chats.collect { event ->
                when (event) {
                    is ChatViewModel.GetChatsEvent.Success -> {
                        Log.d(TAG, "SUCCESS ${event.chatResponse}")
                        viewModel.chatMessages.observe(viewLifecycleOwner, {
                            if (it.isEmpty()) {
                                binding.chatRecyclerView.visibility = View.GONE
                                binding.emptyText.visibility = View.VISIBLE
                            }
                        })

                        // Handle Socket Connection
                        socket.connect().let {
                            it.on(Socket.EVENT_CONNECT) {
                                Log.d(TAG, "Socket connected!!!!!")

                                // Handle initialise Connection
                                val initializeRequest = InitializeRequest(viewModel.getUserToken())
                                val initializeObj = JSONObject(gson.toJson(initializeRequest))
                                Log.d(TAG, initializeObj.toString())
                                socket.emit("initialize", initializeObj)

                                Log.d(TAG, "START SESSION REQUEST")

                                // Handle joinSession Connection
                                val joinSessionRequest =
                                    JoinSessionRequest(viewModel.getSessionID())
                                val joinSessionObj = JSONObject(gson.toJson(joinSessionRequest))
                                Log.d(TAG, joinSessionObj.toString())
                                socket.emit("joinSession", joinSessionObj)

                                runOnUiThread { loader.hide() }
                            }
                            it.on(Socket.EVENT_CONNECT_ERROR) {
                                Log.d(TAG, "Socket connection error!!!!!")
                                runOnUiThread { loader.hide() }
                            }
                        }

                        // Handle Incoming chat messages
                        socket.on("message", onMessage)
                    }
                    is ChatViewModel.GetChatsEvent.Loading -> {
                        Log.d(TAG, "LOADING CHATS....")
                        loader.show()
                    }
                    is ChatViewModel.GetChatsEvent.Failure -> {
                        view.showErrorSnackBar(event.errorMessage)
                        Log.d(TAG, event.errorMessage)
                        loader.hide()
                    }
                    else -> {
                    }
                }
            }
        }

        viewModel.getChats()
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.emit("leaveSession")
        socket.disconnect()
    }
}