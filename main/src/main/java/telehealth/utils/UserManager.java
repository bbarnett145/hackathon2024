package telehealth.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import telehealth.models.Reminder;
import telehealth.models.User;
import telehealth.models.Doctor;
import telehealth.models.Message;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UserManager {
    private static final String USERS_FILE = "users.json";
    private static final String DOCTOR_PATIENT_MAP_FILE = "doctorPatientMap.json";
    private static final String MESSAGES_FILE = "messages.json";
    private static Map<String, User> users = new HashMap<>();
    private static Map<String, Set<String>> doctorPatientMap = new HashMap<>();
    private static Map<String, Set<Reminder>> patientReminders = new HashMap<>();
    private static Map<String, List<Message>> messages = new HashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        loadUsers();
        loadDoctorPatientMap();
        loadMessages();
    }

    public static void addUser(User user) {
        loadUsers();
        if (user.getEmail() != null) {
            users.put(user.getEmail(), user);
            saveUsers();
        }
        if (user instanceof Doctor) {
            doctorPatientMap.put(user.getEmail(), new HashSet<>());
        } else {
            patientReminders.put(user.getEmail(), new HashSet<>());
        }
        saveUsers();
    }

    public static User getUserByEmail(String email) {
        return users.get(email);
    }

    public static void associatePatientWithDoctor(String patientEmail, String doctorEmail) {
        loadDoctorPatientMap();
        doctorPatientMap.computeIfAbsent(doctorEmail, k -> new HashSet<>()).add(patientEmail);
        saveDoctorPatientMap();
    }

    public static void unassociatePatientWithDoctor(String patientEmail, String doctorEmail) {
        loadDoctorPatientMap();
        Set<String> patients = doctorPatientMap.get(doctorEmail);
        if (patients != null) {
            patients.remove(patientEmail);
            if (patients.isEmpty()) {
                doctorPatientMap.remove(doctorEmail);
            }
            saveDoctorPatientMap();
        }
    }

    public static Set<String> getPatientsForDoctor(String doctorEmail) {
        loadDoctorPatientMap();
        return doctorPatientMap.getOrDefault(doctorEmail, new HashSet<>());
    }
    
    public static Set<String> getDoctorsForPatient(String patientEmail) {
        loadDoctorPatientMap();
        return doctorPatientMap.entrySet().stream()
                .filter(entry -> entry.getValue().contains(patientEmail))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public static void addReminderToPatient(String patientEmail, Reminder reminder) {
        if (patientReminders.containsKey(patientEmail)) {
            patientReminders.get(patientEmail).add(reminder);
        }
        saveData();
    }

    public static void removeReminderFromPatient(String patientEmail, Reminder reminder) {
        if (patientReminders.containsKey(patientEmail)) {
            patientReminders.get(patientEmail).remove(reminder);
        }
        saveData();
    }

    public static Set<Reminder> getRemindersForPatient(String patientEmail) {
        return patientReminders.getOrDefault(patientEmail, new HashSet<>());
    }

    public static Collection<User> getAllUsers() {
        return users.values();
    }

    public static List<String> getAllDoctorEmails() {
        loadUsers();
        return users.values().stream()
                .filter(user -> "Doctor".equals(user.getRole()))
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    private static void saveUsers() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(USERS_FILE), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadUsers() {
        try {
            File file = new File(USERS_FILE);
            if (file.exists()) {
                ObjectMapper mapper = new ObjectMapper();
                users = mapper.readValue(file, new TypeReference<Map<String, User>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadDoctorPatientMap() {
        try {
            File file = new File(DOCTOR_PATIENT_MAP_FILE);
            if (file.exists()) {
                ObjectMapper mapper = new ObjectMapper();
                doctorPatientMap = mapper.readValue(file, new TypeReference<Map<String, Set<String>>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveDoctorPatientMap() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(DOCTOR_PATIENT_MAP_FILE), doctorPatientMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadMessages() {
        try {
            File file = new File(MESSAGES_FILE);
            if (file.exists()) {
                ObjectMapper mapper = new ObjectMapper();
                messages = mapper.readValue(file, new TypeReference<Map<String, List<Message>>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveMessages() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(MESSAGES_FILE), messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveData() {
        try {
            objectMapper.writeValue(new File(USERS_FILE), users);
            objectMapper.writeValue(new File(DOCTOR_PATIENT_MAP_FILE), doctorPatientMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData() {
        try {
            File usersFile = new File(USERS_FILE);
            File doctorPatientMapFile = new File(DOCTOR_PATIENT_MAP_FILE);
            if (usersFile.exists()) {
                users = objectMapper.readValue(usersFile, new TypeReference<Map<String, User>>() {});
            }
            if (doctorPatientMapFile.exists()) {
                doctorPatientMap = objectMapper.readValue(doctorPatientMapFile, new TypeReference<Map<String, Set<String>>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User verifyLogin(String email, String password) {
        loadUsers();
        User user = users.get(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public static void sendMessage(String fromEmail, String toEmail, String messageText) {
        if (fromEmail == null || toEmail == null) {
            throw new IllegalArgumentException("Email addresses cannot be null");
        }
        loadMessages();
        String conversationKey = getConversationKey(fromEmail, toEmail);
        messages.computeIfAbsent(conversationKey, k -> new ArrayList<>()).add(new Message(fromEmail, toEmail, messageText));
        saveMessages();
    }

    public static List<Message> getMessages(String fromEmail, String toEmail) {
        if (fromEmail == null || toEmail == null) {
            throw new IllegalArgumentException("Email addresses cannot be null");
        }
        loadMessages();
        String conversationKey = getConversationKey(fromEmail, toEmail);
        return messages.getOrDefault(conversationKey, new ArrayList<>());
    }

    private static String getConversationKey(String email1, String email2) {
        if (email1 == null || email2 == null) {
            throw new IllegalArgumentException("Email addresses cannot be null");
        }
        return email1.compareTo(email2) < 0 ? email1 + "-" + email2 : email2 + "-" + email1;
    }
}
