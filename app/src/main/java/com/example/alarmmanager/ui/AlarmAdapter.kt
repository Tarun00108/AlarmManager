package com.example.alarmmanager.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmmanager.data.Alarm
import com.example.alarmmanager.databinding.ItemAlarmBinding

class AlarmAdapter(private val onToggle: (Alarm) -> Unit, private val onDelete: (Alarm) -> Unit) : ListAdapter<Alarm, AlarmAdapter.AlarmViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = getItem(position)
        holder.bind(alarm, onToggle, onDelete)
    }

    class AlarmViewHolder(private val binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: Alarm, onToggle: (Alarm) -> Unit, onDelete: (Alarm) -> Unit) {
            binding.textViewTime.text = String.format("%02d:%02d", alarm.hour, alarm.minute)
            binding.switchAlarm.isChecked = alarm.isEnabled

            binding.switchAlarm.setOnCheckedChangeListener(null)
            binding.switchAlarm.setOnCheckedChangeListener { _, isChecked ->
                alarm.isEnabled = isChecked
                onToggle(alarm)
            }

            binding.buttonDelete.setOnClickListener {
                onDelete(alarm)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Alarm>() {
        override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem == newItem
        }
    }
}