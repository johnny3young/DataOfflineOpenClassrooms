package com.black3.app.dataofflineopenclassrooms.tripbook

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Bundle
import android.os.Environment
import android.provider.Telephony.Mms.Part.FILENAME
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.black3.app.dataofflineopenclassrooms.R
import com.black3.app.dataofflineopenclassrooms.utils.StorageUtils
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

// 1 – PERMISSIONS MANAGEMENT
private const val RC_STORAGE_WRITE_PERMS = 100

class TripBookActivity : AppCompatActivity() {
    // 1 - FILE MANAGEMENT
    private val FILENAME = "tripBook.txt"

    private val FOLDERNAME = "bookTrip"
    //FOR DESIGN
    private lateinit var linearLayoutExternal: LinearLayout

    private lateinit var linearLayoutInternal: LinearLayout
    private lateinit var radioButtonExternal: RadioButton

    private lateinit var radioButtonInternal: RadioButton
    private lateinit var radioButtonExternalPublic: RadioButton

    private lateinit var radioButtonExternalPrivate: RadioButton
    private lateinit var radioButtonInternalVolatile: RadioButton

    private lateinit var radioButtonInternalNormal: RadioButton

    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_trip_book)
        this.configureToolbar()

        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)

        linearLayoutExternal = findViewById(R.id.trip_book_activity_external_choice)
        linearLayoutInternal = findViewById(R.id.<)

        radioButtonExternal = findViewById(R.id.trip_book_activity_radio_external)
        radioButtonInternal = findViewById(R.id.trip_book_activity_radio_internal)

        radioButtonExternalPublic = findViewById(R.id.trip_book_activity_radio_public)
        radioButtonExternalPrivate = findViewById(R.id.trip_book_activity_radio_private)

        radioButtonInternalVolatile = findViewById(R.id.trip_book_activity_radio_volatile)
        radioButtonInternalNormal = findViewById(R.id.trip_book_activity_radio_normal)

        editText = findViewById(R.id.trip_book_activity_edit_text)

        radioButtonInternal.setOnCheckedChangeListener { buttonView, isChecked ->
            onClickRadioButton(buttonView, isChecked)
        }

        radioButtonExternal.setOnCheckedChangeListener { buttonView, isChecked ->
            onClickRadioButton(buttonView, isChecked)
        }

        radioButtonInternalVolatile.setOnCheckedChangeListener { buttonView, isChecked ->
            onClickRadioButton(buttonView, isChecked)
        }

        radioButtonInternalNormal.setOnCheckedChangeListener { buttonView, isChecked ->
            onClickRadioButton(buttonView, isChecked)
        }

        radioButtonExternalPublic.setOnCheckedChangeListener { buttonView, isChecked ->
            onClickRadioButton(buttonView, isChecked)
        }

        radioButtonExternalPrivate.setOnCheckedChangeListener { buttonView, isChecked ->
            onClickRadioButton(buttonView, isChecked)
        }
        // 2 - Read from storage when starting
        this.readFromStorage()
    }

    // ----------------------------------
    // ACTIONS
    // ----------------------------------
    fun onClickRadioButton(button: CompoundButton, isChecked: Boolean) {

        if (isChecked) {

        }

        // 3 - Read from storage after user clicks on radio buttons
        this.readFromStorage()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_save -> {
                // 4 – Save
                this.save()
                return true
            }
        }
        //lo coloqué para saltarme el error que había acá, luego miro qué hace o qué debo poner realmente
        return false
    }

    // 5 - Save after user clicked on button
    private fun save() {

        if (radioButtonExternal.isChecked) {
            writeExternalStorage() //Save on external storage

        } else {
            //TODO: Save on internal storage
        }

    }

    // 2 - After permission granted or refused
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    // ----------------------------------
    // UTILS - STORAGE
    // ----------------------------------

    @AfterPermissionGranted(RC_STORAGE_WRITE_PERMS)

    // 6 - Read from storage
    private fun readFromStorage() {
        // CHECK PERMISSION
        if (!EasyPermissions.hasPermissions(this, WRITE_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.title_permission),
                RC_STORAGE_WRITE_PERMS,
                WRITE_EXTERNAL_STORAGE
            )
            return
        }

        if (radioButtonExternal.isChecked) {
            if (StorageUtils.isExternalStorageReadable()) {
                // EXTERNAL
                if (radioButtonExternalPublic.isChecked) {
                    // External - Public
                    editText.setText(
                        StorageUtils.getTextFromStorage(

                            Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOCUMENTS
                            ), this, FILENAME, FOLDERNAME
                        )
                    )

                } else {
                    // External - Privatex
                    editText.setText(
                        StorageUtils.getTextFromStorage(

                            getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!,
                            this,
                            FILENAME,
                            FOLDERNAME
                        )
                    )

                }

            }

        } else {
            // 2 - Read from internal storage
            if (radioButtonInternalVolatile.isChecked) {
                // Cache
                editText.setText(
                    StorageUtils.getTextFromStorage(
                        cacheDir,
                        this,
                        FILENAME,
                        FOLDERNAME
                    )
                )
            } else {
                // Normal
                editText.setText(
                    StorageUtils.getTextFromStorage(
                        filesDir,
                        this,
                        FILENAME,
                        FOLDERNAME
                    )
                )
            }

        }

    }

    // 1 - Write internal storage
    private fun writeInternalStorage() {
        if (radioButtonInternalVolatile.isChecked) {
            StorageUtils.setTextInStorage(
                cacheDir,
                this,
                FILENAME,
                FOLDERNAME,
                editText.text.toString()
            )
        } else {
            StorageUtils.setTextInStorage(
                filesDir,
                this,
                FILENAME,
                FOLDERNAME,
                editText.text.toString()
            )
        }
    }
}

// 7 - Write external storage
private fun writeExternalStorage() {

    if (StorageUtils.isExternalStorageWritable()) {
        if (radioButtonExternalPublic.isChecked) {
            StorageUtils.setTextInStorage(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS
                ), this, FILENAME, FOLDERNAME, this.editText.text.toString()
            )

        } else {
            StorageUtils.setTextInStorage(
                getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                this,
                FILENAME,
                FOLDERNAME,
                this.editText.text.toString()
            )
        }

    } else {
        Toast.makeText(
            this,
            getString(R.string.external_storage_impossible_create_file),
            Toast.LENGTH_LONG
        ).show()
    }

}
}