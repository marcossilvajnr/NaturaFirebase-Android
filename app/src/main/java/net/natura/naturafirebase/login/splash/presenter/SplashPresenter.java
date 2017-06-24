package net.natura.naturafirebase.login.splash.presenter;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import net.natura.naturafirebase.base.model.UserDataModel;
import net.natura.naturafirebase.base.presenter.BasePresenter;
import net.natura.naturafirebase.login.splash.SplashContract;

/**
 * Created by marcos on 03/06/17.
 */

public class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter {

    @Override
    public void verifySignin(FirebaseUser firebaseUser) {
        if (view != null) {
            if (firebaseUser != null) {
                final Uri photoUrl = firebaseUser.getPhotoUrl();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = database.getReference("users").child(firebaseUser.getUid());

                if (databaseReference != null) {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserDataModel userDataModel = dataSnapshot.getValue(UserDataModel.class);

                            if (userDataModel != null) {
                                userDataModel.setDeviceToken(FirebaseInstanceId.getInstance().getToken());
                                databaseReference.setValue(userDataModel);

                                if (userDataModel.getUserPhotoUrl() != null && !userDataModel.getUserPhotoUrl().isEmpty()) {
                                    view.openMain();
                                } else {
                                    if (photoUrl != null) {
                                        userDataModel.setUserPhotoUrl(photoUrl.toString());
                                        databaseReference.setValue(userDataModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                view.openMain();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                view.openPhotoRegister();
                                            }
                                        });
                                    } else {
                                        view.openPhotoRegister();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            if (view != null) {
                                view.openMain();
                            }
                        }
                    });
                }
            } else {
                view.openLogin();
            }
        }
    }
}
