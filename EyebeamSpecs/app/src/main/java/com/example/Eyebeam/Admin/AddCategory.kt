package com.example.Eyebeam.Admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.Eyebeam.R
import com.example.Eyebeam.com.example.Eyebeam.Model.AdminCategoryModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AddCategory : AppCompatActivity() {

    private lateinit var editTextCategoryName: EditText
    private lateinit var imageViewCategory: ImageView
    private lateinit var buttonSelectImage: Button
    private lateinit var buttonSubmit: Button

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        // Initialize views
        editTextCategoryName = findViewById(R.id.editTextCategoryName)
        imageViewCategory = findViewById(R.id.imageViewCategory)
        buttonSelectImage = findViewById(R.id.buttonSelectImage)
        buttonSubmit = findViewById(R.id.buttonSubmit)

        // Set up image selection
        buttonSelectImage.setOnClickListener {
            openImageSelector()
        }

        // Set up form submission
        buttonSubmit.setOnClickListener {
            submitCategory()
        }
    }

    private fun openImageSelector() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            imageViewCategory.setImageURI(imageUri)
        }
    }

    private fun submitCategory() {
        val categoryName = editTextCategoryName.text.toString().trim()

        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Please enter category name", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if category title is unique
        checkIfTitleIsUnique(categoryName)
    }

    private fun checkIfTitleIsUnique(categoryName: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Category")
        databaseRef.orderByChild("title").equalTo(categoryName)
            .get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    // Category with the same title exists
                    Toast.makeText(this, "Category title already exists. Please choose a different title.", Toast.LENGTH_SHORT).show()
                } else {
                    // Title is unique, proceed with uploading the image and adding the category
                    uploadImageToFirebase(categoryName)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to check category uniqueness", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFirebase(categoryName: String) {
        val storageRef = FirebaseStorage.getInstance().reference.child("Category/${UUID.randomUUID()}")
        imageUri?.let { uri ->
            storageRef.putFile(uri).addOnSuccessListener { taskSnapshot ->
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    saveCategoryToDatabase(categoryName, imageUrl)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveCategoryToDatabase(categoryName: String, imageUrl: String) {
        val categoryId = FirebaseDatabase.getInstance().getReference("Category").push().key
        val category = AdminCategoryModel(title = categoryName, picUrl = imageUrl)

        categoryId?.let {
            FirebaseDatabase.getInstance().getReference("Category").child(it).setValue(category)
                .addOnSuccessListener {
                    Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show()
                    finish()  // Close activity after success
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to add category", Toast.LENGTH_SHORT).show()
                }
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1001
    }
}
