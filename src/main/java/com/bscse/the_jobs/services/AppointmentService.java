package com.bscse.the_jobs.services;


import com.bscse.the_jobs.config.FirebaseInitialization;
import com.bscse.the_jobs.interfaces.AppointmentCreator;
import com.bscse.the_jobs.models.Appointment;
import com.bscse.the_jobs.models.ConsultantAvailability;
import com.bscse.the_jobs.models.Email;
import com.bscse.the_jobs.models.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class AppointmentService implements AppointmentCreator {
    private final Firestore firestore;
    private static final String APPOINTMENT_COLLECTION_NAME = "appointments";
    private static final String AVAILABILITY_COLLECTION_NAME = "availability";
    private static final String MSG_BODY = "You have successfully registered with The Jobs consultation service!";
    private static final String MSG_SUBJECT = "The Jobs - Registration Completed!";

    public AppointmentService() {
        firestore = FirestoreClient.getFirestore(FirebaseInitialization.getFirebaseApp());
    }





    @Override
    public boolean makeAppointment(Appointment appointment) {
        try {
            CollectionReference inq = firestore.collection(APPOINTMENT_COLLECTION_NAME);
            inq.add(appointment);
            return true;
        }catch (FirestoreException e){
            throw new RuntimeException(e);
        }
    }





    public List<Appointment> getAppointmentByConsultant(String scheduleDate, String nic) throws ExecutionException, InterruptedException {

        //-------Fetch all appointments by scheduled date and time
        
        CollectionReference availability = firestore.collection(AVAILABILITY_COLLECTION_NAME);
        CollectionReference appointment = firestore.collection(APPOINTMENT_COLLECTION_NAME);

        Query query1 = availability.whereEqualTo("date", scheduleDate).whereEqualTo("nic", nic);
        Query query2 = appointment.whereEqualTo("scheduledDate", scheduleDate);

        QuerySnapshot querySnapshot1 = query1.get().get();
        QuerySnapshot querySnapshot2 = query2.get().get();

        List<ConsultantAvailability> availabilityResult = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot1.getDocuments()) {
            ConsultantAvailability consultantAvailability = document.toObject(ConsultantAvailability.class);
            availabilityResult.add(consultantAvailability);
        }


        List<Appointment> filteredAppointment = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot2.getDocuments()) {
            Appointment appointments = document.toObject(Appointment.class);
            filteredAppointment.add(appointments);
        }

        List<Appointment> matchingAppointments = filterAppointmentsByAvailability(filteredAppointment, availabilityResult);

        return matchingAppointments;
    }




    public List<Appointment> filterAppointmentsByAvailability(List<Appointment> filteredAppointment, List<ConsultantAvailability> availabilityResult) {
        List<Appointment> matchingAppointments = new ArrayList<>();

        // Base case: If there are no more available times to check, return an empty list
        if (availabilityResult.isEmpty()) {
            return matchingAppointments;
        }

        // Get the first available time from availabilityResult
        Map<String, Boolean> availableTime = availabilityResult.get(0).getAvailableTime();

        // Iterate through each appointment
        Iterator<Appointment> iterator = filteredAppointment.iterator();
        while (iterator.hasNext()) {
            Appointment appointment = iterator.next();

            // Extract the scheduledTime from the appointment
            Map<String, Boolean> scheduledTime = appointment.getScheduledTime();

            // Check if the appointment's scheduledTime matches the availableTime
            if (scheduledTime != null && matchesScheduledTime(scheduledTime, availableTime)) {
                matchingAppointments.add(appointment);
                iterator.remove(); // Remove the matched appointment from the filtered list
            }
        }

        // Recursively call the function with the remaining available times
        matchingAppointments.addAll(filterAppointmentsByAvailability(filteredAppointment, availabilityResult.subList(1, availabilityResult.size())));

        return matchingAppointments;
    }

    private boolean matchesScheduledTime(Map<String, Boolean> scheduledTime, Map<String, Boolean> availableTime) {
        for (Map.Entry<String, Boolean> entry : availableTime.entrySet()) {
            String timeSlot = entry.getKey();
            boolean isAvailable = entry.getValue();

            // Check if the time slot is in the scheduledTime and matches the availability
            if (scheduledTime.containsKey(timeSlot) && scheduledTime.get(timeSlot) == isAvailable) {
                return true;
            }
        }
        return false;
    }






    public boolean updateAppointmentState(Appointment appointment, String docId){
        //Update appointment state
        ApiFuture<WriteResult> collectionApiFuture = firestore.collection(APPOINTMENT_COLLECTION_NAME).document(docId).set(appointment);
        try {
            collectionApiFuture.get().getUpdateTime().toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return true;
    }





    public List<Appointment> fetchAppointmentByUser(String nic){
        try{
            //Fetch appointment details by nic
            CollectionReference appointment = firestore.collection(APPOINTMENT_COLLECTION_NAME);
            Query query = appointment.whereEqualTo("nic", nic).whereEqualTo("appointmentState", "Approve");
            QuerySnapshot querySnapshot = query.get().get();

            List<Appointment> filteredAppointment = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                Appointment appointments = document.toObject(Appointment.class);
                filteredAppointment.add(appointments);
            }

            return filteredAppointment;
        }catch (FirestoreException e){
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }





}
