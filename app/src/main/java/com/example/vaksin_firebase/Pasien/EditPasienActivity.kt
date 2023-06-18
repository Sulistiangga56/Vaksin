package com.example.vaksin_firebase.Pasien

import android.app.Activity
import android.os.Bundle

class EditPasienActivity {
    private lateinit var binding: ActivityEditPasienBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditPasienBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}