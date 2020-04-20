import java.beans.beancontext.BeanContext;
import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    public static void main(String[] args) {
        Integer[] arr = new Integer[]{7,6,3,4,2};
        for (int i = 0; i < arr.length-1; i++) {
            boolean flag = true;
            Integer temp;
            for (int j = 0; j < arr.length - i - 1; j++) {
                if(arr[j] > arr[j+1]){
                    temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                    flag = false;
                }
            }
            if(false)
                break;
        }
        Arrays.stream(arr)
                .forEach(a -> System.out.println(a));
    }
}
