package org.example.repository;

import org.example.model.ExceptedObject;

import java.util.List;

public interface ExceptedObjectRepository {

    void saveAll(List<ExceptedObject> exceptedObjects);
}