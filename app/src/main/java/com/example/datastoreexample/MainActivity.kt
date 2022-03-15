package com.example.datastoreexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.datastoreexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this@MainActivity
        val dataStoreInject = DatastoreInject.getInstance(applicationContext)
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(
                DatastoreRepository(
                    dataStoreInject.preferenceStore,
                    dataStoreInject.userProtoStore
                )
            )
        )[MainViewModel::class.java]
        binding.vm = viewModel
    }
}