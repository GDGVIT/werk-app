package com.dscvit.werk.ui.session

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.dscvit.werk.R
import com.dscvit.werk.databinding.FragmentChatBinding
import com.dscvit.werk.databinding.FragmentSessionBinding
import com.dscvit.werk.ui.tasks.TaskViewModel
import com.dscvit.werk.ui.utils.buildLoader
import com.dscvit.werk.ui.utils.showErrorSnackBar
import kotlinx.coroutines.flow.collect

class ChatFragment : Fragment() {
    private val TAG: String = this.javaClass.simpleName

    private val viewModel: ChatViewModel by activityViewModels()

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

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
            binding.chatInput.editText!!.setText("")
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
                        loader.hide()
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
}