package com.faruk.sqlconnect;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.SimpleTimeZone;

import android.content.SharedPreferences;

public class servis_bilgi extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    final List<servis_data> servis_data = new ArrayList<servis_data>();// arızaların listViewda gösterilmesi için gerekli array
    private DatePickerDialog serDateBasPicker;
    private DatePickerDialog serDateBitPicker;
    private Button mservisBulButton;
    private Button mGeriButton;
    private TextView mSBagDurumText;
    private Spinner mSerTekSpinner;
    private EditText mSerDateBas;
    private EditText mSerDateBit;
    private TextView mIsSayisi;
    private TextView mTeknisyenText;
    ListView mSerList;
    private SoundPool sesCal; // Esas ses değişkeni
    // ses değişkenleri
    private int calTubularID;
    private int cyrstalID;
    private int korgID;
    // *******************
    private final int NR_OF_SIMULTANEOUS_SOUNDS = 7;
    private final float LEFT_VOLUME = 1.0f;
    private final float RIGHT_VOLUME = 1.0f;
    private final int NO_LOOP = 0;
    private final int PRIORITY = 0;
    private final float NORMAL_PLAY_RATE = 1.0f;
    // *************************************************************************
    public static final String PREFS_NAME = "MyPrefFile"; // Saklanacak değerlerin olduğu dosya
    private String ipAlMain;
    private String userAlMain;
    private String passwordAlMain;
    private String tekNameAlMain;
    private Connection connection = null;
    private ListView mServisList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_servis_bilgi );
        sesCal = new SoundPool(NR_OF_SIMULTANEOUS_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        calTubularID = sesCal.load(getApplicationContext(), R.raw.tubular_c6, 1); //tubular yüklendi.
        cyrstalID = sesCal.load(getApplicationContext(), R.raw.crystal_1, 1); //tubular yüklendi.
        korgID = sesCal.load(getApplicationContext(), R.raw.korg, 1); //korg yüklendi.
        mservisBulButton = findViewById( R.id.serBulButton );
        mSBagDurumText = findViewById( R.id.sBagDurumText );
        mSerDateBas = findViewById( R.id.serDateBas );
        mSerDateBit = findViewById( R.id.serDateBit );
        //mSerTekSpinner = findViewById( R.id.serTekSpinner );
        mIsSayisi = findViewById( R.id.isSayisi );
        mSerList = findViewById( R.id.servisList );
        mGeriButton = findViewById( R.id.geriButton );
        mTeknisyenText = findViewById( R.id.tekNameText );

        //Saklanan değerleri alır ve değişkenlere atar
        SharedPreferences degerAlMain = getSharedPreferences( PREFS_NAME,0 );
        ipAlMain = degerAlMain.getString( "ip",setup.ipVer );
        userAlMain = degerAlMain.getString( "userName",setup.userVer );
        passwordAlMain =degerAlMain.getString( "password",setup.passwordVer );
        tekNameAlMain = degerAlMain.getString( "tekName",setup.tekVer );
        mTeknisyenText.setText( tekNameAlMain );
        // Sql baplantısını yapmak için stringlere koyar
        String ip = ipAlMain;
        String port = "1433";
        String Classes = "net.sourceforge.jtds.jdbc.Driver";
        String database = "mebel";
        String username = userAlMain;
        String password = passwordAlMain;
        String url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+database;
        ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED );
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy( policy );
        try {
            Class.forName( Classes );
            connection = DriverManager.getConnection( url, username, password );
            mSBagDurumText.setText( "PROGRAMA BAĞLANDI" );
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            mSBagDurumText.setText( "BULUNAMADI, INTERNET BAĞLANTINIZI KONTROL EDİNİZ." );
        } catch (SQLException e) {
            e.printStackTrace();
            mSBagDurumText.setText( "BAĞLANILAMADI, INTERNET BAĞLANTINIZI KONTROL EDİNİZ." );
        }
        // servis tarihi başlangıç alanına bugünün tarihinin atanması
        final Calendar serBasToday = Calendar.getInstance();
        final int serBasToDayDay = serBasToday.get( Calendar.DAY_OF_MONTH ); // servis başlangıç tarihinden sorgunun bir haftayı kapsaması için 7 gün çıkartıyorum.
        final int serBastoDayMonth = serBasToday.get( Calendar.MONTH ) - 4;
        final int serBasToDayYear = serBasToday.get( Calendar.YEAR );
        mSerDateBas.setText( serBasToDayDay + "-" + serBastoDayMonth + "-" + serBasToDayYear );
        // servis tarihi başlangıç alanına basıldığında Date Pickerin görüntülenmesi
        mSerDateBas.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final Calendar serBasCldr = Calendar.getInstance();
                final int day = serBasCldr.get( Calendar.DAY_OF_MONTH );
                final int month = serBasCldr.get( Calendar.MONTH );
                final int year = serBasCldr.get( Calendar.YEAR );
                // datepicker
                serDateBasPicker = new DatePickerDialog( servis_bilgi.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mSerDateBas.setText( dayOfMonth + "-" + (month + 1) + "-" + year );
                            }
                        }, year, month, day );
                serDateBasPicker.show();
            }
        } );
        //***************************************************************************
        // servis bitiş başlangıç alanına bugünün tarihinin atanması
        final Calendar serBitToday = Calendar.getInstance();
        final int serBitToDayDay = serBitToday.get( Calendar.DAY_OF_MONTH );
        final int serBitToDayMonth = serBitToday.get( Calendar.MONTH ) + 1;
        final int serBitToDayYear = serBitToday.get( Calendar.YEAR );
        mSerDateBit.setText( serBitToDayDay + "-" + serBitToDayMonth + "-" + serBitToDayYear );
        // servis tarihi başlangıç alanına basıldığında Date Pickerin görüntülenmesi
        mSerDateBit.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final Calendar serBitCldr = Calendar.getInstance();
                final int day = serBitCldr.get( Calendar.DAY_OF_MONTH );
                final int month = serBitCldr.get( Calendar.MONTH );
                final int year = serBitCldr.get( Calendar.YEAR );
                // datepicker
                serDateBitPicker = new DatePickerDialog( servis_bilgi.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mSerDateBit.setText( dayOfMonth + "-" + (month + 1) + "-" + year );
                            }
                        }, year, month, day );
                serDateBitPicker.show();
            }
        } );
        //**********************************************************
      /*  // Teknisyen spinner için listenin atanması ve listede gösterilmesi
        ArrayAdapter adapterSerTeknisyen = ArrayAdapter.createFromResource(
                this,
                R.array.teknisyen,
                R.layout.color_spinner_layout
        );
        adapterSerTeknisyen.setDropDownViewResource( R.layout.color_spinner_layout );
        mSerTekSpinner.setAdapter( adapterSerTeknisyen );
        mSerTekSpinner.setOnItemSelectedListener( this );*/



    }
    //On Create sonu
    public void geri (View view){

        Intent i = new Intent(servis_bilgi.this, MainActivity.class);
        startActivity( i );
    }
    // Servis kayıtlarının bulunup ekrana getirilmesi ve ilgili kaydın kapatılması.
    public void bul(View view)throws ParseException {
        servis_data.clear();
        final String serTarBas = mSerDateBas.getText().toString();
        final String serTarBit = mSerDateBit.getText().toString();
        final String serTek = mTeknisyenText.getText().toString();
        SimpleDateFormat formatter = new SimpleDateFormat( "dd-MM-yyyy" );
        SimpleDateFormat formatter1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS" );
        Date sBasDate = formatter.parse( serTarBas );
        Date sBitDate = formatter.parse(serTarBit);
        String lBasDate = new SimpleDateFormat( "yyyy-MM-dd" ).format( sBasDate );
        String lBitDate = new SimpleDateFormat( "yyyy-MM-dd" ).format( sBitDate );


        if (connection != null) {
            Statement statement = null;
            final ListView servisList = findViewById( R.id.servisList );//xml deki listView link ediliyor
            // ListView için kullanılan adaptör servis_data listView ile ilişkilendiriliyor ve adaptör çağrılıyor.
            final CustomAdapter adapter = new CustomAdapter( getApplicationContext(), servis_data );
            servisList.setAdapter( adapter );

            int sIsSayisi = 0;
            try {
                statement = connection.createStatement();
                final ResultSet resultSet = statement.executeQuery(
                        "SELECT * FROM [ARIZA KAYITLARI]WHERE [TARİH] BETWEEN '"+lBasDate+
                             "' AND '"+lBitDate+"' AND DURUMU = 'GİDİLECEK' AND [GİDEN TEKNİSYEN] = '"+serTek+"' ORDER BY TARİH DESC;" );
                if (resultSet.next()== false){
                    sesCal.play(calTubularID, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, NO_LOOP, NORMAL_PLAY_RATE);
                    Toast.makeText( this,"! KAYIT BULUNAMADI !",Toast.LENGTH_LONG ).show();
                    sIsSayisi = 0;
                    mIsSayisi.setText( "" );
                    servis_data.clear();
                    adapter.notifyDataSetChanged();
                    //return;
                } else {
                do {
                        // SQL den okunan tarih formatını çevirmek için *******************
                        Date sGDate = formatter1.parse( resultSet.getString( 6 ) );
                        String lGDate = new SimpleDateFormat( "dd-MM-YYYY" ).format( sGDate );
                        //***************************************************************************
                        sIsSayisi = sIsSayisi + 1;
                        mIsSayisi.setText( String.valueOf( sIsSayisi ) );

                        // Queryden okunan değerler servis_data listesine atanıyor
                        servis_data.add( new servis_data(
                                        resultSet.getString( 1 ), // Kimlik(Kayıt No)
                                        resultSet.getString( 3 ), // Dosya No
                                        resultSet.getString( 17 ),// Marka
                                        resultSet.getString( 14 ),// Model
                                        resultSet.getString( 16 ),// Seri No
                                        resultSet.getString( 2), // Ünvan
                                        resultSet.getString( 13 ),// Durumu
                                        lGDate,// Geliş Tarihi
                                        resultSet.getString( 18 ),//Lokasyon
                                        resultSet.getString( 9 ), //Giden Teknisyen
                                        resultSet.getString( 5 ),//Şikayet
                                        resultSet.getString( 4 ), // Yetkili
                                        resultSet.getString(  8) // Arızayı alan
                                )

                        );

                    }  while (resultSet.next());
                }
                servisList.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final String kaynoData = servis_data.get( position ).getsKayNo();
                        final String dosnoData = servis_data.get( position ).getsMakNo();
                        final Date bugun = new Date();
                        final String lBugun = new SimpleDateFormat( "yyyy-MM-dd" ).format( bugun );
                        final String lGoster = new SimpleDateFormat( "dd-MM-yyyy" ).format( bugun );
                        final String lSaatGoster = new SimpleDateFormat( "HH:mm" ).format( bugun );
                        final LocalTime simdi = LocalTime.now();
                        final String lSimdi = simdi.toString();
                        //Toast.makeText(getApplicationContext(),dosnoData,Toast.LENGTH_LONG).show();
                        AlertDialog.Builder kapat = new AlertDialog.Builder( servis_bilgi.this);
                        kapat.setPositiveButton( "TAMAM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (connection != null){
                                    Statement statement1 = null;
                                    try {
                                        statement1 = connection.createStatement();
                                        ResultSet resultSet1 = statement1.executeQuery(
                                                "UPDATE [ARIZA KAYITLARI] SET DURUMU = 'KAPANDI', [GİDİŞ TARİHİ] = '"
                                                        +lBugun+"', GSAAT = '"+lSimdi+"' WHERE Kimlik = '"+kaynoData+"';");
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                        sesCal.play(korgID, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, NO_LOOP, NORMAL_PLAY_RATE);
                                        Toast.makeText( getApplicationContext(),kaynoData+" NOLU SERVİS KAYDI BAŞARILI İLE KAPATILDI",Toast.LENGTH_LONG ).show();
                                    }
                                }
                            }
                        } )
                                .setTitle( kaynoData+ " NOLU KAYIT KAPATILACAKTIR" )
                                .setMessage( "SERVİS TARİHİ: " +lGoster+
                                             "               SERVİS SAATİ:   "+lSaatGoster)
                                .setCancelable( false )
                                .setNegativeButton( "İPTAL",null )
                                .show();

                    }
                } );

            } catch (SQLException e) {
                e.printStackTrace();

            }
        } else {
            mSBagDurumText.setText( "BAĞLANTI BAŞARISIZ" );
        }

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
  // ******************************************************************************************************

}