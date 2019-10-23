package com.lenovoexample.tracingvf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class BorrarSupervisorActivity extends AppCompatActivity {

    EditText eCorreo;
    Button bButon;
    int bandera = 0;


    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("supervisors");

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar_supervisor);

        eCorreo = findViewById(R.id.eCorreo);
        progressDialog = new ProgressDialog(this);
        bButon = findViewById(R.id.bButon);

        bButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarSupervisor();
                if(bandera == 0){
                    Toast.makeText(getApplicationContext(), "Ingrese un usuario valido", Toast.LENGTH_LONG).show();
                }else{
                    bandera = 0;
                }
            }
        });
    }

    private void borrarSupervisor() {
        String correo = eCorreo.getText().toString().trim();
        Query q = userRef.orderByChild("email").equalTo(correo);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                    String clave = datasnapshot.getKey();
                    userRef.child(clave).removeValue();
                    progressDialog.setMessage("Borrando");
                    progressDialog.show();
                    Intent intent = new Intent(BorrarSupervisorActivity.this, AdminActivity.class);
                    startActivity(intent);
                    bandera = 1;
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        progressDialog.dismiss();
    }
}
