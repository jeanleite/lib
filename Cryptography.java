package utils;

import org.apache.commons.net.util.Base64;
import org.python.core.__builtin__;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Random;

public class Cryptography {
    private static final String key = "YankBullest2019!";
    private static final String initVector = "BulleST31YankVec";

    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encrypted) {
        if (encrypted == null) {
            return null;
        }
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (javax.crypto.BadPaddingException ex) {
            // Sem erros para essas exceptions
        } catch (javax.crypto.IllegalBlockSizeException ex) {
            // Sem erros para essas exceptions
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static String generateNewPassword(int length) {
        Random random = new Random();
        StringBuilder result = new StringBuilder(length);
        int aux = 0;
        while (result.length() < length) {
            result.append((char) (aux == 0 ? ('A' + random.nextInt(26)) :
                    aux == 1 ? ('a' + random.nextInt(26)) :
                            ('0' + random.nextInt(10))));
            aux = aux == 2 ? 0 : aux + 1;
        }
        System.out.printf("New Password: " + result.toString());
        return result.toString();
    }
}
