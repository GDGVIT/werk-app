package com.dscvit.werk.ui.overview

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dscvit.werk.R
import com.dscvit.werk.databinding.FragmentInviteBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class InviteFragment : Fragment() {
    private var _binding: FragmentInviteBinding? = null
    private val binding get() = _binding!!

    private lateinit var qrBitmap: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInviteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.appBarTitle.text = requireContext().getString(R.string.invite)
        binding.appBar.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val qrEncoder = QRGEncoder("04f0fb4cdd", null, QRGContents.Type.TEXT, 360)
        try {
            qrBitmap = qrEncoder.bitmap
            binding.qrCode.setImageBitmap(qrBitmap)
        } catch (e: Exception) {
            Log.d("BRR", e.toString())
        }

        binding.shareButton.setOnClickListener {
            val cachePath = File(requireContext().externalCacheDir, "werk_images/")
            cachePath.mkdirs()

            val file = File(cachePath, "QR_Image.png")
            try {
                val fileOutputStream = FileOutputStream(file)
                qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                fileOutputStream.flush()
                fileOutputStream.close()

                val qrImageFileUri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().applicationContext.packageName + ".provider",
                    file
                )

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Join the session with the QR code or with the exclusive code: 04f0fb4cdd"
                    )
                    putExtra(Intent.EXTRA_STREAM, qrImageFileUri)
                    type = "image/png"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            } catch (e: Exception) {
                Log.d("BRR", e.toString())
            }
        }

        binding.nextButton.setOnClickListener {
            val action = InviteFragmentDirections.actionInviteFragmentToSessionActivity()
            findNavController().navigate(action)
        }
    }
}