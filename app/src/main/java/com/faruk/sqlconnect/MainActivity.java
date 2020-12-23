package com.faruk.sqlconnect;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font;
import com.tom_roush.pdfbox.pdmodel.graphics.image.JPEGFactory;
import com.tom_roush.pdfbox.pdmodel.graphics.image.LosslessFactory;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
// activity maindeki alanların değişkenleri
    public static EditText mbulText;
    public static TextView mConnText;
    public static EditText mDnoText;
    public static EditText mseriNoText;
    public static EditText mdateText;
    public static EditText munvanText;
    public static EditText mModelText;
    public static EditText mMarkaText;
    public static EditText mbolgeText;
    public static EditText maciklamaText;
    public static EditText msbSayacText;
    public static EditText mclSayacText;
    public static TextView msonSbSayacText;
    public static TextView msonClSayacText;
    public static Spinner mArizaSpinner;
    public static Spinner mislemSpinner;
    public static Spinner mteknisyenSpinner;
    public static Spinner misTipiSpinner;
    public static DatePickerDialog picker; //Tarih için seçiçi
    public static String mcariKod; // Rapora Cari kodu yazmak için
    public static String mMakTip; // Makinenin renkli veya SB olduğunu takip etmek için
    public static Button mKayButton;
    public static Button mservisButton;
    public static Button mcikButton;
    public static Button mScannerButton;
    public static Button mBulButton;
    public static Button mMailButton;
    public static Button mImzaButton;
    public static TextView mMakT;
    public static TextView mTekTextView;
    public static TextView mMailTo;
    public static EditText mYetkili;
    public static Bitmap mCpfLogo;
    public static Bitmap mScaledCpfLogo;
    public static Bitmap mSign;
    //public static Bitmap mScaledSign;
    // *************************

    public static final String PREFS_NAME = "MyPrefFile"; // Saklanacak değerlerin olduğu dosya
    public static String ipAlMain;
    public static String userAlMain;
    public static String passwordAlMain;
    public static String tekNameAlMain;

    // ****************************
    // girilen ve son alınan sayaçları kontrol ettirmek için kullanılacak değişkenler
    private int kTopsay;
    private int ksonSbSayac;
    private int kClSayac;
    private int kSonClSayac;


    // ******************************************************************************

    // Çalınacak sesler ile ilgili değişkenler
    public static SoundPool sesCal; // Esas ses değişkeni
    // ses değişkenleri
    public static int calTubularID;
    public static int cyrstalID;
    public static int korgID;
    // *******************
    public static final int NR_OF_SIMULTANEOUS_SOUNDS = 7;
    public static final float LEFT_VOLUME = 1.0f;
    public static final float RIGHT_VOLUME = 1.0f;
    public static final int NO_LOOP = 0;
    public static final int PRIORITY = 0;
    public static final float NORMAL_PLAY_RATE = 1.0f;
    public static Connection connection = null;
    //PDFBox için değişkenler
    File root;
   // AssetManager assetManager;
    Bitmap pageImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //assetManager = getAssets();
        sesCal = new SoundPool(NR_OF_SIMULTANEOUS_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        calTubularID = sesCal.load(getApplicationContext(), R.raw.tubular_c6, 1); //tubular yüklendi.
        cyrstalID = sesCal.load(getApplicationContext(), R.raw.crystal_1, 1); //tubular yüklendi.
        korgID = sesCal.load(getApplicationContext(), R.raw.korg, 1); //korg yüklendi.
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        // Activity sayfasındaki alanların Java ile ilişkilendirilmesi
        mKayButton =findViewById( R.id.updateButton );
        mservisButton = findViewById( R.id.servisButton );
        mcikButton = findViewById( R.id.cikButton );
        mbulText = findViewById(R.id.bulText);
        mConnText = findViewById(R.id.connText);
        mseriNoText = findViewById(R.id.seriNoText);
        mdateText = findViewById(R.id.dateText);
        munvanText = findViewById(R.id.unvanText);
        mDnoText = findViewById(R.id.makNoText);
        mModelText = findViewById(R.id.modelText);
        mMarkaText = findViewById(R.id.markaText);
        mbolgeText = findViewById(R.id.bolgeText);
        msbSayacText = findViewById( R.id.sbSayacText);
        mclSayacText = findViewById(R.id.clSayacText);
        msonClSayacText = findViewById( R.id.sonClSayacText );
        maciklamaText = findViewById( R.id.aciklamaText);
        mArizaSpinner = findViewById(R.id.arizaSpinner);
        mislemSpinner = findViewById(R.id.islemSpinner);
        msonSbSayacText = findViewById( R.id.sonSbSayacText );
        mTekTextView = findViewById( R.id.tekNameText );
        //mteknisyenSpinner =findViewById( R.id.teknisyenSpinner );
        misTipiSpinner = findViewById( R.id.isTipiSpinner );
        mScannerButton = findViewById( R.id.scanButton );
        mBulButton = findViewById( R.id.bulButton );
        mMailButton = findViewById( R.id.emailButton );
        mImzaButton =findViewById(R.id.imza);
        mMailTo = findViewById( R.id.custMailAdress );
        mYetkili = findViewById( R.id.yetkiliEditText );
        mScannerButton.setVisibility( View.INVISIBLE );
        // PDF rapor için logo ve küçültülmüş logo ve imza
       mSign = BitmapFactory.decodeFile(MainActivity.this.getExternalFilesDir(null).toString()+"/sign.png/");
       //mScaledSign = Bitmap.createScaledBitmap(mSign,70,30,false);
       mCpfLogo = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
       mScaledCpfLogo = Bitmap.createScaledBitmap( mCpfLogo,70,30,false );
        // **************************************
        //Saklanan SQL bağlantı derğerlerini  değişkenlere atar
        SharedPreferences degerAlMain = getSharedPreferences( PREFS_NAME,0 );
        ipAlMain = degerAlMain.getString( "ip",setup.ipVer );
        userAlMain = degerAlMain.getString( "userName",setup.userVer );
        passwordAlMain =degerAlMain.getString( "password",setup.passwordVer );
        tekNameAlMain = degerAlMain.getString( "tekName",setup.tekVer );
        mTekTextView.setText( tekNameAlMain );
        mImzaButton.setVisibility(View.INVISIBLE);
        // Sql baplantısını yapmak için stringlere koyar
        String ip = ipAlMain;
        String port = "1433";
        String Classes = "net.sourceforge.jtds.jdbc.Driver";
        String database = "mebel";
        String username = userAlMain;
        String password = passwordAlMain;
        String url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+database;
        // ************************************************************
        // İmza Sayfasını açmak için
        mImzaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, imza.class);
                startActivity( i );
            }
        });

        // Servis Sayfasını Açmak için *****************************
        mservisButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, servis_bilgi.class);
                startActivity( i );
            }
        } );
        // ************************************************************
        //Arıza spinner için listenin atanması ve listede gösterilmesi
        ArrayAdapter adapterariza = ArrayAdapter.createFromResource(
                this,
                R.array.ariza,
                R.layout.color_spinner_layout
        );
        adapterariza.setDropDownViewResource(R.layout.color_spinner_layout);
        mArizaSpinner.setAdapter(adapterariza);
        mArizaSpinner.setOnItemSelectedListener(this);
        //islem spinner için listenin atanması ve listede gösterilmesi
        ArrayAdapter adapterislem = ArrayAdapter.createFromResource(
                this,
                R.array.islem,
                R.layout.color_spinner_layout
        );
        adapterislem.setDropDownViewResource(R.layout.color_spinner_layout);
        mislemSpinner.setAdapter(adapterislem);
        mislemSpinner.setOnItemSelectedListener(this);
        //************************************************************
        /*// Teknisyen spinner için listenin atanması ve listede gösterilmesi
        ArrayAdapter adapterTeknisyen = ArrayAdapter.createFromResource(
                this,
                R.array.teknisyen,
                R.layout.color_spinner_layout
        );
        adapterTeknisyen.setDropDownViewResource( R.layout.color_spinner_layout );
        mteknisyenSpinner.setAdapter( adapterTeknisyen );
        mteknisyenSpinner.setOnItemSelectedListener( this );
        // **************************************************************************/
        // İs Tipi spinner için listenin atanması ve listede gösterilmesi
        ArrayAdapter adapterisTipi = ArrayAdapter.createFromResource(
                this,
                R.array.is_tipi,
                R.layout.color_spinner_layout

        );
        adapterisTipi.setDropDownViewResource( R.layout.color_spinner_layout );
        misTipiSpinner.setAdapter( adapterisTipi );
        misTipiSpinner.setOnItemSelectedListener( this );
        // *************************************************************************
        // SQL Database bağlantı yapılması için
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username,password);
            mConnText.setText("PROGRAMA BAĞLANDI");
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            mConnText.setText("BULUNAMADI, INTERNET BAĞLANTINIZI KONTROL EDİNİZ.");
        } catch (SQLException e) {
            //e.printStackTrace();
            mConnText.setText("BAĞLANILAMADI, INTERNET BAĞLANTINIZI KONTROL EDİNİZ.");
        }
        mKayButton.setVisibility( View.INVISIBLE ); // Başlangıçta kayıt butonunu devre dışı bırakmak için.
        mMailButton.setVisibility(View.INVISIBLE);
        // Tarih alanına bugünün tarihinin atanması
        final Calendar today =Calendar.getInstance();
        final int toDayDay =today.get(Calendar.DAY_OF_MONTH);
        final int toDayMonth = today.get(Calendar.MONTH)+1;
        final int toDayYear = today.get(Calendar.YEAR);
        mdateText.setText( toDayDay+"-"+toDayMonth+"-"+toDayYear);
        // Tarih alanına basıldığında Date Pickerin görüntülenmesi
        mdateText.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                final int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                final int year = cldr.get(Calendar.YEAR);
                // datepicker
                picker = new DatePickerDialog( MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mdateText.setText( dayOfMonth+ "-"+ (month + 1)+"-"+ year);
                            }
                        },year,month,day);
                picker.show();
            }
        });
        //***************************************************************************
   //Ayar sayfasının görüntülenmesi için
    mConnText.setOnLongClickListener( new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (mbulText.getText().toString().equals( "020564" )) {
                Intent i = new Intent( MainActivity.this, setup.class );
                startActivity( i );
                return false;
            } else {
                return true;
            }


        }
    } );
    // Barcode tarayıcı için Activity çağrılıyor **************************************
        mScannerButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbulText.setText( "" );
                Intent i = new Intent( MainActivity.this, Scanner.class);
                startActivity( i );
            }
        } );
    // Çıkış için *******************************************************************
        mcikButton.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder cikis = new AlertDialog.Builder( MainActivity.this );
                cikis.setPositiveButton( "TAMAM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                } )
                        .setNegativeButton( "İPTAL",null )
                        .setCancelable( true )
                        .setMessage( "!! PROGRAMDAN ÇIKILACAKTIR!!" )
                        .show();
                return false;
            }
        } );
    mBulButton.setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String araText;
            if (mbulText.getText().toString().equals( "" )) {
                araText = Scanner.qrBulData;
                //Toast.makeText(this , araText, Toast.LENGTH_LONG );
            } else {
                araText = mbulText.getText().toString();
            }
            if (connection != null) {
                Statement statement = null;
                try {
                    //Seçilen müşteri bilgilerinin getirilmesi ve ekranda gösterilmesi.
                    statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery( "Select * from [Faruk DENEME] WHERE [DOSYA NO] = '" + araText + "';" );
                    if (resultSet.next() == false) {
                        sesCal.play( calTubularID, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, NO_LOOP, NORMAL_PLAY_RATE );
                        Toast.makeText( MainActivity.this, "KAYIT BULUNAMADI", Toast.LENGTH_LONG ).show();
                        mDnoText.setText( "" );
                        munvanText.setText( "" );
                        mbolgeText.setText( "" );
                        mMarkaText.setText( "" );
                        mModelText.setText( "" );
                        mseriNoText.setText( "" );
                        mKayButton.setVisibility( View.INVISIBLE );
                        return;
                    } else {
                        do {
                            mDnoText.setText( resultSet.getString( 3 ) );
                            munvanText.setText( resultSet.getString( 5 ) );
                            mbolgeText.setText( resultSet.getString( 7 ) );
                            mMarkaText.setText( resultSet.getString( 8 ) );
                            mModelText.setText( resultSet.getString( 9 ) );
                            mseriNoText.setText( resultSet.getString( 12 ) );
                            mcariKod = resultSet.getString( 67 );
                            mMakTip = resultSet.getString( 72 );
                            msbSayacText.setText( "0" );
                            mclSayacText.setText( "0" );
                            mKayButton.setVisibility( View.VISIBLE );
                        } while (resultSet.next());
                    }
                    // Seçilen müşteriye ait son sayaç bilgilerinin RAPOR tablosundangetirilmesi ve ekranda gösterilmesi
                    statement = connection.createStatement();
                    ResultSet resultSet1 = statement.executeQuery( "SELECT MAX(TOPSAY),MAX([SAYAÇ 2]), MAX([DOSYA NO]) FROM RAPOR WHERE [DOSYA NO]= '" + mDnoText.getText() + "';" );
                    while (resultSet1.next()) {
                        msonSbSayacText.setText( resultSet1.getString( 1 ) );
                        msonClSayacText.setText( resultSet1.getString( 2 ) );
                        // Eğer okunan makinede hiç sayaç bilgisi yok ise programın hata vermesini engellemek için 0 değerini
                        // atıyorum.
                        if (msonSbSayacText.getText().equals( "" )) {
                            msonSbSayacText.setText( "0" );
                        }
                        if (msonClSayacText.getText().equals( "" )) {
                            msonClSayacText.setText( "0" );
                        }
                    }
                    //***********************************************************************************************************
                } catch (SQLException e) {
                    e.printStackTrace();

                }

            } else {
                mConnText.setText( "BULUNAMADI, INTERNET BAĞLANTINIZI KONTROL EDİNİZ." );
            }

        }
    } );
    mcikButton.setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText( getApplicationContext(),"PROGRAMDAN ÇIKMAK İÇİN TUŞA UZUN BASINIZ",Toast.LENGTH_LONG ).show();
        }
    } );

    // Müşteriye raporu mail ile göndermek için ***************
    mMailButton.setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // PDF raporun oluşturulması *****************
                final String pdfRapTarihi = mdateText.getText().toString();
                final String pdfTeknisyen = mTekTextView.getText().toString();
                final String pdfMakNo = mDnoText.getText().toString();
                final String pdfUnvan = munvanText.getText().toString();
                final String pdfMarka = mMarkaText.getText().toString();
                final String pdfModel = mModelText.getText().toString();
                final String pdfSeriNo = mseriNoText.getText().toString();
                final String pdfSbSayac = msbSayacText.getText().toString();
                final String pdfClSayac = mclSayacText.getText().toString();
                final String pdfSNo = mseriNoText.getText().toString();
                final String pdfIsTipi = misTipiSpinner.getSelectedItem().toString();
                final String pdfAriza = mArizaSpinner.getSelectedItem().toString();
                final String pdfIslem = mislemSpinner.getSelectedItem().toString();
                final String pdfAciklama = maciklamaText.getText().toString();
                final String pdfYetkiliImza = mYetkili.getText().toString();
                final String pdfTekImza = mTekTextView.getText().toString();
                PdfDocument servisRapor = new PdfDocument();
                Paint mPaint = new Paint();
                PdfDocument.PageInfo servisPageInfo = new PdfDocument.PageInfo.Builder(250,400,1).create();
                PdfDocument.Page mPage = servisRapor.startPage( servisPageInfo );
                Canvas canvas = mPage.getCanvas();
                // Sayfaya yazılacaklar burada
                mPaint.setTextAlign( Paint.Align.LEFT );
                mPaint.setTextSize( 9.0f );
                // Başlık ************************
                canvas.drawText( "MEBEL TEKNİK SERVİS RAPORU",60,23,mPaint );
                mPaint.setStyle( Paint.Style.STROKE );
                mPaint.setStrokeWidth( 1 );
                canvas.drawRect( 58,10,192,30,mPaint);
                //canvas.drawBitmap( mScaledCpfLogo,170,10,mPaint );
                // İlk Satır Tarih ve Teknisyen
                mPaint.setStyle( Paint.Style.FILL );
                mPaint.setTextSize(7.0f);
                mPaint.setColor( Color.BLUE );
                mPaint.setFakeBoldText( true );
                mPaint.setUnderlineText( true );
                canvas.drawText( "TARİH :",10,60,mPaint );
                mPaint.setColor( Color.BLACK );
                mPaint.setFakeBoldText( false );
                mPaint.setUnderlineText( false );
                canvas.drawText( pdfRapTarihi,40,60,mPaint );
                mPaint.setColor( Color.BLUE );
                mPaint.setFakeBoldText( true );
                mPaint.setUnderlineText( true );
                canvas.drawText( "TEKNİSYEN :" ,98,60,mPaint);
                mPaint.setColor( Color.BLACK );
                mPaint.setFakeBoldText( false );
                mPaint.setUnderlineText( false );
                canvas.drawText( pdfTeknisyen,150,60,mPaint );
                canvas.drawLine( 8,50,servisPageInfo.getPageWidth()-5,50,mPaint );
                canvas.drawLine( 8,65,servisPageInfo.getPageWidth()-5,65,mPaint );
                canvas.drawLine( 8, 50,8,65,mPaint);
                canvas.drawLine( 90, 50,90,65,mPaint);
                canvas.drawLine( 245, 50,245,65,mPaint);
                // 2. Satır ****************************************************
                mPaint.setColor( Color.BLUE );
                mPaint.setFakeBoldText( true );
                mPaint.setUnderlineText(true );
                canvas.drawText( "ID.NO :",10,75,mPaint );
                canvas.drawText( "MARKA :",73,75,mPaint);
                canvas.drawText( "MODEL :",157,75,mPaint );
                mPaint.setColor( Color.BLACK );
                mPaint.setFakeBoldText( false );
                mPaint.setUnderlineText(false );
                canvas.drawText( pdfMakNo, 45,75,mPaint );
                canvas.drawText( pdfMarka, 108,75,mPaint);
                canvas.drawText( pdfModel,189,75,mPaint );
                canvas.drawLine( 8,80,servisPageInfo.getPageWidth()-5,80,mPaint );
                canvas.drawLine( 8, 65,8,80,mPaint);
                canvas.drawLine( 71, 65,71,80,mPaint);
                canvas.drawLine( 156, 65,156,80,mPaint);
                canvas.drawLine( 245, 65,245,80,mPaint);
                // 3. Satır
                mPaint.setColor( Color.BLUE );
                mPaint.setFakeBoldText( true );
                mPaint.setUnderlineText(true );
                canvas.drawText( "S.NO:",10, 90,mPaint);
                canvas.drawText( "SB SAY:",92,90,mPaint );
                canvas.drawText( "CL SAY:",167,90,mPaint );
                mPaint.setColor( Color.BLACK );
                mPaint.setFakeBoldText( false );
                mPaint.setUnderlineText(false );
                canvas.drawText( pdfSeriNo,32,90,mPaint );
                canvas.drawText( pdfSbSayac,122,90,mPaint );
                canvas.drawText( pdfClSayac, 195,90,mPaint);
                canvas.drawLine( 8,95,servisPageInfo.getPageWidth()-5,95,mPaint );
                canvas.drawLine( 8, 80,8,95,mPaint);
                canvas.drawLine( 91, 80,91,95,mPaint);
                canvas.drawLine( 165, 80,165,95,mPaint);
                canvas.drawLine( 245, 80,245,95,mPaint);
                // 4. Satır
                mPaint.setColor( Color.BLUE );
                mPaint.setFakeBoldText( true );
                mPaint.setUnderlineText(true );
                canvas.drawText( "ÜNVAN:",10,105,mPaint );
                mPaint.setColor( Color.BLACK );
                mPaint.setFakeBoldText( false );
                mPaint.setUnderlineText(false );
                canvas.drawText( pdfUnvan,42,105,mPaint );
                canvas.drawLine( 8,110,servisPageInfo.getPageWidth()-5,110,mPaint );
                canvas.drawLine( 8, 95,8,110,mPaint);
                canvas.drawLine( 245, 95,245,110,mPaint);
                // 5.Satır
                mPaint.setColor( Color.BLUE );
                mPaint.setFakeBoldText( true );
                mPaint.setUnderlineText(true );
                canvas.drawText( "İŞ TİPİ:",10,120,mPaint );
                mPaint.setColor( Color.BLACK );
                mPaint.setFakeBoldText( false );
                mPaint.setUnderlineText(false );
                canvas.drawText( pdfIsTipi,42,120,mPaint );
                canvas.drawLine( 8,125,servisPageInfo.getPageWidth()-5,125,mPaint );
                canvas.drawLine( 8, 110,8,125,mPaint);
                canvas.drawLine( 245, 110,245,125,mPaint);
                //6.Satır
                mPaint.setColor( Color.BLUE );
                mPaint.setFakeBoldText( true );
                mPaint.setUnderlineText(true );
                canvas.drawText( "ARIZA:",10,135,mPaint );
                mPaint.setColor( Color.BLACK );
                mPaint.setFakeBoldText( false );
                mPaint.setUnderlineText(false );
                canvas.drawText( pdfAriza,42,135,mPaint);
                canvas.drawLine( 8,140,servisPageInfo.getPageWidth()-5,140,mPaint );
                canvas.drawLine( 8, 125,8,140,mPaint);
                canvas.drawLine( 245, 125,245,140,mPaint);
                //7.Satır
                mPaint.setColor( Color.BLUE );
                mPaint.setFakeBoldText( true );
                mPaint.setUnderlineText(true );
                canvas.drawText( "İŞLEM:",10,150,mPaint );
                mPaint.setColor( Color.BLACK );
                mPaint.setFakeBoldText( false );
                mPaint.setUnderlineText(false );
                canvas.drawText( pdfIslem,42,150,mPaint );
                canvas.drawLine( 8,155,servisPageInfo.getPageWidth()-5,155,mPaint );
                canvas.drawLine( 8, 140,8,155,mPaint);
                canvas.drawLine( 245, 140,245,155,mPaint);
                // 8.Satır

                mPaint.setColor( Color.BLUE );
                mPaint.setFakeBoldText( true );
                mPaint.setUnderlineText(true );
                canvas.drawText( "AÇIKLAMA:",10,165,mPaint );
                mPaint.setColor( Color.BLACK );
                mPaint.setFakeBoldText( false );
                mPaint.setUnderlineText(false );
                // Açıklamanın bölünerek yazdırılması
                int aciklamaSize = pdfAciklama.length();// pdf açıklamayı bölmek için boyunu ölçüyorum.
                String[] pdfBol = new String[5];// pdf Açıklamayı bölmek için kullanılan array
                String pdfUpper =pdfAciklama.toUpperCase(); // pdfAçıklamayı PDF de düzgün gözüksün diye büyük harfe çeviriyorum.
                for (int k = 0;k<5;k+=6) {
                    int satir = 10;
                    for (int i = 0; i < aciklamaSize; i += 54) {
                        String msg = pdfUpper.substring(i, Math.min(i + 54, aciklamaSize)); //
                        pdfBol[k] = msg;
                        canvas.drawText( pdfBol[k],10,(165+satir),mPaint);
                        satir = satir +10;


                    }
                }
                canvas.drawLine( 8,235,servisPageInfo.getPageWidth()-5,235,mPaint );
                canvas.drawLine( 8, 155,8,235,mPaint);
                canvas.drawLine( 245, 155,245,235,mPaint);
                // Yetkili ve imza kısmı **********************************************
                mPaint.setColor( Color.BLUE );
                mPaint.setFakeBoldText( true );
                mPaint.setUnderlineText(true );
                canvas.drawText("YETKİLİ ADI:",40,250,mPaint);
                canvas.drawText("İMZASI",45,260,mPaint);
                //canvas.drawBitmap( mScaledSign,30,260,mPaint );
                canvas.drawText("TEKNİSYEN",170,250,mPaint);
                canvas.drawText("İMZASI",175,260,mPaint);
                mPaint.setTextSize(8.0f);
                mPaint.setColor( Color.BLACK );
                mPaint.setFakeBoldText( false );
                mPaint.setUnderlineText(false );
                canvas.drawText(pdfYetkiliImza.toUpperCase(),35,275,mPaint);
                canvas.drawText(pdfTekImza,162,275,mPaint);
                canvas.drawLine( 8,325,servisPageInfo.getPageWidth()-5,325,mPaint );
                canvas.drawLine( 8, 235,8,325,mPaint);
                canvas.drawLine( 245, 235,245,325,mPaint);
                canvas.drawLine( servisPageInfo.getPageWidth()/2, 235,servisPageInfo.getPageWidth()/2,325,mPaint);
                // ******************************************************************************************
                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(6.0f);
                canvas.drawText("Bu rapor teknik servis sistemimiz tarafından bilgi amaçlı tarafınıza elektronik olarak ulaştırılmıştır.", servisPageInfo.getPageWidth()/2,335,mPaint);
                canvas.drawText("Lütfen arıza ve malzeme taleplerinizde makine numarasını bildiriniz", servisPageInfo.getPageWidth()/2,345,mPaint);
                mPaint.setFakeBoldText(true);
                canvas.drawText("Adres: Organize Sanayi Bölgesi 9.Sok. No:10 Taskinköy Lefkoşa - KTTC",servisPageInfo.getPageWidth()/2,360,mPaint);
                canvas.drawText("Tel: 0-392-444 63 23  Faks: 0-392-225 71 73", servisPageInfo.getPageWidth()/2,370,mPaint);
                canvas.drawText("Arıza email: servis@elektrosermebel.com Malzeme email: siparis@elektrosermebel.com" , servisPageInfo.getPageWidth()/2,380,mPaint);
                servisRapor.finishPage( mPage );
                //Dosya Yolunu al ve dosyanın adını koy
                File dosyaYolu = new File(MainActivity.this.getExternalFilesDir(null )+"/Rapor.pdf");
                try{
                    //Toast.makeText(getApplicationContext(),"Rapor "+dosyaYolu+ "konumuna kaydedildi.",Toast.LENGTH_LONG).show();
                    servisRapor.writeTo( new FileOutputStream(dosyaYolu) {
                    } );

                }catch (FileNotFoundException e) {

                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText( getApplicationContext(),"DOSYA YAZILAMADI",Toast.LENGTH_LONG ).show();
                }


            // PDF Sonu ***************************

            final File attachPad = new File(MainActivity.this.getExternalFilesDir(null)+"/Rapor.pdf");// Servis raporunun bulunduğu yer
            final String mailDNo = mDnoText.getText().toString();
            final String mailUnvan = munvanText.getText().toString();
            final String mailMarka = mMarkaText.getText().toString();
            final String mailModel = mModelText.getText().toString();
            final String mailSeriNo = mseriNoText.getText().toString();
            final String mailSbSayac = msbSayacText.getText().toString();
            final String mailClSayac = mclSayacText.getText().toString();
            final String mailAriza = mArizaSpinner.getSelectedItem().toString();
            final String mailIslem = mislemSpinner.getSelectedItem().toString();
            final String mailTeknisyen = mTekTextView.getText().toString();
            final String mailTarih = mdateText.getText().toString();
            final String mailNot = maciklamaText.getText().toString();
            final String mailYetkili = mYetkili.getText().toString();
            final String mailReceiver = mMailTo.getText().toString();

            // Eğer mail bilgilerinde eksiklik varsa uyarılması için gerekli
            if (mailReceiver.equals( "" )|| mailYetkili.equals( "" )||mailSbSayac.equals( "" )||mailDNo.equals( "" )){
                sesCal.play(korgID, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, NO_LOOP, NORMAL_PLAY_RATE);
                Toast.makeText( getApplicationContext(),"MAİL İLGİLİ BİLGİLERDE EKSİKLİK VAR.",Toast.LENGTH_LONG ).show();
                return;
            }
            final String mailBody1 = "Sayın "+mailYetkili.toUpperCase()+", \n\n"+"Firmanızda hizmet vermekte olduğumuz "+ mailDNo+" makine numaralı " +mailMarka+
                    " Marka, "+ mailModel+" Model ve "+mailSeriNo+" seri numaralı cihazınıza "+ mailTarih+" tarihinde "+mailTeknisyen+" isimli teknisyenimiz tarafından servis verilmiş olup cihaz sağlam olarak teslim edilmiştir. Yapılan işin detayları aşağıdaki gibidir.\n\n"+
                    "SİYAH BEYAZ SAYAÇ:  "+ mailSbSayac+" \n\n RENKLİ SAYAÇ:  "+mailClSayac+"\n\n BİLDİRİLEN ARIZA: "
                    +mailAriza+"\n\n YAPILAN İŞ:  "+mailIslem+"\n\n Açıklama: "+mailNot+"\n\n\n\n";
            final String mailBody2 = "Konuyu bilgilerinize arz ederiz."+
                    "\n\n İtirazınız olmaması durumunda bu bilgi maili içeriğinde bulunan servis bilgileri doğru kabul edilecektir."+"" +
                    "\n\n İtirazlarınızı servis@eletrosermebel.com adresine iletebilirsiniz."+
                    "\n\n\n\n\n\n Saygılarımızla,\n\n\n MEBEL BÜROTEKNİK ";
            final ProgressDialog dialog = new ProgressDialog( MainActivity.this );
            dialog.setTitle( "Mail Gönderiliyor" );
            dialog.setMessage( "Lütfen Bekleyiniz" );
            dialog.show();
            Thread sender = new Thread( new Runnable() {
                @Override
                public void run() {
                    try{
                        com.faruk.sqlconnect.GmailSender sender = new com.faruk.sqlconnect.GmailSender( "servis@elektrosermebel.com","Servis1234" );

                        sender.sendMail( "MEBEL Servis Hakkında Bilgilendirme",mailBody1+mailBody2,"servis@elektrosermebel.com",mailReceiver, attachPad );
                        dialog.dismiss();

                    } catch (Exception e){
                        Log.e("mylog", "Error: " + e.getMessage());
                    }
                }
            } );
            sender.start();
            mDnoText.setText( "" );
            munvanText.setText("" );
            mbolgeText.setText( "" );
            mMarkaText.setText( "" );
            mModelText.setText( "" );
            mseriNoText.setText( "" );
            msbSayacText.setText( "0" );
            mclSayacText.setText( "0" );
            msonClSayacText.setText( "" );
            msonSbSayacText.setText("");
            mbulText.setText( "" );
            mMailTo.setText("");
            mYetkili.setText("");
            maciklamaText.setText("");
            mKayButton.setVisibility(View.INVISIBLE);
            mMailButton.setVisibility(View.INVISIBLE);
            sesCal.play( calTubularID, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, NO_LOOP, NORMAL_PLAY_RATE );
            Toast.makeText(MainActivity.this, mailYetkili+"  KİŞİSİNE MAIL GÖNDERİLDİ.", Toast.LENGTH_LONG).show();


        }
    } );
   // **************************************************************************************************
    } // On Create


    public void insertRecord (View view) throws ParseException {
        // Girilen değerlerin querye yazılmadan önce değişkenlere atanması
        final String yDosyaNo = mDnoText.getText().toString();
        final String yStAciklama = mArizaSpinner.getSelectedItem().toString();
        final String yIslem = mislemSpinner.getSelectedItem().toString();
        final String yTeknisyen = mTekTextView.getText().toString();
        final String yisTipi = misTipiSpinner.getSelectedItem().toString();
        final String yDate =  mdateText.getText().toString();
        // Kayıdı yapmadan önce son sayaç ile girilen sayacın karşılaştırılıp dögünün kırılması
        if (yDosyaNo.matches("" )){
            kTopsay = 0;
            ksonSbSayac = 0;
            sesCal.play(calTubularID, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, NO_LOOP, NORMAL_PLAY_RATE);
            Toast.makeText( MainActivity.this,"MAKİNEYİ BULMADAN KAYIT YAPAMAZSINIZ",Toast.LENGTH_LONG ).show();
            return;
        } else {
            kTopsay = Integer.valueOf( msbSayacText.getText().toString());
            ksonSbSayac = Integer.valueOf( msonSbSayacText.getText().toString());
            kClSayac = Integer.valueOf(mclSayacText.getText().toString());
            kSonClSayac = Integer.valueOf(msonClSayacText.getText().toString());

        }

        // Girilen ve so sayaçlarda farklılık kontrolü ve kullanıcının uyarılması
        if (kTopsay < ksonSbSayac){
            sesCal.play(korgID, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, NO_LOOP, NORMAL_PLAY_RATE);
            Toast.makeText( MainActivity.this," SB SAYAÇ DÜŞÜK, SAYACI DÜZELTİN",Toast.LENGTH_LONG ).show();
            return;
        }
        if (kTopsay == ksonSbSayac){
            sesCal.play(korgID, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, NO_LOOP, NORMAL_PLAY_RATE);
            Toast.makeText( MainActivity.this," SB SAYAÇ BİR ÖNCEKİ İLE AYNI OLAMAZ",Toast.LENGTH_LONG ).show();
            return;
        }
        if (!Objects.equals(mMakTip, "RENKLİ")) {
            if (kClSayac != 0) {
                sesCal.play( korgID, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, NO_LOOP, NORMAL_PLAY_RATE );
                Toast.makeText( MainActivity.this, "SİYAH BEYAZ MAKİNEYE RENKLİ SAYAÇ GİRDİNİZ,DÜZELTİN", Toast.LENGTH_LONG ).show();
                return;
            }
        }

        if (mMakTip.equals( "RENKLİ" )) if (kClSayac < kSonClSayac) {
            sesCal.play(korgID, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, NO_LOOP, NORMAL_PLAY_RATE);
            Toast.makeText(MainActivity.this, "RENKLİ SAYAÇ DÜŞÜK, SAYACI DÜZELTİN", Toast.LENGTH_LONG).show();
            return;
        }
        // *****************************************************************************
        //yDate tarihinin formatını değiştirmek için. Bu Sql de date girişinin yyyy.mm.dd olmasından gerekli oldu.
        SimpleDateFormat formatter = new SimpleDateFormat( "dd-MM-yyyy" );
        Date sDate = formatter.parse( yDate );
        final String nDate = new SimpleDateFormat( "yyyy-MM-dd" ).format( sDate );
        final String ySbSayac = msbSayacText.getText().toString();
        final String yTopSayac = msbSayacText.getText().toString();// Toplam sayaca da aynı değeri yazıyorum çünkü TS programında öyle gerekli
        final String yClSayac = mclSayacText.getText().toString();
        final String yNotes = maciklamaText.getText().toString();
        final String yNotesUpperCase = yNotes.toUpperCase();


        AlertDialog.Builder kayit = new AlertDialog.Builder( MainActivity.this );
        kayit.setPositiveButton( "TAMAM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //ESAS KAYIT SORGUSU  ************************************************
                if (connection!= null){
                    Statement statement = null;
                    try {
                        statement = connection.createStatement();

                        int resultSet  = statement.executeUpdate(
                                "INSERT INTO RAPOR ([RAPOR TARİHİ],[DOSYA NO],SAYAÇ,[SAYAÇ 2],TOPSAY, " +
                                        "[STANDART AÇIKLAMA], İŞLEM, NOTLAR, TEKNİSYEN, DEVELOPER,DRUM,BLADE)" +
                                        " VALUES ('"
                                        +nDate+"','"+yDosyaNo+"','"+ySbSayac+"','"+yClSayac+"','"+yTopSayac+"','"
                                        +yStAciklama+"','"+yIslem+"','"+yNotesUpperCase+"','"+yTeknisyen+"','"+yisTipi+"','"+mcariKod+"','ANDROID');"

                        );
                        sesCal.play(cyrstalID, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, NO_LOOP, NORMAL_PLAY_RATE);
                        Toast.makeText( MainActivity.this,"RAPOR BAŞARIYLA KAYIT EDİLDİ, EMAIL GÖNDERMEYİ UNUTMAYINIZ",Toast.LENGTH_SHORT ).show();
                        mMailButton.setVisibility(View.VISIBLE);
                        mKayButton.setVisibility(View.INVISIBLE);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        sesCal.play(calTubularID, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, NO_LOOP, NORMAL_PLAY_RATE);
                        Toast.makeText( MainActivity.this,"RAPOR KAYIT EDİLEMEDİ, TEKRAR DENEYİN",Toast.LENGTH_SHORT ).show();
                    }

                }



            }
        } )

                .setCancelable( false )
                .setNegativeButton( "İPTAL",null )
                .setMessage( "!!KAYIT ETMEK İSTİYORMUSUNUZ!!" )
                .show();

    }// kayıt




    // mAriza spinnerda bir değer seçilince veya seçilmeyince yapılacaklar.
    // gerekli olursa doldurulacak.
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}