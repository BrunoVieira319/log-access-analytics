package com.company.dao;

import org.bson.Document;
import java.util.List;

public interface BaseDao {

    void insertMany(List<Document> logs);
}
