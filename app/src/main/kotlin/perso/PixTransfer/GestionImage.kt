import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import android.provider.MediaStore



class GestionImage {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
        .dispatcher(Dispatcher().apply { maxRequests = 5 })
        .build()

    fun uploadAllImages(context: Context, progressBar: ProgressBar, errorMessage: TextView, statusMessage: TextView) {
        val images = getAllImages(context)
        progressBar.max = 100
        progressBar.visibility = View.VISIBLE

        val batchSize = 10
        val totalBatches = (images.size + batchSize - 1) / batchSize

        for (batchIndex in 0 until totalBatches) {
            val start = batchIndex * batchSize
            val end = minOf(start + batchSize, images.size)
            val batch = images.subList(start, end)

            for ((index, image) in batch.withIndex()) {
                uploadImageWithRetry(context, image, progressBar, errorMessage, statusMessage, images.size, start + index + 1)
            }
        }
    }

    private fun uploadImageWithRetry(
        context: Context,
        imagePath: String,
        progressBar: ProgressBar,
        errorMessage: TextView,
        statusMessage: TextView,
        totalImages: Int,
        currentImage: Int,
        retryCount: Int = 3
    ) {
        val file = File(imagePath)

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "image",
                file.name,
                file.asRequestBody("image/*".toMediaTypeOrNull())
            )
            .build()

        val request = Request.Builder()
            .url("http://your.server.ip/upload")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                if (retryCount > 0) {
                    uploadImageWithRetry(context, imagePath, progressBar, errorMessage, statusMessage, totalImages, currentImage, retryCount - 1)
                } else {
                    (context as? Activity)?.runOnUiThread {
                        progressBar.visibility = View.GONE
                        errorMessage.text = "Failed to connect to the server: ${e.message}"
                        errorMessage.visibility = View.VISIBLE
                        statusMessage.visibility = View.GONE
                    }
                }
            }

            override fun onResponse(call: Call, response: Response) {
                (context as? Activity)?.runOnUiThread {
                    if (response.isSuccessful) {
                        if (currentImage == totalImages) {
                            progressBar.visibility = View.GONE
                            statusMessage.text = "Uploaded $currentImage of $totalImages images"
                        } else {
                            progressBar.progress = (currentImage * 100) / totalImages
                            statusMessage.text = "Uploaded $currentImage of $totalImages images"
                        }
                    } else {
                        if (retryCount > 0) {
                            uploadImageWithRetry(context, imagePath, progressBar, errorMessage, statusMessage, totalImages, currentImage, retryCount - 1)
                        } else {
                            errorMessage.text = "Failed to upload image: ${response.message}"
                            errorMessage.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
    }


    private fun getAllImages(context: Context): List<String> {
        val imageList = mutableListOf<String>()
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (it.moveToNext()) {
                val imagePath = it.getString(columnIndex)
                imageList.add(imagePath)
            }
        }

        return imageList
    }
}
