package homework;


import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private TreeMap<Customer, String> map = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        return getEntryWithKeyClone(map.firstKey());
    }

    private Map.Entry<Customer, String> getEntryWithKeyClone(Customer key) {
        if (key == null) return null;
        Customer keyClone = new Customer(key);
        return new AbstractMap.SimpleEntry<>(keyClone, map.get(key));
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return getEntryWithKeyClone(map.higherKey(customer));
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
