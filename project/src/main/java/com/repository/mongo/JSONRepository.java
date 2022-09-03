package com.repository.mongo;

import com.config.MongoFactoryUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.mongodb.client.MongoCollection;
import com.repository.CrudRepository;
import lombok.NonNull;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class JSONRepository<T> implements CrudRepository<T> {
    private static final JsonSerializer<LocalDateTime> SERIALIZER = (src, typeOfSrc, context) -> src == null ? null
            : new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")));
    private static final JsonDeserializer<LocalDateTime> DESERIALIZER = (jSon, typeOfT, context) -> jSon == null ? null
            : LocalDateTime.parse(jSon.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
    protected static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, SERIALIZER)
            .registerTypeAdapter(LocalDateTime.class, DESERIALIZER).create();

    protected final MongoCollection<Document> collection;

    protected JSONRepository(@NonNull final String collectionName) {
        this.collection = MongoFactoryUtil.connect("nix").getCollection(collectionName);
    }

    protected Optional<T> findById(Class<T> type, String id) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Id must not be empty");
        }
        final Document filter = new Document();
        filter.append("id", id);
        return Optional.ofNullable(collection.find(filter)
                .map(x -> GSON.fromJson(x.toJson(), type))
                .first());
    }

    protected List<T> getAll(Class<T> type) {
        return collection.find()
                .map(x -> GSON.fromJson(x.toJson(), type))
                .into(new ArrayList<>());
    }

    @Override
    public boolean delete(String id) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Id must not be empty");
        }
        final Document filter = new Document();
        filter.append("id", id);
        collection.deleteOne(filter);
        return true;
    }

    protected Document mapFrom(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Object must not be null");
        }
        return Document.parse(GSON.toJson(item));
    }
}