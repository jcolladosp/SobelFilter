package pw.jcollado.sobelfilter

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.provider.MediaStore



class MainActivity : AppCompatActivity() {
    private val READ_REQUEST_CODE = 42
    private var imageUri: Uri? = null
    private var bitmapDest: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setSnackBar()
        setOnClickFab()
        checkPermissionForReadExtertalStorage()
    }

    private fun setSnackBar(){
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun setOnClickFab(){
        fab.setOnClickListener {
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            bitmapDest = Sobel.Sobel(bitmap)
            Glide.with(this).load(bitmapDest).into(imSource)

        }

    }


    private fun performFileSearch(): Boolean {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }

        startActivityForResult(intent, READ_REQUEST_CODE)
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {
            R.id.choose_file -> performFileSearch()

            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {


        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            resultData?.data?.also { uri ->
                imageUri = uri
                Glide.with(this).load(uri).into(imSource)
            }
        }
    }

    fun checkPermissionForReadExtertalStorage() {
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE),1)
            }
        }



}