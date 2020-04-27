import java.beans.beancontext.BeanContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    public static ArrayList<Integer> GetLeastNumbers_Solution(int [] input, int k) {
        List<Integer> list = new ArrayList();
        list.stream()
                .sorted((l1,l2)->l1-l2)
                .forEach(System.out::println);
        int asInt = Arrays.stream(input).sorted().findAny().getAsInt();
        System.out.println(asInt);
        return null;
    }
    
    public static void main(String[] args) {
        System.out.println(GetLeastNumbers_Solution(new int[]{2, 7, 1}, 2));
    }
}
