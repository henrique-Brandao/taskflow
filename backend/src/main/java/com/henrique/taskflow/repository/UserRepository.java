package com.henrique.taskflow.repository;

import com.henrique.taskflow.model.AppUser;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.Optional;

public class UserRepository {

    private final DynamoDbTable<AppUser> userTable;
    private final DynamoDbIndex<AppUser> emailIndex;

    public UserRepository(DynamoDbEnhancedClient enhancedClient) {
        this.userTable = enhancedClient.table("Users", TableSchema.fromBean(AppUser.class));
        this.emailIndex = userTable.index("EmailIndex");
    }

    public Optional<AppUser> findById(String id) {
        AppUser user = userTable.getItem(Key.builder().partitionValue(id).build());
        return Optional.ofNullable(user);
    }

    public Optional<AppUser> findByEmail(String email) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(email).build());
        return emailIndex.query(queryConditional).stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public void save(AppUser user) {
        userTable.putItem(user);
    }
}
