package com.bscse.the_jobs.services;

import com.bscse.the_jobs.config.FirebaseInitialization;
import com.bscse.the_jobs.interfaces.SuperAdmin;
import com.bscse.the_jobs.models.Consultant;
import com.bscse.the_jobs.models.ConsultantAvailability;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AdminServices implements SuperAdmin {
    private final Firestore firestore;
    private static final String SUPER_ADMIN_COLLECTION_NAME = "superadmin";
    private static final String CONSULTANT_COLLECTION_NAME = "consultant";


    public AdminServices() {
        firestore = FirestoreClient.getFirestore(FirebaseInitialization.getFirebaseApp());
    }
    @Override
    public boolean checkAuth(String email, String password) throws ExecutionException, InterruptedException {
        CollectionReference superadmin = firestore.collection(SUPER_ADMIN_COLLECTION_NAME);
        Query query1 = superadmin.whereEqualTo("email", email);
        QuerySnapshot querySnapshot1 = query1.get().get();
        List<com.bscse.the_jobs.models.SuperAdmin> superdminResults = new ArrayList<>();
        if (superdminResults.isEmpty()){
            return false;
        }else if(superdminResults.get(0).getPassword() == password){
            return true;
        }
        return false;
    }

    @Override
    public boolean approveConsultant(String email) throws ExecutionException, InterruptedException {
        // Find the document based on "CONSULTANT EMAIL"
        Query query = firestore.collection(CONSULTANT_COLLECTION_NAME).whereEqualTo("email", email);
        QuerySnapshot querySnapshot = query.get().get();

        // Check if the query returned any documents
        if (!querySnapshot.isEmpty()) {
            for (QueryDocumentSnapshot document : querySnapshot) {
                // Update the document with the new values
                DocumentReference documentRef = firestore.collection(CONSULTANT_COLLECTION_NAME).document(document.getId());
                ApiFuture<WriteResult> updateFuture = documentRef.update(
                        "accountState", "Approved"
                );
                updateFuture.get(); // Wait for the update to complete
            }
        } else {
            return false;
        }


        return false;
    }
}
