import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.DistributedObjectEvent;
import com.hazelcast.core.DistributedObjectListener;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.cluster.MembershipEvent;
import com.hazelcast.cluster.MembershipListener;
import com.hazelcast.partition.MigrationListener;
import com.hazelcast.partition.MigrationState;
import com.hazelcast.partition.PartitionService;
import com.hazelcast.partition.ReplicaMigrationEvent;
import com.hazelcast.map.listener.EntryAddedListener;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Listner {
    private final static String DZIEKANAT_NAME = "weaii";

    public static void main(String[] args) throws UnknownHostException {
        Config config = HConfig.getConfig();

        HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);

        instance.addDistributedObjectListener(new DistributedObjectListener() {

            @Override
            public void distributedObjectDestroyed(DistributedObjectEvent e) {
                System.out.println(e);
            }

            @Override
            public void distributedObjectCreated(DistributedObjectEvent e) {
                System.out.println(e);
            }
        });

        instance.getCluster().addMembershipListener(new MembershipListener() {

            @Override
            public void memberRemoved(MembershipEvent e) {
                System.out.println(e);
            }

            @Override
            public void memberAdded(MembershipEvent e) {
                System.out.println(e);
            }
        });

        PartitionService partitionService = instance.getPartitionService();
        partitionService.addMigrationListener(new MigrationListener() {

            @Override
            public void replicaMigrationFailed(ReplicaMigrationEvent e) {
                System.out.println(e);
            }

            @Override
            public void replicaMigrationCompleted(ReplicaMigrationEvent e) {
                System.out.println(e);
            }

            @Override
            public void migrationStarted(MigrationState s) {
                System.out.println(s);
            }

            @Override
            public void migrationFinished(MigrationState s) {
                System.out.println(s);
            }
        });

        IMap<Long, Dziekanat> clinics = instance.getMap(DZIEKANAT_NAME);

        clinics.addEntryListener((EntryAddedListener<Long, Dziekanat>) (EntryEvent<Long, Dziekanat> e) -> {
            System.out.println(e);
        }, true);

    }
}
class HConfig {

    public static Config getConfig() throws UnknownHostException {
        Config config = new Config();
        NetworkConfig network = config.getNetworkConfig();
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig().addMember(getIPAddress()).setEnabled(true);
        return config;
    }

    public static ClientConfig getClientConfig() throws UnknownHostException {
        ClientConfig config = new ClientConfig();
        ClientNetworkConfig network = config.getNetworkConfig();
        network.addAddress(getIPAddress());
        return config;
    }

    public static String getIPAddress() throws UnknownHostException {
        String ip = InetAddress.getLocalHost().getHostAddress();
        System.out.println("My IP Address: " + ip);
        return ip;
    }
}
