package edu.syr.ez_trip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlannerActivity : AppCompatActivity(), TripRecyclerViewAdapter.MyItemClickListener {

    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    private val STATE = "STATE"
    private var layoutManager: RecyclerView.LayoutManager? = null
    // Should get it from Firebase
    var trips = ArrayList(TripList().tripList)
    var adapter = TripRecyclerViewAdapter(trips)
    private var mTwoPane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planner)

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

        adapter = TripRecyclerViewAdapter(ArrayList(TripList().tripList))
        rview.adapter = adapter

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
            adapter.deleteTrips()
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
            R.id.displayPlan -> {
                Toast.makeText(this, "Clicked Display Plans",Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, task1::class.java)
//                startActivity(intent)
            }

            R.id.editPlan -> {
                Toast.makeText(this, "Clicked Edit Plan",Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, task2::class.java)
//                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onItemClickedFromAdapter(trip: TripData) {
        Toast.makeText(this, "short clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClickedFromAdapter(position: Int) {
        Toast.makeText(this, "Add Duplicate Movie", Toast.LENGTH_SHORT).show()
        adapter.duplicateTrip(position)
    }


}