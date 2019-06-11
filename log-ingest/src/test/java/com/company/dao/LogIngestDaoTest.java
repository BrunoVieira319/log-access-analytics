package com.company.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class LogIngestDaoTest {

    MongodExecutable mongodExe;
    MongodProcess mongod;
    MongoClient mongoClient;

    @Before
    public void configureMongoForTests() throws Exception {
        String bindIp = "localhost";
        int port = 27018;

        MongodStarter starter = MongodStarter.getDefaultInstance();
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(bindIp, port, Network.localhostIsIPv6()))
                .build();
        this.mongodExe = starter.prepare(mongodConfig);
        this.mongod = mongodExe.start();
        this.mongoClient = new MongoClient(bindIp, port);
    }

    @After
    public void closeConnection() {
        this.mongoClient.close();
        this.mongod.stop();
        this.mongodExe.stop();
    }

    @Test
    public void shouldPersistFiveLogsInDB() {
        LogIngestDao baseDao = new LogIngestDao(mongoClient);

        List<Document> logs = new ArrayList<>();
        IntStream.range(0, 5).forEach(n -> logs.add(new Document()));

        baseDao.insertMany(logs);

        MongoCollection<Document> collection = mongoClient.getDatabase("logs").getCollection("logs");
        assertEquals(collection.countDocuments(), 5);
    }
}
