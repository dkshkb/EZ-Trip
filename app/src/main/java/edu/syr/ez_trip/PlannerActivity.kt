package edu.syr.ez_trip

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator

class PlannerActivity : AppCompatActivity(), TripRecyclerViewAdapter.MyItemClickListener {

    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    private val STATE = "STATE"
    private var layoutManager: RecyclerView.LayoutManager? = null
    // Should get it from Firebase
//    var trips = ArrayList(TripList().tripList)
    var trips : ArrayList<TripData> = ArrayList()
    var adapter = TripRecyclerViewAdapter(trips)
    private var mTwoPane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planner)

        val fAuth = FirebaseAuth.getInstance()
        val firebaseUser : FirebaseUser? = fAuth.currentUser
        val database = Firebase.database
        val myRef = database.getReference("Trip Data")
        val tripListRef = myRef.child(firebaseUser?.uid as String)

        //////////////// JUST FOR TESTING, Please ignore
//        tripListRef.child("LA Trip").child("dailyPlans").addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                Log.d(TAG, "LA tripDetails: " + snapshot.value)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//        })

        tripListRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded:" + snapshot.key!!)

                val tripName = snapshot.key as String
                val tripDetails = snapshot.child("tripDetails").value as String
                val dailyPlans = snapshot.child("dailyPlans").value as String
                val newTrip = TripData(tripName, tripDetails, dailyPlans)
                adapter.addTrip(newTrip)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged: ${snapshot.key}")
                val tripName = snapshot.key as String
                val tripDetails = snapshot.child("tripDetails").value as String
                val dailyPlans = snapshot.child("dailyPlans").value as String
                val updatedTrip = TripData(tripName, tripDetails, dailyPlans)
                adapter.updateTrip(updatedTrip)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + snapshot.key!!)
                val tripName = snapshot.key as String
                adapter.deleteOneTrip(tripName)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + snapshot.key!!)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", error.toException())
            }

        })



        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val appBar = supportActionBar

        appBar?.setDisplayHomeAsUpEnabled(true)
//        appBar?.setLogo(R.mipmap.ic_launcher)
//        appBar?.setDisplayUseLogoEnabled(true)

        toolbar.setNavigationOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        layoutManager = LinearLayoutManager(this)
        val rview = findViewById<RecyclerView>(R.id.rview)
        rview.layoutManager = layoutManager

//        adapter = TripRecyclerViewAdapter(ArrayList(TripList().tripList))
//        rview.adapter = adapter

        // Use Animation from Wasabeef library
        rview.adapter = AlphaInAnimationAdapter(adapter).apply {
            setDuration(1000)
            setStartPosition(200)
            setFirstOnly(false)
        }

        // Use Animation from Wasabeef library
        rview.itemAnimator = SlideInLeftAnimator()

        adapter.setMyItemClickListener(this)

        val select_all = findViewById<View>(R.id.select_all)
        select_all.setOnClickListener {
            Toast.makeText(this,"Select All",Toast.LENGTH_SHORT).show()
            adapter.setSelectAll()
        }

        val clear_all = findViewById<View>(R.id.clear_all)
        clear_all.setOnClickListener{
            Toast.makeText(this,"Clear All",Toast.LENGTH_SHORT).show()
            adapter.setClearAll()
        }

        val delete = findViewById<View>(R.id.delete)
        delete.setOnClickListener {
            var selectedItems : ArrayList<String> = ArrayList()
            for(i in 0 until adapter.items.size) {
                if (adapter.items[i].checked!!){
                    selectedItems.add(adapter.items[i].tripName)
                }
            }
            if(selectedItems.size == 0){
                Toast.makeText(this,"Select at least one to delete!",Toast.LENGTH_SHORT).show()
            } else {

                //Deleted all selected trips from firebase
                for (tripName: String in selectedItems) {
                    tripListRef.child(tripName).setValue(null)
                    adapter.deleteOneTrip(tripName)
                }


                // Refresh this activity
                val intent = Intent(this, PlannerActivity::class.java)
                startActivity(intent)

                //adapter.deleteTrips()
            }
        }

        val add = findViewById<View>(R.id.add)
        add.setOnClickListener{
            val intent = Intent(applicationContext, AddTripActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // inflate the menu into toolbar
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId){
            R.id.addTrip -> {
                Toast.makeText(this, "Clicked Add Trip",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AddTripActivity::class.java)
                startActivity(intent)
            }

            R.id.editTrip -> {
//                Toast.makeText(this, "Clicked Edit Trip",Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, task2::class.java)
//                startActivity(intent)
                Log.d(TAG, "numSelected: " + adapter.countSelected().toString())

                if (adapter.countSelected() == 1){
                    // Go to the EditTripActivity
                    Toast.makeText(this, "Clicked Edit Trip",Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "trip: " + adapter?.theOnlySelected()?.tripName)
                    val intent = Intent(this, EditTripActivity::class.java)
                    intent.putExtra("tripNameSelected", adapter?.theOnlySelected()?.tripName)
                    startActivity(intent)
                }

                else {
                    Toast.makeText(this, "You can only edit one trip at a time",Toast.LENGTH_SHORT).show()
                }

            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onItemClickedFromAdapter(trip: TripData) {
        Toast.makeText(this, "short clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClickedFromAdapter(position: Int) {
        Toast.makeText(this, "Add Duplicate Movie", Toast.LENGTH_SHORT).show()

//        val duplicate_trip = adapter.duplicateTrip(position)

        var duplicate_trip = adapter.items[position].copy()
        duplicate_trip.tripName += " copy"

        val fAuth = FirebaseAuth.getInstance()
        val firebaseUser : FirebaseUser? = fAuth.currentUser
        val database = Firebase.database
        val myRef = database.getReference("Trip Data")
        val tripListRef = myRef.child(firebaseUser?.uid as String)

        tripListRef.child(duplicate_trip.tripName).setValue(duplicate_trip).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                Toast.makeText(this, "New Trip Added Successfully", Toast.LENGTH_SHORT)
                    .show()

                val intent = Intent(applicationContext, PlannerActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Error! " + task.exception!!.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }


}