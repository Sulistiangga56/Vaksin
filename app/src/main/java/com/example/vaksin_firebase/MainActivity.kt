package com.example.vaksin_firebase

import android.app.Activity
import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vaksin_firebase.Pasien.AddPasienActivity
import com.example.vaksin_firebase.Pasien.Pasien
import com.example.vaksin_firebase.Pasien.PasienAdapter
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pasienRecyclerView = binding.makananListView
        pasienRecyclerView.layoutManager = LinearLayoutManager(this)
        pasienRecyclerView.setHasFixedSize(true)

        pasienArrayList = arrayListOf()
        pasienAdapter = PasienAdapter(pasienArrayList)

        pasienRecyclerView.adapter = pasienAdapter

        load_data()

        binding.btnAddPasien.setOnClickListener {
            val intentMain = Intent(this, AddPasienActivity::class.java)
            startActivity(intentMain)
        }

        binding.txtSearchPasien.addTextChangedListener(object : TextWatcher) {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val keyword = binding.txtSearchPasien.text.toString()
                if (keyword.isNotEmpty()) {
                    search_data(keyword)
                }
                else {
                    load_data()
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        }
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var pasienRecyclerView: RecyclerView
    private lateinit var pasienArrayList: ArrayList<Pasien>
    private lateinit var db: FirebaseFirestore

        private fun load_data() {
            pasienArrayList.clear()
            db = FirebaseFirestore.getInstance()
            db.collection("pasien").addSnapShotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED)
                            pasienArrayList.add(dc.document.toObject(Pasien::class.java))
                    }
                    pasienAdapter.notifyDataSetChanged()
                }
            })
        }

    private fun search_data(keyword :String) {
        pasienArrayList.clear()

        db = FirebaseFirestore.getInstance()

        val query = db.collection("pasien")
            .orderBy("nama")
            .startAt(keyword)
            .get()
        query.addOnSuccessListener {
            pasienArrayList.clear()
            for (document in it) {
                pasienArrayList.add(document.toObject(Pasien::class.java))
            }
        }
    }
    }


