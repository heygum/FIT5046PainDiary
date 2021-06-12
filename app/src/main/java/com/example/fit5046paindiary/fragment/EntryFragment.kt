package com.example.fit5046paindiary.fragment

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.fit5046paindiary.*
import com.example.fit5046paindiary.ViewModel.RecordViewModel
import com.example.fit5046paindiary.Worker.DbWorker
import com.example.fit5046paindiary.databinding.EntryFragmentBinding
import com.example.fit5046paindiary.entity.Pain
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class EntryFragment:Fragment() {
    private var _binding: EntryFragmentBinding? = null
    private val binding get()  = _binding!!
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EntryFragmentBinding.inflate(inflater,container,false)
        val view = binding.root
        val pres = activity?.getPreferences(Context.MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()
        val db = Firebase.firestore


        //Get date and user email
        val dateTime = Calendar.getInstance().time
        val date = SimpleDateFormat("dd/MM/yyyy").format(dateTime)
        val user = auth.currentUser
        var email = "Unknown"
        if(user != null)
            email = user.email

        //EDIT SLIDER
        binding.painSlider.setLabelFormatter{
            val format = NumberFormat.getIntegerInstance()
            if(format.format(it)=="0")
            {
                "0: No pain"
            }
            else if(format.format(it) == "10")
            {
                "10: Worst possible pain"
            }
            else{
                "${format.format(it)}"
            }
        }

        //EDIT SPINNER
        if (container != null) {
            ArrayAdapter.createFromResource(container.context,R.array.pain_location_array,android.R.layout.simple_spinner_item)
                .also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.painLocationSpinner.adapter = adapter
                }
        }

        val painViewModel: RecordViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
            context?.applicationContext as Application
        ).create(RecordViewModel::class.java)

        /*painViewModel.allPain.observe(viewLifecycleOwner, Observer {
        })*/

        lifecycleScope.launch(Dispatchers.Main){
            val temp = painViewModel.check(date,email)
            if (temp != null) {
                if(temp.date == date && temp.email == email){
                    binding.painSlider.value = temp.level.toFloat()
                    binding.painLocationSpinner.setSelection(locaionToInt(temp.location))
                    binding.moodGroup.clearCheck()
                    binding.moodGroup.check(temp.mood)
                    binding.stepToday.setText(temp.step.toString())
                    disableEdit()
                } else
                    enableEdit()
            }
        }

        //SAVE BUTTOM
        binding.entrySave.setOnClickListener {
            if(binding.moodGroup.checkedRadioButtonId == -1 || binding.stepToday.text.toString() == ""
                || binding.setpGoal.text.toString()=="")
                Toast.makeText(context,"You need to fill everything.",Toast.LENGTH_SHORT).show()
            else
            {
                var level = binding.painSlider.value.toInt()
                var location = binding.painLocationSpinner.selectedItem.toString()
                var mood = binding.moodGroup.checkedRadioButtonId
                var step = binding.stepToday.text.toString().toInt()


                var temp = 0.0
                temp = pres?.getString("temp","0")?.toDouble()!!
                var humi = 0
                humi = pres.getString("humi","0")?.toInt()!!
                var pre = 0
                pre = pres.getString("pre","0")?.toInt()!!

                val pain = Pain(level,location,mood,step,date,temp,humi,pre,email)

                lifecycleScope.launch(Dispatchers.Main){
                    val checkPain = painViewModel.check(date,email)
                    if(checkPain == null){
                        painViewModel.insert(pain)
                    }
                    else
                    {
                        checkPain.step = pain.step
                        checkPain.humi = pain.humi
                        checkPain.mood = pain.mood
                        checkPain.location = pain.location
                        checkPain.level = pain.level
                        checkPain.press = pain.press
                        checkPain.temp = pain.temp
                        painViewModel.update(checkPain)
                    }

                    //SAVE TO FIREBASE
                    var dateSplit = date.split("/")
                    var dateDb = ""
                    for(each in dateSplit)
                        dateDb = dateDb + each
                    db.collection("pains").document("$dateDb" + "-" + "$email").set(pain).addOnSuccessListener {
                        Log.d("db","add a new data into firebase.")
                    }
                        .addOnFailureListener {
                            Log.w("db","can not add a new data into firebase.")
                        }

                    //SET WORKMANAGER
                    val nowTime = System.currentTimeMillis()
                    val c = Calendar.getInstance()
                    c.time = dateTime
                    c.set(Calendar.HOUR,22)
                    val passTime = nowTime - c.timeInMillis
                    val dbWork = PeriodicWorkRequestBuilder<DbWorker>(1,TimeUnit.DAYS).
                    setInitialDelay(passTime,TimeUnit.MILLISECONDS).build()
                    activity?.let { it1 -> WorkManager.getInstance(it1).enqueue(dbWork) }
                }
                disableEdit()
                Toast.makeText(context,"Save successful.", Toast.LENGTH_SHORT).show()
            }

        }

        // EDIT BUTTOM
        binding.entryEdit.setOnClickListener {
            enableEdit()
            Toast.makeText(context,"You can edit now.", Toast.LENGTH_SHORT).show()
        }


        //Set notification logic
        val hour = pres?.getInt("hour",-1)
        val min = pres?.getInt("min",-1)
        if(hour == -1 && min == -1) {
            binding.notiTime.setText(null)
            setAlarm(pres)
            Toast.makeText(context,"You need to select a notification time first.",Toast.LENGTH_LONG).show()
        }
        else
            binding.notiTime.setText("Notification time: $hour: $min")
        binding.notiButton.setOnClickListener {
            if (pres != null) {
                setAlarm(pres)
            }
        }
        return view
    }


    //Set Alarm
    fun setAlarm(pres:SharedPreferences){
        val editor = pres.edit()
        val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select the notification time for daily record")
            .build()
        timePicker.show(childFragmentManager,"tag")
        timePicker.addOnPositiveButtonClickListener {

            val hour = timePicker.hour
            val min = timePicker.minute
            if (editor != null) {
                editor.putInt("hour",hour)
            }
            if (editor != null) {
                editor.putInt("min",min)
            }
            editor.apply()

            val calendar:Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY,hour)
                set(Calendar.MINUTE,min-2)
            }

            val alarm:AlarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent:PendingIntent  = Intent(context, AlarmServer::class.java).let { intent ->
                intent.setPackage(activity?.packageName)
                PendingIntent.getBroadcast(context, 0, intent, 0)

            }

            alarm.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,alarmIntent
            )

            binding.notiTime.setText("Notification time: $hour: $min")
        }
    }

    fun enableEdit(){
        binding.entryEdit.alpha = 0.5F
        binding.entryEdit.isClickable = false
        binding.entrySave.alpha = 1F
        binding.entrySave.isClickable = true
        binding.painSlider.isEnabled = true
        binding.painLocationSpinner.isEnabled = true
        binding.setpGoal.isEnabled = true
        binding.stepToday.isEnabled = true
        for (i in 0..(binding.moodGroup.childCount-1))
            binding.moodGroup.getChildAt(i).isEnabled = true
    }

    fun disableEdit(){
        binding.entrySave.alpha = 0.5F
        binding.entrySave.isClickable = false
        binding.entryEdit.alpha = 1F
        binding.entryEdit.isClickable = true
        binding.painSlider.isEnabled = false
        binding.painLocationSpinner.isEnabled = false
        binding.setpGoal.isEnabled = false
        binding.stepToday.isEnabled = false
        for (i in 0..(binding.moodGroup.childCount-1))
            binding.moodGroup.getChildAt(i).isEnabled = false
    }

    fun locaionToInt(location:String) = when(location){
            "back" -> 1
            "neck" -> 2
            "head" -> 3
            "hips" -> 4
            "abdomen" -> 5
            "elbows"  -> 6
            "shoulders" -> 7
            "shins" -> 8
            "jaw" -> 9
            "facial" -> 10
            else  -> -1
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}






