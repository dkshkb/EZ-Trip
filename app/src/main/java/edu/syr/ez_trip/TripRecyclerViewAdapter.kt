package edu.syr.ez_trip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.lang.String.join

class TripRecyclerViewAdapter(var items : ArrayList<TripData>) : RecyclerView.Adapter<TripRecyclerViewAdapter.ViewHolder>() {

    var myListener: MyItemClickListener?= null
    var lastPosition = -1 // for animation
    fun setMyItemClickListener(listener: MyItemClickListener){
        this.myListener = listener
    }

    interface MyItemClickListener {
        fun onItemClickedFromAdapter(trip : TripData)
        fun onItemLongClickedFromAdapter(position : Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val tripName = itemView.findViewById<TextView>(R.id.rvTripName)
        val tripDetails = itemView.findViewById<TextView>(R.id.rvTripDetails)
        val tripSelect = itemView.findViewById<CheckBox>(R.id.rvChx)
        val tripDailyPlans = itemView.findViewById<TextView>(R.id.rvTripDailyPlans)

        init{
            tripSelect.setOnClickListener{
                // When click on the check box, true -> false, false -> true
                items[adapterPosition].checked = !items[adapterPosition].checked
            }

            itemView.setOnClickListener{
                if(myListener != null){
                    if(adapterPosition != androidx.recyclerview.widget.RecyclerView.NO_POSITION){
                        myListener!!.onItemClickedFromAdapter(items[adapterPosition])
                    }
                }
            }

            itemView.setOnLongClickListener {
                if(myListener != null){
                    if(adapterPosition != androidx.recyclerview.widget.RecyclerView.NO_POSITION){
                        myListener!!.onItemLongClickedFromAdapter(adapterPosition)
                    }
                }
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // In my dataset, all the movies have above 8 rating, so I decided to use 8.7 instead of 8
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.trip_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trip = items[position]
        // NEED TO get the following data from firebase
        holder.tripName.setText(trip.tripName)
        holder.tripDetails.setText("Details: " + trip.tripDetails)
        holder.tripSelect.isChecked = trip.checked
        holder.tripDailyPlans.setText("The following are daily plans:\n" + trip.dailyPlans.replace(";", "\n"))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getItem(index: Int): Any {
        return items[index]
    }

    fun setClearAll() {
        for (i in items.indices) {
            items[i].checked = false
        }
        notifyDataSetChanged()
    }

    fun setSelectAll() {
        for (i in items.indices) {
            items[i].checked = true
        }
        notifyDataSetChanged()
    }

    fun countSelected() : Int{
        var cnt = 0
        for(i in 0 until items.size)
            if(items[i].checked!!)
                cnt += 1
        return cnt
    }

    fun theOnlySelected() : TripData?{
        for(i in 0 until items.size)
            if(items[i].checked!!)
                return items[i].copy()
        return null
    }

    fun deleteTrips() {
        var cnt = 0
        for(i in 0 until items.size)
            if(items[i].checked!!)
                cnt += 1
        for(i in 0 until cnt){
            for(j in items.indices){
                if(items[j].checked!!){
                    items.removeAt(j)
                    notifyItemRemoved(j)
                    break
                }
            }
        }
        notifyDataSetChanged()
    }

    fun deleteOneTrip(tripName : String){
        for(j in items.indices){
            if(items[j].equals(tripName)){
                items.removeAt(j)
                notifyItemRemoved(j-1)
                break
            }
        }
        notifyDataSetChanged()
    }

    fun duplicateTrip(position: Int) : TripData {
        var trip = items[position].copy()
        trip.tripName += " copy"
        items.add(position+1, trip)
        notifyItemInserted(position+1)
        return trip
    }

    fun addTrip(trip : TripData) {
        items.add(trip)
        notifyDataSetChanged()
    }

    fun updateTrip(updatedTrip: TripData){
        for (trip in items){
            if (trip.tripName == updatedTrip.tripName) {
                trip.tripDetails = updatedTrip.tripDetails
                trip.dailyPlans = updatedTrip.dailyPlans
            }
        }
        notifyDataSetChanged()
    }

    fun refresh(){
        notifyDataSetChanged()
    }


}