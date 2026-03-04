package dataproviders;

import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.List;

public class LoginDataProvider {
    //Login Feature
    @DataProvider(name = "validLoginData")
    public static Object [][] validLoginData(){
        return new Object[][]{
                {"admin","admin"}
        };
    }
    private static void addData(List<Object[]> target, Object[][] source) {
        for (Object[] row : source) {
            target.add(row);
        }
    }

    @DataProvider(name = "invalidAllLogin")
    public static Object[][] invalidAllLogin() {
        List<Object[]> data = new ArrayList<>();

        addData(data, invalidLoginData());
        addData(data, sqlInjections());
        addData(data, specialCharacters());
        addData(data, longString());

        return data.toArray(new Object[0][0]);
    }
    @DataProvider(name = "invalidLoginData")
    public static Object [][] invalidLoginData(){
        return new Object[][]{
                {"wronguser","wronguser"}
        };
    }
    @DataProvider(name = "emptyCredentials")
    public static Object [][] emptyCredentials(){
        return new Object[][]{
                {"",""},
                {"","admin"},
                {"admin",""}
        };
    }
    @DataProvider(name = "specialCharacters")
    public static Object[][] specialCharacters(){
        return new Object[][]{
                {"admin#$", "pass123"},
                {"admin", "pass@#$%"},
                {"<script>alert('xss)</script>","password"}
        };
    }
    @DataProvider(name = "sqlInjections")
    public static Object[][] sqlInjections(){
        return new Object[][]{
                {"admin' OR '1'='1", "anything"},
                {"admin'--", "password"},
                {"' OR 1=1--", "' OR 1=1--"}
        };
    }
    @DataProvider(name = "longString")
    public static Object[][] longString(){
        return new Object[][] {
                {"a".repeat(100), "password"},
                {"admin","p".repeat(100)}
        };
    }
}
