package perso.android.PixTransfer

import GestionImage
import android.Manifest
import android.PixTransfer.R
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private val gestionImage = GestionImage()
    private lateinit var progressBar: ProgressBar
    private lateinit var errorMessage: TextView
    private lateinit var statusMessage: TextView

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startImageUpload()
            } else {
                errorMessage.text = "Permission denied. Cannot access images."
                errorMessage.visibility = TextView.VISIBLE
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)
        errorMessage = findViewById(R.id.errorMessage)
        statusMessage = findViewById(R.id.statusMessage)

        findViewById<Button>(R.id.uploadButton).setOnClickListener {
            checkAndRequestPermissions()
        }
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            startImageUpload()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun startImageUpload() {
        progressBar.visibility = ProgressBar.VISIBLE
        errorMessage.visibility = TextView.GONE
        statusMessage.visibility = TextView.VISIBLE
        statusMessage.text = "Connecting to server..."
        gestionImage.uploadAllImages(this, progressBar, errorMessage, statusMessage)
    }
}
