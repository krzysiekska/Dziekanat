import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.map.IMap;
import com.hazelcast.map.EntryProcessor;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

public class Operation{
    final private static Long pid = Long.decode(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]) << 32;
    final private static Random r = new Random(pid);
    private final Map<Long, Dziekanat> Dziekanats;
    private final static String DziekanatS_NAME = "Dziekanats";

    private final HazelcastInstance client;

    public Operation() {
        Config cfg = new Config();
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
        Dziekanats = instance.getMap(DziekanatS_NAME);

        ClientConfig clientConfig = new ClientConfig();
        client = HazelcastClient.newHazelcastClient(clientConfig);
    }

    public Long addDziekanat(Dziekanat Dziekanat) {
        Long key = Math.abs(pid + r.nextInt());
        System.out.println("PUT " + key + " => " + Dziekanat);
        Dziekanats.put(key, Dziekanat);
        return key;
    }

    public void edit() {
        IMap<Long, Dziekanat> DziekanatsIMap = client.getMap(DziekanatS_NAME);
        DziekanatsIMap.executeOnEntries(new HEntryProcessor());
    }

    public void delDziekanat(Long key) {
        Dziekanats.remove(key);
    }

    public void showDziekanat() {
        System.out.println("All Dziekanats: ");
        Dziekanats.entrySet().forEach((e) -> {
            System.out.println(e.getKey() + " => " + e.getValue());
        });
    }

    public void executor() {
        IExecutorService executor = client.getExecutorService("exec");
        executor.submitToAllMembers(new HCallable());
    }

    public void foreachDziekanat() {
        System.out.println("Foreach");
        Dziekanats.entrySet().forEach((e) -> {
            System.out.println(e.getKey() + " => " + e.getValue());
        });
    }


}
class HEntryProcessor implements EntryProcessor<Long, Dziekanat, Object>, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public Object process(Map.Entry<Long, Dziekanat> e) {
        Dziekanat dziekanat = e.getValue();
        String name = dziekanat.getStudentName();
        if (name.equals(name.toLowerCase())) {
            name = name.toUpperCase();
            dziekanat.setStudentName(name);
        } else {
            name = name.toLowerCase();
            dziekanat.setStudentName(name);
        }

        System.out.println("Prcessing name = " + name);
        e.setValue(dziekanat);

        return name;
    }
}

class HCallable implements Callable<Integer>, Serializable, HazelcastInstanceAware {

    private static final long serialVersionUID = 1L;
    private final static String DZIEKANAT_NAME = "weaii";
    private transient HazelcastInstance instance;

    @Override
    public Integer call() {
        IMap<Long, Dziekanat> dziekanat = instance.getMap(DZIEKANAT_NAME);
        Set<Long> keys = dziekanat.localKeySet();
        keys.forEach((k) -> {
            System.out.println("Instance " + instance + " " + k + " => " + dziekanat.get(k));
        });
        return keys.size();
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance instance) {
        this.instance = instance;
    }

}

