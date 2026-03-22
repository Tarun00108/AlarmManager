package com.example.alarmmanager

import android.Manifest
import android.app.AlarmManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.alarmmanager.databinding.ActivityMainBinding
import com.example.alarmmanager.ui.AlarmAdapter
import com.example.alarmmanager.ui.AlarmViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: AlarmViewModel by viewModels()
    private lateinit var alarmAdapter: AlarmAdapter

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                checkAndRequestExactAlarmPermission()
            } else {
                showPermissionSnackbar("Notification permission is required to show alarms.")
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        setupRecyclerView()
        observeAlarms()
        checkAndRequestPermissions()

        binding.fabAddAlarm.setOnClickListener {
            showTimePickerDialog()
        }
    }


    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                    checkAndRequestExactAlarmPermission()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    showPermissionSnackbar("Notification permission is needed to show upcoming alarms.") {
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            checkAndRequestExactAlarmPermission()
        }
    }

    private fun checkAndRequestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                showPermissionSnackbar("Exact alarm permission is required for this app to function.") {
                    Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).also { intent ->
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun showPermissionSnackbar(message: String, action: (() -> Unit)? = null) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
        if (action != null) {
            snackbar.setAction("Grant") {
                action()
            }
        }
        snackbar.show()
    }


    private fun setupRecyclerView() {
        alarmAdapter = AlarmAdapter(
            onToggle = { alarm -> viewModel.updateAlarm(alarm) },
            onDelete = { alarm -> viewModel.deleteAlarm(alarm) }
        )
        binding.recyclerViewAlarms.adapter = alarmAdapter
    }

    private fun observeAlarms() {
        viewModel.alarms.observe(this) { alarms ->
            alarmAdapter.submitList(alarms)

            if (alarms.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.recyclerViewAlarms.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.recyclerViewAlarms.visibility = View.VISIBLE
            }
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                viewModel.scheduleAlarm(hourOfDay, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // 24-hour format
        ).show()
    }
}