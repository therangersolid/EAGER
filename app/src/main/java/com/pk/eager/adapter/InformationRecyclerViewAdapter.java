package com.pk.eager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.pk.eager.R;
import com.pk.eager.ReportObject.CompactReport;
import com.pk.eager.util.CompactReportUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Purvesh on 7/15/2017.
 */
public class InformationRecyclerViewAdapter extends RecyclerView.Adapter<InformationRecyclerViewAdapter.InformationViewHolder> {

    Context context;
    List<CompactReport> reportList;
    LatLng currentLocation;

    public static class InformationViewHolder extends RecyclerView.ViewHolder {

        TextView reportTitle;
        TextView reportInformation;
        TextView reportLocation;
        ImageView incidentTypeLogo;
        ImageView locationMarkerIcon;

        InformationViewHolder(View itemView) {
            super(itemView);

            reportTitle = (TextView) itemView.findViewById(R.id.reportTitleTextView);
            reportInformation = (TextView) itemView.findViewById(R.id.reportInformationTextView);
            reportLocation = (TextView) itemView.findViewById(R.id.reportLocationTextView);
            incidentTypeLogo = (ImageView) itemView.findViewById(R.id.incidentTypeLogo);
            locationMarkerIcon = (ImageView) itemView.findViewById(R.id.locationMarkerIcon);

        }
    }

    public InformationRecyclerViewAdapter(){

    }

    public InformationRecyclerViewAdapter(Context context, List<CompactReport> reportList, LatLng location){
        this.context = context;
        this.reportList = reportList;
        this.currentLocation = location;
    }


    @Override
    public InformationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_information_list, viewGroup, false);
        InformationViewHolder ivh = new InformationViewHolder(v);
        return ivh;
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    @Override
    public void onBindViewHolder(InformationViewHolder informationViewHolder, final int i) {

        CompactReport report = reportList.get(i);
        LatLng reportLocation = new LatLng(report.latitude,report.longitude);
        String roundDistance = "";

        CompactReportUtil cmpUtil = new CompactReportUtil();
        Map<String, String> reportData = cmpUtil.parseReportData(report,"list");

        if(!report.type.equals("feed-missing") && !report.type.equals("feed-weather")){
            double distanceInMile = cmpUtil.distanceBetweenPoints(currentLocation,reportLocation);
            roundDistance = String.format("%.2f", distanceInMile);
            roundDistance = roundDistance + " miles far";
            informationViewHolder.locationMarkerIcon.setVisibility(View.VISIBLE);
        } else {
            // Hiding the location icon as it is not available for feed-missing & feed-weather
            informationViewHolder.locationMarkerIcon.setVisibility(View.INVISIBLE);
        }

        String reportTitle = reportData.get("title");
        String fullInfo = reportData.get("information");

        informationViewHolder.reportTitle.setText(reportTitle);
        informationViewHolder.reportInformation.setText(fullInfo);
        informationViewHolder.reportLocation.setText(roundDistance);

        switch(reportTitle){
            case "Medical":
                informationViewHolder.incidentTypeLogo.setImageResource(R.drawable.hospital);
                break;
            case "Fire":
                informationViewHolder.incidentTypeLogo.setImageResource(R.drawable.flame);
                break;
            case "Police":
                informationViewHolder.incidentTypeLogo.setImageResource(R.drawable.siren);
                break;
            case "Traffic":
                informationViewHolder.incidentTypeLogo.setImageResource(R.drawable.cone);
                break;
            case "Utility":
                informationViewHolder.incidentTypeLogo.setImageResource(R.drawable.repairing);
                break;
            case "Weather":
            case "Missing":
            case "Crime":
                informationViewHolder.incidentTypeLogo.setImageResource(R.drawable.rss);
                break;
        }
    }

    public void updateList(List<CompactReport> list){
        reportList.clear();
        reportList.addAll(list);
        notifyDataSetChanged();
    }

    public void filterByQuery(String query){
        List<CompactReport> temp = new ArrayList();

        CompactReportUtil cmpUtil = new CompactReportUtil();

        for(CompactReport cmpReport: reportList){

            Map<String, String> reportData = cmpUtil.parseReportData(cmpReport,"list");
            String reportTitle = reportData.get("title");

            if(reportTitle.toLowerCase().equals(query.toLowerCase())){
                temp.add(cmpReport);
            }
        }
        //update recyclerview
       updateList(temp);
    }

    public void filterByCategory(List<String> categoryList){

        List<CompactReport> temp = new ArrayList();
        CompactReportUtil cmpUtil = new CompactReportUtil();

        for(CompactReport cmpReport: reportList){

            Map<String, String> reportData = cmpUtil.parseReportData(cmpReport,"list");
            String reportTitle = reportData.get("title");

            for (String category : categoryList) {

                if (reportTitle.toLowerCase().equals(category.toLowerCase())) {
                    temp.add(cmpReport);
                }
            }

        }
        //update recyclerview
        updateList(temp);
    }

    public void filterByDistance(double queryDistance, LatLng specifiedLocation){

        List<CompactReport> temp = new ArrayList();
        CompactReportUtil cmpUtil = new CompactReportUtil();
        double distanceInMile;

        for(CompactReport cmpReport: reportList){
            LatLng reportLocation = new LatLng(cmpReport.latitude,cmpReport.longitude);

            if(specifiedLocation != null){
                distanceInMile = cmpUtil.distanceBetweenPoints(specifiedLocation,reportLocation);
            }else{
                distanceInMile = cmpUtil.distanceBetweenPoints(currentLocation,reportLocation);
            }

            if (distanceInMile <= queryDistance){
                temp.add(cmpReport);
            }
        }
        updateList(temp);
    }

    public void combineFilter(List<String> categoryList, double queryDistance, LatLng specifiedLocation) {

        List<CompactReport> temp = new ArrayList();
        CompactReportUtil cmpUtil = new CompactReportUtil();
        double distanceInMile;

        for (CompactReport cmpReport : reportList) {

            Map<String, String> reportData = cmpUtil.parseReportData(cmpReport,"list");
            String reportTitle = reportData.get("title");

            LatLng reportLocation = new LatLng(cmpReport.latitude,cmpReport.longitude);

            if(specifiedLocation != null){
                distanceInMile = cmpUtil.distanceBetweenPoints(specifiedLocation,reportLocation);
            }else{
                distanceInMile = cmpUtil.distanceBetweenPoints(currentLocation,reportLocation);
            }

            for (String category : categoryList) {
                if ((reportTitle.toLowerCase().equals(category.toLowerCase())) && distanceInMile <= queryDistance) {
                    temp.add(cmpReport);
                }
            }
        }

        updateList(temp);
    }
}
