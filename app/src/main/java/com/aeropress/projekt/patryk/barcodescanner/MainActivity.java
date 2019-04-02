package com.aeropress.projekt.patryk.barcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView barcode;
    private EditText name;
    private EditText cal;
    private EditText car;
    private EditText fat;
    private EditText prot;
    private Button button;

    private IntentIntegrator scanner;
    private DBHelper dbHelper;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_notifications:
                    scanner.initiateScan();
                    return true;
            }
            return false;
        }
    };
    private void clearInput()
    {
        name.setText("");
        cal.setText("");
        car.setText("");
        fat.setText("");
        prot.setText("");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        IntentResult result = scanner.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,    "Cancelled",Toast.LENGTH_LONG).show();
            } else {
                final Product product = dbHelper.getProductFromDB(result.getContents());
                if(product!=null)
                {
                    Toast.makeText(this,    "Product founded",Toast.LENGTH_LONG).show();
                    clearInput();
                    barcode.setText(product.barcode);
                    name.setText(product.name);
                    cal.setText(String.valueOf(product.cal));
                    car.setText(String.valueOf(product.carbon));
                    fat.setText(String.valueOf(product.fat));
                    prot.setText(String.valueOf(product.protein));
                    button.setText("Edit");
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean flag= isAnyFieldEmpty();
                            if(flag)
                            {
                                dbHelper.updateProductValues(new Product(
                                        barcode.getText().toString(),
                                        name.getText().toString(),
                                        Float.valueOf(cal.getText().toString()),
                                        Float.valueOf(car.getText().toString()),
                                        Float.valueOf(fat.getText().toString()),
                                        Float.valueOf(prot.getText().toString())));
                                Toast.makeText(MainActivity.this,    "Product updated",Toast.LENGTH_LONG).show();
                            }
                            button.setVisibility(View.INVISIBLE);
                        }
                    });
                }else
                {
                    Toast.makeText(this,    "Product don't exist",Toast.LENGTH_LONG).show();
                    barcode.setText(result.getContents());
                    clearInput();
                    button.setVisibility(View.VISIBLE);
                    button.setText("Add");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean flag= isAnyFieldEmpty();
                            if(flag)
                            {
                                dbHelper.insertNewProduct(new Product(
                                        barcode.getText().toString(),
                                        name.getText().toString(),
                                        Float.valueOf(cal.getText().toString()),
                                        Float.valueOf(car.getText().toString()),
                                        Float.valueOf(fat.getText().toString()),
                                        Float.valueOf(prot.getText().toString())));
                                Toast.makeText(MainActivity.this,    "Product added",Toast.LENGTH_LONG).show();
                            }
                            button.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private boolean isAnyFieldEmpty() {
        if(name.getText().length()<4)
        {
            name.setError("nazwa o dlugosci minimum 5");
            return false;
        }
        if(cal.getText().length()==0)
        {
            cal.setError("pole wymagane");
            return false;
        }
        if(car.getText().length()==0)
        {
            car.setError("pole wymagane");
            return false;
        }
        if(fat.getText().length()==0)
        {
            fat.setError("pole wymagane");
            return false;
        }
        if(prot.getText().length()==0)
        {
            prot.setError("pole wymagane");
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barcode = (TextView) findViewById(R.id.barcode);
        name = (EditText) findViewById(R.id.name);
        cal = (EditText) findViewById(R.id.cal);
        car = (EditText) findViewById(R.id.carb);
        fat=(EditText)findViewById(R.id.fats);
        prot = (EditText) findViewById(R.id.prot);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        scanner=new IntentIntegrator(this);
        dbHelper = new DBHelper(MainActivity.this);
        button = (Button)findViewById(R.id.button);
        button.setVisibility(View.INVISIBLE);
        name.clearFocus();
        cal.clearFocus();
        car.clearFocus();
        fat.clearFocus();
        prot.clearFocus();
    }

    private String DecodeProductOrigin(String barcode)
    {

        int originCode=Integer.getInteger(barcode.substring(0,3));
        Pair<Integer,Integer>code=new Pair<>(0,19);
        ArrayList<String> coutries = new ArrayList<>();
        return "aaa";
    }


}
