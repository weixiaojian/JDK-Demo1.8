import java.sql.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author langao_q
 * @create 2020-04-16 10:48
 */
public class Test{

    public static volatile Test test = null;

    public static Test getTest(){
        if(test == null){
            synchronized(Test.class){
                if(test == null){
                     return new Test();
                }
            }
        }
        return test;
    }

    public static void main(String[] args) {
        System.out.println(getTest());
        System.out.println(getTest());
        System.out.println(getTest());
        System.out.println(getTest());
        System.out.println(getTest());
        System.out.println(getTest());
    }

}
