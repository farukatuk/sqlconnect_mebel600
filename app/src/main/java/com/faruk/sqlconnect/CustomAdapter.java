package com.faruk.sqlconnect;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private LayoutInflater servisListInflater;
    private List<servis_data> servis_data;
    public CustomAdapter(Context activity, List<servis_data> servis_data) {
    servisListInflater = (LayoutInflater) activity.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
    this.servis_data = servis_data;

    }

    @Override
    public int getCount() {
        return servis_data.size();
    }

    @Override
    public Object getItem(int i) {
        return servis_data.get( i );
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View lineView;
        lineView = servisListInflater.inflate( R.layout.servis_list,null );
        TextView serListKayNo = lineView.findViewById( R.id.serListKayNo );
        TextView serListMakNo = lineView.findViewById( R.id.serListMakNo );
        TextView serListMarka = lineView.findViewById( R.id.serListMarka );
        TextView serListModel = lineView.findViewById( R.id.serListModel );
        TextView serListSerNo = lineView.findViewById( R.id.serListSerNo );
        TextView serListUnvan = lineView.findViewById( R.id.serListUnvan );
        TextView serListDurum = lineView.findViewById( R.id.serListDurum );
        TextView serListTarih = lineView.findViewById( R.id.serListTarih );
        TextView serListSaat =  lineView.findViewById( R.id.serListSaat );
        TextView serlistTek = lineView.findViewById( R.id.serListTek );
        TextView serListSikayet = lineView.findViewById( R.id.serListSikayet );
        TextView serListYetkili = lineView.findViewById( R.id.serListYetkili );
        TextView serlistAlan = lineView.findViewById( R.id.serListAlan );
        com.faruk.sqlconnect.servis_data sList =    servis_data.get( i );
        serListKayNo.setText( sList.getsKayNo());
        serListMakNo.setText( sList.getsMakNo() );
        serListMarka.setText( sList.getsMarka() );
        serListModel.setText( sList.getsModel() );
        serListSerNo.setText( sList.getsSeriNo() );
        serListUnvan.setText( sList.getsUnvan() );
        serListDurum.setText( sList.getsDurum() );
        serListTarih.setText( sList.getsGelTarih() );
        serListSaat.setText( sList.getsGelSaat() );
        serlistTek.setText( sList.getsTeknisyen() );
        serListSikayet.setText( sList.getsSikayet() );
        serListYetkili.setText(  sList.getsYetkili());
        serlistAlan.setText( sList.getsAlan() );
        return lineView;

    }
}

