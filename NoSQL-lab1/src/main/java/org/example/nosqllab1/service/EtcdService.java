package org.example.nosqllab1.service;


import io.etcd.jetcd.*;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.kv.TxnResponse;
import io.etcd.jetcd.op.Cmp;
import io.etcd.jetcd.op.CmpTarget;
import io.etcd.jetcd.op.Op;
import io.etcd.jetcd.options.PutOption;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class EtcdService {
    private final Client client;
    private final KV kvclient;
    private final Lease leaseClient;

    public EtcdService(@Value("${etcd.url}") String etcdUrl) {
        this.client = Client.builder().endpoints(etcdUrl)
                .keepaliveTime(java.time.Duration.ofMinutes(10))
                .build();
        this.kvclient = client.getKVClient();
        this.leaseClient = client.getLeaseClient();
    }

    @PreDestroy
    public void close() {
        if (client != null) {
            client.close();
        }
    }

    private ByteSequence byteOf(String str) {
        return ByteSequence.from(str.getBytes(StandardCharsets.UTF_8));
    }

    public void saveResetCode(String email, String code) {
        String key = "reset_code:" + email;
        try {
            long leaseId = leaseClient.grant(60).get(3, TimeUnit.SECONDS).getID();
            kvclient.put(byteOf(key), byteOf(code),
                    PutOption.newBuilder().withLeaseId(leaseId).build()).get();
        } catch (Exception e) {
            throw new RuntimeException("Etcd lease error", e);
        }
    }

    public Optional<String> get(String key) {
        try {
            GetResponse response = kvclient.get(byteOf(key)).get(3, TimeUnit.SECONDS);
            if (response.getKvs().isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(response.getKvs().get(0).getValue().toString(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void delete(String key) {
        try {
            kvclient.delete(byteOf(key)).get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("Etcd is unavailable when deleting the key: " + e.getMessage());
        }
    }

    public void saveCatche(String key, String jsonValue) {
        try {
            long leaseId = leaseClient.grant(60).get(3, TimeUnit.SECONDS).getID();
            kvclient.put(byteOf(key), byteOf(jsonValue),
                    PutOption.newBuilder().withLeaseId(leaseId).build()).get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Cache write error in Etcd", e);
        }
    }

    public long incrementLoginAttempts(String email) {
        String key = "login_attempts:" + email;
        try {
            while (true) {
                GetResponse response = kvclient.get(byteOf(key)).get(3, TimeUnit.SECONDS);
                if (!response.getKvs().isEmpty()) {
                    KeyValue kvPair = response.getKvs().get(0);

                    String currentValueStr = kvPair.getValue().toString(StandardCharsets.UTF_8);
                    long newValue = Long.parseLong(currentValueStr) + 1;

                    long existingLeaseId = kvPair.getLease();

                    TxnResponse txnResponse = kvclient.txn()
                            .If(new Cmp(byteOf(key), Cmp.Op.EQUAL, CmpTarget.value(byteOf(currentValueStr))))
                            .Then(Op.put(
                                    byteOf(key),
                                    byteOf(String.valueOf(newValue)),
                                    PutOption.newBuilder().withLeaseId(existingLeaseId).build()
                            ))
                            .commit().get(3, TimeUnit.SECONDS);

                    if (txnResponse.isSucceeded()) {
                        return newValue;
                    }
                } else {
                    long leaseId = leaseClient.grant(300).get(3, TimeUnit.SECONDS).getID();

                    TxnResponse txnResponse = kvclient.txn()
                            .If(new Cmp(byteOf(key), Cmp.Op.EQUAL, CmpTarget.version(0)))
                            .Then(Op.put(
                                    byteOf(key),
                                    byteOf("1"),
                                    PutOption.newBuilder().withLeaseId(leaseId).build()
                            ))
                            .commit().get(3, TimeUnit.SECONDS);

                    if (txnResponse.isSucceeded()) {
                        return 1;
                    }

                    leaseClient.revoke(leaseId).get(3, TimeUnit.SECONDS);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("NoSQL atomic counter error", e);
        }
    }
}
