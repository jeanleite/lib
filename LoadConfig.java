package utils;

import bean.Config;
import bean.Credential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LoadConfig {
    private static final String filePath = "Config.json";

    public static Config loadConfig() {
        Config config = null;
        List<Credential> list = new ArrayList<>();
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            config = gson.fromJson(br, Config.class);
            return decrypt(config);
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        return null;
    }

    public static Config decrypt(Config encryptConfig) {
        boolean rewritefile = false;
        Config decryptConfig = new Config();
        String host = Cryptography.decrypt(encryptConfig.getHost());
        String database = Cryptography.decrypt(encryptConfig.getDatabase());
        String usernameDB = Cryptography.decrypt(encryptConfig.getUsernameDB());
        String passwdDB = Cryptography.decrypt(encryptConfig.getPasswdDB());
        String view = Cryptography.decrypt(encryptConfig.getView());
        String matricula = Cryptography.decrypt(encryptConfig.getMatricula());
        String email = Cryptography.decrypt(encryptConfig.getEmail());
        String to = Cryptography.decrypt(encryptConfig.getTo());
        if (host == null) {
            host = encryptConfig.getHost();
            encryptConfig.setHost(Cryptography.encrypt(encryptConfig.getHost()));
            rewritefile = true;
        }
        if (database == null) {
            database = encryptConfig.getDatabase();
            encryptConfig.setDatabase(Cryptography.encrypt(encryptConfig.getDatabase()));
            rewritefile = true;
        }
        if (usernameDB == null) {
            usernameDB = encryptConfig.getUsernameDB();
            encryptConfig.setUsernameDB(Cryptography.encrypt(encryptConfig.getUsernameDB()));
            rewritefile = true;
        }
        if (passwdDB == null) {
            passwdDB = encryptConfig.getPasswdDB();
            encryptConfig.setPasswdDB(Cryptography.encrypt(encryptConfig.getPasswdDB()));
            rewritefile = true;
        }
        if (matricula == null) {
            matricula = encryptConfig.getMatricula();
            encryptConfig.setMatricula(Cryptography.encrypt(encryptConfig.getMatricula()));
            rewritefile = true;
        }
        if (email == null) {
            email = encryptConfig.getEmail();
            encryptConfig.setEmail(Cryptography.encrypt(encryptConfig.getEmail()));
            rewritefile = true;
        }
        if (to == null) {
            to = encryptConfig.getTo();
            encryptConfig.setTo(Cryptography.encrypt(encryptConfig.getTo()));
            rewritefile = true;
        }
        if (view == null) {
            view = encryptConfig.getView();
            encryptConfig.setView(Cryptography.encrypt(encryptConfig.getView()));
            rewritefile = true;
        }
        decryptConfig.setHost(host);
        decryptConfig.setView(view);
        decryptConfig.setTo(to);
        decryptConfig.setMatricula(matricula);
        decryptConfig.setPasswdDB(passwdDB);
        decryptConfig.setUsernameDB(usernameDB);
        decryptConfig.setDatabase(database);
        decryptConfig.setEmail(email);
        List<Credential> listCredentials = new ArrayList<Credential>();
        if (encryptConfig.getCredentials() != null) {
            for (Credential e : encryptConfig.getCredentials()) {
                Credential credential = new Credential();
                String user = Cryptography.decrypt(e.getUser());
                String pass = Cryptography.decrypt(e.getPass());
                if (user == null) {
                    user = e.getUser();
                    e.setUser(Cryptography.encrypt(user));
                    rewritefile = true;
                }
                if (pass == null) {
                    pass = e.getPass();
                    e.setPass(Cryptography.encrypt(pass));
                    rewritefile = true;
                }
                credential.setPortal(e.getPortal());
                credential.setUser(user);
                credential.setPass(pass);
                listCredentials.add(credential);
            }
        }
        decryptConfig.setCredentials(listCredentials);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (rewritefile) {
            String json = gson.toJson(encryptConfig);
            try {
                FileWriter fw = new FileWriter(filePath, false);
                fw.write(json);
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(rewritefile);
        System.out.println(gson.toJson(decryptConfig));
        System.out.println(gson.toJson(encryptConfig));
        return decryptConfig;
    }
}
