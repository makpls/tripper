package com.tripper;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tripper.db.entities.Day;
import com.tripper.db.entities.Event;
import com.tripper.db.entities.Tag;
import com.tripper.db.entities.Trip;
import com.tripper.db.relationships.DaySegmentWithEvents;
import com.tripper.db.relationships.DayWithSegmentsAndEvents;
import com.tripper.db.relationships.TripWithDaysAndDaySegments;
import com.tripper.ui.homepage.list.HomePageListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class TripOverviewAdapter extends RecyclerView.Adapter<TripOverviewAdapter.TripHolder> {

    //private List<DayWithSegmentsAndEvents> day_item;
    Geocoder geocoder;
    private List<DayWithSegmentsAndEvents> day_item;
    private Context context;
    //private LayoutInflater layoutInflater;
    //private HomePageListViewModel homePageListViewModel;
    private TripOverviewViewModel tripOverviewViewModel;
    private long tripId;
    //List<Address> addresses;


    public TripOverviewAdapter(Context context, TripOverviewViewModel tripOverviewViewModel, TripWithDaysAndDaySegments trip_with_days) {
        this.context = context;
        //this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tripOverviewViewModel = tripOverviewViewModel;
        this.day_item = trip_with_days.days;
        this.tripId = trip_with_days.trip.id;
        //geocoder = new Geocoder(context, Locale.getDefault());
    }


    @NonNull
    @Override
    public TripHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_event_item,parent,false);
        return new TripHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripHolder holder, int position) {
        Day currentDay = day_item.get(position).day;

        int dayCount  = position+1;
        String date = String.format("%1$tb %1$te, %1$tY", currentDay.date);
        holder.textViewDay.setText("Day "+ dayCount);


//        Double lat = Double.valueOf(currentDay.locationLat);
//        Double lon = Double.valueOf(currentDay.locationLon);
//        Log.d(TAG, "onBindViewHolder: "+ lat);
//        try {
//            addresses = geocoder.getFromLocation(lat, lon, 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String address = addresses.get(0).getAddressLine(0);
        holder.textViewAddress.setText(date);

        holder.textViewMorning.setText("Morning" );
        holder.textViewAfternoon.setText("Afternoon");
        holder.textViewEvening.setText("Evening");

        List<DaySegmentWithEvents> segmentsAndEvents = day_item.get(position).daySegments;
        Log.d(TAG, "onBindViewHolder: ");

        StringBuilder mbuilder = new StringBuilder();
        mbuilder.append("Morning:");

        if( segmentsAndEvents!=null& segmentsAndEvents.size()>=1) {
            for (Event events : segmentsAndEvents.get(0).events) {
                mbuilder.append(events.name + "\n");
                holder.textViewMorning.setText(mbuilder.toString());
            }
        }

        StringBuilder abuilder = new StringBuilder();
        abuilder.append("Afternoon:");

        if( segmentsAndEvents!=null& segmentsAndEvents.size()>=2) {
            for (Event events : segmentsAndEvents.get(1).events) {
                abuilder.append(events.name + "\n");
                holder.textViewAfternoon.setText(abuilder.toString());
            }
        }

        StringBuilder ebuilder = new StringBuilder();
        ebuilder.append("Evening:");

        if(segmentsAndEvents!=null& segmentsAndEvents.size()>=3) {
            for (Event events : segmentsAndEvents.get(2).events) {
                ebuilder.append(events.name + "\n");
                holder.textViewEvening.setText(ebuilder.toString());
            }
        }


        //set up each button
        holder.buttonAddMorningEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //TODO: this is temp code to get basic events working
                long segId = segmentsAndEvents.get(0).daySegment.id;
                Log.d(TAG, "onClick: "+segId);
                Intent intent = new Intent(context, LocationSuggestion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("segId",segId);
                intent.putExtra("tripId",tripId);
                context.startActivity(intent);
//                Event event = new Event();
//                event.name = result.name;
//                event.locationLon = Double.toString(result.geometry.location.lng);
//                event.locationLat = Double.toString(result.geometry.location.lat);
//                event.tripId = trip.trip.id;
//                event.segmentId = trip.days.get(0).daySegments.get(0).daySegment.id;
//                locationSuggestionViewModel.insertEvent(event);
//                Intent intent = new Intent(context, TripOverview.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("tripId", trip.trip.id);
//                context.startActivity(intent);
            }
        });

        holder.buttonAddAfternoonEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //TODO: this is temp code to get basic events working
                long segId = segmentsAndEvents.get(1).daySegment.id;
                Log.d(TAG, "onClick: "+ segId);
                Intent intent = new Intent(context, LocationSuggestion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("segId",segId);
                intent.putExtra("tripId",tripId);
                context.startActivity(intent);
            }
        });
        holder.buttonAddEveningEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //TODO: this is temp code to get basic events working
                long segId = segmentsAndEvents.get(2).daySegment.id;
                Log.d(TAG, "onClick: "+ segId);
                Intent intent = new Intent(context, LocationSuggestion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("segId",segId);
                intent.putExtra("tripId",tripId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return day_item.size();
    }

//    public void setDay_item(List<Trip> days){
//        this.day_item = days;
//        notifyDataSetChanged();
//    }

    class TripHolder extends RecyclerView.ViewHolder{
        private TextView textViewAddress;
        private TextView textViewDay;
        private TextView textViewMorning;
        private TextView textViewAfternoon;
        private TextView textViewEvening;
        private ImageButton buttonAddMorningEvent;
        private ImageButton buttonAddAfternoonEvent;
        private ImageButton buttonAddEveningEvent;


        public TripHolder(View itemView) {
            super(itemView);
            textViewAddress = itemView.findViewById(R.id.address);
            textViewDay = itemView.findViewById(R.id.day);
            textViewMorning = itemView.findViewById(R.id.morning_event);
            textViewAfternoon = itemView.findViewById(R.id.afternoon_event);
            textViewEvening = itemView.findViewById(R.id.evening_event);
            buttonAddMorningEvent = itemView.findViewById(R.id.add_morning_event);
            buttonAddAfternoonEvent = itemView.findViewById(R.id.add_afternoon_event);
            buttonAddEveningEvent = itemView.findViewById(R.id.add_evening_event);
        }
    }
}
