package com.company.db;

import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class EmbeddedMongoDb {

    private static final MongodStarter starter = MongodStarter.getDefaultInstance();
    private MongodExecutable mongodExe;
    private MongodProcess mongod;
    private MongoClient mongoClient;

    public void start(String bindIp, int port) {
        try {
            IMongodConfig mongodConfig = new MongodConfigBuilder()
                    .version(Version.Main.PRODUCTION)
                    .net(new Net(bindIp, port, Network.localhostIsIPv6()))
                    .build();
            this.mongodExe = starter.prepare(mongodConfig);
            this.mongod = mongodExe.start();
            this.mongoClient = new MongoClient(bindIp, port);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() {
        this.mongoClient.close();
        this.mongod.stop();
        this.mongodExe.stop();
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
}
