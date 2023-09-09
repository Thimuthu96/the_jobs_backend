package com.bscse.the_jobs.services;


import com.bscse.the_jobs.config.FirebaseInitialization;
import com.bscse.the_jobs.interfaces.AuthService;
import com.bscse.the_jobs.models.*;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import com.google.firebase.auth.UserRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService implements AuthService {
    private final Firestore firestore;
    private final FirebaseAuth firebaseAuth;
    private static final String USER_COLLECTION_NAME = "users";
    private static final String JOBSEEKER_COLLECTION_NAME = "jobseeker";
    private static final String CONSULTANT_COLLECTION_NAME = "consultant";
    private static final String CONSULTANT_AVAILABILITY_COLLECTION_NAME = "availability";




    public UserService() {
        firestore = FirestoreClient.getFirestore(FirebaseInitialization.getFirebaseApp());
        firebaseAuth = FirebaseAuth.getInstance(FirebaseInitialization.getFirebaseApp());
    }



    public void addJobseekerData(JobSeeker jobSeeker){
        //POST data into users and job-seekers collections
        try {
            CollectionReference jobseeker = firestore.collection(JOBSEEKER_COLLECTION_NAME);
            jobseeker.add(jobSeeker);
        }catch (FirestoreException e){
            throw new RuntimeException(e);
        }
    }





    public void addConsultantData(Consultant consultant){
        //POST data into users and consultants collections
        try {
            CollectionReference consultantRef = firestore.collection(CONSULTANT_COLLECTION_NAME);
            consultantRef.add(consultant);
        }catch (FirestoreException e){
            throw new RuntimeException(e);
        }
    }





    public void addConsultantAvailability(ConsultantAvailability consultantAvailability){
        //POST data into consultant availability
        try{
            CollectionReference consultantAvailabilityRef = firestore.collection(CONSULTANT_AVAILABILITY_COLLECTION_NAME);
            consultantAvailabilityRef.add(consultantAvailability);
        }catch (FirestoreException e){
            throw new RuntimeException(e);
        }
    }





    @Override
    public boolean signup(String email, String password) {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);

            UserRecord userRecord = firebaseAuth.createUser(request);
            return true;
        }catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
    }





    @Override
    public boolean checkJobSeekerUser(String email) {
        try{
            CollectionReference users = firestore.collection(JOBSEEKER_COLLECTION_NAME);

            // Create a query to filter users by email and user_role
            Query query = users.whereEqualTo("email", email);

            QuerySnapshot querySnapshot = query.get().get();

            List<User> filteredUsers = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                User user = document.toObject(User.class);
                filteredUsers.add(user);
            }

            if (filteredUsers.isEmpty()){
                return false;
            }else {
                return true;
            }
        }catch (FirestoreException | InterruptedException | ExecutionException e){
            return false;
        }
    }





    @Override
    public boolean checkConsultantUser(String email) {
        try{
            CollectionReference users = firestore.collection(CONSULTANT_COLLECTION_NAME);

            // Create a query to filter users by email and user_role
            Query query = users.whereEqualTo("email", email);

            QuerySnapshot querySnapshot = query.get().get();

            List<User> filteredUsers = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                User user = document.toObject(User.class);
                filteredUsers.add(user);
            }

            if (filteredUsers.isEmpty()){
                return false;
            }else {
                return true;
            }
        }catch (FirestoreException | InterruptedException | ExecutionException e){
            return false;
        }
    }





    @Override
    public boolean checkIfRegistered(String nic) {
        //Check whether user account already available or not
        try{
            CollectionReference users = firestore.collection(JOBSEEKER_COLLECTION_NAME);

            // Create a query to filter users by nic
            Query query = users.whereEqualTo("nic", nic);

            QuerySnapshot querySnapshot = query.get().get();

            List<User> filteredUsers = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                User user = document.toObject(User.class);
                filteredUsers.add(user);
            }

            if (filteredUsers.isEmpty()){
                return false;
            }else {
                return true;
            }
        }catch (FirestoreException | InterruptedException | ExecutionException e){
            return false;
        }
    }





    public boolean updateConsultantState(String nic){

        try {

            Query query = firestore.collection(CONSULTANT_COLLECTION_NAME).whereEqualTo("nic", nic);
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

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return true;
    }





    public boolean updateConsultantData(Consultant consultant){

        try {

            Query query = firestore.collection(CONSULTANT_COLLECTION_NAME).whereEqualTo("nic", consultant.getNic());
            QuerySnapshot querySnapshot = query.get().get();

            // Check if the query returned any documents
            if (!querySnapshot.isEmpty()) {
                for (QueryDocumentSnapshot document : querySnapshot) {
                    // Update the document with the new values
                    DocumentReference documentRef = firestore.collection(CONSULTANT_COLLECTION_NAME).document(document.getId());
                    ApiFuture<WriteResult> updateFuture = documentRef.update(
                            "contactNumber", consultant.getContactNumber(),
                            "name", consultant.getName(),
                            "specializeJobField", consultant.getSpecializeJobField()
                    );
                    updateFuture.get(); // Wait for the update to complete
                }
            } else {
                return false;
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return true;
    }





    public List<Consultant> getConsultantData(String nic) throws ExecutionException, InterruptedException {

        //-------Fetch consultant data by nic
        List<Consultant> consultant = new ArrayList<>();
        try {
            CollectionReference consultantData = firestore.collection(CONSULTANT_COLLECTION_NAME);
            Query query1 = consultantData.whereEqualTo("nic", nic);
            QuerySnapshot querySnapshot1 = query1.get().get();


            for (QueryDocumentSnapshot document : querySnapshot1.getDocuments()) {
                Consultant consultantInfo = document.toObject(Consultant.class);
                consultant.add(consultantInfo);
            }
        }catch (FirestoreException e){
            e.printStackTrace();
        }

        return consultant;
    }





}
