package com.repository.mongo;

import com.config.MongoFactoryUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.interfaces.ContainIdAble;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.repository.CrudRepository;
import lombok.NonNull;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public abstract class JSONRepository<T extends ContainIdAble> implements CrudRepository<T> {
    private static final JsonSerializer<LocalDateTime> SERIALIZER = (src, typeOfSrc, context) -> src == null ? null
            : new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")));
    private static final JsonDeserializer<LocalDateTime> DESERIALIZER = (jSon, typeOfT, context) -> jSon == null ? null
            : LocalDateTime.parse(jSon.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
    protected static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, SERIALIZER)
            .registerTypeAdapter(LocalDateTime.class, DESERIALIZER).create();
    private final Class<T> type;
    protected final MongoCollection<Document> collection;

    protected JSONRepository(@NonNull final Class<T> type) {
        this.type = type;
        this.collection = MongoFactoryUtil.connect("nix").getCollection(type.getSimpleName().toLowerCase());
    }

    @Override
    public Optional<T> findById(String id) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Id must not be empty");
        }
        return Optional.ofNullable(collection.find(eq("id", id))
                .map(x -> GSON.fromJson(x.toJson(), type))
                .first());
    }

    @Override
    public List<T> getAll() {
        return collection.find()
                .map(x -> GSON.fromJson(x.toJson(), type))
                .into(new ArrayList<>());
    }

    @Override
    public boolean save(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Object must not be null");
        }
        collection.insertOne(mapFrom(item));
        return true;
    }

    @Override
    public boolean save(List<T> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }
        items.forEach(this::save);
        return true;
    }

    @Override
    public boolean update(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Object must not be null");
        }
        final Document updateObject = new Document();
        updateObject.append("$set", mapFrom(item));
        final UpdateResult updateResult = collection.updateOne(eq("id", item.getId()), updateObject);
        return !(updateResult.wasAcknowledged() && updateResult.getMatchedCount() == 0);
    }

    @Override
    public boolean delete(String id) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Id must not be empty");
        }
        final DeleteResult deleteResult = collection.deleteOne(eq("id", id));
        return !(deleteResult.wasAcknowledged() && deleteResult.getDeletedCount() == 0);
    }

    @Override
    public List<T> delete(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Object must not be null");
        }
        delete(item.getId());
        return getAll();
    }

    protected Document mapFrom(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Object must not be null");
        }
        return Document.parse(GSON.toJson(item));
    }
}