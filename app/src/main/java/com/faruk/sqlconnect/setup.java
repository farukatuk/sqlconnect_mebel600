package com.faruk.sqlconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class setup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
private String ipt;
private String usr;
private String pswd;
private String tkname;
private EditText mIpAdress;
private EditText mUserName;
private EditText mPassword;
private Spinner mTekAdiSpinner;
private TextView mipTextView;
private Button mKayitButton;
private TextView mIpTextView;
private TextView mUserNameText;
private TextView mPasswordText;
private TextView mTekNameText;
private TextView mProgAyar;
private TextView mipAdressText2;
private TextView mPasswordText2;
private TextView mTekAdTextView2;
private TextView mPasswordText3;
private TextView mMevcutBaglantıText;
private Button mButton;
static public String ipVer;
static public String userVer;
static public String passwordVer;
static public String tekVer;

public static final String PREFS_NAME = "MyPrefFile"; // Saklanacak değerlerin olduğu dosya

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_setup );
        mIpAdress = findViewById( R.id.setupIpAdress );
        mUserName = findViewById( R.id.setupUserName );
        mPassword = findViewById( R.id.setupPassword );
        mTekAdiSpinner = findViewById( R.id.setupTekAdi );
        mKayitButton = findViewById( R.id.setupKayitButton );
        mIpTextView = findViewById( R.id.ipText );
       // mUserNameText = findViewById( R.id.usrNameText );
        //mPasswordText = findViewById( R.id.passwordText );
        mTekNameText = findViewById( R.id.tekNameText);
        //mProgAyar = findViewById( R.id.progAyarTitle );
        //mipAdressText2 = findViewById( R.id.ipAdressText );
        //mPasswordText2 = findViewById( R.id.passwordText2 );
        //mPasswordText3 = findViewById( R.id.passwordText3 );
        //mMevcutBaglantıText = findViewById( R.id.mevcutBaglantiText );


    // Teknisyen adı sipinner için Array adapter
        ArrayAdapter setupTek = ArrayAdapter.createFromResource(
                this,
                R.array.teknisyen,
                R.layout.color_spinner_layout
        );
    setupTek.setDropDownViewResource( R.layout.color_spinner_layout );
    mTekAdiSpinner.setAdapter( setupTek );
    mTekAdiSpinner.setOnItemSelectedListener ( this );
    // **************************************************************
        SharedPreferences degerAl = getSharedPreferences( PREFS_NAME,0 );
        ipVer = degerAl.getString( "ip",ipt );
        userVer = degerAl.getString( "userName",usr );
        passwordVer = degerAl.getString( "password",pswd );
        tekVer  = degerAl.getString( "tekName",tkname );
        mIpTextView.setText( ipVer );
        mTekNameText.setText(tekVer  );

    } //on create sonu
    public void KayitEt (View view) {
        ipt = mIpAdress.getText().toString();
        usr = mUserName.getText().toString();
        pswd = mPassword.getText().toString();
        tkname = mTekAdiSpinner.getSelectedItem().toString();
        if (ipt.equals( "" ) || usr.equals( "" ) || pswd.equals( "" ) || tkname.equals( "" )) {
            Toast.makeText( getApplicationContext(), "İLGİLİ ALANLARIN HEPSİNİ DOLDURUMADINIZ", Toast.LENGTH_LONG ).show();
            return;
        } else {
            android.app.AlertDialog.Builder uyar = new AlertDialog.Builder( setup.this);
            uyar.setPositiveButton( "TAMAM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences ipSave = getSharedPreferences( PREFS_NAME, 0 );
                    SharedPreferences user = getSharedPreferences( PREFS_NAME, 0 );
                    SharedPreferences password = getSharedPreferences( PREFS_NAME, 0 );
                    SharedPreferences tekname = getSharedPreferences( PREFS_NAME, 0 );
                    SharedPreferences.Editor editor = ipSave.edit();
                    SharedPreferences.Editor editor1 = user.edit();
                    SharedPreferences.Editor editor2 = password.edit();
                    SharedPreferences.Editor editor3 = tekname.edit();
                    editor.putString( "ip", ipt );
                    editor1.putString( "userName", usr );
                    editor2.putString( "password", pswd );
                    editor3.putString( "tekName", tkname );
                    editor.commit();
                    editor1.commit();
                    editor2.commit();
                    editor3.commit();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    finish();
                    startActivity(intent);

                }
            } )
             .setMessage( "DİKKAT KAYIT YAPILIP PROGRAMDAN PROGRAM TEKRAR BAŞLATILACAKTIR"  )
             .setCancelable( true )
             .show();


        }
    }
    // Adapter view için ek kodlar kendisi ekliyor
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    // *******************************************************************

}